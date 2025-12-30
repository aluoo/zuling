package com.anyi.sparrow.withdraw.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.anyi.common.account.constant.EmployAccountChangeEnum;
import com.anyi.common.account.domain.EmployeeAccount;
import com.anyi.common.account.service.EmployeeAccountChangeService;
import com.anyi.common.account.service.IEmployeeAccountService;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.qfu.QfuService;
import com.anyi.common.qfu.dto.QfuPaymentInvokeReq;
import com.anyi.common.qfu.dto.QfuPaymentInvokeResp;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.util.MoneyUtil;
import com.anyi.common.util.SnowflakeIdWorker;
import com.anyi.common.withdraw.domain.dto.StateSnapshotDTO;
import com.anyi.sparrow.account.service.impl.IEmployeeRealNameVerificationService;
import com.anyi.sparrow.assist.system.service.SysDictService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.utils.DateUtils;
import com.anyi.sparrow.organize.employee.dao.DeptDao;
import com.anyi.sparrow.organize.employee.service.EmService;
import com.anyi.common.withdraw.domain.enums.WithdrawApplyStatusEnum;
import com.anyi.sparrow.withdraw.constant.WithdrawCardStatusEnum;
import com.anyi.sparrow.withdraw.constant.WithdrawCardTypeEnum;
import com.anyi.sparrow.withdraw.domain.WithdrawEmployeeApply;
import com.anyi.sparrow.withdraw.domain.WithdrawEmployeeCard;
import com.anyi.sparrow.withdraw.dto.*;
import com.anyi.sparrow.withdraw.req.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 提现模块服务
 *
 * @author shenbh
 * @since 2023/3/23
 */
@Service
@Slf4j
public class WithdrawService {

    @Autowired
    private IEmployeeAccountService employeeAccountService;
    @Autowired
    private IWithdrawEmployeeCardService withdrawEmployeeCardService;
    @Autowired
    private IWithdrawEmployeeApplyService withdrawEmployeeApplyService;
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private EmService emService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmployeeAccountChangeService employeeAccountChangeService;

    @Autowired
    private SysDictService dictService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private IEmployeeRealNameVerificationService employeeRealNameVerificationService;

    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private QfuService qfuService;


    public WithdrawIndexDTO getAppWithdrawIndexData(Long employeeId) {

        WithdrawIndexDTO dto = new WithdrawIndexDTO();

        EmployeeAccount employeeAccount = employeeAccountService.getByEmployeeId(employeeId);

        dto.setAbleBalance(employeeAccount.getAbleBalance());

        Employee employee = emService.getById(employeeId);

        /*if(employee.getCompanyType().intValue()==1 && (employee.getLevel().intValue()==1 || employee.getLevel().intValue()==2)){
            throw new BusinessException(99999,"没有提现的权限");
        }*/

        List<WithdrawEmployeeCard> cards = withdrawEmployeeCardService.findAvailableCards(employeeId);

        if (CollectionUtil.isNotEmpty(cards)) {

            Long employeeCompanyId = employee.getCompanyId();
            Company company = companyService.getById(employeeCompanyId);
            // 判断是否能开票
            if (!employee.getPublicFlag()) {
                //            不能开票 返回cardType1 和返回cardType2
                List<WithdrawEmployeeCard> cards1 = filterCardType(cards, WithdrawCardTypeEnum.BANK_CARD);
                List<WithdrawEmployeeCard> cards2 = filterCardType(cards, WithdrawCardTypeEnum.ZFB_ACCOUNT);
                List<AvaliableCardDTO> cardType1 = getAvaliableCardDTOS(cards1);

                List<AvaliableCardDTO> cardType2 = getAvaliableCardDTOS(cards2);
                fillAccWithdraw(cardType1, employeeId);
                fillAccWithdraw(cardType2, employeeId);
                dto.setCardType1(cardType1);
                dto.setCardType2(cardType2);

                Long dailyLimit = getLimit("withdraw_daily_Limit_times", false, 1L);
                dto.setLimitTimes(dailyLimit.intValue());

                Integer companyType = employee.getCompanyType();
                Integer employeeType = employee.getType();
                Integer role = null;
                if (Arrays.asList(2, 3).contains(companyType)) {
                    switch (employeeType) {
                        case 1:
                        case 3:
                            // 负责人
                            role = 1;
                            break;
                        case 2:
                        case 4:
                            // 员工
                            role = 2;
                            break;
                        default:
                            break;
                    }
                }
                Long dailyLimitYuanMin = getLimit("withdraw_daily_Limit_amount_min", false, 2000L);
                // Long dailyLimitYuanMax = getLimit("withdraw_daily_Limit_amount_max", false, 300000L);
                // 提现上限，前端有用到该字段来限制提交按钮，因兼容需要，将上限写死大数跳过按钮限制逻辑，由后端做实际上限控制
                Long dailyLimitYuanMax = 1000000000L;
                if (role != null && role == 1) {
                    // 负责人
                    dailyLimitYuanMin = getLimit("withdraw_daily_Limit_amount_min_store_leader", false, 100000L);
                    // dailyLimitYuanMax = getLimit("withdraw_daily_Limit_amount_max_store_leader", false, 1000000L);
                }
                if (role != null && role == 2) {
                    // 员工
                    dailyLimitYuanMin = getLimit("withdraw_daily_Limit_amount_min_staff", false, 1000L);
                    // dailyLimitYuanMax = getLimit("withdraw_daily_Limit_amount_max_staff", false, 100000L);
                }
                dto.setLimitMin(dailyLimitYuanMin);
                dto.setLimitMax(dailyLimitYuanMax);
            } else {
                // 能开票
                List<WithdrawEmployeeCard> cards3 = filterCardType(cards, WithdrawCardTypeEnum.PUBLIC_ACCOUNT);
                List<AvaliableCardDTO> cardType3 = getAvaliableCardDTOS(cards3);
                fillAccWithdraw(cardType3, employeeId);
                dto.setCardType3(cardType3);

                Long dailyLimit = getLimit("withdraw_daily_Limit_times", true, 1L);//  "withdraw_daily_Limit_times"
                Long dailyLimitYuanMin = getLimit("withdraw_daily_Limit_amount_min", true, 50000L); //  "withdraw_daily_Limit_amount_min"
                Long dailyLimitYuanMax = getLimit("withdraw_daily_Limit_amount_max", true, 5000000L);//"withdraw_daily_Limit_amount_max"

                dto.setLimitMin(dailyLimitYuanMin);
                dto.setLimitMax(dailyLimitYuanMax);
                dto.setLimitTimes(dailyLimit.intValue());
            }
        }

        dto.setRealNameVerified(employeeRealNameVerificationService.isVerified(employeeId));
        return dto;
    }

    private void fillAccWithdraw(List<AvaliableCardDTO> cards, Long employeeId) {
        if (CollectionUtil.isNotEmpty(cards)) {
            for (AvaliableCardDTO item : cards) {
                Long accWithdrawAmount = withdrawEmployeeApplyService.queryMonthAccWithdrawAmountByAccountNo(employeeId, item.getAccountNo());
                item.setAccWithdraw("本月累计提现" + MoneyUtil.convert(accWithdrawAmount) + "元");
            }
        }
    }

    //    @NotNull
    private List<AvaliableCardDTO> getAvaliableCardDTOS(List<WithdrawEmployeeCard> cards1) {

        if (CollectionUtil.isEmpty(cards1)) {
            return null;
        }
        return cards1.stream().map(item -> convert(item)).collect(Collectors.toList());
    }

    public AvaliableCardDTO convert(WithdrawEmployeeCard card) {
        if (card == null) {
            return null;
        }
        AvaliableCardDTO avaliableCardDTO = new AvaliableCardDTO();
        avaliableCardDTO.setCardId(card.getId());
        avaliableCardDTO.setType(card.getType());
        avaliableCardDTO.setAccountName(card.getAccountName());
        avaliableCardDTO.setAccountNo(card.getAccountNo());
        avaliableCardDTO.setOwnerName(card.getOwnerName());
        avaliableCardDTO.setIdCard(card.getIdCard());
        avaliableCardDTO.setLatestTime(card.getLatestTime());
        avaliableCardDTO.setCompanyName(card.getCompanyName());
        avaliableCardDTO.setCompanyTaxNo(card.getCompanyTaxNo());
        avaliableCardDTO.setAddress(card.getAddress());
        return avaliableCardDTO;
    }

    private List<WithdrawEmployeeCard> filterCardType(List<WithdrawEmployeeCard> cards, WithdrawCardTypeEnum cardTypeEnum) {
        List<WithdrawEmployeeCard> cardTypeList = cards.stream().filter(item -> item.getType() == cardTypeEnum.getType())
                .sorted(Comparator.comparing(WithdrawEmployeeCard::getLatestTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        return cardTypeList;
    }

    /**
     * 绑定银行卡
     *
     * @param employeeId 员工id
     * @param req
     */
    public void bindBankcard(Long employeeId, BandCardBindReq req) {

        int limit = 5;
        req.setAccountNo(StrUtil.removeAll(req.getAccountNo(), " "));
        if (req.getAccountNo().length() < 13) {
            throw new BusinessException(-1, "卡号格式错误");
        }
        WithdrawCardTypeEnum cardTypeEnum = WithdrawCardTypeEnum.BANK_CARD;
        long bindNum = withdrawEmployeeCardService.queryCardCountBy(employeeId, cardTypeEnum.getType());
        // 判断已绑定数量
        if (limit <= bindNum) {
            throw new BusinessException(BizError.WITHDRAW_BIND_CARD_NUM_LIMIT, new Object[]{limit + ""});
        }

        // 判断卡号是否已存在
        Boolean cardNoExist = withdrawEmployeeCardService.checkAccountNoExists(req.getAccountNo());
        if (cardNoExist) {
            throw new BusinessException(BizError.WITHDRAW_BIND_CARD_NO_EXISTS, new Object[]{req.getAccountNo()});
        }

        // 保存
        WithdrawEmployeeCard card = buildCard(req, cardTypeEnum, employeeId);
        withdrawEmployeeCardService.save(card);

    }

    private WithdrawEmployeeCard buildCard(BandCardBindReq req, WithdrawCardTypeEnum cardTypeEnum, Long employeeId) {
        if (req == null) {
            return null;
        }
        WithdrawEmployeeCard withdrawEmployeeCard = new WithdrawEmployeeCard();
        withdrawEmployeeCard.setEmployeeId(employeeId);
        withdrawEmployeeCard.setType(cardTypeEnum.getType());
        withdrawEmployeeCard.setAccountName(req.getAccountName());
        withdrawEmployeeCard.setAccountNo(req.getAccountNo());
        withdrawEmployeeCard.setOwnerName(req.getOwnerName());
        withdrawEmployeeCard.setIdCard(req.getIdCard());
        withdrawEmployeeCard.setPassTime(LocalDateTime.now());
        withdrawEmployeeCard.setLatestTime(withdrawEmployeeCard.getPassTime());
        withdrawEmployeeCard.setStatus(WithdrawCardStatusEnum.normal.getType());
        return withdrawEmployeeCard;
    }

    public void unbindBankcard(Long employeeId, Long cardId) {

        WithdrawEmployeeCard card = withdrawEmployeeCardService.getCardBy(employeeId, cardId);
        if (card == null) {
            throw new BusinessException(BizError.WITHDRAW_EMPLOYEE_CARD_EXISTS);
        }

        withdrawEmployeeCardService.removeById(cardId);
    }

    public CardInfoDTO getWithdrawBindInfo(Long employeeId, Long cardId) {

        WithdrawEmployeeCard card = withdrawEmployeeCardService.getCardBy(employeeId, cardId);
        if (card == null) {
            throw new BusinessException(BizError.WITHDRAW_EMPLOYEE_CARD_EXISTS);
        }


        CardInfoDTO dto = convertToCardInfoDTO(card);

        return dto;
    }

    private CardInfoDTO convertToCardInfoDTO(WithdrawEmployeeCard card) {
        if (card == null) {
            return null;
        }
        CardInfoDTO cardInfoDTO = new CardInfoDTO();
        cardInfoDTO.setCardId(card.getId());
        cardInfoDTO.setType(card.getType());
        cardInfoDTO.setAccountName(card.getAccountName());
        cardInfoDTO.setAccountNo(card.getAccountNo());
        cardInfoDTO.setOwnerName(card.getOwnerName());
        cardInfoDTO.setIdCard(card.getIdCard());
        cardInfoDTO.setCompanyName(card.getCompanyName());
        cardInfoDTO.setCompanyTaxNo(card.getCompanyTaxNo());
        cardInfoDTO.setAddress(card.getAddress());
        cardInfoDTO.setStatus(card.getStatus());
        cardInfoDTO.setIllegalTypes(card.getIllegalTypes());
        return cardInfoDTO;
    }

    public List<CardInfoDTO> getEmployeeBinds(Long employeeId, WithdrawCardTypeEnum cardTypeEnum) {
        List<WithdrawEmployeeCard> cards = withdrawEmployeeCardService.listCardBy(employeeId, cardTypeEnum);
        List<CardInfoDTO> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(cards)) {
            result = cards.stream().map(item -> convertToCardInfoDTO(item)).collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(result)) {
                //  累计提现设置
                for (CardInfoDTO dto : result) {
                    Long accWithdrawAmount = withdrawEmployeeApplyService.queryMonthAccWithdrawAmountByAccountNo(employeeId, dto.getAccountNo());
                    dto.setAccWithdraw("本月累计提现" + MoneyUtil.convert(accWithdrawAmount) + "元");
                    if (dto.getIllegal()) {
                        dto.setIllegalMsg("异常");
                    }
                }
            }

        }

        return result;
    }

    public void bindZFB(Long employeeId, ZfbBindReq req) {
        int limit = 5;
        req.setAccountNo(StrUtil.removeAll(req.getAccountNo(), " "));
        WithdrawCardTypeEnum cardTypeEnum = WithdrawCardTypeEnum.ZFB_ACCOUNT;
        long bindNum = withdrawEmployeeCardService.queryCardCountBy(employeeId, cardTypeEnum.getType());
        // 判断已绑定数量
        if (limit <= bindNum) {
            throw new BusinessException(BizError.WITHDRAW_BIND_CARD_NUM_LIMIT, new Object[]{limit + ""});
        }

        // 判断卡号是否已存在
        Boolean cardNoExist = withdrawEmployeeCardService.checkAccountNoExists(req.getAccountNo());
        if (cardNoExist) {
            throw new BusinessException(BizError.WITHDRAW_BIND_CARD_NO_EXISTS, new Object[]{req.getAccountNo()});
        }

        // 保存
        WithdrawEmployeeCard card = buildZfbCard(req, cardTypeEnum, employeeId);
        withdrawEmployeeCardService.save(card);
    }

    private WithdrawEmployeeCard buildZfbCard(ZfbBindReq req, WithdrawCardTypeEnum cardTypeEnum, Long employeeId) {
        if (req == null) {
            return null;
        }
        WithdrawEmployeeCard withdrawEmployeeCard = new WithdrawEmployeeCard();
        withdrawEmployeeCard.setEmployeeId(employeeId);
        withdrawEmployeeCard.setType(cardTypeEnum.getType());
        withdrawEmployeeCard.setAccountName("支付宝");
        withdrawEmployeeCard.setAccountNo(req.getAccountNo());
        withdrawEmployeeCard.setOwnerName(req.getOwnerName());
        withdrawEmployeeCard.setIdCard(req.getIdCard());
        withdrawEmployeeCard.setPassTime(LocalDateTime.now());
        withdrawEmployeeCard.setLatestTime(withdrawEmployeeCard.getPassTime());
        withdrawEmployeeCard.setStatus(WithdrawCardStatusEnum.normal.getType());
        return withdrawEmployeeCard;
    }

    public void bindPublicAccount(Long employeeId, PublicAccountBindReq req) {
        int limit = 1;
        WithdrawCardTypeEnum cardTypeEnum = WithdrawCardTypeEnum.PUBLIC_ACCOUNT;
        long bindNum = withdrawEmployeeCardService.queryCardCountBy(employeeId, cardTypeEnum.getType());
        // 判断已绑定数量
        if (limit <= bindNum) {
            throw new BusinessException(BizError.WITHDRAW_BIND_CARD_NUM_LIMIT, new Object[]{limit + ""});
        }

        // 判断卡号是否已存在
        Boolean cardNoExist = withdrawEmployeeCardService.checkAccountNoExists(req.getAccountNo());
        if (cardNoExist) {
            throw new BusinessException(BizError.WITHDRAW_BIND_CARD_NO_EXISTS, new Object[]{req.getAccountNo()});
        }

        // 保存
        WithdrawEmployeeCard card = buildPublicAccount(req, cardTypeEnum, employeeId);
        withdrawEmployeeCardService.save(card);
    }

    private WithdrawEmployeeCard buildPublicAccount(PublicAccountBindReq req, WithdrawCardTypeEnum cardTypeEnum, Long employeeId) {
        if (req == null) {
            return null;
        }
        WithdrawEmployeeCard withdrawEmployeeCard = new WithdrawEmployeeCard();
        withdrawEmployeeCard.setEmployeeId(employeeId);
        withdrawEmployeeCard.setType(cardTypeEnum.getType());
        withdrawEmployeeCard.setCompanyName(req.getCompanyName());
        withdrawEmployeeCard.setCompanyTaxNo(req.getCompanyTaxNo());
        withdrawEmployeeCard.setAddress(req.getAddress());

        withdrawEmployeeCard.setAccountName(req.getAccountName());
        withdrawEmployeeCard.setAccountNo(req.getAccountNo());
        withdrawEmployeeCard.setPassTime(LocalDateTime.now());
        withdrawEmployeeCard.setLatestTime(withdrawEmployeeCard.getPassTime());
        withdrawEmployeeCard.setStatus(WithdrawCardStatusEnum.normal.getType());
        return withdrawEmployeeCard;
    }

    /**
     * 计算税额
     *
     * @param employeeId 员工id
     * @return
     */
    public AmountTaxDTO calculateTax(Long employeeId, NormalApplyReq req) {

        AmountTaxDTO dto = new AmountTaxDTO();
        dto.setAmount(req.getAmount());
        dto.setAutoPayment(false);
        BigDecimal taxRate = getTaxRate();
        WithdrawEmployeeCard card = withdrawEmployeeCardService.getCardBy(employeeId, req.getCardId());
        if (card == null) {
            throw new BusinessException(BizError.WITHDRAW_EMPLOYEE_CARD_EXISTS);
        }

        if (card.getType() == WithdrawCardTypeEnum.PUBLIC_ACCOUNT.getType()) {
            // 20230719 需求变动:不是对公账户体现才需要税额
            taxRate = BigDecimal.valueOf(0L);
        }

//        BigDecimal taxRate = BigDecimal.valueOf(0.068D);
        log.info("taxRate {}", taxRate);
        BigDecimal amountDecimal = BigDecimal.valueOf(req.getAmount());
        log.info("amountDecimal {}", amountDecimal);
        BigDecimal taxAmount = taxRate.multiply(amountDecimal);

        log.info("taxAmount {}", taxAmount);

        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String formattedResult = decimalFormat.format(taxAmount);
        log.info("formattedResult {}", formattedResult);
        if (formattedResult.charAt(formattedResult.length() - 1) >= '1') {
            taxAmount = new BigDecimal(formattedResult);
            double roundedResult = Math.ceil(taxAmount.doubleValue());
            log.info("roundedResult {}", roundedResult);
            formattedResult = decimalFormat.format(roundedResult);
            log.info("formattedResult2 {}", formattedResult);
            taxAmount = new BigDecimal(formattedResult);
        } /*else {
            taxAmount = taxRate;
        }*/
        dto.setTaxAmount(taxAmount.longValue());

        log.info("提现金额{}  税率{}  应交税额{}", req.getAmount(), taxRate, taxAmount);


        Employee employee = emService.getById(employeeId);
        Long employeeCompanyId = employee.getCompanyId();
        Company company = companyService.getById(employeeCompanyId);
        Integer companyType = employee.getCompanyType();
        Integer employeeType = employee.getType();
        Integer role;
        /*if (!Arrays.asList(2, 3).contains(companyType)) {
            throw new BusinessException(99999,"没有提现的权限");
        }
        if (!Arrays.asList(1, 2, 3, 4).contains(employeeType)) {
            throw new BusinessException(99999,"没有提现的权限");
        }*/
        switch (employeeType) {
            case 1:
            case 3:
                // 负责人
                role = 1;
                break;
            case 2:
            case 4:
                // 员工
                role = 2;
                break;
            default:
                throw new BusinessException(99999,"没有提现的权限");
        }

        EmployeeAccount employeeAccount = employeeAccountService.getByEmployeeId(employeeId);
//        boolean isInvoiceApply = (req instanceof InvoiceApplyReq);
        // 验证所选的提现方式是否正确
        if (card.getType() == WithdrawCardTypeEnum.PUBLIC_ACCOUNT.getType()) {
            // 判断是否有权限提交对公提现
            if (!employee.getPublicFlag()) {
                throw new BusinessException(BizError.WITHDRAW_NO_PRIVILIGE_PUBLIC_APPLY_ERROR);
            }
        }

        // 能开票的：>=500 且 <=50000  每日限1次（成功的），每月收款账户最大500000
        // 不能开票的： >=20 且 <=3000  每日限1次（成功的），每月单个收款账户最大80000
        // 单月已提交的提现金额（包含审核中和已打款的）
        // 单日已提交提现次数(成功的)

        //  判断单次提现金额,不能超过账户余额
        if (dto.getAmount() > employeeAccount.getAbleBalance()) {
            throw new BusinessException(BizError.WITHDRAW_OVER_ABLEBALANCE_ERROR, new Object[]{MoneyUtil.convert(employeeAccount.getAbleBalance())});
        }

        // 当日提现次数 （不包含失败）
        Long times = withdrawEmployeeApplyService.queryTodayApplyTimes(employeeId);
        // 查询卡号本月的已提现金额（包含已到账和未到账）
        Long accAmountCommited = withdrawEmployeeApplyService.queryMonthAccAmountCommited(employeeId, card.getAccountNo());
        // 查询卡号本月的已提现到账金额
        Long accAmountArrived = withdrawEmployeeApplyService.queryMonthAccWithdrawAmountByAccountNo(employeeId, card.getAccountNo());
        // 本月在途提现 = 所有已提交 - 所有已到账
        Long accAmountWaitArrived = accAmountCommited - accAmountArrived;
        if (card.getType() == WithdrawCardTypeEnum.PUBLIC_ACCOUNT.getType()) {
            //  判断每天次数
            Long dailyLimit = getLimit("withdraw_daily_Limit_times", true, 1L);//  "withdraw_daily_Limit_times"
            Long dailyLimitYuanMin = getLimit("withdraw_daily_Limit_amount_min", true, 50000L); //  "withdraw_daily_Limit_amount_min"
            Long dailyLimitYuanMax = getLimit("withdraw_daily_Limit_amount_max", true, 5000000L);//"withdraw_daily_Limit_amount_max"
            //  判断当月累计提现金额(已提交的)
            Long monthlyAccAmount = getLimit("withdraw_monthly_Limit_acc_amount", true, 50000000L);//"withdraw_monthly_Limit_acc_amount"
            checkWithdrawLimit(card.getType(), dto, times, accAmountCommited, accAmountArrived, accAmountWaitArrived, dailyLimit, dailyLimitYuanMin, dailyLimitYuanMax, monthlyAccAmount);
        } else {
            Long dailyLimit = getLimit("withdraw_daily_Limit_times", false, 1L);
            Long monthlyAccAmount = getLimit("withdraw_monthly_Limit_acc_amount", false, 8000000L);
            checkWithdrawLimit(card.getType(), dto, times, accAmountCommited, accAmountArrived, accAmountWaitArrived, dailyLimit, monthlyAccAmount);
            Long dailyLimitYuanMin;
            Long dailyLimitYuanMax;
            if (role == 1) {
                // 负责人 1000元-10000元
                dailyLimitYuanMin = getLimit("withdraw_daily_Limit_amount_min_store_leader", false, 100000L);
                dailyLimitYuanMax = getLimit("withdraw_daily_Limit_amount_max_store_leader", false, 1000000L);
                if (dto.getAmount() < dailyLimitYuanMin) {
                    throw new BusinessException(BizError.WITHDRAW_OVER_DAILY_LIMIT_RANGE_ERROR, new Object[]{String.valueOf(dailyLimitYuanMin / 100), String.valueOf(dailyLimitYuanMax / 100)});
                }
                dto.setAutoPayment(dto.getAmount() >= dailyLimitYuanMin && dto.getAmount() <= dailyLimitYuanMax);
            }
            if (role == 2) {
                // 店员 10元-1000元
                dailyLimitYuanMin = getLimit("withdraw_daily_Limit_amount_min_staff", false, 1000L);
                dailyLimitYuanMax = getLimit("withdraw_daily_Limit_amount_max_staff", false, 100000L);
                if (dto.getAmount() < dailyLimitYuanMin) {
                    throw new BusinessException(BizError.WITHDRAW_OVER_DAILY_LIMIT_RANGE_ERROR, new Object[]{String.valueOf(dailyLimitYuanMin / 100), String.valueOf(dailyLimitYuanMax / 100)});
                }
                dto.setAutoPayment(dto.getAmount() >= dailyLimitYuanMin && dto.getAmount() <= dailyLimitYuanMax);
            }
        }
        return dto;
    }


    @NotNull
    private BigDecimal getTaxRate() {
        String taxRateConfig = dictService.getByNameWithCache("withdraw_tax_rate");
        BigDecimal taxRate = BigDecimal.valueOf(0.068D);
        if (StringUtils.isNotEmpty(taxRateConfig)) {
            taxRate = BigDecimal.valueOf(Double.parseDouble(taxRateConfig));
        }
        return taxRate;
    }


    public MailAdressDTO getMailAddress() {

        String address = dictService.getByNameWithCache("withdraw_mail_address");
        String name = dictService.getByNameWithCache("withdraw_mail_name");
        String phone = dictService.getByNameWithCache("withdraw_mail_phone");

        MailAdressDTO dto = new MailAdressDTO(address, name, phone);
        return dto;

    }

    public Long getLimit(String limitKey, Boolean isPulbic, Long defaultValue) {
        String dictKey = (isPulbic ? "public_" : "not_public_") + limitKey;
        String limitValue = dictService.getByNameWithCache(dictKey);
        return StringUtils.isNotEmpty(limitValue) ? Long.parseLong(limitValue) : defaultValue;
    }

    @Transactional(rollbackFor = Exception.class)
    public ApplyIdDTO createApply(Long employeeId, NormalApplyReq req) {

        Employee employee = emService.getById(employeeId);
        WithdrawEmployeeCard card = withdrawEmployeeCardService.getCardBy(employeeId, req.getCardId());
        if (card == null) {
            throw new BusinessException(BizError.WITHDRAW_EMPLOYEE_CARD_EXISTS);
        }
        /*if(employee.getCompanyType().intValue()==1 && (employee.getLevel().intValue()==1 || employee.getLevel().intValue()==2)){
            throw new BusinessException(99999,"没有提现的权限");
        }*/
        Long employeeCompanyId = employee.getCompanyId();
        Company company = companyService.getById(employeeCompanyId);
        EmployeeAccount employeeAccount = employeeAccountService.getByEmployeeId(employeeId);
        boolean isInvoiceApply = (req instanceof InvoiceApplyReq);
        // 验证所选的提现方式是否正确
        if (isInvoiceApply) {
            // 判断是否有权限提交对公提现
            if (!employee.getPublicFlag()) {
                throw new BusinessException(BizError.WITHDRAW_NO_PRIVILIGE_PUBLIC_APPLY_ERROR);
            }

            if (card.getType() != WithdrawCardTypeEnum.PUBLIC_ACCOUNT.getType()) {
                throw new BusinessException(BizError.WITHDRAW_EMPLOYEE_CARD_SELECTED_ERROR);
            }
            InvoiceApplyReq invoiceApplyReq = (InvoiceApplyReq) req;
            if (invoiceApplyReq.getInvoiceType() == 2) {
                if (StringUtils.isAnyBlank(invoiceApplyReq.getExpressCompany(), invoiceApplyReq.getExpressNo())) {
                    throw new BusinessException(BizError.WITHDRAW_EXPRESS_COMPANY_OR_NO_EMPTY_ERROR);
                }
            }


        } else if (card.getType() == WithdrawCardTypeEnum.PUBLIC_ACCOUNT.getType()) {
            throw new BusinessException(BizError.WITHDRAW_EMPLOYEE_CARD_SELECTED_ERROR);
        }
        AmountTaxDTO amountTaxDTO = calculateTax(employeeId, req);

//        AYCX开头 后台审核
//        AYQD开头 不能开票  子后台审核
        boolean inventedWithdrawFlag = employee.getPublicFlag();// 是否用于子后台审核
        String applyNo = "";
        if (inventedWithdrawFlag) {
            applyNo = "AYCX" + DateUtils.getCurDateTimeStr2() + SnowflakeIdWorker.betweenLong(100L, 999L, true);
        } else {
            applyNo = "AYQD" + DateUtils.getCurDateTimeStr2() + SnowflakeIdWorker.betweenLong(100L, 999L, true);
        }

        WithdrawEmployeeApply apply = new WithdrawEmployeeApply();

        apply.setId(snowflakeIdService.nextId());
        apply.setApplyNo(applyNo);
        apply.setEmployeeId(employeeId);
        apply.setAmount(amountTaxDTO.getAmount());
        apply.setTaxAmount(amountTaxDTO.getTaxAmount());
        apply.setInAmount(amountTaxDTO.getAmount() - amountTaxDTO.getTaxAmount());
        apply.setType(card.getType());
        apply.setAccountName(card.getAccountName());
        apply.setAccountNo(card.getAccountNo());
        apply.setOwnerName(card.getOwnerName());
        apply.setIdCard(card.getIdCard());
        if (card.getType() == WithdrawCardTypeEnum.PUBLIC_ACCOUNT.getType()) {

            apply.setCompanyName(card.getCompanyName());
            apply.setCompanyTaxNo(card.getCompanyTaxNo());
            apply.setAddress(card.getAddress());
            apply.setToPublic(true);
            apply.setInvoiceFlag(true);

            InvoiceApplyReq invoiceApplyReq = (InvoiceApplyReq) req;
            // 发票类型( 1-电子,2-纸质)
            apply.setInvoiceType(invoiceApplyReq.getInvoiceType());
            apply.setInvoiceImgs(invoiceApplyReq.getInvoiceImgs().stream().collect(Collectors.joining(",")));
            // 发票上传时间
            apply.setInvoiceUploadTime(LocalDateTime.now());
            // 物流公司
            apply.setExpressCompany(invoiceApplyReq.getExpressCompany());
            // 物流单号
            apply.setExpressNo(invoiceApplyReq.getExpressNo());
            // 发票审核状态
            apply.setInvoiceStatus(0);
        } else {
            apply.setToPublic(false);
            apply.setInvoiceFlag(false);
        }

        apply.setStatus(WithdrawApplyStatusEnum.wait_audit.getType());
        apply.setCreateTime(LocalDateTime.now());


        StateSnapshotDTO stateSnapshotDTO = new StateSnapshotDTO(apply.getStatus(), apply.getRemark(), LocalDateTime.now());
        apply.setStateSnapshot(JSONUtil.toJsonStr(Arrays.asList(stateSnapshotDTO)));
        apply.setAncestors(employeeAccount.getAncestors());
        apply.setToPlatform(inventedWithdrawFlag);

        //  提现申请入库
        withdrawEmployeeApplyService.save(apply);
        //  账户余额变动
        EmployAccountChangeEnum accountChangeEnum = EmployAccountChangeEnum.withdraw;
        employeeAccountChangeService.changeAccount(employeeId, accountChangeEnum, amountTaxDTO.getAmount(), apply.getId(), "提现");

        // 自动提现功能开关
        boolean autoPaymentFuncAble = getQfuAutoPaymentFuncAble();
        if (amountTaxDTO.getAutoPayment() && autoPaymentFuncAble) {
            autoPayment(apply.getId());
        }

        return new ApplyIdDTO(apply.getId());
    }

    private Boolean getQfuAutoPaymentFuncAble() {
        int defaultValue = 0;
        String value = Optional.ofNullable(dictService.getByNameWithCache("qfu_auto_payment_func_able")).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error("get qfu_auto_payment_switch error: {}", ExceptionUtil.getMessage(e));
        }
        return defaultValue == 1;
    }

    private void autoPayment(Long applyId) {
        WithdrawEmployeeApply apply = withdrawEmployeeApplyService.getById(applyId);
        log.info("auto payment start applyId-{} applyNo-{}", applyId, apply.getApplyNo());
        JSONArray jsonArray = JSONUtil.parseArray(apply.getStateSnapshot());
        // 自动审核通过-待打款
        List<StateSnapshotDTO> stateSnapshotDTOs = jsonArray.stream().map(item -> JSONUtil.toBean((JSONObject) item, StateSnapshotDTO.class)).collect(Collectors.toList());
        StateSnapshotDTO waitPaySnapshot = new StateSnapshotDTO(WithdrawApplyStatusEnum.wait_pay.getType(), null, LocalDateTime.now());
        stateSnapshotDTOs.add(waitPaySnapshot);
        // payment
        QfuPaymentInvokeReq req = QfuPaymentInvokeReq.builder()
                .order(apply.getApplyNo())
                .account(apply.getAccountNo())
                .value(apply.getInAmount().intValue())
                .name(apply.getOwnerName())
                .identity(apply.getIdCard())
                .phone(LoginUserContext.getUser().getMobileNumber())
                .type(apply.getType())
                .build();
        log.info("auto payment applyNo-{} req-{}", apply.getApplyNo(), JSONUtil.toJsonStr(req));
        QfuPaymentInvokeResp resp = qfuService.paymentInvoke(req);
        if (resp == null || !resp.getCode().equals(0)) {
            throw new BusinessException(-1, "打款失败，请重试");
        }
        log.info("auto payment applyNo-{} applyType-{} resp-{}", apply.getApplyNo(), apply.getType(), JSONUtil.toJsonStr(resp));
        // 打款接口-打款中
        StateSnapshotDTO payingSnapshot = new StateSnapshotDTO(WithdrawApplyStatusEnum.paying.getType(), null, LocalDateTime.now());
        stateSnapshotDTOs.add(payingSnapshot);
        apply.setStateSnapshot(JSONUtil.toJsonStr(stateSnapshotDTOs));
        apply.setStatus(WithdrawApplyStatusEnum.paying.getType());
        apply.setUpdateTime(LocalDateTime.now());
        withdrawEmployeeApplyService.updateById(apply);
        log.info("auto payment end applyNo-{}", apply.getApplyNo());
    }

    private void checkWithdrawLimit(Integer cardType, AmountTaxDTO amountTaxDTO, Long times, Long accAmountCommited, Long accAmountArrived, Long accAmountWaitArrived, Long dayliyLimit, Long dayliyLimitYuanMin, Long dayliyLimitYuanMax, Long monthlyAccAmount) {
        if (times >= dayliyLimit) {
            throw new BusinessException(BizError.WITHDRAW_OVER_DAILY_LIMIT_TIMES_ERROR, new Object[]{String.valueOf(dayliyLimit)});
        }

        if (!(amountTaxDTO.getAmount() >= dayliyLimitYuanMin && amountTaxDTO.getAmount() <= dayliyLimitYuanMax)) {
            throw new BusinessException(BizError.WITHDRAW_OVER_DAILY_LIMIT_RANGE_ERROR, new Object[]{String.valueOf(dayliyLimitYuanMin / 100), String.valueOf(dayliyLimitYuanMax / 100)});
        }

        if ((accAmountCommited + amountTaxDTO.getAmount()) > monthlyAccAmount) {
            throw new BusinessException(BizError.WITHDRAW_OVER_MONTHLY_LIMIT_ACC_AMOUNT_ERROR, new Object[]{converCardType(cardType), String.valueOf(monthlyAccAmount / 100), String.valueOf(accAmountArrived / 100), String.valueOf(accAmountWaitArrived / 100)});
        }
    }

    private void checkWithdrawLimit(Integer cardType, AmountTaxDTO amountTaxDTO, Long times, Long accAmountCommited, Long accAmountArrived, Long accAmountWaitArrived, Long dailyLimit, Long monthlyAccAmount) {
        if (times >= dailyLimit) {
            throw new BusinessException(BizError.WITHDRAW_OVER_DAILY_LIMIT_TIMES_ERROR, new Object[]{String.valueOf(dailyLimit)});
        }

        if ((accAmountCommited + amountTaxDTO.getAmount()) > monthlyAccAmount) {
            throw new BusinessException(BizError.WITHDRAW_OVER_MONTHLY_LIMIT_ACC_AMOUNT_ERROR, new Object[]{converCardType(cardType), String.valueOf(monthlyAccAmount / 100), String.valueOf(accAmountArrived / 100), String.valueOf(accAmountWaitArrived / 100)});
        }
    }

    private String converCardType(int cardType) {

        if (cardType == WithdrawCardTypeEnum.BANK_CARD.getType()) {
            return "银行卡";
        }

        if (cardType == WithdrawCardTypeEnum.ZFB_ACCOUNT.getType()) {
            return "支付宝账户";
        }

        if (cardType == WithdrawCardTypeEnum.PUBLIC_ACCOUNT.getType()) {
            return "对公账户";
        }

        return "";
    }

    public ApplyDetailDTO getApplyDetail(Long employeeId, Long applyId) {

        WithdrawEmployeeApply apply = withdrawEmployeeApplyService.getById(applyId);
        if (apply == null || !apply.getEmployeeId().equals(employeeId)) {
            throw new BusinessException(BizError.PARAM_ERROR);
        }

        ApplyDetailDTO dto = buildApplyDetailDTO(apply);
        return dto;
    }

    private ApplyDetailDTO buildApplyDetailDTO(WithdrawEmployeeApply apply) {
        if (apply == null) {
            return null;
        }
        ApplyDetailDTO applyDetailDTO = new ApplyDetailDTO();
        applyDetailDTO.setAmount(MoneyUtil.convert(apply.getAmount()));
        applyDetailDTO.setTaxAmount(MoneyUtil.convert(apply.getTaxAmount()));
        applyDetailDTO.setType(apply.getType());
        applyDetailDTO.setAccountName(apply.getAccountName());
        applyDetailDTO.setAccountNo(apply.getAccountNo());
        applyDetailDTO.setOwnerName(apply.getOwnerName());
        applyDetailDTO.setCompanyName(apply.getCompanyName());

        JSONArray jsonArray = JSONUtil.parseArray(apply.getStateSnapshot());

        List<StateSnapshotDTO> stateSnapshotDTOs = jsonArray.stream().map(item -> JSONUtil.toBean((JSONObject) item, StateSnapshotDTO.class)).collect(Collectors.toList());

        Integer maxStatus = stateSnapshotDTOs.stream().mapToInt(StateSnapshotDTO::getStatus).max().getAsInt();

        boolean isFail = apply.getStatus() == WithdrawApplyStatusEnum.fail.getType();

        List<ApplyStageDTO> stageDTOS = new ArrayList<>();

        stageDTOS.add(buildStateOne(maxStatus, isFail, stateSnapshotDTOs));
        stageDTOS.add(buildStateTwo(maxStatus, isFail, stateSnapshotDTOs));
        stageDTOS.add(buildStateThree(maxStatus, isFail, stateSnapshotDTOs));
        stageDTOS.add(buildStateFour(maxStatus, isFail, stateSnapshotDTOs));

        StateSnapshotDTO first = stateSnapshotDTOs.get(0);

        ApplyStageDTO firstStage = stageDTOS.get(0);
        firstStage.setReachFlag(true);
        firstStage.setReachTime(first.getReachTime());

        applyDetailDTO.setStage(stageDTOS);
        return applyDetailDTO;
    }


    private ApplyStageDTO buildStateOne(Integer maxStatus, boolean isFail, List<StateSnapshotDTO> stateSnapshotDTOs) {
        ApplyStageDTO applyStageDTOItem = new ApplyStageDTO();
        applyStageDTOItem.setTitle("提现申请成功(预计24小时到账)");
        applyStageDTOItem.setReachFlag(true);
        applyStageDTOItem.setReachTime(stateSnapshotDTOs.get(0).getReachTime());
        return applyStageDTOItem;
    }

    private ApplyStageDTO buildStateTwo(Integer maxStatus, boolean isFail, List<StateSnapshotDTO> stateSnapshotDTOs) {
        ApplyStageDTO applyStageDTOItem = new ApplyStageDTO();
        applyStageDTOItem.setReachFlag(true);
        applyStageDTOItem.setReachTime(stateSnapshotDTOs.get(0).getReachTime());
        if (maxStatus == WithdrawApplyStatusEnum.wait_audit.getType()) {
            if (isFail) {
                StateSnapshotDTO failState = stateSnapshotDTOs.get(stateSnapshotDTOs.size() - 1);
                applyStageDTOItem.setTitle("审核失败，请重新提交申请");
                applyStageDTOItem.setFailMsg(failState.getRemark());
            } else {
                applyStageDTOItem.setTitle("提现审核");
            }
        } else {
            applyStageDTOItem.setTitle("审核成功");
        }
        return applyStageDTOItem;
    }

    private ApplyStageDTO buildStateThree(Integer maxStatus, boolean isFail, List<StateSnapshotDTO> stateSnapshotDTOs) {
        ApplyStageDTO applyStageDTOItem = new ApplyStageDTO();
        applyStageDTOItem.setTitle("打款中");
        if (maxStatus >= WithdrawApplyStatusEnum.wait_pay.getType()) {
            applyStageDTOItem.setReachFlag(true);
            applyStageDTOItem.setReachTime(stateSnapshotDTOs.get(1).getReachTime());
        } else {
            applyStageDTOItem.setReachFlag(false);
        }
        return applyStageDTOItem;
    }

    private ApplyStageDTO buildStateFour(Integer maxStatus, boolean isFail, List<StateSnapshotDTO> stateSnapshotDTOs) {
        ApplyStageDTO applyStageDTOItem = new ApplyStageDTO();
        applyStageDTOItem.setTitle("提现到账");
        if (maxStatus == WithdrawApplyStatusEnum.pay_success.getType()) {
            StateSnapshotDTO lastState = stateSnapshotDTOs.get(stateSnapshotDTOs.size() - 1);
            applyStageDTOItem.setReachFlag(true);
            applyStageDTOItem.setReachTime(lastState.getReachTime());
        } else {
            if (maxStatus >= WithdrawApplyStatusEnum.wait_pay.getType()) {
                if (isFail) {
                    applyStageDTOItem.setReachFlag(true);
                    StateSnapshotDTO failState = stateSnapshotDTOs.get(stateSnapshotDTOs.size() - 1);
                    applyStageDTOItem.setTitle("提现失败，请重新提交申请");
                    applyStageDTOItem.setFailMsg(failState.getRemark());
                }
            } else {
                applyStageDTOItem.setReachFlag(false);
            }
        }
        return applyStageDTOItem;
    }


    private String converIndexToTitle(int index, Integer maxStatus, boolean isFail, List<StateSnapshotDTO> StateSnapshotDTOs) {
        String title = null;
        switch (index) {
            case 0:
                title = "提现申请成功(预计24小时到账)";
                break;
            case 1:
                if (maxStatus == WithdrawApplyStatusEnum.wait_audit.getType()) {
                    if (isFail) {
                        title = "审核失败，请重新提交申请";
                    } else {
                        title = "提现审核";
                    }
                } else {
                    title = "审核成功";
                }
                break;
            case 2:
                title = "打款中";
                break;
            case 3:
                title = "提现到账";
                break;
            default:
                title = "";
        }
        return title;
    }

    public static void main(String[] args) {
        String snapshot = "[\n" +
                "            {\n" +
                "                \"title\": \"提现申请成功(预计24小时到账)\",\n" +
                "                \"reachFlag\": true,\n" +
                "                \"reachTime\": 1679452174935,\n" +
                "                \"failMsg\": null\n" +
                "            },\n" +
                "            {\n" +
                "                \"title\": \"提现审核\",\n" +
                "                \"reachFlag\": true,\n" +
                "                \"reachTime\": 1679452174935,\n" +
                "                \"failMsg\": null\n" +
                "            },\n" +
                "            {\n" +
                "                \"title\": \"打款中\",\n" +
                "                \"reachFlag\": false,\n" +
                "                \"reachTime\": null,\n" +
                "                \"failMsg\": null\n" +
                "            }\n" +
                "        ]";
        JSONArray jsonArray = JSONUtil.parseArray(snapshot);

        JSONObject jsonObject = (JSONObject) jsonArray.get(jsonArray.size() - 2);
        System.out.println(jsonObject);
//        for (Object item : jsonArray.jsonIter()) {
//            System.out.println(item);
//        }
        String formattedResult = "16.0";
        System.out.println(formattedResult.charAt(formattedResult.length() - 1));

    }


}