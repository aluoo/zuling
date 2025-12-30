package com.anyi.common.domain.param;

import com.anyi.common.advice.IError;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ApiModel("返回值")
public class Response<T> {
    public static int SUCCESS_CODE = 200;

    @ApiModelProperty("返回值CODE, (成功=200)")
    private int code = SUCCESS_CODE;

    @ApiModelProperty("业务异常消息")
    private String message;

    @ApiModelProperty("弹框消息")
    private String popMsg;

    @ApiModelProperty("异常堆栈信息")
    private String extMessage;

    @ApiModelProperty("数据")
    private T data;

    @ApiModelProperty("总条数,暂时未用")
    private Integer count;

    private static final Map emptyMap = Collections.emptyMap();

    private static final List emtypList = Collections.emptyList();

    public static <T> Response<T> ok() {
        return new Response(emptyMap);
    }

    public static <T> Response<T> ok(T data) {
//        if(data instanceof PageInfo) {
//            return ok((PageInfo)data);
//        }
//        if(data instanceof Page) {
//            return ok((Page)data);
//        }
        if(data == null) {
            return new Response(emptyMap);
        }
        return new Response<T>(data);
    }

    public static <T> Response<List<T>> ok(PageInfo<T> data) {
        if (data == null) {
            return new Response(emptyMap);
        }
        Response<List<T>> response = new Response<>(data.getList());
        response.setCount((int) data.getTotal());
        return response;
    }
//    public static <T> Response<T> ok(PageInfo data) {
//        if(data == null) {
//            return new Response(emtypList);
//        }
//        Response response = new Response(data.getList());
//        //response.setCount(data.getPages());
//        return response;
//    }
//
//    public static <T> Response<T> ok(Page data) {
//        if(data == null) {
//            return new Response(emtypList);
//        }
//        Response response = new Response(data);
//        //response.setCount(data.getPages());
//        return response;
//    }

    public static <T> Response<T> failed(IError error, String extMessage) {
//        return new Response(error.getCode(), error.getMessage(), extMessage, null);
        return new Response(error.getCode(), extMessage, extMessage, null);
    }

    public static <T> Response<T> failed(IError error) {
        return failed(error, null);
    }

    public static <T> Response<T> failed(int code, String message, String extMessage) {
        return new Response(code, message, extMessage, null);
    }

    public static <T> Response<T> failed(int code, String message) {
        return failed(code, message, null);
    }

    public static <T> Response<T> failed(int code, String message, String extMessage,String popMsg) {
        return new Response(code, message, extMessage,popMsg,null);
    }



    public Response() {
    }

    public Response(T data) {
        this.data = data;
        this.message = "请求成功";
    }


    public Response(int code, String message, String extMessage,String popMsg, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.extMessage = extMessage;
        this.popMsg=popMsg;
    }

    public Response(int code, String message, String extMessage, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.extMessage = extMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getExtMessage() {
        return extMessage;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getPopMsg() {
        return popMsg;
    }

    public void setPopMsg(String popMsg) {
        this.popMsg = popMsg;
    }
}