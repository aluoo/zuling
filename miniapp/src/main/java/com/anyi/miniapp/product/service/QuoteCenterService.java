package com.anyi.miniapp.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.constant.RedisLockKeyConstants;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.OrderLog;
import com.anyi.common.product.domain.OrderQuotePriceLog;
import com.anyi.common.product.domain.dto.ProductDTO;
import com.anyi.common.product.domain.dto.QuoteCommissionInfoDTO;
import com.anyi.common.product.domain.enums.OrderOperationEnum;
import com.anyi.common.product.domain.enums.OrderQuoteLogStatusEnum;
import com.anyi.common.product.domain.enums.OrderQuoteLogSubStatusEnum;
import com.anyi.common.product.domain.request.RecyclerCancelQuoteReq;
import com.anyi.common.product.domain.request.RecyclerQuoteLogListReq;
import com.anyi.common.product.domain.request.RecyclerQuoteReq;
import com.anyi.common.product.domain.request.RecyclerRejectQuoteReq;
import com.anyi.common.product.domain.response.*;
import com.anyi.common.product.service.*;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.common.service.QuotePriceCalculationService;
import com.anyi.common.util.CurrencyUtils;
import com.anyi.common.util.DistributionLockUtil;
import com.anyi.common.util.MoneyUtil;
import com.anyi.miniapp.interceptor.UserManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/11
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class QuoteCenterService {
    @Autowired
    private OrderQuotePriceLogService orderQuotePriceLogService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderLogService orderlogService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderOptionService orderOptionService;
    @Autowired
    private CommonSysDictService dictService;
    @Autowired
    private QuotePriceCalculationService quotePriceCalculationService;
    @Autowired
    private DistributionLockUtil distributionLockUtil;

    public RecyclerQuotePriceInfoVO calcQuotePriceInfo(RecyclerQuoteReq req) {
        int priceInt = CurrencyUtils.multiply(req.getPrice(), CurrencyUtils.HUNDRED).intValue();
        int quoteWarningThresholdPrice = dictService.getQuoteWarningThresholdPrice();
        RecyclerQuotePriceInfoVO vo = RecyclerQuotePriceInfoVO.builder()
                .orderQuoteLogId(req.getOrderQuoteLogId())
                .price(NumberUtil.decimalFormat("0.00", req.getPrice()))
                .warningThresholdPrice(MoneyUtil.fenToYuan(quoteWarningThresholdPrice))
                .warning(priceInt > quoteWarningThresholdPrice)
                .platformCommission("0")
                .platformCommissionRule("0.00")
                .build();
        // set commission
        QuoteCommissionInfoDTO commissionInfo = quotePriceCalculationService.calcCommissionByType(priceInt, CommissionBizType.PLAT_SERVICE, UserManager.getCurrentUser().getCompanyId());
        if (commissionInfo == null) {
            return vo;
        }
        vo.setPlatformCommission(MoneyUtil.fenToYuan(commissionInfo.getActualCommission()));
        vo.setPlatformCommissionRule(commissionInfo.getActualCommissionRuleFormat());
        return vo;
    }

    private void checkQuoteExpired(DateTime now, Date createTime) {
        int quoteExpiredMinutes = dictService.getQuoteExpiredMinutes();
        DateTime quoteExpiredTime = DateUtil.offset(createTime, DateField.MINUTE, quoteExpiredMinutes);
        int compare = DateUtil.compare(now, quoteExpiredTime);
        if (compare > 0) {
            throw new BaseException(BizError.MB_ORDER_QUOTE_ALREADY_CLOSED);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void quote(RecyclerQuoteReq req) {
        OrderQuotePriceLog quotePriceLog = Optional.ofNullable(orderQuotePriceLogService.getById(req.getOrderQuoteLogId()))
                .orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));

        if (quotePriceLog.getStatus().equals(OrderQuoteLogStatusEnum.CANCELED.getCode())) {
            throw new BaseException(BizError.MB_ORDER_QUOTE_ALREADY_CLOSED);
        }
        if (quotePriceLog.getStatus().equals(OrderQuoteLogStatusEnum.PENDING_QUOTE.getCode())) {
            throw new BaseException(-1, "请先抢单");
        }
        if (quotePriceLog.getStatus().equals(OrderQuoteLogStatusEnum.QUOTED.getCode())) {
            throw new BaseException(-1, "已报价");
        }
        if (quotePriceLog.getEmployeeId() != null && !quotePriceLog.getEmployeeId().equals(UserManager.getCurrentUser().getEmployeeId())) {
            throw new BaseException(BizError.MB_NO_PERMISSION);
        }
        Order order = Optional.ofNullable(orderService.getById(quotePriceLog.getOrderId()))
                .orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        if (!order.getQuotable()) {
            throw new BaseException(BizError.MB_ORDER_QUOTE_ALREADY_CLOSED);
        }
        DateTime now = DateUtil.date();
        // check expired
        checkQuoteExpired(now, order.getCreateTime());

        // 输入的报价
        int originalQuotePrice = CurrencyUtils.multiply(req.getPrice(), CurrencyUtils.HUNDRED).intValue();
        // 计算服务费
        QuoteCommissionInfoDTO platformCommissionInfo = quotePriceCalculationService.calcCommissionByType(originalQuotePrice, CommissionBizType.PLAT_SERVICE, UserManager.getCurrentUser().getCompanyId());
        // 计算门店压价
        Long storeCompanyId = order.getStoreCompanyId();
        QuoteCommissionInfoDTO storeCommissionInfo = quotePriceCalculationService.calcCommissionByType(originalQuotePrice, CommissionBizType.PHONE_DOWN, storeCompanyId);
        // 计算平台补贴
        QuoteCommissionInfoDTO platformSubsidyInfo = quotePriceCalculationService.calcPlatformSubsidyByType(originalQuotePrice, CommissionBizType.PHONE_DOWN, storeCompanyId);
        // 计算总体报价时长
        long quoteTimeSpent = DateUtil.between(order.getCreateTime(), now, DateUnit.MS);
        // 计算实际报价时长 = 报价时间-抢单时间
        OrderLog lockQuoteLog = orderlogService.lambdaQuery()
                .eq(OrderLog::getCreateBy, UserManager.getCurrentUser().getEmployeeId())
                .eq(OrderLog::getOrderId, req.getOrderQuoteLogId())
                .eq(OrderLog::getOperationStatus, OrderOperationEnum.LOCK_QUOTE.getCode())
                .orderByDesc(OrderLog::getCreateTime)
                .last("limit 1")
                .one();
        long quoteTimeSpentReal = DateUtil.between(lockQuoteLog.getCreateTime(), now, DateUnit.MS);
        // 计算成交价 = 原始报价 - 门店压价
        int finalPrice = originalQuotePrice - storeCommissionInfo.getActualCommission();
        // 计算实际付款价格 = 原始报价 + 平台抽成金额
        int actualPaymentPrice = originalQuotePrice + platformCommissionInfo.getActualCommission();
        // 校验余额是否充足
        quotePriceCalculationService.validateBalance(UserManager.getCurrentUser().getCompanyId(), actualPaymentPrice);
        // 更新报价
        boolean success = orderQuotePriceLogService.lambdaUpdate()
                .set(OrderQuotePriceLog::getEmployeeId, UserManager.getCurrentUser().getEmployeeId())
                // 原始报价
                .set(OrderQuotePriceLog::getOriginalQuotePrice, originalQuotePrice)
                // 平台抽成金额
                .set(OrderQuotePriceLog::getPlatformCommission, platformCommissionInfo.getActualCommission())
                // 平台抽成规则
                .set(OrderQuotePriceLog::getPlatformCommissionRule, platformCommissionInfo.getActualCommissionRule())
                // 门店压价金额
                .set(OrderQuotePriceLog::getCommission, storeCommissionInfo.getActualCommission())
                // 门店压价规则
                .set(OrderQuotePriceLog::getCommissionRule, storeCommissionInfo.getActualCommissionRule())
                // 平台补贴
                .set(OrderQuotePriceLog::getPlatformSubsidyPrice, platformSubsidyInfo.getPlatformSubsidyPrice())
                // 成交价
                .set(OrderQuotePriceLog::getFinalPrice, finalPrice)
                // 实际付款价格
                .set(OrderQuotePriceLog::getActualPaymentPrice, actualPaymentPrice)
                .set(OrderQuotePriceLog::getQuoted, true)
                .set(OrderQuotePriceLog::getQuoteTime, now)
                .set(OrderQuotePriceLog::getQuoteTimeSpent, quoteTimeSpent)
                .set(OrderQuotePriceLog::getQuoteTimeSpentReal, quoteTimeSpentReal)
                .set(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.QUOTED.getCode())
                .eq(OrderQuotePriceLog::getId, req.getOrderQuoteLogId())
                .eq(OrderQuotePriceLog::getEmployeeId, UserManager.getCurrentUser().getEmployeeId())
                .eq(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.QUOTING.getCode())
                .update(new OrderQuotePriceLog());
        if (success) {
            // 日志
            orderlogService.addLog(UserManager.getCurrentUser().getEmployeeId(),
                    req.getOrderQuoteLogId(),
                    OrderQuoteLogStatusEnum.QUOTED.getCode(),
                    OrderOperationEnum.QUOTE.getCode(),
                    OrderOperationEnum.QUOTE.getDesc(),
                    StrUtil.isNotBlank(req.getQuoteRemark())?req.getQuoteRemark():OrderOperationEnum.QUOTE.getRemark());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelQuote(RecyclerCancelQuoteReq req) {
        boolean success = orderQuotePriceLogService.lambdaUpdate()
                .set(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.PENDING_QUOTE.getCode())
                .set(OrderQuotePriceLog::getSubStatus, OrderQuoteLogSubStatusEnum.PENDING_QUOTE.getCode())
                .set(OrderQuotePriceLog::getEmployeeId, null)
                .eq(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.QUOTING.getCode())
                .eq(OrderQuotePriceLog::getEmployeeId, UserManager.getCurrentUser().getEmployeeId())
                .eq(OrderQuotePriceLog::getId, req.getOrderQuoteLogId())
                .update(new OrderQuotePriceLog());
        if (!success) {
            throw new BaseException(BizError.MB_ORDER_ERROR_STATUS);
        }
        // 日志
        String reqReason = StrUtil.isBlank(req.getReason()) ? "无" : req.getReason();
        String reqRemark = StrUtil.isBlank(req.getRemark()) ? "无" : req.getRemark();
        String remark = StrUtil.format(OrderOperationEnum.CANCEL_LOCK_QUOTE.getRemark(), reqReason, reqRemark);
        orderlogService.addLog(UserManager.getCurrentUser().getEmployeeId(),
                req.getOrderQuoteLogId(),
                OrderQuoteLogStatusEnum.PENDING_QUOTE.getCode(),
                OrderOperationEnum.CANCEL_LOCK_QUOTE.getCode(),
                OrderOperationEnum.CANCEL_LOCK_QUOTE.getDesc(),
                remark);
    }

    @Transactional(rollbackFor = Exception.class)
    public void rejectQuote(RecyclerRejectQuoteReq req) {
        OrderQuotePriceLog quotePriceLog = Optional.ofNullable(orderQuotePriceLogService.getById(req.getOrderQuoteLogId()))
                .orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));

        if (quotePriceLog.getStatus().equals(OrderQuoteLogStatusEnum.CANCELED.getCode())) {
            throw new BaseException(BizError.MB_ORDER_QUOTE_ALREADY_CLOSED);
        }
        if (quotePriceLog.getStatus().equals(OrderQuoteLogStatusEnum.PENDING_QUOTE.getCode())) {
            throw new BaseException(-1, "请先抢单");
        }
        if (quotePriceLog.getStatus().equals(OrderQuoteLogStatusEnum.QUOTED.getCode())) {
            String msg = "已报价";
            if (quotePriceLog.getSubStatus().equals(OrderQuoteLogSubStatusEnum.REJECT_QUOTE.getCode())) {
                msg = "已拒绝";
            }
            throw new BaseException(-1, msg);
        }
        if (quotePriceLog.getEmployeeId() != null && !quotePriceLog.getEmployeeId().equals(UserManager.getCurrentUser().getEmployeeId())) {
            throw new BaseException(BizError.MB_NO_PERMISSION);
        }
        Order order = Optional.ofNullable(orderService.getById(quotePriceLog.getOrderId()))
                .orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        if (!order.getQuotable()) {
            throw new BaseException(BizError.MB_ORDER_QUOTE_ALREADY_CLOSED);
        }
        // 计算报价时长
        DateTime now = DateUtil.date();
        long quoteTimeSpent = DateUtil.between(order.getCreateTime(), now, DateUnit.MS);
        // 计算实际报价时长 = 报价时间-抢单时间
        OrderLog lockQuoteLog = orderlogService.lambdaQuery()
                .eq(OrderLog::getCreateBy, UserManager.getCurrentUser().getEmployeeId())
                .eq(OrderLog::getOrderId, req.getOrderQuoteLogId())
                .eq(OrderLog::getOperationStatus, OrderOperationEnum.LOCK_QUOTE.getCode())
                .orderByDesc(OrderLog::getCreateTime)
                .last("limit 1")
                .one();
        long quoteTimeSpentReal = DateUtil.between(lockQuoteLog.getCreateTime(), now, DateUnit.MS);
        boolean success = orderQuotePriceLogService.lambdaUpdate()
                .set(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.QUOTED.getCode())
                .set(OrderQuotePriceLog::getSubStatus, OrderQuoteLogSubStatusEnum.REJECT_QUOTE.getCode())
                // 原始报价
                .set(OrderQuotePriceLog::getOriginalQuotePrice, 0)
                // 平台抽成金额
                .set(OrderQuotePriceLog::getPlatformCommission, 0)
                // 平台抽成规则
                .set(OrderQuotePriceLog::getPlatformCommissionRule, 0)
                // 门店压价金额
                .set(OrderQuotePriceLog::getCommission, 0)
                // 门店压价规则
                .set(OrderQuotePriceLog::getCommissionRule, 0)
                // 平台补贴
                .set(OrderQuotePriceLog::getPlatformSubsidyPrice, 0)
                // 成交价
                .set(OrderQuotePriceLog::getFinalPrice, 0)
                // 实际付款价格
                .set(OrderQuotePriceLog::getActualPaymentPrice, 0)
                .set(OrderQuotePriceLog::getQuoted, true)
                .set(OrderQuotePriceLog::getQuoteTime, now)
                .set(OrderQuotePriceLog::getQuoteTimeSpent, quoteTimeSpent)
                .set(OrderQuotePriceLog::getQuoteTimeSpentReal, quoteTimeSpentReal)
                .eq(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.QUOTING.getCode())
                .eq(OrderQuotePriceLog::getEmployeeId, UserManager.getCurrentUser().getEmployeeId())
                .eq(OrderQuotePriceLog::getId, req.getOrderQuoteLogId())
                .update(new OrderQuotePriceLog());
        if (!success) {
            throw new BaseException(BizError.MB_ORDER_ERROR_STATUS);
        }
        // 日志
        orderlogService.addLog(UserManager.getCurrentUser().getEmployeeId(),
                req.getOrderQuoteLogId(),
                OrderQuoteLogStatusEnum.QUOTED.getCode(),
                OrderOperationEnum.REJECT_QUOTE.getCode(),
                OrderOperationEnum.REJECT_QUOTE.getDesc(),
                req.getReason());
    }

    @Transactional(rollbackFor = Exception.class)
    public RecyclerLockQuoteVO lockQuote(Long orderQuoteLogId) {
        String lockKey = StrUtil.format(RedisLockKeyConstants.MB_ORDER_RECYCLER_QUOTE_LOCK_KEY, orderQuoteLogId);
        return distributionLockUtil.lock(
                () -> lockQuoteBase(orderQuoteLogId),
                0,
                () -> lockKey,
                BizError.MB_ORDER_ALREADY_LOCK_ERROR.getMessage(),
                null);
    }

    @Transactional(rollbackFor = Exception.class)
    public RecyclerLockQuoteVO lockQuoteBase(Long orderQuoteLogId) {
        // 判断自己是否有报价中的单，有则抛出
        boolean exists = orderQuotePriceLogService.lambdaQuery()
                .eq(OrderQuotePriceLog::getEmployeeId, UserManager.getCurrentUser().getEmployeeId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.QUOTING.getCode())
                .exists();
        if (exists) {
            throw new BaseException(-1, "您当前有未报价完成订单，请先去报价");
        }
        // 抢单
        OrderQuotePriceLog quotePriceLog = Optional.ofNullable(orderQuotePriceLogService.lambdaQuery()
                .eq(OrderQuotePriceLog::getId, orderQuoteLogId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .last("for update")
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        // 判断订单状态，不正确抛出
        if (quotePriceLog.getStatus().equals(OrderQuoteLogStatusEnum.CANCELED.getCode())) {
            throw new BaseException(BizError.MB_ORDER_QUOTE_ALREADY_CLOSED);
        }
        if (!quotePriceLog.getStatus().equals(OrderQuoteLogStatusEnum.PENDING_QUOTE.getCode())) {
            throw new BaseException(BizError.MB_ORDER_ALREADY_LOCK_ERROR);
        }
        // 更新状态
        boolean success = orderQuotePriceLogService.lambdaUpdate()
                .set(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.QUOTING.getCode())
                .set(OrderQuotePriceLog::getSubStatus, OrderQuoteLogSubStatusEnum.QUOTING.getCode())
                .set(OrderQuotePriceLog::getEmployeeId, UserManager.getCurrentUser().getEmployeeId())
                .eq(OrderQuotePriceLog::getId, orderQuoteLogId)
                .eq(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.PENDING_QUOTE.getCode())
                .update(new OrderQuotePriceLog());
        if (!success) {
            throw new BaseException(BizError.MB_ORDER_ALREADY_LOCK_ERROR);
        }
        // 日志
        orderlogService.addLog(UserManager.getCurrentUser().getEmployeeId(),
                orderQuoteLogId,
                OrderQuoteLogStatusEnum.QUOTING.getCode(),
                OrderOperationEnum.LOCK_QUOTE.getCode(),
                OrderOperationEnum.LOCK_QUOTE.getDesc(),
                OrderOperationEnum.LOCK_QUOTE.getRemark());

        // 返回剩余时间（报价过期时间）
        Order order = orderService.lambdaQuery()
                .select(Order::getId, AbstractBaseEntity::getCreateTime)
                .eq(Order::getId, quotePriceLog.getOrderId())
                .one();
        // check expired
        checkQuoteExpired(DateUtil.date(), order.getCreateTime());
        RecyclerLockQuoteVO vo = RecyclerLockQuoteVO.builder().createTime(order.getCreateTime()).build();
        vo.setQuoteExpiredTime(dictService.getQuoteExpiredMinutes());
        return vo;
    }

    public RecyclerQuoteCountInfoVO countQuoteLog(RecyclerQuoteLogListReq req) {
        Long employeeId = req.getOnlySelf() ? UserManager.getCurrentUser().getEmployeeId() : null;
        Long companyId = UserManager.getCurrentUser().getCompanyId();
        RecyclerQuoteCountInfoVO vo = orderQuotePriceLogService.countQuoteLog(companyId, employeeId);
        vo.setRecyclerCompanyId(companyId);
        // 只展示当天的作废数量
        Long todayCanceledCount = orderQuotePriceLogService.lambdaQuery()
                .eq(OrderQuotePriceLog::getCompanyId, companyId)
                .eq(OrderQuotePriceLog::getEmployeeId, employeeId)
                .eq(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.CANCELED.getCode())
                .eq(AbstractBaseEntity::getDeleted, false)
                .between(AbstractBaseEntity::getCreateTime, DateUtil.beginOfDay(DateUtil.date()).toJdkDate(), DateUtil.endOfDay(DateUtil.date()).toJdkDate())
                .count();
        vo.setCanceledCount(Optional.ofNullable(todayCanceledCount).map(Long::intValue).orElse(0));
        return vo;
    }

    public PageInfo<RecyclerQuoteLogInfoVO> listOrder(RecyclerQuoteLogListReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        LambdaQueryWrapper<OrderQuotePriceLog> qw = new LambdaQueryWrapper<OrderQuotePriceLog>()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(OrderQuotePriceLog::getCompanyId, UserManager.getCurrentUser().getCompanyId())
                .eq(req.getOnlySelf() != null && req.getOnlySelf(), OrderQuotePriceLog::getEmployeeId, UserManager.getCurrentUser().getEmployeeId())
                .eq(req.getStatus() != null, OrderQuotePriceLog::getStatus, req.getStatus());
        if (req.getStatus() != null && req.getStatus().equals(OrderQuoteLogStatusEnum.PENDING_QUOTE.getCode())) {
            qw.orderByAsc(AbstractBaseEntity::getCreateTime);
        } else {
            qw.orderByDesc(AbstractBaseEntity::getCreateTime);
            qw.orderByDesc(AbstractBaseEntity::getUpdateTime);
        }
        List<OrderQuotePriceLog> list = orderQuotePriceLogService.list(qw);
        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }
        List<RecyclerQuoteLogInfoVO> vos = BeanUtil.copyToList(list, RecyclerQuoteLogInfoVO.class);
        List<Long> orderIds = vos.stream().map(RecyclerQuoteLogInfoVO::getOrderId).collect(Collectors.toList());
        Map<Long, OrderDetailVO> orderDetailInfoMap = buildOrderDetailMap(orderIds);
        List<Long> recyclerEmployeeIds = vos.stream().map(RecyclerQuoteLogInfoVO::getEmployeeId).collect(Collectors.toList());
        Map<Long, Employee> recyclerEmployeeInfoMap = employeeService.getEmployeeInfoMap(recyclerEmployeeIds);
        vos.forEach(vo -> {
            // 设置报价师名称
            if (vo.getEmployeeId() != null) {
                vo.setEmployeeName(Optional.ofNullable(recyclerEmployeeInfoMap.get(vo.getEmployeeId())).map(Employee::getName).orElse(null));
            }
            // 设置订单信息
            vo.setOrderInfo(orderDetailInfoMap.get(vo.getOrderId()));
            // 设置操作按钮权限
            vo.setOperationBtn(UserManager.getCurrentUser().getEmployeeId());
            // 设置报价用时
            vo.setQuoteTimeSpentReal();
            // 设置价格信息
            vo.setPriceInfo();
        });
        PageInfo<RecyclerQuoteLogInfoVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

    private Map<Long, OrderDetailVO> buildOrderDetailMap(List<Long> orderIds) {
        List<OrderDetailVO> details = buildOrderDetails(orderIds);
        if (CollUtil.isEmpty(details)) {
            return Collections.emptyMap();
        }
        return details.stream().collect(Collectors.toMap(OrderBaseVO::getId, Function.identity()));
    }

    private List<OrderDetailVO> buildOrderDetails(List<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return null;
        }
        List<Order> list = orderService.lambdaQuery().in(Order::getId, orderIds).list();
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        List<Long> productIds = list.stream().map(Order::getProductId).collect(Collectors.toList());
        Map<Long, ProductDTO> productInfoMap = productService.getProductInfoMap(productIds);
        List<Long> storeCompanyIds = list.stream().map(Order::getStoreCompanyId).collect(Collectors.toList());
        Map<Long, Company> companyInfoMap = companyService.getCompanyInfoMap(storeCompanyIds);
        Map<Long, String> orderSpecInfoMap = orderOptionService.buildOrderSpecInfoMap(orderIds);
        List<OrderDetailVO> details = BeanUtil.copyToList(list, OrderDetailVO.class);
        int productOrderExpiredMinutes = dictService.getProductOrderExpiredMinutes();
        int quoteExpiredMinutes = dictService.getQuoteExpiredMinutes();
        details.forEach(vo -> {
            vo.setStoreCompanyName(Optional.ofNullable(companyInfoMap.get(vo.getStoreCompanyId())).map(Company::getName).orElse(null));
            // 设置规格信息
            vo.setSpec(orderSpecInfoMap.get(vo.getId()));
            // 设置品牌logo
            vo.setBrandLogo(Optional.ofNullable(productInfoMap.get(vo.getProductId())).map(ProductDTO::getBrandLogo).orElse(null));
            // 设置过期时间
            vo.setExpiredTime(productOrderExpiredMinutes, quoteExpiredMinutes);
        });
        return details;
    }
}