package com.anyi.common.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.jdl.JdlService;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.OrderCustomerReceivePayment;
import com.anyi.common.product.domain.OrderCustomerRefundPayment;
import com.anyi.common.product.domain.OrderOption;
import com.anyi.common.product.domain.dto.ProductDTO;
import com.anyi.common.product.domain.enums.OptionCodeEnum;
import com.anyi.common.product.domain.enums.OrderStatusEnum;
import com.anyi.common.product.domain.enums.ShippingOrderStatusEnum;
import com.anyi.common.product.domain.enums.ShippingTypeEnum;
import com.anyi.common.product.domain.response.OrderBaseVO;
import com.anyi.common.product.domain.response.OrderDetailVO;
import com.anyi.common.product.domain.response.ShippingOrderDetailVO;
import com.anyi.common.product.service.*;
import com.anyi.common.util.MoneyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/16
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public abstract class AbstractOrderInfoManage {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderOptionService orderOptionService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CommonSysDictService dictService;
    @Autowired
    protected JdlService jdlService;
    @Autowired
    private OrderCustomerReceivePaymentService orderCustomerReceivePaymentService;
    @Autowired
    private OrderCustomerRefundPaymentService orderCustomerRefundPaymentService;

    public static final String JD_COMPANY_CODE = "JD";

    protected void setOperationBtn(OrderBaseVO vo, Long employeeId, Long companyId) {
        vo.setOperationBtn(employeeId, companyId);
    }

    protected boolean needJdTrace(ShippingOrderDetailVO vo) {
        return vo != null
                && vo.getStatus() > ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode()
                && vo.getShippingType() != null
                && vo.getShippingType().equals(ShippingTypeEnum.ONLINE.getCode())
                && StrUtil.isNotBlank(vo.getTrackNo())
                && JD_COMPANY_CODE.equals(vo.getTrackCompanyCode());
    }

    protected void setLogisticsTrace(ShippingOrderDetailVO vo) {
        // 设置物流轨迹
        if (needJdTrace(vo)) {
            vo.setLogisticsTrace(jdlService.getOrderTrace(vo.getTrackNo()));
        }
    }

    protected void setStoreInfo(OrderBaseVO vo) {
        Map<Long, Employee> employeeInfoMap = employeeService.getEmployeeInfoMap(Stream.of(vo.getStoreEmployeeId(), vo.getRecyclerEmployeeId()).filter(Objects::nonNull).collect(Collectors.toSet()));
        Map<Long, Company> companyInfoMap = companyService.getCompanyInfoMap(Stream.of(vo.getStoreCompanyId(), vo.getRecyclerCompanyId()).filter(Objects::nonNull).collect(Collectors.toSet()));

        // 设置门店信息
        vo.setCompanyInfo(companyInfoMap);
        vo.setEmployeeInfo(employeeInfoMap);
    }

    protected void setBrandLogo(OrderDetailVO vo) {
        Map<Long, ProductDTO> productInfoMap = productService.getProductInfoMap(Collections.singletonList(vo.getProductId()));
        // 设置品牌logo
        vo.setBrandLogo(Optional.ofNullable(productInfoMap.get(vo.getProductId())).map(ProductDTO::getBrandLogo).orElse(null));
    }

    protected void setBrandLogo(OrderDetailVO vo, Map<Long, ProductDTO> map) {
        if (CollUtil.isEmpty(map)) {
            return;
        }
        vo.setBrandLogo(Optional.ofNullable(map.get(vo.getProductId())).map(ProductDTO::getBrandLogo).orElse(null));
    }

    protected void setSpec(OrderDetailVO vo) {
        Map<Long, String> orderSpecInfoMap = orderOptionService.buildOrderSpecInfoMap(Collections.singletonList(vo.getId()));
        // 设置规格信息
        vo.setSpec(orderSpecInfoMap.get(vo.getId()));
    }

    protected void setSpec(OrderDetailVO vo, Map<Long, String> map) {
        if (CollUtil.isEmpty(map)) {
            return;
        }
        vo.setSpec(map.get(vo.getId()));
    }

    protected void setImages(OrderDetailVO vo) {
        Map<Long, List<String>> imagesInfoMap = orderOptionService.buildOrderImagesInfoMap(Collections.singletonList(vo.getId()));
        // 设置图片信息
        vo.setImages(imagesInfoMap.get(vo.getId()));
    }

    protected void setImages(OrderDetailVO vo, Map<Long, List<String>> map) {
        if (CollUtil.isEmpty(map)) {
            return;
        }
        vo.setImages(map.get(vo.getId()));
    }

    protected void setBaseOrderInfo(OrderDetailVO vo) {
        Long orderId = vo.getId();
        Map<Long, String> orderSpecInfoMap = orderOptionService.buildOrderSpecInfoMap(Collections.singletonList(orderId));
        Map<Long, List<String>> imagesInfoMap = orderOptionService.buildOrderImagesInfoMap(Collections.singletonList(orderId));
        Map<Long, ProductDTO> productInfoMap = productService.getProductInfoMap(Collections.singletonList(vo.getProductId()));

        // 设置品牌logo
        this.setBrandLogo(vo, productInfoMap);
        // 设置规格信息
        this.setSpec(vo, orderSpecInfoMap);
        // 设置图片信息
        this.setImages(vo, imagesInfoMap);
        // 设置门店信息
        this.setStoreInfo(vo);
        // 设置过期时间
        vo.setExpiredTime(dictService.getProductOrderExpiredMinutes(), dictService.getQuoteExpiredMinutes());
        // 设置价格信息
        vo.setPriceInfo();
    }

    protected List<OrderDetailVO> setOrderInfo(List<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return null;
        }
        // 设置订单信息
        List<Order> orders = orderService.lambdaQuery().in(Order::getId, orderIds).list();
        List<OrderDetailVO> details = BeanUtil.copyToList(orders, OrderDetailVO.class);
        List<Long> productIds = orders.stream().map(Order::getProductId).collect(Collectors.toList());
        Map<Long, String> orderSpecInfoMap = orderOptionService.buildOrderSpecInfoMap(orderIds);
        Map<Long, ProductDTO> productInfoMap = productService.getProductInfoMap(productIds);
        Set<Long> companyIds = OrderBaseVO.extractIds(details, OrderBaseVO::getStoreCompanyId, OrderBaseVO::getRecyclerCompanyId);
        Map<Long, Company> companyInfoMap = companyService.getCompanyInfoMap(companyIds);
        details.forEach(o -> {
            o.setCompanyInfo(companyInfoMap);
            // 设置品牌logo
            this.setBrandLogo(o, productInfoMap);
            // 设置规格信息
            this.setSpec(o, orderSpecInfoMap);
            // 设置价格信息
            o.setPriceInfo();
        });
        return details;
    }

    protected void setOrderOptionInfo(OrderDetailVO vo) {
        List<OrderOption> allOptions = orderOptionService.getAllOptionsByOrderId(vo.getId());
        vo.setRom(Optional.ofNullable(orderOptionService.getByCode(allOptions, OptionCodeEnum.ROM)).map(OrderOption::getValue).orElse(null));
        vo.setRam(Optional.ofNullable(orderOptionService.getByCode(allOptions, OptionCodeEnum.RAM)).map(OrderOption::getValue).orElse(null));
        vo.setColor(Optional.ofNullable(orderOptionService.getByCode(allOptions, OptionCodeEnum.COLOR)).map(OrderOption::getValue).orElse(null));
        vo.setMasterImages(orderOptionService.listValueByCode(allOptions, OptionCodeEnum.IMG));
        vo.setOtherImages(orderOptionService.listValueByCode(allOptions, OptionCodeEnum.OTHER_IMG));
        List<OrderOption> reportOptions = orderOptionService.listByCode(allOptions, Arrays.asList(OptionCodeEnum.FUNC_OK, OptionCodeEnum.FUNC_ERR));
        if (CollUtil.isNotEmpty(reportOptions)) {
            List<OrderDetailVO.Report> collect = reportOptions.stream().map(o -> {
                boolean isOk = o.getCode().equals(OptionCodeEnum.FUNC_OK.getCode());
                String errMsg = isOk ? null : o.getValue();
                return OrderDetailVO.Report.builder().title(o.getTitle()).isOk(isOk).errorMessage(errMsg).build();
            }).collect(Collectors.toList());
            vo.setReports(collect);
        }
    }

    protected void setReceivePaymentInfo(OrderDetailVO vo) {
        if (vo.getStatus() < OrderStatusEnum.PENDING_PAYMENT.getCode()) {
            return;
        }
        OrderCustomerReceivePayment payment = orderCustomerReceivePaymentService.getAvailableByOrderId(vo.getId());
        if (payment == null) {
            return;
        }
        OrderDetailVO.ReceivePaymentInfo info = OrderDetailVO.ReceivePaymentInfo.builder()
                .name(payment.getName())
                .mobile(payment.getMobile())
                .idCard(payment.getIdCard())
                .amount(payment.getAmount())
                .amountStr(MoneyUtil.fenToYuan(payment.getAmount() != null ? payment.getAmount() : 0))
                .receiveStatus(payment.getStatus())
                .receiveTime(payment.getReceiveTime())
                .qrCodeUrl(payment.getQrCodeUrl())
                .build();
        vo.setPaymentInfo(info);
    }

    protected void setRefundPaymentInfo(OrderDetailVO vo) {
        if (!vo.getStatus().equals(OrderStatusEnum.REFUNDED.getCode())) {
            return;
        }
        OrderCustomerRefundPayment payment = orderCustomerRefundPaymentService.getAvailableByOrderId(vo.getId());
        if (payment == null) {
            return;
        }
        OrderDetailVO.RefundPaymentInfo info = OrderDetailVO.RefundPaymentInfo.builder()
                .amount(payment.getAmount())
                .amountStr(MoneyUtil.fenToYuan(payment.getAmount() != null ? payment.getAmount() : 0))
                .refundStatus(payment.getStatus())
                .payTime(payment.getPayTime())
                .qrCodeUrl(payment.getQrCodeUrl())
                .reason(payment.getReason())
                .build();
        vo.setRefundInfo(info);
    }
}