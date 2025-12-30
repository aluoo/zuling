package com.anyi.sparrow.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.jdl.JdlService;
import com.anyi.common.product.domain.*;
import com.anyi.common.product.domain.dto.AddressDTO;
import com.anyi.common.product.domain.enums.*;
import com.anyi.common.product.domain.request.CreateLogisticsReq;
import com.anyi.common.product.domain.request.PreCreateLogisticsReq;
import com.anyi.common.product.domain.request.ShippingOrderQueryReq;
import com.anyi.common.product.domain.response.OrderDetailVO;
import com.anyi.common.product.domain.response.PreCreateLogisticsVO;
import com.anyi.common.product.domain.response.ShippingOrderDetailVO;
import com.anyi.common.product.service.*;
import com.anyi.common.service.AbstractOrderInfoManage;
import com.anyi.common.service.CommissionProcessService;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.common.snowWork.SnowflakeIdService;
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
import java.util.stream.Stream;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/8
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class LogisticsManageService extends AbstractOrderInfoManage {
    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private CommonSysDictService dictService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private ShippingOrderRelService shippingOrderRelService;
    @Autowired
    private ShippingOrderImageService shippingOrderImageService;
    @Autowired
    private ShippingOrderAddressService shippingOrderAddressService;
    @Autowired
    private TrackCompanyService trackCompanyService;
    @Autowired
    private ProductAddressService productAddressService;
    @Autowired
    private CommissionProcessService commissionProcessService;
    @Autowired
    private JdlService jdlService;

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

    public ShippingOrderDetailVO detailShippingOrder(Long shippingOrderId) {
        ShippingOrder shippingOrder = getShippingOrderNotValidCompany(shippingOrderId);
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
                .eq(ShippingOrder::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(req.getStatus() != null, ShippingOrder::getStatus, req.getStatus())
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
        Map<Long, List<ShippingOrderAddress>> addressInfoMap = shippingOrderAddressService.buildAddressMapGroupByShippingOrderIds(shippingOrderIds);
        Map<Long, List<String>> imageInfoMap = shippingOrderImageService.buildImageUrlMapGroupByShippingOrderIds(shippingOrderIds);
        Map<Long, Long> relationCountInfoMap = shippingOrderRelService.countByShippingOrderIds(shippingOrderIds);
        vos.forEach(vo -> {
            vo.setCompanyInfo(companyInfoMap);
            vo.setEmployeeInfo(employeeInfoMap);
            // 设置收货发货地址
            setShippingOrderAddressInfo(addressInfoMap, vo);
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

    public PreCreateLogisticsVO preCreateLogistics(PreCreateLogisticsReq req) {
        PreCreateLogisticsVO vo = PreCreateLogisticsVO.builder().build();
        ShippingOrder shippingOrder = getShippingOrder(req.getShippingOrderId());
        List<Long> orderIds = shippingOrderRelService.listOrderIdsByShippingOrderId(shippingOrder.getId());
        if (CollUtil.isEmpty(orderIds)) {
            // throw new BaseException(-1, "发货订单不存在");
            throw new BaseException(BizError.MB_ORDER_NOT_EXISTS);
        }
        BeanUtil.copyProperties(shippingOrder, vo);
        vo.setShippingOrderId(req.getShippingOrderId());
        // 设置物流公司列表
        vo.setTrackCompanies(trackCompanyService.listAllTrackCompanyDTO());
        List<Long> companyIds = Stream.of(shippingOrder.getStoreCompanyId(), shippingOrder.getRecyclerCompanyId()).collect(Collectors.toList());
        Map<Long, Company> companyInfoMap = companyService.getCompanyInfoMap(companyIds);
        // 收货地址 回收商管理者账号地址
        AddressDTO receiveAddr = getShippingAddress(req.getShippingOrderId(), ShippingAddressTypeEnum.RECEIVE.getCode());
        if (receiveAddr == null) {
            receiveAddr = getAddress(shippingOrder.getRecyclerCompanyId());
        }
        if (receiveAddr != null) {
            receiveAddr.setCompanyId(shippingOrder.getRecyclerCompanyId());
            receiveAddr.setCompanyName(Optional.ofNullable(companyInfoMap.get(shippingOrder.getRecyclerCompanyId())).map(Company::getName).orElse(null));
        }
        vo.setReceiveAddress(receiveAddr);
        // 发货地址列表 门店管理者账号地址列表
        List<AddressDTO> senderAddrList = listAddress(shippingOrder.getStoreCompanyId());
        if (CollUtil.isNotEmpty(senderAddrList)) {
            Optional<Company> storeCompanyOpt = Optional.ofNullable(companyInfoMap.get(shippingOrder.getStoreCompanyId()));
            senderAddrList.forEach(addr -> {
                addr.setCompanyId(shippingOrder.getStoreCompanyId());
                addr.setCompanyName(storeCompanyOpt.map(Company::getName).orElse(null));
            });
        }
        vo.setSenderAddress(senderAddrList);
        // 设置订单信息
        List<OrderDetailVO> orders = super.setOrderInfo(orderIds);
        vo.setOrders(orders);
        // 设置图片信息
        vo.setImages(shippingOrderImageService.listImagesUrlByShippingOrderId(shippingOrder.getId()));
        // 设置过期时间
        if (vo.getStatus().equals(ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode())) {
            vo.setExpiredTime(dictService.getShippingOrderExpiredMinutes());
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createLogistics(CreateLogisticsReq req) {
        ShippingOrder shippingOrder = getShippingOrder(req.getShippingOrderId());
        if (!shippingOrder.getStatus().equals(ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode())) {
            throw new BaseException(BizError.MB_ORDER_OPERATION_ERROR_STATUS);
        }
        // 记录图片信息
        saveShippingOrderImage(req);
        boolean success;
        if (req.getShippingType().equals(ShippingTypeEnum.OFFLINE.getCode())) {
            // 线下
            if (StrUtil.isBlank(req.getTrackNo())) {
                throw new BaseException(-1, "物流单号不能为空");
            }
            // 记录快递公司编码/名称/快递单号
            success = shippingOrderService.lambdaUpdate()
                    .set(ShippingOrder::getApplyLogisticsTime, new Date())
                    .set(ShippingOrder::getStatus, ShippingOrderStatusEnum.PENDING_RECEIPT.getCode())
                    .set(ShippingOrder::getTrackCompanyCode, req.getTrackCompanyCode())
                    .set(ShippingOrder::getTrackCompanyName, req.getTrackCompanyName())
                    .set(ShippingOrder::getTrackNo, req.getTrackNo())
                    .set(ShippingOrder::getShippingType, ShippingTypeEnum.OFFLINE.getCode())
                    .eq(ShippingOrder::getStatus, ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode())
                    .eq(ShippingOrder::getId, shippingOrder.getId())
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .update(new ShippingOrder());
        } else {
            // 线上
            if (!dictService.getShippingOnlineSwitch()) {
                throw new BaseException(-1, "线上物流暂未开放");
            }
            // 记录发货收货地址信息
            saveShippingOrderAddress(req);
            // 从京东物流接口下单获取trackNo
            String trackNo = jdlService.createOrder(req).getWaybillCode();
            // 记录快递公司编码/名称/快递单号/期望揽收时间
            success = shippingOrderService.lambdaUpdate()
                    .set(ShippingOrder::getApplyLogisticsTime, new Date())
                    .set(ShippingOrder::getStatus, ShippingOrderStatusEnum.PENDING_RECEIPT.getCode())
                    .set(ShippingOrder::getTrackCompanyCode, req.getTrackCompanyCode())
                    .set(ShippingOrder::getTrackCompanyName, req.getTrackCompanyName())
                    .set(ShippingOrder::getTrackNo, trackNo)
                    .set(ShippingOrder::getPickupStartTime, req.getPickupStartTime())
                    .set(ShippingOrder::getPickupEndTime, req.getPickupEndTime())
                    .set(ShippingOrder::getShippingType, ShippingTypeEnum.ONLINE.getCode())
                    .eq(ShippingOrder::getStatus, ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode())
                    .eq(ShippingOrder::getId, shippingOrder.getId())
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .update(new ShippingOrder());
        }
        if (success) {
            List<Long> orderIds = shippingOrderRelService.listOrderIdsByShippingOrderId(shippingOrder.getId());

            /*// 结算节点改到确认收货后再执行
            // 结算佣金
            commissionProcessService.settle(orderIds);
            // 发放平台补贴给做单员工
            commissionProcessService.createPlatformSubsidyToStoreEmployee(orderIds);*/

            // 日志
            OrderOperationEnum orderOperationEnum = req.getShippingType().equals(ShippingTypeEnum.ONLINE.getCode())
                    ? OrderOperationEnum.LOGISTICS_ORDER_ONLINE
                    : OrderOperationEnum.LOGISTICS_ORDER_OFFLINE;
            orderLogService.addLog(LoginUserContext.getUser().getId(),
                    shippingOrder.getId(),
                    ShippingOrderStatusEnum.PENDING_RECEIPT.getCode(),
                    orderOperationEnum.getCode(),
                    orderOperationEnum.getDesc(),
                    orderOperationEnum.getRemark());
            // 记录报价订单物流下单日志
            orderLogService.addLogBatch(LoginUserContext.getUser().getId(),
                    orderIds,
                    OrderStatusEnum.PENDING_RECEIPT.getCode(),
                    orderOperationEnum.getCode(),
                    orderOperationEnum.getDesc(),
                    orderOperationEnum.getRemark());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelShippingOrder(Long shippingOrderId) {
        // 发货订单设为已取消
        boolean success = shippingOrderService.lambdaUpdate()
                .set(ShippingOrder::getStatus, ShippingOrderStatusEnum.CANCELED.getCode())
                .eq(ShippingOrder::getId, shippingOrderId)
                .eq(ShippingOrder::getStatus, ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new ShippingOrder());
        if (!success) {
            throw new BaseException(BizError.MB_ORDER_ERROR_STATUS);
        }
        // 日志
        orderLogService.addLog(LoginUserContext.getUser().getId(),
                shippingOrderId,
                ShippingOrderStatusEnum.CANCELED.getCode(),
                OrderOperationEnum.CANCEL_SHIPPING_ORDER.getCode(),
                OrderOperationEnum.CANCEL_SHIPPING_ORDER.getDesc(),
                OrderOperationEnum.CANCEL_SHIPPING_ORDER.getRemark());
        // 报价订单设为待发货
        List<Long> orderIds = shippingOrderRelService.listOrderIdsByShippingOrderId(shippingOrderId);
        if (CollUtil.isEmpty(orderIds)) {
            return;
        }
        success = orderService.lambdaUpdate()
                .set(Order::getStatus, OrderStatusEnum.PENDING_SHIPMENT.getCode())
                .in(Order::getId, orderIds)
                .update(new Order());
        if (success) {
            // 日志
            orderLogService.addLogBatch(LoginUserContext.getUser().getId(),
                    orderIds,
                    OrderStatusEnum.PENDING_SHIPMENT.getCode(),
                    OrderOperationEnum.CANCEL_SHIPPING_ORDER.getCode(),
                    OrderOperationEnum.CANCEL_SHIPPING_ORDER.getDesc(),
                    OrderOperationEnum.CANCEL_SHIPPING_ORDER.getRemark());
        }
    }

    private AddressDTO getShippingAddress(Long shippingOrderId, Integer type) {
        ShippingOrderAddress addr = shippingOrderAddressService.getAddressByShippingOrderAndType(shippingOrderId, type);
        return addr != null ? BeanUtil.copyProperties(addr, AddressDTO.class) : null;
    }

    private AddressDTO getAddress(Long companyId) {
        Employee employee = getCompanyManager(companyId);
        if (employee == null) {
            return null;
        }
        ProductAddress bean = productAddressService.lambdaQuery()
                .eq(ProductAddress::getEmpId, employee.getId())
                .orderByDesc(ProductAddress::getUpdateTime)
                .orderByDesc(ProductAddress::getCreateTime)
                .last("limit 1")
                .one();
        return bean != null ? BeanUtil.copyProperties(bean, AddressDTO.class) : null;
    }

    private List<AddressDTO> listAddress(Long companyId) {
        Employee employee = getCompanyManager(companyId);
        if (employee == null) {
            return null;
        }
        List<ProductAddress> list = productAddressService.lambdaQuery()
                .eq(ProductAddress::getEmpId, employee.getId())
                .orderByDesc(ProductAddress::getUpdateTime)
                .orderByDesc(ProductAddress::getCreateTime)
                .list();
        return CollUtil.isNotEmpty(list) ? BeanUtil.copyToList(list, AddressDTO.class) : null;
    }

    private Employee getCompanyManager(Long companyId) {
        Company company = companyService.getById(companyId);
        if (company == null || company.getEmployeeId() == null) {
            return null;
        }
        return employeeService.getById(company.getEmployeeId());
    }

    private ShippingOrder getShippingOrder(Long shippingOrderId) {
        ShippingOrder shippingOrder = shippingOrderService.lambdaQuery()
                .eq(ShippingOrder::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(ShippingOrder::getId, shippingOrderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one();
        if (shippingOrder == null) {
            // throw new BaseException(-1, "发货订单不存在");
            throw new BaseException(BizError.MB_ORDER_NOT_EXISTS);
        }
        return shippingOrder;
    }

    private ShippingOrder getShippingOrderNotValidCompany(Long shippingOrderId) {
        ShippingOrder shippingOrder = shippingOrderService.lambdaQuery()
                // .eq(ShippingOrder::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(ShippingOrder::getId, shippingOrderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one();
        if (shippingOrder == null) {
            // throw new BaseException(-1, "发货订单不存在");
            throw new BaseException(BizError.MB_ORDER_NOT_EXISTS);
        }
        return shippingOrder;
    }

    private void saveShippingOrderAddress(CreateLogisticsReq req) {
        shippingOrderAddressService.removeExistByShippingOrderId(req.getShippingOrderId());
        AddressDTO sendAddress = req.getSendAddress();
        AddressDTO receiveAddress = req.getReceiveAddress();
        ShippingOrderAddress sender = BeanUtil.copyProperties(sendAddress, ShippingOrderAddress.class);
        sender.setId(snowflakeIdService.nextId());
        sender.setShippingOrderId(req.getShippingOrderId());
        sender.setType(ShippingAddressTypeEnum.SEND.getCode());
        ShippingOrderAddress receiver = BeanUtil.copyProperties(receiveAddress, ShippingOrderAddress.class);
        receiver.setId(snowflakeIdService.nextId());
        receiver.setShippingOrderId(req.getShippingOrderId());
        receiver.setType(ShippingAddressTypeEnum.RECEIVE.getCode());
        List<ShippingOrderAddress> list = Arrays.asList(sender, receiver);
        shippingOrderAddressService.saveBatch(list);
    }

    private void saveShippingOrderImage(CreateLogisticsReq req) {
        shippingOrderImageService.removeExistByShippingOrderId(req.getShippingOrderId());
        List<String> images = req.getImages();
        if (CollUtil.isEmpty(images)) {
            return;
        }
        List<ShippingOrderImage> list = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            if (StrUtil.isBlank(images.get(i))) {
                continue;
            }
            ShippingOrderImage img = ShippingOrderImage.builder()
                    .id(snowflakeIdService.nextId())
                    .shippingOrderId(req.getShippingOrderId())
                    .sort(i)
                    .url(images.get(i))
                    .build();
            list.add(img);
        }
        if (CollUtil.isNotEmpty(list)) {
            shippingOrderImageService.saveBatch(list);
        }
    }
}