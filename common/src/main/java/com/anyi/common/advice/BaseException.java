package com.anyi.common.advice;

public class BaseException extends RuntimeException {
//    private IError error;

    private int code;

    private String extMessage;

    private String message;

    private String posMsg;

    public BaseException(IError error, String extMessage, String posMsg) {
        this(error);
        this.extMessage = extMessage;
        this.posMsg=posMsg;
    }

    public BaseException(IError error, String extMessage) {
        this(error);
        this.extMessage = extMessage;
    }

    public BaseException(IError error) {
        super(error.getMessage());
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public BaseException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseException(int code, String message, String extMessage, String posMsg) {
        this.code = code;
        this.message = message;
        this.extMessage = extMessage;
        this.posMsg = posMsg;
    }


//    public IError getError() {
//        return error;
//    }

    public String getExtMessage() {
        return extMessage;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getPosMsg() {
        return posMsg;
    }
}
