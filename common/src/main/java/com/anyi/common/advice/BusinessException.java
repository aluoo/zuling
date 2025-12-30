package com.anyi.common.advice;

import cn.hutool.core.util.StrUtil;


import java.text.MessageFormat;

public class BusinessException extends BaseException {

    private static final String PLATFORM_FREFIX = "";
    public BusinessException(IError error) {
        super(error.getCode(), PLATFORM_FREFIX + error.getMessage());

    }

    public BusinessException(IError error, String extMessage) {
        super(error.getCode(), PLATFORM_FREFIX + error.getMessage(), extMessage, null);
    }

    public BusinessException(IError error, String extMessage,String posMsg) {
        super(error.getCode(), PLATFORM_FREFIX + error.getMessage(), extMessage, posMsg);

    }

    /**
     * 消息格式化
     * @param error
     * @param args
     */
    public BusinessException(IError error, Object[] args) {
        super(error.getCode(), PLATFORM_FREFIX + MessageFormat.format(error.getMessage(), args));
    }


    public BusinessException(BizError error, String remark, boolean replace) {
        super(error.getCode(), PLATFORM_FREFIX + StrUtil.format(error.getMessage(), remark));
    }

    public BusinessException(int code, String message) {
        super(code,message);
    }
}
