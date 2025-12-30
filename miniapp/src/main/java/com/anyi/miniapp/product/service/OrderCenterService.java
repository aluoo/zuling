package com.anyi.miniapp.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.constant.RedisLockKeyConstants;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.OrderLog;
import com.anyi.common.product.domain.ShippingOrder;
import com.anyi.common.product.domain.ShippingOrderAddress;
import com.anyi.common.product.domain.dto.AddressDTO;
import com.anyi.common.product.domain.dto.LogisticsPriceDTO;
import com.anyi.common.product.domain.dto.OrderQuoteInfoDTO;
import com.anyi.common.product.domain.enums.*;
import com.anyi.common.product.domain.request.OrderQuoteQueryReq;
import com.anyi.common.product.domain.request.ShippingOrderQueryReq;
import com.anyi.common.product.domain.response.OrderDetailVO;
import com.anyi.common.product.domain.response.RecyclerShippingOrderCountInfoVO;
import com.anyi.common.product.domain.response.ShippingOrderDetailVO;
import com.anyi.common.product.service.*;
import com.anyi.common.service.AbstractOrderInfoManage;
import com.anyi.common.service.CommissionProcessService;
import com.anyi.common.service.LogisticsPriceProcessService;
import com.anyi.common.util.DistributionLockUtil;
import com.anyi.miniapp.interceptor.UserManager;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lop.open.api.sdk.domain.ECAP.CommonQueryOrderApi.commonGetActualFeeInfoV1.CommonActualFeeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/12
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderCenterService extends AbstractOrderInfoManage {
    @Autowired
    private OrderService orderService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private OrderQuotePriceLogService orderQuotePriceLogService;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private ShippingOrderRelService shippingOrderRelService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private ShippingOrderAddressService shippingOrderAddressService;
    @Autowired
    private ShippingOrderImageService shippingOrderImageService;
    @Autowired
    private LogisticsPriceProcessService logisticsPriceProcessService;
    @Autowired
    private CommissionProcessService commissionProcessService;
    @Autowired
    private DistributionLockUtil distributionLockUtil;

    public ShippingOrderDetailVO detailShippingOrder(Long shippingOrderId) {
        ShippingOrder shippingOrder = shippingOrderService.lambdaQuery()
                .eq(ShippingOrder::getRecyclerCompanyId, UserManager.getCurrentUser().getCompanyId())
                .eq(ShippingOrder::getId, shippingOrderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one();
        if (shippingOrder == null) {
            throw new BaseException(BizError.MB_ORDER_NOT_EXISTS);
        }
        List<Long> orderIds = shippingOrderRelService.listOrderIdsByShippingOrderId(shippingOrder.getId());
        ShippingOrderDetailVO vo = BeanUtil.copyProperties(shippingOrder, ShippingOrderDetailVO.class);
        List<Long> companyIds = Stream.of(vo.getStoreCompanyId(), vo.getRecyclerCompanyId()).collect(Collectors.toList());
        List<Long> employeeIds = Stream.of(vo.getStoreEmployeeId(), vo.getRecyclerEmployeeId()).collect(Collectors.toList());
        Map<Long, Company> companyInfoMap = companyService.getCompanyInfoMap(companyIds);
        Map<Long, Employee> employeeInfoMap = employeeService.getEmployeeInfoMap(employeeIds);
        Map<Long, List<ShippingOrderAddress>> addressInfoMap = shippingOrderAddressService.buildAddressMapGroupByShippingOrderIds(Collections.singletonList(shippingOrderId));
        vo.setCompanyInfo(companyInfoMap);
        vo.setEmployeeInfo(employeeInfoMap);
        // 设置收货发货地址
        setShippingOrderAddressInfo(addressInfoMap, vo);
        // 设置订单信息
        List<OrderDetailVO> orders = super.setOrderInfo(orderIds);
        vo.setOrders(orders);
        // 设置图片信息
        vo.setImages(shippingOrderImageService.listImagesUrlByShippingOrderId(shippingOrder.getId()));
        // 设置运费
        vo.setPriceStr();
        // 设置物流轨迹
        super.setLogisticsTrace(vo);
        return vo;
    }

    public PageInfo<ShippingOrderDetailVO> listShippingOrder(ShippingOrderQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<ShippingOrder> list = shippingOrderService.lambdaQuery()
                .eq(ShippingOrder::getRecyclerCompanyId, UserManager.getCurrentUser().getCompanyId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .ne(ShippingOrder::getStatus, ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode())
                .eq(req.getStatus() != null, ShippingOrder::getStatus, req.getStatus())
                .ne(ShippingOrder::getStatus, ShippingOrderStatusEnum.CANCELED.getCode())
                // 屏蔽掉未下单物流的发货订单
                .isNotNull(ShippingOrder::getShippingType)
                .isNotNull(ShippingOrder::getTrackNo)
                .isNotNull(ShippingOrder::getTrackCompanyName)
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }
        List<ShippingOrderDetailVO> vos = BeanUtil.copyToList(list, ShippingOrderDetailVO.class);
        List<Long> shippingOrderIds = vos.stream().map(ShippingOrderDetailVO::getId).collect(Collectors.toList());
        Set<Long> employeeIds = ShippingOrderDetailVO.extractIds(vos, ShippingOrderDetailVO::getStoreEmployeeId, ShippingOrderDetailVO::getRecyclerEmployeeId);
        Set<Long> companyIds = ShippingOrderDetailVO.extractIds(vos, ShippingOrderDetailVO::getStoreCompanyId, ShippingOrderDetailVO::getRecyclerCompanyId);
        Map<Long, Company> companyInfoMap = companyService.getCompanyInfoMap(companyIds);
        Map<Long, Employee> employeeInfoMap = employeeService.getEmployeeInfoMap(employeeIds);
        Map<Long, List<String>> imageInfoMap = shippingOrderImageService.buildImageUrlMapGroupByShippingOrderIds(shippingOrderIds);
        Map<Long, Long> relationCountInfoMap = shippingOrderRelService.countByShippingOrderIds(shippingOrderIds);
        vos.forEach(vo -> {
            vo.setCompanyInfo(companyInfoMap);
            vo.setEmployeeInfo(employeeInfoMap);
            // 设置图片信息
            vo.setImages(imageInfoMap.get(vo.getId()));
            // 设置寄出设备数量
            vo.setOrderCount(relationCountInfoMap.get(vo.getId()).intValue());
            // 设置运费
            vo.setPriceStr();
        });
        PageInfo<ShippingOrderDetailVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

    public OrderDetailVO detailOrder(Long orderId) {
        Order order = orderService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Order::getId, orderId)
                .one();
        if (order == null) {
            throw new BaseException(BizError.MB_ORDER_NOT_EXISTS);
        }
        OrderDetailVO vo = new OrderDetailVO();
        BeanUtil.copyProperties(order, vo);
        // 设置订单基础信息
        super.setBaseOrderInfo(vo);
        // 设置订单选项信息
        super.setOrderOptionInfo(vo);
        // 设置报价信息
        OrderQuoteInfoDTO quoteInfo = orderQuotePriceLogService.getQuoteInfo(OrderQuoteQueryReq.builder().orderId(orderId).recyclerCompanyId(UserManager.getCurrentUser().getCompanyId()).build());
        Optional.ofNullable(quoteInfo).ifPresent(o -> {
            // 设置报价用时
            o.setQuoteTimeSpentReal();
            // 设置价格信息
            o.setPriceInfo();
            // 设置操作按钮权限
            o.setOperationBtn(UserManager.getCurrentUser().getEmployeeId());
            if (o.getEmployeeId() != null) {
                Map<Long, Employee> recyclerEmployeeInfoMap = employeeService.getEmployeeInfoMap(Collections.singletonList(o.getEmployeeId()));
                o.setEmployeeName(Optional.ofNullable(recyclerEmployeeInfoMap.get(o.getEmployeeId())).map(Employee::getName).orElse(null));
            }
            // 设置拒绝原因
            if (o.getSubStatus().equals(OrderQuoteLogSubStatusEnum.REJECT_QUOTE.getCode())) {
                List<OrderLog> rejectLogs = orderLogService.lambdaQuery()
                        .eq(OrderLog::getOrderId, o.getId())
                        .eq(OrderLog::getStatus, OrderQuoteLogStatusEnum.QUOTED.getCode())
                        .eq(OrderLog::getOperationStatus, OrderOperationEnum.REJECT_QUOTE.getCode())
                        .list();
                if (CollUtil.isNotEmpty(rejectLogs)) {
                    o.setRejectReason(rejectLogs.get(0).getRemark());
                }
            }
        });
        vo.setRecyclerInfo(quoteInfo);
        return vo;
    }

    public RecyclerShippingOrderCountInfoVO countShippingOrder() {
        RecyclerShippingOrderCountInfoVO vo = RecyclerShippingOrderCountInfoVO.builder()
                .pendingReceiptCount(0)
                .build();
        Long pendingReceiptCount = shippingOrderService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(ShippingOrder::getRecyclerCompanyId, UserManager.getCurrentUser().getCompanyId())
                .eq(ShippingOrder::getStatus, ShippingOrderStatusEnum.PENDING_RECEIPT.getCode())
                .count();
        vo.setPendingReceiptCount(pendingReceiptCount.intValue());
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void confirmReceipt(Long shippingOrderId) {
        String lockKey = StrUtil.format(RedisLockKeyConstants.MB_ORDER_RECYCLER_CONFIRM_RECEIPT_LOCK_KEY, shippingOrderId);
        distributionLockUtil.lock(
                () -> confirmReceiptBase(shippingOrderId),
                0,
                () -> lockKey,
                BizError.MB_ORDER_ALREADY_RECEIPT.getMessage(),
                null);
    }

    private void confirmReceiptBase(Long shippingOrderId) {
        // 找出发货订单
        ShippingOrder shippingOrder = Optional.ofNullable(shippingOrderService.lambdaQuery()
                .eq(ShippingOrder::getId, shippingOrderId)
                .eq(ShippingOrder::getRecyclerCompanyId, UserManager.getCurrentUser().getCompanyId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .last("for update")
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        if (shippingOrder.getStatus().equals(ShippingOrderStatusEnum.FINISHED.getCode())) {
            throw new BaseException(BizError.MB_ORDER_ALREADY_RECEIPT);
        }
        if (!shippingOrder.getStatus().equals(ShippingOrderStatusEnum.PENDING_RECEIPT.getCode())) {
            throw new BaseException(BizError.MB_ORDER_OPERATION_ERROR_STATUS);
        }
        // 找出对应报价订单
        List<Long> orderIds = shippingOrderRelService.listOrderIdsByShippingOrderId(shippingOrderId);
        if (CollUtil.isEmpty(orderIds)) {
            throw new BaseException(BizError.MB_ORDER_NOT_EXISTS);
        }

        ShippingOrderDetailVO detailVO = BeanUtil.copyProperties(shippingOrder, ShippingOrderDetailVO.class);
        LogisticsPriceDTO logisticsPrice = new LogisticsPriceDTO();
        if (needJdTrace(detailVO)) {
            // 查询物流费用，计算折扣前运费金额，更新运费金额，扣减回收商账户
            CommonActualFeeResponse actualFeeInfo = jdlService.getActualFeeInfo(shippingOrder.getTrackNo());
            logisticsPrice = logisticsPriceProcessService.calcLogisticsPrice(actualFeeInfo);
            logisticsPrice.setShippingOrderId(shippingOrder.getId());
            logisticsPrice.setRecyclerCompanyId(shippingOrder.getRecyclerCompanyId());
            commissionProcessService.deductionRecyclerAccountForLogistics(logisticsPrice);
        }

        Date now = new Date();
        // 更新发货订单状态
        boolean success = shippingOrderService.lambdaUpdate()
                .set(ShippingOrder::getStatus, ShippingOrderStatusEnum.FINISHED.getCode())
                .set(ShippingOrder::getRecyclerEmployeeId, UserManager.getCurrentUser().getEmployeeId())
                .set(ShippingOrder::getConfirmReceiptTime, now)
                .set(logisticsPrice.getPrice() != null, ShippingOrder::getPrice, logisticsPrice.getPrice())
                .set(logisticsPrice.getDiscountRate() != null, ShippingOrder::getDiscountRate, logisticsPrice.getDiscountRate())
                .set(logisticsPrice.getDiscountPrice() != null, ShippingOrder::getDiscountPrice, logisticsPrice.getDiscountPrice())
                .set(logisticsPrice.getOriginalPrice() != null, ShippingOrder::getOriginalPrice, logisticsPrice.getOriginalPrice())
                .eq(ShippingOrder::getId, shippingOrderId)
                .eq(ShippingOrder::getStatus, ShippingOrderStatusEnum.PENDING_RECEIPT.getCode())
                .update(new ShippingOrder());
        if (!success) {
            throw new BaseException(BizError.MB_ORDER_ERROR_STATUS);
        }
        // 批量更新报价订单状态
        orderService.lambdaUpdate()
                .set(Order::getStatus, OrderStatusEnum.FINISHED.getCode())
                .set(Order::getConfirmReceiptTime, now)
                .in(Order::getId, orderIds)
                .update(new Order());

        // 结算佣金
        commissionProcessService.settle(orderIds);
        // 发放平台补贴给做单员工
        commissionProcessService.createPlatformSubsidyToStoreEmployee(orderIds);

        // 操作日志
        orderLogService.addLog(
                UserManager.getCurrentUser().getEmployeeId(),
                shippingOrderId,
                ShippingOrderStatusEnum.FINISHED.getCode(),
                OrderOperationEnum.CONFIRM_RECEIPT.getCode(),
                OrderOperationEnum.CONFIRM_RECEIPT.getDesc(),
                OrderOperationEnum.CONFIRM_RECEIPT.getRemark()
        );
        orderLogService.addLogBatch(
                UserManager.getCurrentUser().getEmployeeId(),
                orderIds,
                OrderStatusEnum.FINISHED.getCode(),
                OrderOperationEnum.CONFIRM_RECEIPT.getCode(),
                OrderOperationEnum.CONFIRM_RECEIPT.getDesc(),
                OrderOperationEnum.CONFIRM_RECEIPT.getRemark()
        );
    }

    private void setShippingOrderAddressInfo(Map<Long, List<ShippingOrderAddress>> addressInfoMap, ShippingOrderDetailVO vo) {
        // 设置发货地址
        List<ShippingOrderAddress> addresses = addressInfoMap.get(vo.getId());
        Map<Integer, ShippingOrderAddress> addressTypeMap = shippingOrderAddressService.buildAddressMapByType(addresses);
        ShippingOrderAddress senderAddr = addressTypeMap.get(ShippingAddressTypeEnum.SEND.getCode());
        AddressDTO sAddr = senderAddr != null ? BeanUtil.copyProperties(senderAddr, AddressDTO.class) : null;
        if (sAddr != null) {
            sAddr.setCompanyId(vo.getStoreCompanyId());
            sAddr.setCompanyName(vo.getStoreCompanyName());
        }
        vo.setSenderAddress(sAddr);
        // 设置收货地址
        ShippingOrderAddress receiveAddr = addressTypeMap.get(ShippingAddressTypeEnum.RECEIVE.getCode());
        AddressDTO rAddr = receiveAddr != null ? BeanUtil.copyProperties(receiveAddr, AddressDTO.class) : null;
        if (rAddr != null) {
            rAddr.setCompanyId(vo.getRecyclerCompanyId());
            rAddr.setCompanyName(vo.getRecyclerCompanyName());
        }
        vo.setReceiveAddress(rAddr);
    }
}