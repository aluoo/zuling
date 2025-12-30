package com.anyi.common.result;

import com.anyi.common.enums.ResultCodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应实体
 * <p>
 * <br/>
 * 格式如下
 *
 * <pre>
 * {
 *     "data": "自定义业务Response",
 *     "message": "操作成功",
 *     "code": 1,
 * }
 * </pre>
 */

@Data
public class ResponseDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作状态
     */
    private int code;

    /**
     * 状态附加消息
     */
    private String message;

    /**
     * 响应内容
     */
    private T data;

    private ResponseDTO() {
    }

    private ResponseDTO(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDTO<T> response(int state, String message, T content) {
        ResponseDTO<T> responseDTO = new ResponseDTO<>();
        responseDTO.setCode(state);
        responseDTO.setMessage(message);
        responseDTO.setData(content);
        return responseDTO;
    }


    public static <T> ResponseDTO<T> response(ResultCodeEnum resultCodeEnum) {
        return response(resultCodeEnum, null);
    }

    public static <T> ResponseDTO<T> response(ResultCodeEnum resultCodeEnum, T content) {
        return response(resultCodeEnum.getState(), resultCodeEnum.getMessage(), content);
    }

    public static <T> ResponseDTO<T> response(int state, String message) {
        return response(state, message, null);
    }

    public static <T> ResponseDTO<T> ofError() {
        return new ResponseDTO<T>(ResultCodeEnum.INTERNAL_ERROR.getState(), "", null);
    }

    public static <T> ResponseDTO<T> ofError(String message) {
        return new ResponseDTO<T>(ResultCodeEnum.INTERNAL_ERROR.getState(), message, null);
    }

    public static <T> ResponseDTO<T> ofError(int state, String message) {
        return ofError(state, message, null);
    }

    public static <T> ResponseDTO<T> ofError(int state, String message, T content) {
        return new ResponseDTO<T>(state, message, content);
    }


    public static <T> ResponseDTO<T> success() {
        return response(ResultCodeEnum.SUCCESS);
    }

    public static <T> ResponseDTO<T> success(T content) {
        return response(ResultCodeEnum.SUCCESS, content);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == 200;
    }

}
