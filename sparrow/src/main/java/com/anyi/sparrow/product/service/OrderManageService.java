package com.anyi.sparrow.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.oss.FileUploader;
import com.anyi.common.product.domain.*;
import com.anyi.common.product.domain.dto.*;
import com.anyi.common.product.domain.enums.*;
import com.anyi.common.product.domain.request.*;
import com.anyi.common.product.domain.response.OrderApplyVO;
import com.anyi.common.product.domain.response.OrderDetailVO;
import com.anyi.common.product.domain.response.ReceivePaymentInfoVO;
import com.anyi.common.product.service.*;
import com.anyi.common.service.AbstractOrderInfoManage;
import com.anyi.common.service.CommissionProcessService;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.common.service.OrderNoProcessService;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.user.domain.UserAccount;
import com.anyi.common.user.service.UserAccountService;
import com.anyi.common.util.MoneyUtil;
import com.anyi.common.util.SpringContextUtil;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.wechat.service.TemplateMsgService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/23
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderManageService extends AbstractOrderInfoManage {
    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private OrderNoProcessService orderNoProcessService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private CommissionProcessService commissionProcessService;
    @Autowired
    private CommonSysDictService dictService;
    @Autowired
    private OrderOptionService orderOptionService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private OrderQuotePriceLogService orderQuotePriceLogService;
    @Autowired
    private OrderCustomerReceivePaymentService orderCustomerReceivePaymentService;
    @Autowired
    private OrderCustomerRefundPaymentService orderCustomerRefundPaymentService;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private FileUploader fileUploader;
    @Autowired
    private TemplateMsgService templateMsgService;
    @Autowired
    private UserAccountService userAccountService;
    private final static String TMPDIR = System.getProperty("java.io.tmpdir");

    public List<OrderQuoteInfoDTO> listQuoteInfoByOrder(OrderQuoteQueryReq req) {
        List<OrderQuoteInfoDTO> list = orderQuotePriceLogService.listQuoteInfoByOrderId(req);
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<Long> quoteLogIds = list.stream()
                .map(OrderQuoteInfoDTO::getId)
                .collect(Collectors.toList());

        List<Long> rejectQuoteLogIds = list.stream()
                .filter(o -> o.getStatus().equals(OrderQuoteLogStatusEnum.QUOTED.getCode()) && o.getSubStatus().equals(OrderQuoteLogSubStatusEnum.REJECT_QUOTE.getCode()))
                .map(OrderQuoteInfoDTO::getId)
                .collect(Collectors.toList());
        List<OrderLog> rejectLogs = new ArrayList<>();
        if (CollUtil.isNotEmpty(rejectQuoteLogIds)) {
            rejectLogs = orderLogService.lambdaQuery()
                    .in(OrderLog::getOrderId, rejectQuoteLogIds)
                    .eq(OrderLog::getStatus, OrderQuoteLogStatusEnum.QUOTED.getCode())
                    .eq(OrderLog::getOperationStatus, OrderOperationEnum.REJECT_QUOTE.getCode())
                    .list();
        }
        //报价备注
        List<OrderLog> quoteLogs = orderLogService.lambdaQuery()
                .in(OrderLog::getOrderId, quoteLogIds)
                .eq(OrderLog::getStatus, OrderQuoteLogStatusEnum.QUOTED.getCode())
                .eq(OrderLog::getOperationStatus, OrderOperationEnum.QUOTE.getCode())
                .list();

        Map<Long, OrderLog> quoteLogMap = quoteLogs.stream().collect(Collectors.toMap(OrderLog::getOrderId, Function.identity()));
        Map<Long, OrderLog> rejectLogMap = rejectLogs.stream().collect(Collectors.toMap(OrderLog::getOrderId, Function.identity()));
        list.forEach(dto -> {
            // 设置报价用时
            dto.setQuoteTimeSpent();
            // 设置价格信息
            dto.setPriceInfo();
            // 设置拒绝原因
            dto.setRejectReason(Optional.ofNullable(rejectLogMap.get(dto.getId())).map(OrderLog::getRemark).orElse(null));
            // 设置报价备注
            dto.setQuoteRemark(Optional.ofNullable(quoteLogMap.get(dto.getId())).map(OrderLog::getRemark).orElse(null));
        });
        return list;
    }

    public PageInfo<OrderDetailVO> listOrder(OrderQueryReq req) {
        // Long companyManagerId = companyService.getCompanyManagerId(LoginUserContext.getUser().getCompanyId());
        // boolean isManager = LoginUserContext.getUser().getId().equals(companyManagerId);
        List<Long> storeCompanyIds = new ArrayList<>();
        storeCompanyIds.add(LoginUserContext.getUser().getCompanyId());
        if (LoginUserContext.getUser().getCompanyType().equals(CompanyType.CHAIN.getCode())) {
            Collection<Long> ids = companyService.getSingleStoreIdsByChainStoreId(LoginUserContext.getUser().getCompanyId());
            if (CollUtil.isNotEmpty(ids)) {
                storeCompanyIds.addAll(ids);
            }
        }
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<Order> list = orderService.lambdaQuery()
                // .eq(Order::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .in(Order::getStoreCompanyId, storeCompanyIds)
                .eq(AbstractBaseEntity::getDeleted, false)
                // 非店长，只能查看自己的订单
                // .eq(!isManager, Order::getStoreEmployeeId, LoginUserContext.getUser().getId())
                .eq(req.getOnlySelf() != null && req.getOnlySelf(), Order::getStoreEmployeeId, LoginUserContext.getUser().getId())
                .eq(req.getStatus() != null, Order::getStatus, req.getStatus())
                .ge(req.getBeginTime() != null, Order::getCreateTime, req.getBeginTime())
                .le(req.getEndTime() != null, Order::getCreateTime, req.getEndTime())
                .and(StrUtil.isNotBlank(req.getKeyword()), wp -> wp.eq(Order::getId, Long.valueOf(req.getKeyword())).or().eq(Order::getOrderNo, req.getKeyword()))
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }

        List<OrderDetailVO> vos = BeanUtil.copyToList(list, OrderDetailVO.class);
        List<Long> storeEmployeeIds = vos.stream().map(OrderDetailVO::getStoreEmployeeId).collect(Collectors.toList());
        List<Long> orderIds = vos.stream().map(OrderDetailVO::getId).collect(Collectors.toList());
        List<Long> productIds = list.stream().map(Order::getProductId).collect(Collectors.toList());
        Map<Long, List<String>> orderImagesInfoMap = orderOptionService.buildOrderImagesInfoMap(orderIds);
        Map<Long, String> orderSpecInfoMap = orderOptionService.buildOrderSpecInfoMap(orderIds);
        Map<Long, ProductDTO> productInfoMap = productService.getProductInfoMap(productIds);
        Map<Long, Employee> employeeInfoMap = employeeService.getEmployeeInfoMap(storeEmployeeIds);
        Map<Long, Integer> orderQuotePriceLogCountMap = orderQuotePriceLogService.countGroupByOrderIds(orderIds);
        Map<Long, ShippingOrderDTO> shippingOrderInfoMap = shippingOrderService.buildShippingOrderMapByOrderIds(orderIds);
        int productOrderExpiredMinutes = dictService.getProductOrderExpiredMinutes();
        int quoteExpiredMinutes = dictService.getQuoteExpiredMinutes();
        vos.forEach(vo -> {
            // 设置过期时间
            vo.setExpiredTime(productOrderExpiredMinutes, quoteExpiredMinutes);
            // 设置下单人信息
            vo.setStoreEmployeeName(Optional.ofNullable(employeeInfoMap.get(vo.getStoreEmployeeId())).map(Employee::getName).orElse(null));
            // 设置品牌logo
            super.setBrandLogo(vo, productInfoMap);
            // 设置图片信息
            super.setImages(vo, orderImagesInfoMap);
            // 设置规格信息
            super.setSpec(vo, orderSpecInfoMap);
            // 设置已经报价的回收商数量
            vo.setQuotedRecyclerCount(Optional.ofNullable(orderQuotePriceLogCountMap.get(vo.getId())).orElse(0));
            // 设置成交价格
            vo.setFinalPriceStr();
            // 设置物流信息
            setOrderExpressInfo(shippingOrderInfoMap, vo);
            // 设置操作按钮权限
            super.setOperationBtn(vo, LoginUserContext.getUser().getId(), LoginUserContext.getUser().getCompanyId());
        });

        PageInfo<OrderDetailVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

    private void setOrderExpressInfo(Map<Long, ShippingOrderDTO> map, OrderDetailVO vo) {
        if (vo.getStatus().equals(OrderStatusEnum.PENDING_RECEIPT.getCode()) || vo.getStatus().equals(OrderStatusEnum.FINISHED.getCode())) {
            ShippingOrderDTO dto = map.get(vo.getId());
            if (dto == null) {
                return;
            }
            OrderDetailVO.ExpressInfo exp = OrderDetailVO.ExpressInfo.builder()
                    .shippingOrderId(dto.getId())
                    .trackCompanyName(dto.getTrackCompanyName())
                    .trackNo(dto.getTrackNo())
                    .createTime(dto.getCreateTime())
                    .applyLogisticsTime(dto.getApplyLogisticsTime())
                    .build();
            vo.setExpressInfo(exp);
        }
    }

    public OrderDetailVO detailOrder(Long orderId) {
        Order order = Optional.ofNullable(orderService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Order::getId, orderId)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        OrderDetailVO vo = new OrderDetailVO();
        BeanUtil.copyProperties(order, vo);
        boolean directManager = companyService.isDirectManager(LoginUserContext.getUser().getCompanyId(), LoginUserContext.getUser().getId());
        if (!directManager) {
            // 只给连锁负责人账号一人看（不包括连锁负责人下属部门管理员）；
            // 其他所有层级账号隐藏【压价】+【补贴】字段；
            vo.setCommission(null);
            vo.setPlatformSubsidyPrice(null);
        }
        // 设置订单基础信息
        super.setBaseOrderInfo(vo);
        // 设置订单选项信息
        super.setOrderOptionInfo(vo);

        List<OrderQuoteInfoDTO> recyclerQuoteInfoList = listQuoteInfoByOrder(OrderQuoteQueryReq.builder().orderId(orderId).build());
        if (CollUtil.isNotEmpty(recyclerQuoteInfoList)) {
            if (!Arrays.asList(OrderStatusEnum.UNCHECKED.getCode(), OrderStatusEnum.PENDING_PAYMENT.getCode()).contains(vo.getStatus())) {
                recyclerQuoteInfoList = recyclerQuoteInfoList.stream().filter(OrderQuoteInfoDTO::getQuoted).collect(Collectors.toList());
            }
        }
        // 设置报价信息列表
        vo.setRecyclerQuoteInfoList(recyclerQuoteInfoList);

        // 设置交易信息（确认报价的回收商）
        if (vo.getQuotePriceLogId() != null) {
            List<OrderQuoteInfoDTO> list = listQuoteInfoByOrder(OrderQuoteQueryReq.builder().id(vo.getQuotePriceLogId()).build());
            vo.setRecyclerInfo(CollUtil.isNotEmpty(list) ? list.get(0) : null);
        }

        // 设置已经报价的回收商数量
        vo.setQuotedRecyclerCount(orderQuotePriceLogService.countByOrderId(orderId));
        // 设置物流信息
        Map<Long, ShippingOrderDTO> shippingOrderInfoMap = shippingOrderService.buildShippingOrderMapByOrderIds(Collections.singletonList(orderId));
        setOrderExpressInfo(shippingOrderInfoMap, vo);

        // 设置收款信息
        super.setReceivePaymentInfo(vo);

        // 设置退款信息
        super.setRefundPaymentInfo(vo);

        // 设置操作按钮权限
        super.setOperationBtn(vo, LoginUserContext.getUser().getId(), LoginUserContext.getUser().getCompanyId());

        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderApplyVO applyOrder(OrderApplyReq req) {
        Long orderId = snowflakeIdService.nextId();
        Order order = Order.builder()
                .id(orderId)
                .storeEmployeeId(LoginUserContext.getUser().getId())
                .storeCompanyId(LoginUserContext.getUser().getCompanyId())
                .orderNo(orderNoProcessService.nextOrderNo())
                .status(OrderStatusEnum.UNCHECKED.getCode())
                .productId(req.getProductId())
                .productName(req.getProductName())
                .remark(req.getRemark())
                // 是否绑码
                .bound(false)
                // 是否核验
                .verified(false)
                // 是否可以报价
                .quotable(true)
                .build();
        orderService.save(order);

        // 快照 snapshot 原始optionals的值
        List<Tree<Long>> optionals = optionService.buildOptionTreeBaseByProductId(req.getProductId());

        // 规格/图片/功能选项信息
        List<OrderOption> orderOptions = saveCheckOptions(req, orderId, optionals);

        // 快照暂时不要，json格式转换不出原Tree对象，不好处理
        // orderOptionSnapshotService.saveSnapshot(orderId, optionals);

        // 日志
        orderLogService.addLog(LoginUserContext.getUser().getId(),
                orderId,
                OrderStatusEnum.UNCHECKED.getCode(),
                OrderOperationEnum.CREATE.getCode(),
                OrderOperationEnum.CREATE.getDesc(),
                OrderOperationEnum.CREATE.getRemark());

        // 获取所有回收商
        List<Company> recyclers = companyService.listAllRecyclers();
        List<Long> recyclerIds = recyclers.stream().map(Company::getId).collect(Collectors.toList());
        // 创建报价记录给回收商，默认推给所有，后续可能只推给部分指定回收商
        InitOrderQuotePriceLogDTO quotePriceLogDTO = orderQuotePriceLogService.initQuoteLog(orderId, recyclerIds);
        log.info("applyOrder.info: 对{}个回收商推送了报价订单", quotePriceLogDTO.getInitCount());
        // 报价记录创建日志
        orderLogService.addLogBatch(LoginUserContext.getUser().getId(),
                quotePriceLogDTO.getOrderQuotePriceLogIds(),
                OrderQuoteLogStatusEnum.PENDING_QUOTE.getCode(),
                OrderOperationEnum.INIT_QUOTE.getCode(),
                OrderOperationEnum.INIT_QUOTE.getDesc(),
                OrderOperationEnum.INIT_QUOTE.getRemark());

        // 异步推送消息给回收商员工
        int pushCount = pushMsg(recyclerIds, orderOptions, order);
        log.info("applyOrder.info: 对{}个员工推送了报价订单消息", pushCount);

        List<OrderApplyVO.RecyclerInfo> recyclerInfos = new ArrayList<>();
        recyclers.forEach(o -> {
            OrderApplyVO.RecyclerInfo info = OrderApplyVO.RecyclerInfo.builder()
                    .id(o.getId())
                    .name(o.getName())
                    .avatar(o.getFrontUrl())
                    .build();
            recyclerInfos.add(info);
        });

        return OrderApplyVO.builder().orderId(orderId).recyclers(recyclerInfos).build();
    }

    private int pushMsg(List<Long> recyclerIds, List<OrderOption> orderOptions, Order order) {
        if (CollUtil.isEmpty(recyclerIds)) {
            return 0;
        }
        List<Employee> employees = employeeService.lambdaQuery()
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode())
                .in(Employee::getCompanyId, recyclerIds)
                .list();
        if (CollUtil.isEmpty(employees)) {
            return 0;
        }
        List<String> mobiles = employees.stream().map(Employee::getMobileNumber).collect(Collectors.toList());
        List<UserAccount> accounts = userAccountService.lambdaQuery()
                .select(UserAccount::getId, UserAccount::getOfficialOpenId)
                .in(UserAccount::getMobileNumber, mobiles)
                .list();
        if (CollUtil.isEmpty(accounts)) {
            return 0;
        }
        List<String> openIds = accounts.stream().map(UserAccount::getOfficialOpenId).filter(StrUtil::isNotBlank).collect(Collectors.toList());
        if (CollUtil.isEmpty(openIds)) {
            return 0;
        }
        String spec = orderOptionService.buildOrderSpecInfo(orderOptions);
        DateTime now = DateUtil.date();
        String orderId = order.getId().toString();
        String orderNo = order.getOrderNo();
        String productName = StrUtil.format("{} {}", order.getProductName(), spec);
        String companyName = Optional.ofNullable(companyService.lambdaQuery().eq(Company::getId, order.getStoreCompanyId()).one()).map(Company::getName).orElse(null);
        String createTime = DateUtil.format(now, DatePattern.CHINESE_DATE_FORMATTER);

        openIds.forEach(openId -> ThreadUtil.execAsync(() -> templateMsgService.pushOrderMsg(openId, productName, orderNo,companyName, createTime, orderId)));
        return openIds.size();
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(OrderCancelReq req) {
        Order order = Optional.ofNullable(
                orderService.lambdaQuery()
                        .eq(AbstractBaseEntity::getDeleted, false)
                        .eq(Order::getId, req.getId())
                        .one()
        ).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        // 非本人提交的订单不可操作
        validateSelfOrder(order);
        // 订单在待确认，待收款时可以取消订单
        List<Integer> status = Arrays.asList(OrderStatusEnum.UNCHECKED.getCode(), OrderStatusEnum.PENDING_PAYMENT.getCode());
        validateOrderStatus(order, OrderStatusEnum.UNCHECKED.getCode(), OrderStatusEnum.PENDING_PAYMENT.getCode());
        boolean success = orderService.lambdaUpdate()
                .set(Order::getStatus, OrderStatusEnum.CANCELED.getCode())
                .set(Order::getQuotable, false)
                .eq(Order::getId, req.getId())
                .eq(Order::getDeleted, false)
                .in(Order::getStatus, status)
                .eq(Order::getStoreEmployeeId, LoginUserContext.getUser().getId())
                .update(new Order());
        if (success) {
            // 订单日志
            orderLogService.addLog(LoginUserContext.getUser().getId(),
                    req.getId(),
                    OrderStatusEnum.CANCELED.getCode(),
                    OrderOperationEnum.MANUAL_CLOSE.getCode(),
                    OrderOperationEnum.MANUAL_CLOSE.getDesc(),
                    OrderOperationEnum.MANUAL_CLOSE.getRemark());
            // 更新报价日志
            List<Long> canceledQuoteLogIds = orderQuotePriceLogService.cancelTrade(req.getId());
            // 报价日志 已作废-交易取消
            orderLogService.addLogBatch(LoginUserContext.getUser().getId(),
                    canceledQuoteLogIds,
                    OrderQuoteLogStatusEnum.CANCELED.getCode(),
                    OrderOperationEnum.QUOTE_CANCELED_CANCEL_TRADE.getCode(),
                    OrderOperationEnum.QUOTE_CANCELED_CANCEL_TRADE.getDesc(),
                    OrderOperationEnum.QUOTE_CANCELED_CANCEL_TRADE.getRemark());
            if (order.getStatus().equals(OrderStatusEnum.PENDING_PAYMENT.getCode())) {
                // 已确认交易的，需要回滚资金流水
                OrderQuotePriceLog confirmedQuotePriceLog = orderQuotePriceLogService.getById(order.getQuotePriceLogId());
                commissionProcessService.rollbackRecyclerAccount(order.getRecyclerCompanyId(), confirmedQuotePriceLog, order.getProductName());
                // 回滚待结算佣金记录
                commissionProcessService.cancelWaitSettle(order.getId(), order.getProductName());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderApplyVO confirmOrderQuote(OrderConfirmQuoteReq req) {
        Order order = Optional.ofNullable(orderService.lambdaQuery()
                .eq(Order::getId, req.getId())
                .eq(Order::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        validateSelfOrder(order);
        validateOrderStatus(order, OrderStatusEnum.UNCHECKED.getCode());

        OrderQuotePriceLog confirmedQuotePriceLog = Optional.ofNullable(orderQuotePriceLogService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(OrderQuotePriceLog::getId, req.getQuotePriceLogId())
                .eq(OrderQuotePriceLog::getOrderId, req.getId())
                .one()).orElseThrow(() -> new BaseException(BizError.MB_QUOTE_LOG_NOT_EXISTS));
        if (confirmedQuotePriceLog.getOriginalQuotePrice() <= 0) {
            throw new BaseException(BizError.MB_ORDER_CANNOT_CONFIRM_TRADE);
        }
        // 更新订单
        boolean success = orderService.lambdaUpdate()
                .set(Order::getStatus, OrderStatusEnum.PENDING_PAYMENT.getCode())
                .set(Order::getImeiNo, req.getImeiNo())
                .set(Order::getOriginalQuotePrice, confirmedQuotePriceLog.getOriginalQuotePrice())
                .set(Order::getFinalPrice, confirmedQuotePriceLog.getFinalPrice())
                .set(Order::getCommission, confirmedQuotePriceLog.getCommission())
                .set(Order::getPlatformSubsidyPrice, confirmedQuotePriceLog.getPlatformSubsidyPrice())
                .set(Order::getRecyclerEmployeeId, confirmedQuotePriceLog.getEmployeeId())
                .set(Order::getRecyclerCompanyId, confirmedQuotePriceLog.getCompanyId())
                .set(Order::getQuotePriceLogId, req.getQuotePriceLogId())
                .set(Order::getFinishQuoteTime, new Date())
                .set(Order::getQuotable, false)
                .eq(Order::getId, req.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Order::getStatus, OrderStatusEnum.UNCHECKED.getCode())
                .update(new Order());
        if (!success) {
            throw new BaseException(BizError.MB_ORDER_ERROR_STATUS);
        }
        // 更新报价记录
        ConfirmQuoteInfoDTO confirmQuoteInfoDTO = orderQuotePriceLogService.confirmQuote(order.getId(), confirmedQuotePriceLog.getId());
        // 扣除对应回收商金额
        commissionProcessService.deductionRecyclerAccount(confirmedQuotePriceLog.getCompanyId(), confirmedQuotePriceLog, order.getProductName());
        // 生成待结算佣金
        commissionProcessService.createWaitSettle(order, confirmedQuotePriceLog);
        // 插入日志
        orderLogService.addLog(LoginUserContext.getUser().getId(),
                order.getId(),
                OrderStatusEnum.PENDING_PAYMENT.getCode(),
                OrderOperationEnum.CONFIRM_QUOTE.getCode(),
                OrderOperationEnum.CONFIRM_QUOTE.getDesc(),
                OrderOperationEnum.CONFIRM_QUOTE.getRemark());
        // 报价日志-已确认交易
        orderLogService.addLog(LoginUserContext.getUser().getId(),
                confirmedQuotePriceLog.getId(),
                OrderQuoteLogStatusEnum.QUOTED.getCode(),
                OrderOperationEnum.QUOTE_CONFIRMED.getCode(),
                OrderOperationEnum.QUOTE_CONFIRMED.getDesc(),
                OrderOperationEnum.QUOTE_CONFIRMED.getRemark());
        // 报价日志-未入选
        orderLogService.addLogBatch(LoginUserContext.getUser().getId(),
                confirmQuoteInfoDTO.getNotConfirmedQuoteIds(),
                OrderQuoteLogStatusEnum.CANCELED.getCode(),
                OrderOperationEnum.QUOTE_CANCELED_NOT_CONFIRMED.getCode(),
                OrderOperationEnum.QUOTE_CANCELED_NOT_CONFIRMED.getDesc(),
                OrderOperationEnum.QUOTE_CANCELED_NOT_CONFIRMED.getRemark());

        return OrderApplyVO.builder().orderId(req.getId()).build();
    }

    public ReceivePaymentInfoVO getReceivePaymentInfo(Long orderId) {
        ReceivePaymentInfoVO vo = ReceivePaymentInfoVO.builder().build();
        Order order = Optional.ofNullable(orderService.lambdaQuery()
                .eq(Order::getId, orderId)
                // .eq(Order::getStatus, OrderStatusEnum.PENDING_PAYMENT.getCode())
                .eq(Order::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        BeanUtil.copyProperties(order, vo);
        vo.setAmount(vo.getFinalPrice());
        vo.setAmountStr();
        Map<Long, String> orderSpecInfoMap = orderOptionService.buildOrderSpecInfoMap(Collections.singletonList(orderId));
        // 设置规格信息
        vo.setSpec(orderSpecInfoMap.get(orderId));
        // 设置门店信息
        super.setStoreInfo(vo);
        OrderCustomerReceivePayment payment = orderCustomerReceivePaymentService.getAvailableByOrderId(orderId);
        vo.setReceivePaymentInfo(payment);
        return vo;
    }



    @Transactional(rollbackFor = Exception.class)
    public ReceivePaymentInfoVO applyReceivePayment(ReceivePaymentApplyReq req) {
        Order order = Optional.ofNullable(orderService.lambdaQuery()
                .eq(Order::getId, req.getId())
                .eq(Order::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        // 非本人提交的订单不可操作
        validateSelfOrder(order);
        validateOrderStatus(order, OrderStatusEnum.PENDING_PAYMENT.getCode());

        OrderCustomerReceivePayment payment = orderCustomerReceivePaymentService.getAvailableByOrderId(order.getId());
        ReceivePaymentInfoVO vo = BeanUtil.copyProperties(order, ReceivePaymentInfoVO.class);
        vo.setAmount(vo.getFinalPrice());
        vo.setAmountStr();
        if (payment != null) {
            vo.setQrCodeUrl(payment.getQrCodeUrl());
            return vo;
        }
        Long paymentId = snowflakeIdService.nextId();
        String qrCodeUrl = generateQrCode(order.getId(), paymentId, order.getFinalPrice());
        payment  = OrderCustomerReceivePayment.builder()
                .id(paymentId)
                .orderId(order.getId())
                .outBizNo(snowflakeIdService.nextId().toString())
                .name(req.getName())
                .mobile(req.getMobile())
                .idCard(req.getIdCard())
                .amount(order.getFinalPrice())
                .type(PaymentTypeEnum.ALIPAY.getCode())
                .status(ReceivePaymentStatusEnum.PENDING_PAYMENT.getCode())
                .qrCodeUrl(qrCodeUrl)
                .build();
        boolean success = orderCustomerReceivePaymentService.save(payment);
        // log
        if (success) {
            orderLogService.addLog(LoginUserContext.getUser().getId(),
                    order.getId(),
                    OrderStatusEnum.PENDING_PAYMENT.getCode(),
                    OrderOperationEnum.APPLY_PAY_INFO.getCode(),
                    OrderOperationEnum.APPLY_PAY_INFO.getDesc(),
                    OrderOperationEnum.APPLY_PAY_INFO.getRemark());
        }

        vo.setQrCodeUrl(qrCodeUrl);
        return vo;
    }

    private String generateQrCode(Long orderId, Long paymentId, Integer amount) {
        String baseUrl = "https://www.anyichuxing.com/collection/#/?orderId={}&paymentId={}&env={}&amount={}";
        String url = StrUtil.format(baseUrl, orderId, paymentId, SpringContextUtil.isProduction() ? "production" : "test", MoneyUtil.fenToYuan(amount));
        String fileName = snowflakeIdService.nextId() + ".jpeg";
        File imgFile = new File(TMPDIR + File.separator + fileName);
        QrCodeUtil.generate(url, 300, 300, imgFile);
        return fileUploader.upload(imgFile, fileName, "aycx-wechat");
    }

    private List<OrderOption> saveCheckOptions(OrderApplyReq req, Long orderId, List<Tree<Long>> optionals) {
        if (CollUtil.isEmpty(req.getCheckedOptions())) {
            return new ArrayList<>();
        }
        List<OrderOption> ops = new ArrayList<>();
        req.getCheckedOptions().forEach(op -> {
            OrderOption oi = OrderOption.builder()
                    .id(snowflakeIdService.nextId())
                    .orderId(orderId)
                    .optionId(op.getOptionId())
                    .code(op.getCode())
                    .value(op.getValue())
                    .build();
            Tree<Long> node = optionals.get(0).getNode(oi.getOptionId());
            String title = getTitle(node, oi.getCode());
            oi.setTitle(title);
            ops.add(oi);
        });

        // 保存对应的机况选项
        OrderOption machineStatus = ops.stream().filter(o -> o.getCode().equals(OptionCodeEnum.MCHSTAT.getCode())).findAny().orElse(null);
        if (machineStatus == null) {
            Long optionId = req.getCheckedOptions().get(0).getOptionId();
            Tree<Long> firstNode = optionals.get(0).getNode(optionId);
            OrderOption oi = orderOptionService.buildMachineStatusOptionInfo(firstNode, OptionCodeEnum.MCHSTAT, snowflakeIdService.nextId(), orderId);
            if (oi != null) {
                ops.add(oi);
            }
        }

        if (CollUtil.isNotEmpty(ops)) {
            orderOptionService.saveBatch(ops);
        }
        return ops;
    }

    private String getTitle(Tree<Long> node, String code) {
        switch (EnumUtil.getBy(OptionCodeEnum::getCode, code)) {
            case FUNC_ERR:
            case FUNC_OK: {
                return orderOptionService.getOptionTitleByCode(node, OptionCodeEnum.FUNC_TITLE);
            }
            case IMG:
            case OTHER_IMG: {
                return node.getName().toString();
            }
            case RAM: {
                return orderOptionService.getOptionTitleByCode(node, OptionCodeEnum.RAM_TITLE);
            }
            case ROM: {
                return orderOptionService.getOptionTitleByCode(node, OptionCodeEnum.ROM_TITLE);
            }
            case MCHSTAT: {
                return orderOptionService.getOptionTitleByCode(node, OptionCodeEnum.MCHSTAT_TITLE);
            }
            default: {
                return null;
            }
        }
    }

    private void validateSelfOrder(Order order) {
        if (order == null) {
            return;
        }
        if (!order.getStoreEmployeeId().equals(LoginUserContext.getUser().getId())) {
            throw new BaseException(BizError.MB_NO_PERMISSION);
        }
    }

    private void validateOrderStatus(Order order, Integer... statusList) {
        if (order == null || ArrayUtil.isEmpty(statusList)) {
            return;
        }
        if (!ArrayUtil.contains(statusList, order.getStatus())) {
            throw new BaseException(BizError.MB_ORDER_OPERATION_ERROR_STATUS);
        }
    }
}