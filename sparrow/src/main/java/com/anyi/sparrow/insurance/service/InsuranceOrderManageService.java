package com.anyi.sparrow.insurance.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.constant.RedisLockKeyConstants;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.enums.PayEnterEnum;
import com.anyi.common.insurance.constant.CompanyAccountChangeEnum;
import com.anyi.common.insurance.domain.*;
import com.anyi.common.insurance.domain.dto.DiInsuranceOrderPictureDTO;
import com.anyi.common.insurance.domain.dto.DiProductInsurancePriceDTO;
import com.anyi.common.insurance.enums.*;
import com.anyi.common.insurance.req.*;
import com.anyi.common.insurance.response.DiInsuranceInfoVO;
import com.anyi.common.insurance.response.InsuranceOrderApplyVO;
import com.anyi.common.insurance.response.InsuranceOrderDetailVO;
import com.anyi.common.insurance.response.InsuranceOrderRefundDetailVO;
import com.anyi.common.insurance.service.*;
import com.anyi.common.product.domain.Product;
import com.anyi.common.product.domain.ProductSku;
import com.anyi.common.product.domain.dto.OrderLogDTO;
import com.anyi.common.product.domain.enums.OrderOperationEnum;
import com.anyi.common.product.domain.request.OrderQueryReq;
import com.anyi.common.product.service.OrderLogService;
import com.anyi.common.product.service.ProductService;
import com.anyi.common.product.service.ProductSkuService;
import com.anyi.common.result.WxPayVO;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.common.service.PayApplyRecordService;
import com.anyi.common.service.ProductWxPayService;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.user.domain.UserAccount;
import com.anyi.common.util.CurrencyUtils;
import com.anyi.common.util.DistributionLockUtil;
import com.anyi.common.util.MoneyUtil;
import com.anyi.common.wx.CommonWxUtils;
import com.anyi.common.wx.MchIdService;
import com.anyi.sparrow.applet.user.service.UserAccountProcessService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.utils.WxUtils;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsQueryRequest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsRequest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsResult;
import com.github.binarywang.wxpay.bean.ecommerce.TransactionsResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/6
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class InsuranceOrderManageService {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService skuService;
    @Autowired
    private DiInsuranceService insuranceService;
    @Autowired
    private DiProductInsuranceService productInsuranceService;
    @Autowired
    private DiProductInsurancePriceService productInsurancePriceService;
    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private DiTypeService diTypeService;
    @Autowired
    private DiInsuranceOrderService insuranceOrderService;
    @Autowired
    private DiInsuranceOrderPictureService insuranceOrderPictureService;
    @Autowired
    private DiInsuranceOrderPaymentService insuranceOrderPaymentService;
    @Autowired
    private DiSkuInsuranceService skuInsuranceService;
    @Autowired
    private WxUtils wxUtils;
    @Autowired
    private UserAccountProcessService userAccountProcessService;
    @Autowired
    private DistributionLockUtil distributionLockUtil;
    @Autowired
    private ProductWxPayService productWxPayService;
    @Autowired
    private PayApplyRecordService payApplyRecordService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private MchIdService mchIdService;
    @Value("${wx.pay.digitalInsuranceOrderPaymentNotifyUrl}")
    private String digitalInsuranceOrderPaymentNotifyUrl;
    @Autowired
    private CommonSysDictService dictService;
    @Autowired
    private CompanyAccountChangeService companyAccountChangeService;
    @Autowired
    private OrderLogService orderLogService;

    public List<DiInsuranceInfoVO> listInsuranceByMobile(DiInsuranceInfoQueryReq req) {
        ProductSku sku = skuService.lambdaQuery()
                .eq(ProductSku::getId, req.getSkuId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .one();
        if (sku == null || sku.getRetailPrice() == null) {
            return new ArrayList<>();
        }
        // 手机对应保险产品列表
        List<DiProductInsurance> productInsurances = productInsuranceService.lambdaQuery()
                .eq(DiProductInsurance::getProductId, req.getProductId())
                .eq(DiProductInsurance::getDeleted, false)
                .eq(DiProductInsurance::getActivated, true)
                .list();
        if (CollUtil.isEmpty(productInsurances)) {
            return new ArrayList<>();
        }
        Set<Long> insuranceIds = productInsurances.stream().map(DiProductInsurance::getInsuranceId).collect(Collectors.toSet());
        List<DiInsurance> insurances = insuranceService.lambdaQuery()
                .in(DiInsurance::getId, insuranceIds)
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(DiInsurance::getStatus, 1)
                .list();
        List<DiInsuranceInfoVO> resp = new ArrayList<>();
        insurances.forEach(i -> {
            DiInsuranceInfoVO vo = DiInsuranceInfoVO.builder()
                    .id(i.getId())
                    .name(i.getName())
                    .period(i.getPeriod())
                    .build();
            // set sale price
            DiProductInsurancePriceDTO infoByPriceInterval = productInsurancePriceService.getBaseMapper().getInfoByPriceInterval(DiProductInsurancePriceDTO.builder().insuranceId(i.getId()).queryPrice(sku.getRetailPrice()).build());
            if (infoByPriceInterval == null) {
                return;
            }
            vo.setSalePrice(infoByPriceInterval.getSalePrice());
            vo.setSalePriceStr(MoneyUtil.fenToYuan(infoByPriceInterval.getSalePrice()));
            vo.setNormalPrice(infoByPriceInterval.getNormalPrice());
            vo.setNormalPriceStr(MoneyUtil.fenToYuan(infoByPriceInterval.getNormalPrice()));
            resp.add(vo);
        });

        return resp;
    }

    public PageInfo<InsuranceOrderDetailVO> listOrder(OrderQueryReq req) {
        Long companyManagerId = companyService.getCompanyManagerId(LoginUserContext.getUser().getCompanyId());
        boolean isManager = LoginUserContext.getUser().getId().equals(companyManagerId);
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<DiInsuranceOrder> list = insuranceOrderService.lambdaQuery()
                .eq(DiInsuranceOrder::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(req.getStatus() != null, DiInsuranceOrder::getStatus, req.getStatus())
                // 非店长，只能查看自己的订单
                .eq(!isManager, DiInsuranceOrder::getStoreEmployeeId, LoginUserContext.getUser().getId())
                // .eq(req.getOnlySelf() != null && req.getOnlySelf(), DiInsuranceOrder::getStoreEmployeeId, LoginUserContext.getUser().getId())
                .ge(req.getBeginTime() != null, DiInsuranceOrder::getCreateTime, req.getBeginTime())
                .le(req.getEndTime() != null, DiInsuranceOrder::getCreateTime, req.getEndTime())
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }

        List<InsuranceOrderDetailVO> vos = BeanUtil.copyToList(list, InsuranceOrderDetailVO.class);
        Set<Long> companyIds = vos.stream().map(InsuranceOrderDetailVO::getStoreCompanyId).collect(Collectors.toSet());
        Set<Long> employeeIds = vos.stream().map(InsuranceOrderDetailVO::getStoreEmployeeId).collect(Collectors.toSet());
        Set<Long> orderIds = vos.stream().map(InsuranceOrderDetailVO::getId).collect(Collectors.toSet());
        Map<Long, List<DiInsuranceOrderPicture>> picturesMap = insuranceOrderPictureService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .in(DiInsuranceOrderPicture::getInsuranceOrderId, orderIds)
                .list().stream().collect(Collectors.groupingBy(DiInsuranceOrderPicture::getInsuranceOrderId));
        Map<Long, Company> companyInfoMap = companyService.getCompanyInfoMap(companyIds);
        Map<Long, Employee> employeeInfoMap = employeeService.getEmployeeInfoMap(employeeIds);
        Map<Long, DiInsuranceOrderPayment> paymentMap = insuranceOrderPaymentService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .in(DiInsuranceOrderPayment::getInsuranceOrderId, orderIds)
                .list().stream().collect(Collectors.toMap(DiInsuranceOrderPayment::getInsuranceOrderId, Function.identity()));
        int insuranceOrderExpiredMinutes = dictService.getInsuranceOrderExpiredMinutes();
        vos.forEach(vo -> {
            vo.setStoreCompanyName(Optional.ofNullable(companyInfoMap.get(vo.getStoreCompanyId())).map(Company::getName).orElse(null));
            vo.setStoreEmployeeName(Optional.ofNullable(employeeInfoMap.get(vo.getStoreEmployeeId())).map(Employee::getName).orElse(null));
            vo.setStoreEmployeeMobile(Optional.ofNullable(employeeInfoMap.get(vo.getStoreEmployeeId())).map(Employee::getMobileNumber).orElse(null));
            vo.setPriceStr(MoneyUtil.fenToYuan(vo.getPrice()));
            List<DiInsuranceOrderPicture> diInsuranceOrderPictures = picturesMap.get(vo.getId());
            List<DiInsuranceOrderPictureDTO> diInsuranceOrderPictureDTOS = BeanUtil.copyToList(diInsuranceOrderPictures, DiInsuranceOrderPictureDTO.class);
            vo.setPictures(diInsuranceOrderPictureDTOS);
            vo.setCanCancel(false);
            vo.setCanRefund(false);
            vo.setCanEdit(false);
            if (vo.getStatus().equals(DiOrderStatusEnum.PENDING_PAYMENT.getCode())) {
                vo.setCanCancel(true);
            }
            if (vo.getStatus().equals(DiOrderStatusEnum.PENDING_AUDIT.getCode())) {
                vo.setCanRefund(true);
            }
            if (vo.getStatus().equals(DiOrderStatusEnum.PENDING_AUDIT.getCode()) && vo.getSubStatus().equals(DiOrderSubStatusEnum.AUDIT_ORDER_FAILED.getCode())) {
                vo.setCanEdit(true);
            }
            vo.setPayType(Optional.ofNullable(paymentMap.get(vo.getId())).map(DiInsuranceOrderPayment::getType).orElse(null));
            vo.setPayStatus(Optional.ofNullable(paymentMap.get(vo.getId())).map(DiInsuranceOrderPayment::getStatus).orElse(null));
            vo.setExpiredTime(insuranceOrderExpiredMinutes);
        });
        PageInfo<InsuranceOrderDetailVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

    public InsuranceOrderDetailVO detailOrder(Long orderId) {
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                        .eq(AbstractBaseEntity::getDeleted, false)
                        .eq(DiInsuranceOrder::getId, orderId)
                        .eq(DiInsuranceOrder::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                        .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        InsuranceOrderDetailVO vo = BeanUtil.copyProperties(order, InsuranceOrderDetailVO.class);
        DiInsuranceOrderPayment payment = insuranceOrderPaymentService.lambdaQuery()
                .eq(DiInsuranceOrderPayment::getInsuranceOrderId, order.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .one();
        Map<Long, List<DiInsuranceOrderPicture>> picturesMap = insuranceOrderPictureService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(DiInsuranceOrderPicture::getInsuranceOrderId, vo.getId())
                .list().stream().collect(Collectors.groupingBy(DiInsuranceOrderPicture::getInsuranceOrderId));
        Map<Long, Company> companyInfoMap = companyService.getCompanyInfoMap(Collections.singletonList(vo.getStoreCompanyId()));
        Map<Long, Employee> employeeInfoMap = employeeService.getEmployeeInfoMap(Collections.singletonList(vo.getStoreEmployeeId()));
        int insuranceOrderExpiredMinutes = dictService.getInsuranceOrderExpiredMinutes();
        vo.setExpiredTime(insuranceOrderExpiredMinutes);

        vo.setStoreCompanyName(Optional.ofNullable(companyInfoMap.get(vo.getStoreCompanyId())).map(Company::getName).orElse(null));
        vo.setStoreEmployeeName(Optional.ofNullable(employeeInfoMap.get(vo.getStoreEmployeeId())).map(Employee::getName).orElse(null));
        vo.setStoreEmployeeMobile(Optional.ofNullable(employeeInfoMap.get(vo.getStoreEmployeeId())).map(Employee::getMobileNumber).orElse(null));
        vo.setProductSkuRetailPriceStr(MoneyUtil.fenToYuan(vo.getProductSkuRetailPrice()));
        vo.setPriceStr(MoneyUtil.fenToYuan(vo.getPrice()));
        vo.setPayStatus(Optional.ofNullable(payment).map(DiInsuranceOrderPayment::getStatus).orElse(null));
        vo.setPayType(Optional.ofNullable(payment).map(DiInsuranceOrderPayment::getType).orElse(null));
        List<DiInsuranceOrderPicture> diInsuranceOrderPictures = picturesMap.get(vo.getId());
        List<DiInsuranceOrderPictureDTO> diInsuranceOrderPictureDTOS = BeanUtil.copyToList(diInsuranceOrderPictures, DiInsuranceOrderPictureDTO.class);
        vo.setPictures(diInsuranceOrderPictureDTOS);
        vo.setCanCancel(false);
        vo.setCanRefund(false);
        vo.setCanEdit(false);
        if (vo.getStatus().equals(DiOrderStatusEnum.PENDING_PAYMENT.getCode())) {
            vo.setCanCancel(true);
        }
        if (vo.getStatus().equals(DiOrderStatusEnum.PENDING_AUDIT.getCode())) {
            vo.setCanRefund(true);
        }
        if (vo.getStatus().equals(DiOrderStatusEnum.PENDING_AUDIT.getCode()) && vo.getSubStatus().equals(DiOrderSubStatusEnum.AUDIT_ORDER_FAILED.getCode())) {
            vo.setCanEdit(true);
        }
        List<Integer> customerStatus = Arrays.asList(
                OrderOperationEnum.DI_ORDER_CREATE.getCode(),
                OrderOperationEnum.DI_ORDER_PAY.getCode(),
                OrderOperationEnum.DI_ORDER_REFUND_APPLY.getCode(),
                OrderOperationEnum.DI_ORDER_REFUND_AUDIT_FAILED.getCode(),
                OrderOperationEnum.DI_ORDER_REFUND_AUDIT_PASSED.getCode(),
                OrderOperationEnum.DI_ORDER_AUDIT_FAILED.getCode(),
                OrderOperationEnum.DI_ORDER_EDIT_INFO_CUSTOMER.getCode(),
                OrderOperationEnum.DI_ORDER_AUDIT_PASSED.getCode(),
                OrderOperationEnum.DI_ORDER_CANCEL_MANUAL.getCode(),
                OrderOperationEnum.DI_ORDER_CANCEL_AUTO.getCode(),
                OrderOperationEnum.DI_ORDER_REFUND.getCode()
        );
        List<OrderLogDTO> logs = orderLogService.listLog(orderId, customerStatus);
        vo.setLogs(logs);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public InsuranceOrderApplyVO applyOrder(InsuranceOrderApplyReq req) {
        // todo product status
        Product product = productService.getById(req.getProductId());
        ProductSku sku = skuService.getById(req.getSkuId());
        DiInsurance insurance = insuranceService.getById(req.getInsuranceId());
        DiType insuranceType = diTypeService.getById(insurance.getTypeId());
        DiSkuInsurance skuInsurance = Optional.ofNullable(skuInsuranceService.lambdaQuery()
                .eq(DiSkuInsurance::getSkuId, sku.getId())
                .eq(DiSkuInsurance::getInsuranceId, insurance.getId())
                .one()).orElseThrow(() -> new BaseException(-1, "商品配置异常"));

        DiProductInsurancePriceDTO infoByPriceInterval = productInsurancePriceService.getBaseMapper()
                .getInfoByPriceInterval(DiProductInsurancePriceDTO.builder().insuranceId(insurance.getId()).queryPrice(sku.getRetailPrice()).build());
        Long orderId = snowflakeIdService.nextId();

        // valid price 价格不能低于门店进货价 normalPrice
        // BigDecimal priceDecimal = req.getPrice();
        int price = CurrencyUtils.multiply(req.getPrice(), CurrencyUtils.HUNDRED).intValue();
        if (price < infoByPriceInterval.getNormalPrice()) {
            String err = StrUtil.format("价格设置不得低于最低售价，请重新修改。", MoneyUtil.fenToYuan(infoByPriceInterval.getNormalPrice()));
            throw new BaseException(-1, err);
        }

        DiInsuranceOrder order = DiInsuranceOrder.builder()
                .id(orderId)
                .storeEmployeeId(LoginUserContext.getUser().getId())
                .storeCompanyId(LoginUserContext.getUser().getCompanyId())
                .productSkuId(sku.getId())
                .insuranceId(insurance.getId())
                .productName(product.getName())
                .productSpec(sku.getSpec())
                .productSkuRetailPrice(sku.getRetailPrice())
                .insurancePeriod(insurance.getPeriod())
                .insuranceName(insurance.getName())
                .insuranceType(insuranceType.getName())
                .status(DiOrderStatusEnum.PENDING_PAYMENT.getCode())
                .subStatus(DiOrderSubStatusEnum.PENDING_PAYMENT.getCode())
                .customName(req.getCustomName())
                .customPhone(req.getCustomPhone())
                .idCard(req.getIdCard())
                .imeiNo(req.getImeiNo())
                .price(price)
                .insuranceNormalPrice(infoByPriceInterval.getNormalPrice())
                .insuranceDownPrice(skuInsurance.getDownPrice())
                .insuranceStatus(DiOrderInsuranceStatusEnum.PENDING_UPLOAD.getCode())
                .remark(req.getRemark())
                .build();

        insuranceOrderService.save(order);

        // save picture
        List<DiInsuranceOrderPicture> pictures = new ArrayList<>();
        req.getPictures().forEach(p -> pictures.add(DiInsuranceOrderPicture.builder()
                .insuranceOrderId(orderId)
                .type(p.getType())
                .url(p.getUrl())
                .build()));
        insuranceOrderPictureService.saveBatch(pictures);

        // create payment
        DiInsuranceOrderPayment payment = DiInsuranceOrderPayment.builder()
                .insuranceOrderId(orderId)
                .status(DiOrderPayStatusEnum.PENDING_PAYMENT.getCode())
                .amount(price)
                .build();
        insuranceOrderPaymentService.save(payment);

        // log
        orderLogService.addLog(
                LoginUserContext.getUser().getId(),
                order.getId(),
                DiOrderStatusEnum.PENDING_PAYMENT.getCode(),
                OrderOperationEnum.DI_ORDER_CREATE.getCode(),
                OrderOperationEnum.DI_ORDER_CREATE.getDesc(),
                OrderOperationEnum.DI_ORDER_CREATE.getRemark()
        );

        return InsuranceOrderApplyVO.builder().orderId(orderId).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void editOrder(InsuranceOrderEditReq req) {
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                .eq(DiInsuranceOrder::getId, req.getOrderId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        // 非本人提交的订单不可操作
        validateSelfOrder(order);
        // 校验订单状态
        validateOrderStatus(order, DiOrderStatusEnum.PENDING_AUDIT.getCode());
        validateOrderSubStatus(order, DiOrderSubStatusEnum.AUDIT_ORDER_FAILED.getCode());
        // 更新
        boolean success = insuranceOrderService.lambdaUpdate()
                .set(DiInsuranceOrder::getCustomName, req.getCustomName())
                .set(DiInsuranceOrder::getCustomPhone, req.getCustomPhone())
                .set(DiInsuranceOrder::getIdCard, req.getIdCard())
                .set(DiInsuranceOrder::getImeiNo, req.getImeiNo())
                .set(DiInsuranceOrder::getRemark, req.getRemark())
                .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.PENDING_AUDIT_ORDER.getCode())
                .eq(DiInsuranceOrder::getId, order.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new DiInsuranceOrder());
        if (success) {
            // 删除旧图片
            insuranceOrderPictureService.lambdaUpdate()
                    .set(AbstractBaseEntity::getDeleted, true)
                    .eq(DiInsuranceOrderPicture::getInsuranceOrderId, order.getId())
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .update(new DiInsuranceOrderPicture());
            // 保存新图片
            List<DiInsuranceOrderPicture> pictures = new ArrayList<>();
            req.getPictures().forEach(p -> pictures.add(DiInsuranceOrderPicture.builder()
                    .insuranceOrderId(order.getId())
                    .type(p.getType())
                    .url(p.getUrl())
                    .build()));
            insuranceOrderPictureService.saveBatch(pictures);
            // log
            orderLogService.addLog(
                    LoginUserContext.getUser().getId(),
                    order.getId(),
                    DiOrderStatusEnum.PENDING_AUDIT.getCode(),
                    OrderOperationEnum.DI_ORDER_EDIT_INFO_CUSTOMER.getCode(),
                    OrderOperationEnum.DI_ORDER_EDIT_INFO_CUSTOMER.getDesc(),
                    OrderOperationEnum.DI_ORDER_EDIT_INFO_CUSTOMER.getRemark()
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) {
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                .eq(DiInsuranceOrder::getId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        // 非本人提交的订单不可操作
        validateSelfOrder(order);
        // 校验订单状态
        validateOrderStatus(order, DiOrderStatusEnum.PENDING_PAYMENT.getCode());
        // 更新
        boolean success = insuranceOrderService.lambdaUpdate()
                .set(DiInsuranceOrder::getStatus, DiOrderStatusEnum.CANCELED.getCode())
                .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.MANUAL_CANCELED.getCode())
                .eq(DiInsuranceOrder::getId, orderId)
                .eq(DiInsuranceOrder::getStatus, DiOrderStatusEnum.PENDING_PAYMENT.getCode())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new DiInsuranceOrder());
        if (success) {
            // 删除payment
            insuranceOrderPaymentService.lambdaUpdate()
                    .set(AbstractBaseEntity::getDeleted, true)
                    .eq(DiInsuranceOrderPayment::getInsuranceOrderId, orderId)
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .update(new DiInsuranceOrderPayment());
            // 日志
            orderLogService.addLog(
                    LoginUserContext.getUser().getId(),
                    order.getId(),
                    DiOrderStatusEnum.CANCELED.getCode(),
                    OrderOperationEnum.DI_ORDER_CANCEL_MANUAL.getCode(),
                    OrderOperationEnum.DI_ORDER_CANCEL_MANUAL.getDesc(),
                    OrderOperationEnum.DI_ORDER_CANCEL_MANUAL.getRemark()
            );
        }
    }

    public InsuranceOrderRefundDetailVO getRefundInfo(Long orderId) {
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                .eq(DiInsuranceOrder::getId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        DiInsuranceOrderPayment payment = Optional.ofNullable(insuranceOrderPaymentService.lambdaQuery()
                .eq(DiInsuranceOrderPayment::getInsuranceOrderId, order.getId())
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        if (order.getStatus().equals(DiOrderStatusEnum.CANCELED.getCode())) {
            throw new BaseException(-1, "订单已关闭");
        }
        if (payment.getStatus().equals(DiOrderPayStatusEnum.REFUNDED.getCode())) {
            throw new BaseException(-1, "订单已退款");
        }
        InsuranceOrderRefundDetailVO vo = BeanUtil.copyProperties(order, InsuranceOrderRefundDetailVO.class);
        vo.setProductSkuRetailPriceStr(MoneyUtil.fenToYuan(vo.getProductSkuRetailPrice()));
        vo.setPriceStr(MoneyUtil.fenToYuan(vo.getPrice()));
        vo.setPayStatus(Optional.ofNullable(payment).map(DiInsuranceOrderPayment::getStatus).orElse(null));
        vo.setPayType(Optional.ofNullable(payment).map(DiInsuranceOrderPayment::getType).orElse(null));
        vo.setReasons(dictService.getInsuranceOrderRefundReasons());
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void applyRefund(InsuranceOrderRefundApplyReq req) {
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                .eq(DiInsuranceOrder::getId, req.getOrderId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        DiInsuranceOrderPayment payment = Optional.ofNullable(insuranceOrderPaymentService.lambdaQuery()
                .eq(DiInsuranceOrderPayment::getInsuranceOrderId, order.getId())
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        // 非本人提交的订单不可操作
        validateSelfOrder(order);
        // 校验订单状态
        validateOrderStatus(order, DiOrderStatusEnum.PENDING_AUDIT.getCode());
        // note 校验支付订单状态

        // 更新订单
        boolean success = insuranceOrderService.lambdaUpdate()
                .set(DiInsuranceOrder::getStatus, DiOrderStatusEnum.REFUNDING.getCode())
                .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.PENDING_AUDIT_REFUND.getCode())
                .eq(DiInsuranceOrder::getId, order.getId())
                .eq(DiInsuranceOrder::getStatus, DiOrderStatusEnum.PENDING_AUDIT.getCode())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new DiInsuranceOrder());
        if (success) {
            // 更新支付账单
            insuranceOrderPaymentService.lambdaUpdate()
                    .set(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.PENDING_REFUND.getCode())
                    .set(DiInsuranceOrderPayment::getRefundReason, req.getReason())
                    .set(DiInsuranceOrderPayment::getRefundRemark, req.getRemark())
                    .eq(DiInsuranceOrderPayment::getId, payment.getId())
                    .eq(DiInsuranceOrderPayment::getInsuranceOrderId, order.getId())
                    .eq(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.PAYED.getCode())
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .update(new DiInsuranceOrderPayment());
            // 日志
            orderLogService.addLog(
                    LoginUserContext.getUser().getId(),
                    order.getId(),
                    DiOrderStatusEnum.REFUNDING.getCode(),
                    OrderOperationEnum.DI_ORDER_REFUND_APPLY.getCode(),
                    OrderOperationEnum.DI_ORDER_REFUND_APPLY.getDesc(),
                    OrderOperationEnum.DI_ORDER_REFUND_APPLY.getRemark()
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public InsuranceOrderPaymentInfoVO applyWechatPay(Long orderId) {
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                .eq(DiInsuranceOrder::getId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        DiInsuranceOrderPayment payment = Optional.ofNullable(insuranceOrderPaymentService.lambdaQuery()
                .eq(DiInsuranceOrderPayment::getInsuranceOrderId, order.getId())
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        if (order.getStatus().equals(DiOrderStatusEnum.CANCELED.getCode())) {
            throw new BaseException(-1, "订单已关闭");
        }
        if (payment.getStatus().equals(DiOrderPayStatusEnum.PAYED.getCode())) {
            throw new BaseException(-1, "订单已支付");
        }
        if (payment.getStatus().equals(DiOrderPayStatusEnum.REFUNDED.getCode())) {
            throw new BaseException(-1, "订单已退款");
        }
        String outTradeNo = CommonWxUtils.createOrderNo();
        // 生成小程序二维码
        Map<String, String> params = Collections.singletonMap("id", order.getId().toString());
        String qrCodeUrl = wxUtils.genQrCode("pages/digitalInsuranceRefund/digitalInsuranceRefund?", params, 60);

        insuranceOrderPaymentService.lambdaUpdate()
                .set(DiInsuranceOrderPayment::getOutTradeNo, outTradeNo)
                .set(DiInsuranceOrderPayment::getQrCodeUrl, qrCodeUrl)
                .set(DiInsuranceOrderPayment::getType, DiOrderPayTypeEnum.WECHAT.getCode())
                .eq(DiInsuranceOrderPayment::getId, payment.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new DiInsuranceOrderPayment());

        return InsuranceOrderPaymentInfoVO.builder()
                .amount(order.getPrice())
                .amountStr(MoneyUtil.fenToYuan(order.getPrice()))
                .status(payment.getStatus())
                .productName(order.getProductName())
                .productSpec(order.getProductSpec())
                .insuranceName(order.getInsuranceName())
                .insuranceType(order.getInsuranceType())
                .insurancePeriod(order.getInsurancePeriod())
                .qrCodeUrl(qrCodeUrl).build();
    }

    public WxPayVO pay(Long orderId, String authCode) {
        String lockKey = StrUtil.format(RedisLockKeyConstants.WX_PAY_DI_PAYMENT_PAY_LOCK_KEY, orderId);
        return distributionLockUtil.lock(
                () -> payBase(orderId, authCode),
                0,
                () -> lockKey,
                "支付中",
                null);
    }

    private WxPayVO payBase(Long orderId, String authCode) {
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                .eq(DiInsuranceOrder::getId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        DiInsuranceOrderPayment payment = Optional.ofNullable(insuranceOrderPaymentService.lambdaQuery()
                .eq(DiInsuranceOrderPayment::getInsuranceOrderId, order.getId())
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        // 根据authCode获取用户openId
        UserAccount ua = userAccountProcessService.loginByCode(authCode);

        // 异步记录申请日志
        payApplyRecordService.addRecord(orderId,
                ua.getId(),
                PayEnterEnum.DI_ORDER_PAYMENT.getUrl(),
                payment.getOutTradeNo(),
                mchIdService.getJxzSubMchId(),
                PayEnterEnum.DI_ORDER_PAYMENT.getBizType());

        DiInsuranceOrderPayment newPayment = preQueryPayOrderStatus(payment);
        PartnerTransactionsRequest payReq = buildPayRequest(newPayment, ua.getOpenId());
        TransactionsResult.JsapiResult res = productWxPayService.partnerTransactions(payReq);
        return BeanUtil.copyProperties(res, WxPayVO.class);
    }

    /**
     * 查询支付订单状态，成功支付抛出；其他情况关闭订单重新生成商户订单号
     */
    private DiInsuranceOrderPayment preQueryPayOrderStatus(DiInsuranceOrderPayment payment) {
        String oldOtn = payment.getOutTradeNo();
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        PartnerTransactionsQueryRequest req = buildPayQueryReq(oldOtn, wxPayService);

        PartnerTransactionsResult res;
        try {
            res = productWxPayService.queryPartnerTransactions(req);
        } catch (BaseException e) {
            log.info("InsuranceOrderManageService.preQueryPayOrderStatus.error: {}", e.getMessage());
            return payment;
        }

        boolean payed = WxPayConstants.WxpayTradeStatus.SUCCESS.equals(res.getTradeState());

        if (payed) {
            log.info("InsuranceOrderManageService.preQueryPayOrderStatus.info: 订单已支付");
            throw new BaseException(-1, "订单已支付");
        }
        productWxPayService.closePartnerTransactions(payment.getOutTradeNo());
        String newOtn = CommonWxUtils.createOrderNo();
        payment.setOutTradeNo(newOtn);
        insuranceOrderPaymentService.lambdaUpdate()
                .set(DiInsuranceOrderPayment::getOutTradeNo, newOtn)
                .eq(DiInsuranceOrderPayment::getId, payment.getId())
                .update(new DiInsuranceOrderPayment());
        log.info("InsuranceOrderManageService.preQueryPayOrderStatus.info: 订单查询状态为-{}, 关闭支付订单, 重新生成订单号, oldOtn-{}, newOtn-{}",
                res.getTradeStateDesc(), oldOtn, newOtn);
        return payment;
    }

    private PartnerTransactionsRequest buildPayRequest(DiInsuranceOrderPayment payment, String openId) {
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        PartnerTransactionsRequest.Amount amount = new PartnerTransactionsRequest.Amount();
        amount.setCurrency(WxPayConstants.CurrencyType.CNY);
        amount.setTotal(payment.getAmount());
        //if (anyiConfig.isTestEnable()) {
        //    amount.setTotal(1);
        //}
        PartnerTransactionsRequest.Payer payer = new PartnerTransactionsRequest.Payer();
        payer.setSubOpenid(openId);
        return PartnerTransactionsRequest.builder()
                .spAppid(wxPayService.getConfig().getAppId())
                .spMchid(wxPayService.getConfig().getMchId())
                .subAppid(wxPayService.getConfig().getSubAppId())
                .subMchid(wxPayService.getConfig().getSubMchId())
                .notifyUrl(digitalInsuranceOrderPaymentNotifyUrl)
                .description(dictService.getDiInsuranceOrderPaymentDescription())
                .outTradeNo(payment.getOutTradeNo())
                .amount(amount)
                .payer(payer)
                // 30分钟后失效
                //.timeExpire(CommonWxUtils.getTimeExpireV3(payment.getCreateTime(), 30))
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void applyBalancePay(Long orderId) {
        String lockKey = StrUtil.format(RedisLockKeyConstants.WX_PAY_DI_PAYMENT_PAY_SUCCESS_LOCK_KEY, orderId);
        distributionLockUtil.lock(
                () -> balancePayBase(orderId),
                0,
                () -> lockKey,
                "支付中",
                null);
    }

    private void balancePayBase(Long orderId) {
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                .eq(DiInsuranceOrder::getId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        DiInsuranceOrderPayment payment = Optional.ofNullable(insuranceOrderPaymentService.lambdaQuery()
                .eq(DiInsuranceOrderPayment::getInsuranceOrderId, order.getId())
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        // 非本人提交的订单不可操作
        validateSelfOrder(order);
        if (order.getStatus().equals(DiOrderStatusEnum.CANCELED.getCode())) {
            throw new BaseException(-1, "订单已关闭");
        }
        if (payment.getStatus().equals(DiOrderPayStatusEnum.PAYED.getCode())) {
            throw new BaseException(-1, "订单已支付");
        }
        if (payment.getStatus().equals(DiOrderPayStatusEnum.REFUNDED.getCode())) {
            throw new BaseException(-1, "订单已退款");
        }
        insuranceOrderPaymentService.lambdaUpdate()
                .set(DiInsuranceOrderPayment::getType, DiOrderPayTypeEnum.ACCOUNT.getCode())
                .eq(DiInsuranceOrderPayment::getId, payment.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new DiInsuranceOrderPayment());
        boolean success = companyAccountChangeService.changeAccount(order.getStoreCompanyId(),
                CompanyAccountChangeEnum.insurance_pay,
                payment.getAmount().longValue(),
                order.getId(),
                dictService.getDiInsuranceOrderPaymentDescription());
        if (!success) {
            throw new BaseException(-1, "支付失败，资金账户余额不足，请及时充值。");
        }
        insuranceOrderService.lambdaUpdate()
                .set(DiInsuranceOrder::getStatus, DiOrderStatusEnum.PENDING_AUDIT.getCode())
                .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.PENDING_AUDIT_ORDER.getCode())
                .eq(DiInsuranceOrder::getId, order.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new DiInsuranceOrder());
        insuranceOrderPaymentService.lambdaUpdate()
                .set(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.PAYED.getCode())
                .set(DiInsuranceOrderPayment::getPayTime, DateUtil.date().toJdkDate())
                .eq(DiInsuranceOrderPayment::getId, payment.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new DiInsuranceOrderPayment());
        // log
        orderLogService.addLog(
                LoginUserContext.getUser().getId(),
                order.getId(),
                DiOrderStatusEnum.PENDING_AUDIT.getCode(),
                OrderOperationEnum.DI_ORDER_PAY.getCode(),
                OrderOperationEnum.DI_ORDER_PAY.getDesc(),
                OrderOperationEnum.DI_ORDER_PAY.getRemark()
        );
    }

    private PartnerTransactionsQueryRequest buildPayQueryReq(String outTradeNo, WxPayService wxPayService) {
        PartnerTransactionsQueryRequest req = new PartnerTransactionsQueryRequest();
        req.setOutTradeNo(outTradeNo);
        req.setSpMchid(wxPayService.getConfig().getMchId());
        req.setSubMchid(wxPayService.getConfig().getSubMchId());
        return req;
    }

    private void validateSelfOrder(DiInsuranceOrder order) {
        if (order == null) {
            return;
        }
        if (!order.getStoreEmployeeId().equals(LoginUserContext.getUser().getId())) {
            throw new BaseException(BizError.MB_NO_PERMISSION);
        }
    }

    private void validateOrderStatus(DiInsuranceOrder order, Integer... statusList) {
        if (order == null || ArrayUtil.isEmpty(statusList)) {
            return;
        }
        if (!ArrayUtil.contains(statusList, order.getStatus())) {
            throw new BaseException(BizError.MB_ORDER_OPERATION_ERROR_STATUS);
        }
    }

    private void validateOrderSubStatus(DiInsuranceOrder order, Integer... statusList) {
        if (order == null || ArrayUtil.isEmpty(statusList)) {
            return;
        }
        if (!ArrayUtil.contains(statusList, order.getSubStatus())) {
            throw new BaseException(BizError.MB_ORDER_OPERATION_ERROR_STATUS);
        }
    }
}