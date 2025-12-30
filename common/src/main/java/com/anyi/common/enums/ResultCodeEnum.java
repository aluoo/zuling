package com.anyi.common.enums;

import lombok.Getter;

/**
 * 状态码定义：
 * <br/>
 * <table border="1">
 * <tr>
 *    <th>状态码</th>
 *    <th>说明</th>
 * </tr>
 * <tr>
 *    <td>1</td>
 *    <td>访问成功</td>
 * </tr>
 * <tr>
 *    <td>10xx</td>
 *    <td>系统级的错误，例如1002表示服务器内部错误</td>
 * </tr>
 * <tr>
 *     <td>aabbxxx</td>
 *     <td>业务状态码，aa是模块代号，从20开始；bb是子模块代号，从00开始；xxx是具体的状态码</td>
 * </tr>
 * </table>
 */
@Getter
public enum ResultCodeEnum {

    /**
     * 1, 操作成功
     */
    SUCCESS(200, "success"),

    /**
     * 1002, 服务器内部错误
     */
    INTERNAL_ERROR(1002, "服务器内部错误"),

    /**
     * 1003, 非法的访问类型
     */
    INVALID_ACCESS(1003, "非法的访问"),

    /**
     * 1004, 身份认证失败
     */
    AUTH_FAILED(1004, "身份认证失败"),
    
    /**
     * 1005 alert统一错误相应
     */
    ALERT_ERROR(1005,"alert统一错误相应"),

    /**
     * 1006, 没有数据改变，请使用缓存
     */
    NO_DATA_CHANGE(1006, "没有数据改变，请使用缓存"),

    /**
     * 1007, 防护码失败
     */
    PROTECTCODE_ERROR(1007, "防护码失败"),

    /**
     * 1008, 参数错误
     */
    PARAM_ERROR(1008, "参数错误"),
    
    /**
     * 1009, 服务器开小差，请稍后再试
     */
    SERVER_ERROR(1009, "服务器开小差，请稍后再试"),
    
    /**
     * 1010, 请求频繁，请稍后再试
    */
    REPETITION_ERROR(1010, "请求频繁，请稍后再试"),

    /**
     * 1997, token错误
     */
    CSRF_CODE_ERROR(1997, "token错误"),
    /**
     * 303, 图形验证码错误
     */
    VERIFYCODE_IMAGE_ERROR(303, "图形验证码错误"),

    TOKEN_INVALID(401, "token失效，请重新登录"),

    FLOW_SENTINEL_ERROR(508, "当前访问人数较多，请稍后重试"),

    SMS_TOO_frequency(11000, "验证码发送过于频繁"),

    OUT_DAY_SEND_TIMES(11001, "超出当日发送次数"),

    CODE_ERROR(11002, "验证码错误"),

    CODE_EXPIRE(11003, "验证码已过期"),

    CODE_ALREADY_USED(11003, "验证码已使用，请重新获取"),

    EXIST_VIOLATE_ORDER(11004, "当前ETC账户存在欠费订单不可申请注销。请及时处理!"),

    EMPLOYEE_NOT_EXIST(401, "员工不存在"),
    ;

    private final int state;
    private final String message;

    ResultCodeEnum(int state) {
        this(state, null);
    }

    ResultCodeEnum(int state, String message) {
        this.state = state;
        this.message = message;
    }

}