package com.anyi.common.advice;


public enum BizError implements IError {
    NET_ERROR(90000, "网络错误，请稍后重试"),
    OUTER_ERROR(90001, "外部错误"),
    WX_TOKEN_ERROR(90002, "微信TOKEN获取生产中控失败"),
    FILE_SIZE_ERROR(90003, "文件大小超出"),
    WX_MAP_TEMPLATE_ERROR(90004, "推送公众号模版消息失败"),
    SMS_TOO_frequency(11000, "验证码发送过于频繁"),
    OUT_DAY_SEND_TIMES(11001, "超出当日发送次数"),
    COde_error(11002, "验证码错误"),
    CODE_EXPIRE(11003, "验证码已过期"),
    CODE_ALREADY_USED(11003, "验证码已使用，请重新获取"),
    SIGN_OUT(11004, "签约次数超限"),
    PARAM_ERROR(11005, "参数错误！"),
    ISSUE_ERROR(11006, "{0}"),

    Had_etc(12001, "该车辆已开卡"),
    ORDER_NOT_EXIST(12002, "订单不存在"),
    Type_not_exist(12003, "卡类型不存在"),
    ETC_BINDING(12004, "etc已绑定"),
    CANT_DEAL(12005, "无法处理的请求"),
    Encode_failed(12006, "加密失败"),
    IDCARD_ERROR(12007, "身份验证失败，请修改后重试"),
    VERIFY_FAILD(12008, "高速网络请求异常，请稍后重试!"),
    MOBILE_NOT_MATCH_ETC(12009, "您的微信手机号，还没有开通ETC卡"),
    VEHICLE_EXIST(12010, "该车辆已经存在订单"),
    VEHICLE_OWNER_NOT_MATCH_ID_CARD(12011, "行驶证车辆所有人姓名与身份证姓名不匹配"),
    OBU_BINDING(12012, "obu已绑定"),
    OBU_ACTIVE_ERROR(12013, "obu激活失败"),
    OWNER_BANK_CARD(12014, "只能使用本人银行卡！"),
    CARD_ID_EXIST(12015, "卡号已存在"),
    SIGN_ERROR(12016, "签名错误!"),
    CARD_NOT_EXIST(12017, "卡号不存在"),
    ACTIVE_OBU_ETC_NO_NOT_SAME(12018, "etc卡错误，请使用正确的etc卡{0}进行激活"),
    DISABLE_ETC_NO(12019, "该卡片有问题需要回收，请更换一张卡片后继续操作"),
    OBU_ALREADY_ACTIVATED(12020, "obu已经激活"),
    PLATE_NOT_FOUND(12021, "车牌号不存在或者未激活成功"),
    Black_ID_CARD(12022, "该身份证在黑名单中，不可办理！"),
    CANT_CREATE_ORDER(12023, "暂无法创建订单！"),
    CLOSE_ORDER_ERROR(12024, "关闭订单失败！"),
    CLOSE_ORDER_ALREADY_CLOSED_ERROR(12025, "订单已关闭"),
    EMPLOYEE_ACCOUNT_NOT_EXIST(13001, "员工账号不存在"),
    CANT_DELETE_EMPLOYEE_ACCOUNT(13002, "不能删除该员工账号"),
    EMPLOYEE_LOGIN_COUNT_ERROR(13003, "连续输入错误{0}次验证码，账号锁定24小时"),
    TEMPORARY_EMPLOYEE_USER(13004, "请用账号密码登录"),
    TO_REGISTER_TEMPORARY_EMPLOYEE(13005, "请先注册账号"),
    ERROR_TEMPORARY_EMPLOYEE_PASSWORD(13006, "密码错误"),
    TO_FROZE_EMPLOYEE(13007, "您的账号存在风险操作，或其他不符合平台要求的违规行为，请联系上级管理员"),
    TO_RECYCLE_EMPLOYEE(13008, "暂无权限登陆"),

    FILE_UPLOAD_ERROR(14001, "文件上传失败"),
    GEN_QRCODE_ERROR(14002, "生成二维码失败"),

    USER_LOGIN_ERROR(15001, "用户不存在"),
    GET_OPENID_ERROR(15002, "获取openid失败"),
    USER_MOBILE_IS_NOT_EMPTY(15003, "用户手机号不能为空"),
    PAY_ORDER_ERROR(15004, "支付失败"),
    USER_ETC_ORDER_NOT_EXIST(15005, "订单不存在"),
    USER_ORDER_STATUS_CHANGED(15006, "订单状态已改变"),
    ORDER_NOT_PAY(15007, "押金未支付成功"),
    ORDER_IN_PAY(15008, "订单正在支付中"),
    ORDER_ALREADY_PAY(15009, "订单已支付"),
    USER_MOBILE_EXIST(15090, "用户手机号已绑定"),
    USER_NOT_SAME_COMPANY(15091, "不能查看其它公司员工的信息"),
    USER_NOT_MANAGER_DEPT(15092, "不能查看非自己管理部门员工的信息"),
    NO_PERM_USER_ORDER(15093, "没有小程序申请订单查看权限"),

    USER_EXIST(19001, "手机号已注册"),
    DEPT_NOT_EXIST(19002, "部门不存在"),
    DEPT_EXIST(19003, "该部门名称已经存在，建议在名称后面加数字"),
    ID_NAME_NOT_MATCH(19004, "该手机号已注册，你填写的姓名和手机号不匹配"),
    EMP_POWER_ERROR(19005, "该员工不在你的操作限制内，请找他的上级操作!"),
    REPLACE_MANAGER(19005, "由于该员工同时管理其他部门，暂时无法调整"),
    CREATE_DEPT_ERROR(19006, "创建部门出错，请稍后重试"),
    NOT_MANAGER_DEPT(19007, "不能查看非自己管理部门的信息"),
    NOT_EXIST_MANAGER(19007, "该部门没有管理员"),
    CANT_CREATE_CHANNEL(19008, "暂不支持创建渠道，如有需要，请联系智源安逸出行客服处理。电话：400-6686-333"),
    EM_FREEZE(19009, "该账号已被冻结，暂不能成为管理员"),
    CANNOT_CREAT_CHILD_DEPT_EMP_CHANNEL(19010, "该部门或渠道暂无权限往下创建！"),
    CANNOT_EDIT_EMPLOYEE_DEPT(19020, "不可更改员工部门！"),


    STOCK_NOT_ENOUGH_TIP(16000, "库存不足,当前库存为{0},包含在途转移物料数量{1}"),
    STOCK_NOT_ENOUGH(16001, "当前库存不足"),
    STOCK_CHANGED(16002, "库存发生变化，请重新尝试"),
    HAS_UN_COMPLETE_MOV_ORDER(16003, "有未完成的调拨订单"),
    HAS_EMP_STOCK(16004, "当前员工有物料库存"),
    ORDER_ALREADY_CONFIRM(16005, "订单已确认"),
    ORDER_ALREADY_REFUSE_RECEIVE(16006, "订单已拒绝收货"),
    ORDER_ALREADY_REFUSE_SEND(16007, "订单已拒绝出货"),
    CAN_NOT_CREATE_ORDER(16008, "同一公司/渠道管理部门员工不能相互调拨"),
    ADDRESS_NOT_EXIST(16009, "收货地址不存在"),
    STOCK_REACH_LIMIT(16010, "库存达到上限"),
    Count_ERROR(16011, "新增额度只能是50的倍数！"),
    AMOUNT_ERROR(16012, "金额错误"),
    STOCK_NUM_ERROR(16013, "安逸出行物料中心调拨只能是{0}的倍数"),
    NOT_SUPPORT_SELF_REV(16014, "安逸出行物料中心调拨只支持邮寄"),
    CAN_NOT_OPERATE_ADDRESS(16015, "安逸出行物料中心人员不能操作默认收货地址"),
    Quo_status_error(16016, "提升额度订单状态不合法，请联系管理员"),
    ADDRESS_INCORRECT(16017, "请选择省市区"),
    DEPOSIT_AMOUNT_ERROR(16018, "押金只能在0.01元到99元之间"),
    PICK_TYPE_ERROR(16018, "自提和邮寄必须支持一种"),
    DEF_PICK_TYPE_ERROR(16019, "默认收货方式设置错误"),

    COMPANY_EXIST(19006, "该渠道名称已经存在，请修改后再提交"),
    EM_CANT_CANNEL(19007, "该手机号已经注册，不能成为代理管理员。请更换手机号，或者咨询客服电话：400-6686-333"),
    Name_empty(19008, "请填写管理员姓名"),
    Mobile_EMPTY(19009, "请填写管理员手机号"),
    COMPANY_NOT_EXIST(19010, "渠道不存在!"),
    CHANNEL_ERROR(19011, "请先将渠道移交给其他员工，或下线渠道"),
    EM_NOT_ZERO(19012, "请先将该员工下属移交"),
    Dept_not_empty(19013, "请先将该部门员工移交其他部门"),
    Dept_Type_error(19014, "请下线渠道!"),
    HAS_CHILD_DEPT(19015, "该部门还有子部门，请先删除子部门"),
    NOT_MANAGER_COMPANY(19016, "不能查看非自己管理公司的信息"),
    LEVEL_ERROR(19017, "您暂时没有权限创建渠道，如有需要，请联系智源安逸出行客服处理。电话：400-6686-333"),
    NO_MANAGER(19018, "该部门没有管理员,不能创建子部门"),
    MOBILE_LENGTH_ERROR(19019, "请输入正确的手机号长度"),
    ERROR_PARENT_DEPT(19120, "不能将自己设置为自己的所属部门"),
    CANT_CHG_DEPT_CMP(19121, "暂不支持改变部门所在渠道"),
    EMPLOYEE_NAME_ERROR(19122, "名字只能使用“汉字”，“数字”，“大小写英文"),

    WX_PAY_ERROR(50000, "微信支付出错，请稍后重试"),
    WX_REFUND_ERROR(50001, "微信退款出错，请稍后重试"),


    NO_DATA(60000, "无可恢复数据"),
    PLATE_LENGTH_ERROR(60001, "车牌长度异常"),
    ETC_DEVICE_ERROR(60002, "etc设备号不匹配"),
    ETC_NOT_EXIST(60003, "etc卡不存在"),
    OBU_NO_ERROR(60004, "obu序列号不匹配"),

    HIGHWAY_NET_ERROR(60005, "高速网络异常，请稍后重试"),
    OBU_NOT_MATCH(60006, "obu序列号有误，序列号为:{0}"),
    BANK_TIME_OUT(60007, "该身份证已经绑定{0}台车辆，请换其他身份证绑定！"),
    Cancel_order_not_exist(60008, "取消订单不存在"),
    RE_WRITE_CARD(60009, "写卡失败，请重新写卡!"),

    EGO_VEH_EXIST(70001, "车牌号已被登记"),
    PLATE_RULE_ERROR(70002, "车牌规则错误"),

    RVT_EMP_NOT_EXIST(80001, "推荐员工不存在"),
    RVT_VEH_EXIST(80002, "该车牌已登记"),


    ORDER_STATUS_ERROR(21001, "订单状态异常"),
    NOW_NOT_SUPPORT(21002, "暂不支持此业务"),
    NOW_FAILED(21003, "高速暂无法写卡，请联系客服"),
    CARD_NO_ERROR(21004, "请使用尾号{0}的卡！"),
    OBU_NUM_ERROR(21005, "请使用尾号序列号为{0}的OBU激活！"),
    FINISH_SIGN(21006, "请先完成签约!"),
    NOT_SUPPORT_CAR(21007, "不支持的车型！"),
    CHANGE_CARD(21008, "卡已使用，请换卡重试！"),
    NOT_FINISH(21009, "未完成订单，不可注销"),
    ADDRESS_MUST_NOT_EMPTY(21010, "联系地址不能为null"),
    ADDRESS_NOT_MATCH_PATTERN(21011, "联系地址格式只能为汉字字母数字组成"),

    NOT_SUP_QUERY_BLACK_LIST(22001, "只支持苏通卡黑名单查询"),


    Create_EMP_CFG(23001, "创建员工编号失败"),
    CFG_EXIST(23002, "配置已经存在!"),
    OUT_INDEX(23003, "超出范围"),
    CONFIG_NOT_EXIST(23004, "配置不存在"),
    BANK_Not_exist(23005, "银行不存在"),
    NAME_OR_PHONE_ERROR(23006, "姓名或手机号错误!"),


    WALLET_EMP_EXSIT(24001, "员工已创建钱包"),
    WALLET_OPENID_EXSIT(24002, "微信已绑定钱包"),
    WALLET_BIND_WX_ERRO(24003, "钱包绑定微信失败"),
    WALLET_NOT_EXIST(24004, "钱包还未开通"),
    TRANS_ALREADY_EXIST(24005, "交易已存在"),
    TRANS_AMOUNT_ERRO(24006, "交易金额录入错误"),
    WALLET_OPENID_NOT_EXIST(24007, "钱包还未绑定微信"),
    TRANS_TX_OVER(24008, "提现金额不能超过钱包总金额"),
    WX_TRANS_TX_FAILED(24009, "微信提现失败"),
    TRANS_TX_INST_LOG_ERRO(24010, "新增提现记录失败"),
    TRANS_WALLET_STAT_CHANGED(24011, "钱包状态已更新，请刷新重试"),
    TRANS_LOG_STAT_CHANGED(24012, "交易流水状态已更新,请刷新重试"),
    TRANS_TYPE_NOT_SUPPORT(24013, "不支持此交易类型"),
    TRANS_IN_NULL_PLATNUMBER(24014, "车牌号不能为空"),
    TRANS_IN_NULL_ORDER(24015, "订单号不能为空"),
    TRANS_IN_ERRO(24016, "增加钱包收入失败"),
    TRANS_BANKNO_ERRO(24017, "信息录入有误,请检查后重试"),
    WALLET_NOT_CHANNEL(24018, "渠道不支持钱包功能"),
    TRANS_TX_DISENABLE(24019, "提现出现网络异常，请稍后重试"),
    TRANS_TX_OVER_DAYLIMIT(24010, "提现超过天最大申请数"),
    NO_PERM_WALLET_OP(24011, "没有操作此钱包权限"),
    TRANS_TX_CALLBACK_ERRO(24022, "提现状态回写失败"),
    TRANS_TX_STATUS_UP_ERRO(24023, "提现状态修改失败"),

    HAD_UPDATE(25000, "已被其他人获取"),
    CANT_FINISH(25001, "请先移除或完成所有订单！"),


    IN_REVIEW(26001, "您的订单正在审核中！"),
    REVIEW_FAILED(26002, "您的订单审核已被拒绝，如有疑问，请联系客服!"),
    TO_REVIEW(26003, "由于{0}您的订单已进入审核，如确认信息无误，请联系客服！"),
    UPDATE_ERROR(26004, "由于高速接口升级，请重新创建订单！"),
    BIZ_STOP(26005, "业务暂停！"),

    MP_USER_INFO(27001, "获取用户信息异常"),
    MP_QRCODE(27002, "生成临时二维码失败"),
    MINI_MOBILE(27003, "获取手机号码错误"),
    ORDER_IS_NULL(27004, "订单不存在"),
    MOBILE_IS_NULL(27005, "请先完成手机号码授权"),
    MOBILE_CODE_IS_NULL(27006, "请输入手机验证码"),
    ORDER_BIND_STATUS(27008, "操作失败，当前订单状态为：{0}。"),
    BEST_SIGN_ERROR(27009, "上上签签署调用失败：{0}"),
    BEST_SIGN_USER(27010, "上上签用户注册失败，返回结果：{0}"),
    BEST_SIGN_TEMPLATE_VALUES(27011, "上上签合同模板参数错误"),
    ETC_ORDER_USER(27012, "订单绑定的用户不一致。"),
    PAY_ORDER(27013, "订单支付失败"),
    ORDER_PAID(27014, "订单已支付"),
    ORDER_UN_PAID(27015, "订单未支付"),
    ORDER_PAY_STATUS(27016, "支付订单状态错误"),
    CONTRACT_SIGN_STATUS(27017, "用户已签署合同，当前合同状态：{0}"),
    CREDIT_SCORE(27018, "积分余额不足"),
    AGREEMENT_OPENED(27019, "用户已开通微信车主服务"),
    ORDER_END(27020, "订单已结束"),

    REMOTE_ERROR(27004, "{}"),

    ORDER_CANNOT_COMMIT_AGAINST(28001,"订单审核已通过，不能重复提交！！" ),

    //分佣
    COMMISSION_MEMBER_REPEATE(40000, "用户已经配置过方案了"),
    COMMISSION_MEMBER_MATCH(40001, "只能查看自己创建的方案"),
    COMMISSION_MEMBER_DELETE(40002, "只能删除自己创建的方案"),

    COMMISSION_MEMBER_NOT_MATCH(40003, "不是自己创建的方案"),

    COMMISSION_MEMBER_NOT_DIRECT(40004, "不是自己的直系下级"),

    COMMISSION_CONF_MAX(40005, "分佣不得超过上级分配的数额"),

    COMMISSION_CONF_NOT_EXIST(40006, "不存在可供配置的套餐"),

    COMMISSION_PLANNAME_REPEAT(40007, "方案名称重复了"),

    COMMISSION_MEMBER_PLAN_NOT_EXIST(40008, "请联系上级配置方案"),

    COMMISSION_NOT_ADMIN(40009, "需要平台账户"),

    COMMISSION_CONF_MAX_LIMIT(40010, "推广分佣不得超过最大数额"),

    COMMISSION_CONF_SERVICE_MAX_LIMIT(40011, "服务费分佣不得超过最大数额"),

    COMMISSION_MEMBER_PLAN_PUBLISH_NOT_EXIST(40012, "请联系上级配置推广佣金方案"),





    //=======================提现相关41000~41999==============================
    WITHDRAW_BIND_CARD_NUM_LIMIT(41000, "最多可绑定{0}张,如需更换，请先解绑其他卡片。"),
    /**
     * 41001 卡号{0}已被绑定!!
     */
    WITHDRAW_BIND_CARD_NO_EXISTS(41001, "卡号{0}已被绑定!!"),
    /**
     * 41002 未绑定此提现卡号
     */
    WITHDRAW_EMPLOYEE_CARD_EXISTS(41002, "未绑定此提现卡号"),
    /**
     * 41003 提现方式错误
     */
    WITHDRAW_EMPLOYEE_CARD_SELECTED_ERROR(41003, "提现方式错误"),
    /**
     * 41004 没有权限提交对公提现申请
     */
    WITHDRAW_NO_PRIVILIGE_PUBLIC_APPLY_ERROR(41004, "没有权限提交对公提现申请"),

    /**
     * 41005 超过当前可提现金额,当前可已提现余额为{}
     */
    WITHDRAW_OVER_ABLEBALANCE_ERROR(41005, "超过当前可提现金额,当前可已提现余额为{0}"),

    /**
     *
     * 41006 每日限{0}次
     */
    WITHDRAW_OVER_DAILY_LIMIT_TIMES_ERROR(41006, "每日限{0}次"),

    /**
     *
     * 41007 平台规定，每个{0}单月最大可提现金额为{1}元.本月该卡已提现{2}元，提现待处理{3}元,请修改提现金额或更换银行卡/支付宝账户
     */
    WITHDRAW_OVER_MONTHLY_LIMIT_ACC_AMOUNT_ERROR(41007, "平台规定，每个{0}单月最大可提现金额为{1}元.本月该卡已提现{2}元，提现待处理{3}元,请修改提现金额或更换银行卡/支付宝账户"),

    /**
     *
     * 41008 单次可提现{0}~{1}
     */
    WITHDRAW_OVER_DAILY_LIMIT_RANGE_ERROR(41008, "单次可提现{0}~{1}"),

    /**
     *
     * 41009 物流公司或物流单号不能为空!
     */
    WITHDRAW_EXPRESS_COMPANY_OR_NO_EMPTY_ERROR(41009, "物流公司或物流单号不能为空!"),
    NO_DATA_PRIVILEGEL_ERROR(410010, "暂无权限查看，如有疑问请联系上级!"),
    WITHDRAW_EXIST_COMMISSION_BACK(410011, "您有 【注销追扣】 未完结，暂时无法提现! 追扣成功后，自动恢复提现功能。"),

    WITHDRAW_EXIST_AFTER_SALE(410012, "您有 【客户投诉】 未完结，暂时无法提现! 请先处理客诉。"),
    //=======================提现相关41000~41999==============================


    //=======================押金相关51000~51999==============================
    DEPOSIT_ORDER_NO_EXISTS(51000, "订单不存在"),
    DEPOSIT_AMOUNT_BALANCE_NOT_ENOUGH(51001, "员工可用额度不足"),
    DEPOSIT_REFUND_ORDER_NO_EXISTS(51002, "没有满足额度退款条件的订单"),
    DEPOSIT_ORDER_STATUS_CHANGED(51003, "订单状态已改变"),
    DEPOSIT_AMOUNT_NO_EXISTS(51004, "员工额度账户不存在"),
    DEPOSIT_ACCOUNT_LOCK_FAIL(51005, "员工额度账户锁获取失败"),
    DEPOSIT_ACCOUNT_PROCESS_FAIL(51006, "员工额度账户操作失败"),
    DEPOSIT_REFUND_EXTRA(51007, "提现额度超出了限制范围"),

    DEPOSIT_SUPPLE_EXTRA(51008, "循环补货额度超出了限制范围"),
    DEPOSIT_SUPPLE_CLOSE(51009, "循环补货功能维护中"),
    DEPOSIT_ORDER_BALANCE_NOT_ENOUGH(51010, "退还金额大于该笔订单可退金额"),
    DEPOSIT_STORE_BALANCE_NOT_ENOUGH(51011, "总得退还金额超出了预存额度"),

    //=======================押金相关51000~51999==============================

    //=======================工单申诉相关52000~52999==============================
    ACTIVE_APPLY_DEAL_STATUS_ETCORDER_HAS_ACTIVE(52008, "该ETC办理订单已激活，不能再提交激活申诉！"),
    ACTIVE_APPLY_INVALID_APPEAL_APPLY_TYPE(52009, "无效的申诉类型！"),
    ACTIVE_APPLY_NEED_VLP(52010, "请输入正确车牌！"),
    ACTIVE_APPLY_EXIST_WAIT_OR_HASDEAL(52011, "该etc订单编号已存在待处理申诉单或已处理申诉单！"),
    ACTIVE_APPLY_APPLYID_INVALID(52012, "无效的申诉ID！"),
    ACTIVE_APPLY_CANNOT_CLOSE(52013, "该申诉状态已变更，不能再关闭申诉！"),



    //=======================工单申诉相关52000~52999==============================


    //=======================视博9901相关60000~61999==============================
    CYX_API_UNAUTHORIZED_ERROR(60000, "鉴权失败！"),
    CYX_API_SIGN_PARSE_ERROR(60001, "签名解析失败！"),
    CYX_API_INVALID_SIGN_INFO(60002, "签名鉴权失败"),

    CYX_API_MISSING_APPID(60003, "缺少appid参数"),
    CYX_API_MISSING_SIGN(60004, "缺少sign参数"),
    CYX_API_MISSING_SIGNTYPE(60005, "缺少signtype参数"),
    CYX_API_MISSING_TIMESTAMP(60006, "缺少timestamp参数"),
    CYX_API_REPLAY_ATTACK(60007, "重放攻击"),
    CYX_API_TIME_OUT(60008, "时间超时"),
    CYX_API_SIGN_NOT_MATCH(60009, "签名计算不匹配"),

    CYX_API_INVALID_APP_ID(60010, "签名鉴权失败"),
    //=======================视博9901相关60000~61999==============================

    //=======================北辰5201相关70000~71999==============================
    BC_NET_ERROR(70000, "北辰网络请求错误"),
    //=======================视博9901相关60000~61999==============================

    MB_ORDER_ID_NULL(210000, "订单ID不能为空"),
    MB_ORDER_NOT_EXISTS(210001, "订单不存在"),
    MB_QUOTE_LOG_NOT_EXISTS(210002, "报价记录不存在"),
    MB_ORDER_ERROR_STATUS(210003, "订单状态异常，请刷新重试"),
    MB_ORDER_OPERATION_ERROR_STATUS(210004, "当前订单状态不可操作"),
    MB_NO_PERMISSION(210005, "非本人提交的订单不可操作"),
    MB_ORDER_ALREADY_LOCK_ERROR(210006, "来晚了，其它报价师已抢单"),
    MB_ORDER_QUOTE_ALREADY_CLOSED(210007, "报价已关闭"),
    MB_ORDER_ALREADY_RECEIPT(210008, "已收货"),
    MB_ORDER_CANNOT_CONFIRM_TRADE(210009, "此订单无法交易"),
    MBR_SHOP_CODE(310001, "已生成"),
    ;


    int code;
    String message;

    BizError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}