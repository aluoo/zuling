package com.anyi.sparrow.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.account.domain.EmployeeAccount;
import com.anyi.common.account.service.IEmployeeAccountService;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.ShippingOrder;
import com.anyi.common.product.domain.ShippingOrderRel;
import com.anyi.common.product.domain.dto.OrderShippingCountDTO;
import com.anyi.common.product.domain.enums.OrderOperationEnum;
import com.anyi.common.product.domain.enums.OrderStatusEnum;
import com.anyi.common.product.domain.enums.ShippingOrderStatusEnum;
import com.anyi.common.product.domain.request.RecyclerOrderQueryReq;
import com.anyi.common.product.domain.request.ShippingOrderCreateReq;
import com.anyi.common.product.domain.response.*;
import com.anyi.common.product.service.*;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.util.MoneyUtil;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/6
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ShippingOrderManageService {
    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private OrderOptionService orderOptionService;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private ShippingOrderRelService shippingOrderRelService;
    @Autowired
    private IEmployeeAccountService employeeAccountService;

    public List<ShippingRecyclerInfo> recyclerList() {
        List<ShippingRecyclerInfo> resp = new ArrayList<>();
        List<Order> orderList = orderService.lambdaQuery()
                .select(Order::getRecyclerCompanyId)
                .in(Order::getStatus, Arrays.asList(OrderStatusEnum.PENDING_SHIPMENT.getCode(), OrderStatusEnum.PENDING_RECEIPT.getCode()))
                .eq(Order::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .groupBy(Order::getRecyclerCompanyId)
                .list();
        if (CollUtil.isEmpty(orderList)) {
            return resp;
        }
        List<Long> recyclerIds = orderList.stream().filter(Objects::nonNull).map(Order::getRecyclerCompanyId).collect(Collectors.toList());
        if (CollUtil.isEmpty(recyclerIds)) {
            return resp;
        }
        List<Company> companyList = companyService.lambdaQuery().in(Company::getId, recyclerIds).list();
        if (CollUtil.isEmpty(companyList)) {
            return resp;
        }
        Date now = new Date();
        OrderShippingCountDTO countReq = OrderShippingCountDTO.builder()
                .storeCompanyId(LoginUserContext.getUser().getCompanyId())
                .recyclerCompanyIds(recyclerIds)
                .now(now)
                .build();
        Map<Long, Integer> pendingShipmentCountMap = orderService.getPendingShipmentCountMap(countReq);
        Map<Long, Integer> overdueCountMap = orderService.getOverdueCountMap(countReq);
        Map<Long, Integer> pendingReceiptCountMap = orderService.getPendingReceiptCountMap(countReq);

        companyList.forEach(cmp -> {
            ShippingRecyclerInfo vo = ShippingRecyclerInfo.builder()
                    .companyId(cmp.getId())
                    .companyName(cmp.getName())
                    .build();
            vo.setPendingShipmentCount(Optional.ofNullable(pendingShipmentCountMap.get(cmp.getId())).orElse(0));
            vo.setOverdueCount(Optional.ofNullable(overdueCountMap.get(cmp.getId())).orElse(0));
            vo.setPendingReceiptCount(Optional.ofNullable(pendingReceiptCountMap.get(cmp.getId())).orElse(0));
            resp.add(vo);
        });
        return resp;
    }

    public void validateRecycler(Long recyclerCompanyId) {
        // validate 校验回收商余额是否为负数
        Long managerId = Optional.ofNullable(
                companyService.getCompanyManagerId(recyclerCompanyId)
        ).orElseThrow(() -> new BaseException(-1, "账户不存在"));
        EmployeeAccount managerAccount = Optional.ofNullable(
                employeeAccountService.getByEmployeeId(managerId)
        ).orElseThrow(() -> new BaseException(-1, "账户不存在"));

        int balance = managerAccount.getAbleBalance().intValue();
        if (balance < MoneyUtil.HUNDRED) {
            throw new BaseException(-1, "回收商账户异常，暂时不可发货");
        }
    }

    public PageInfo<ShippingRecyclerOrderInfoVO> recyclerOrderList(RecyclerOrderQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<Order> list = orderService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Order::getStatus, OrderStatusEnum.PENDING_SHIPMENT.getCode())
                .eq(req.getVerified() != null, Order::getVerified, req.getVerified())
                .eq(StrUtil.isNotBlank(req.getOrderNo()), Order::getOrderNo, req.getOrderNo())
                .eq(Order::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(Order::getRecyclerCompanyId, req.getCompanyId())
                .orderByDesc(Order::getVerified)
                .orderByDesc(Order::getBound)
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }
        List<ShippingRecyclerOrderInfoVO> vos = BeanUtil.copyToList(list, ShippingRecyclerOrderInfoVO.class);

        List<Long> orderIds = vos.stream().map(OrderBaseVO::getId).collect(Collectors.toList());
        Map<Long, List<String>> orderImagesInfoMap = orderOptionService.buildOrderImagesInfoMap(orderIds);
        Map<Long, String> orderSpecInfoMap = orderOptionService.buildOrderSpecInfoMap(orderIds);
        Set<Long> companyIds = OrderBaseVO.extractIds(vos, OrderBaseVO::getStoreCompanyId, OrderBaseVO::getRecyclerCompanyId);
        Map<Long, Company> companyInfoMap = companyService.getCompanyInfoMap(companyIds);
        Set<Long> employeeIds = OrderBaseVO.extractIds(vos, OrderBaseVO::getStoreEmployeeId, OrderBaseVO::getRecyclerEmployeeId);
        Map<Long, Employee> employeeInfoMap = employeeService.getEmployeeInfoMap(employeeIds);

        DateTime now = DateUtil.date();
        vos.forEach(vo -> {
            // 设置图片信息
            vo.setImages(orderImagesInfoMap.get(vo.getId()));
            // 设置规格信息
            vo.setSpec(orderSpecInfoMap.get(vo.getId()));
            // 设置已成交天数
            long between = DateUtil.between(vo.getFinishQuoteTime() != null ? vo.getFinishQuoteTime() : vo.getCreateTime(), now, DateUnit.DAY);
            vo.setConfirmDay(Long.valueOf(between).intValue());
            // 设置成交价格
            vo.setFinalPriceStr();
            //
            vo.setCompanyInfo(companyInfoMap);
            vo.setEmployeeInfo(employeeInfoMap);
        });

        PageInfo<ShippingRecyclerOrderInfoVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

    public RecyclerOrderVerifyCountVO recyclerOrderVerifyCount(Long recyclerCompanyId) {
        OrderShippingCountDTO countReq = OrderShippingCountDTO.builder()
                .storeCompanyId(LoginUserContext.getUser().getCompanyId())
                .recyclerCompanyId(recyclerCompanyId)
                .verified(false)
                .build();
        OrderShippingCountDTO unverifiedCount = orderService.getBaseMapper().countOrderVerifyGroupByRecycler(countReq);
        countReq.setVerified(true);
        OrderShippingCountDTO verifiedCount = orderService.getBaseMapper().countOrderVerifyGroupByRecycler(countReq);
        return RecyclerOrderVerifyCountVO.builder()
                .unverifiedCount(Optional.ofNullable(unverifiedCount).map(OrderShippingCountDTO::getCount).orElse(0))
                .verifiedCount(Optional.ofNullable(verifiedCount).map(OrderShippingCountDTO::getCount).orElse(0))
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void bindOrderNo(Long orderId) {
        boolean success = orderService.lambdaUpdate()
                .set(Order::getBound, true)
                .eq(Order::getStatus, OrderStatusEnum.PENDING_SHIPMENT.getCode())
                .eq(Order::getBound, false)
                .eq(Order::getId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new Order());
        if (success) {
            orderLogService.addLog(
                    LoginUserContext.getUser().getId(),
                    orderId,
                    OrderStatusEnum.PENDING_SHIPMENT.getCode(),
                    OrderOperationEnum.BOUND.getCode(),
                    OrderOperationEnum.BOUND.getDesc(),
                    OrderOperationEnum.BOUND.getRemark());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void verifyOrder(Long orderId) {
        Order order = orderService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Order::getId, orderId)
                .one();
        if (order == null) {
            throw new BaseException(BizError.MB_ORDER_NOT_EXISTS);
        }
        if (order.getVerified()) {
            throw new BaseException(-1, "该订单已核验");
        }
        if (!order.getBound()) {
            throw new BaseException(-1, "该订单还未绑码");
        }
        boolean success = orderService.lambdaUpdate()
                .set(Order::getVerified, true)
                .eq(Order::getStatus, OrderStatusEnum.PENDING_SHIPMENT.getCode())
                .eq(Order::getVerified, false)
                .eq(Order::getBound, true)
                .eq(Order::getId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new Order());
        if (!success) {
            throw new BaseException(BizError.MB_ORDER_ERROR_STATUS);
        }
        orderLogService.addLog(
                LoginUserContext.getUser().getId(),
                orderId,
                OrderStatusEnum.PENDING_SHIPMENT.getCode(),
                OrderOperationEnum.VERIFIED.getCode(),
                OrderOperationEnum.VERIFIED.getDesc(),
                OrderOperationEnum.VERIFIED.getRemark());
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderApplyVO createShippingOrder(ShippingOrderCreateReq req) {
        // 校验订单状态，如果订单状态不正确，抛出异常
        List<Long> orderIds = validateCreate(req);
        Long shippingOrderId = snowflakeIdService.nextId();
        // 创建发货订单
        ShippingOrder shippingOrder = ShippingOrder.builder()
                .id(shippingOrderId)
                .storeCompanyId(LoginUserContext.getUser().getCompanyId())
                .storeEmployeeId(LoginUserContext.getUser().getId())
                .recyclerCompanyId(req.getCompanyId())
                .status(ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode())
                .build();
        shippingOrderService.save(shippingOrder);
        // 创建订单关联
        List<ShippingOrderRel> relations = orderIds.stream().map(id -> ShippingOrderRel.builder()
                .id(snowflakeIdService.nextId())
                .orderId(id)
                .shippingOrderId(shippingOrderId)
                .build()).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(relations)) {
            shippingOrderRelService.saveBatch(relations);
        }
        // 更新原订单状态为已发货
        boolean success = orderService.lambdaUpdate()
                .set(Order::getStatus, OrderStatusEnum.PENDING_RECEIPT.getCode())
                .in(Order::getId, orderIds)
                .eq(Order::getDeleted, false)
                .update(new Order());
        if (success) {
            // 订单日志
            orderLogService.addLogBatch(LoginUserContext.getUser().getId(),
                    orderIds,
                    OrderStatusEnum.PENDING_RECEIPT.getCode(),
                    OrderOperationEnum.CONFIRM_SHIPMENT.getCode(),
                    OrderOperationEnum.CONFIRM_SHIPMENT.getDesc(),
                    OrderOperationEnum.CONFIRM_SHIPMENT.getRemark());
            // 发货订单日志
            orderLogService.addLog(LoginUserContext.getUser().getId(),
                    shippingOrderId,
                    ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode(),
                    OrderOperationEnum.CREATE_SHIPPING_ORDER.getCode(),
                    OrderOperationEnum.CREATE_SHIPPING_ORDER.getDesc(),
                    OrderOperationEnum.CREATE_SHIPPING_ORDER.getRemark());
        }
        return OrderApplyVO.builder().orderId(shippingOrderId).build();
    }

    /**
     * 校验订单状态，过滤空的订单ID
     * @param req ShippingOrderCreateReq
     * @return List
     */
    private List<Long> validateCreate(ShippingOrderCreateReq req) {
        List<Long> originalOrderIds = req.getOrderIds();
        if (CollUtil.isEmpty(originalOrderIds)) {
            throw new BaseException(-1, "订单ID不能为空");
        }
        List<Long> orderIds = originalOrderIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (CollUtil.isEmpty(orderIds)) {
            throw new BaseException(-1, "订单ID不能为空");
        }
        List<Order> orders = orderService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Order::getRecyclerCompanyId, req.getCompanyId())
                .in(Order::getId, orderIds)
                .list();
        if (CollUtil.isEmpty(orders)) {
            throw new BaseException(BizError.MB_ORDER_ERROR_STATUS);
        }
        orders.forEach(o -> {
            if (o.getStatus().equals(OrderStatusEnum.REFUNDED.getCode())) {
                throw new BaseException(-1, "存在已退款订单，请重试");
            }
            if (!o.getBound()) {
                throw new BaseException(-1, "存在未绑码订单，请重试");
            }
            if (!o.getVerified()) {
                throw new BaseException(-1, "存在未核验订单，请重试");
            }
            if (!o.getStatus().equals(OrderStatusEnum.PENDING_SHIPMENT.getCode())) {
                throw new BaseException(-1, "存在异常订单，请重试");
            }
        });
        return orderIds;
    }
}