package com.anyi.sparrow.base.exception;

import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.IError;

import java.text.MessageFormat;

/**
 * 山东信元API异常
 */
public class ShangDongApiException extends BaseException {

    private static final String PLATFORM_FREFIX = "高速-";

    public ShangDongApiException(IError error) {
        super(error.getCode(), PLATFORM_FREFIX + error.getMessage());
    }

    public ShangDongApiException(IError error, String extMessage) {
        super(error.getCode(), PLATFORM_FREFIX + error.getMessage(), extMessage, null);
    }

    public ShangDongApiException(IError error, String extMessage, String posMsg) {
        super(error.getCode(), PLATFORM_FREFIX + error.getMessage(), extMessage, posMsg);
    }

    /**
     * 消息格式化
     *
     * @param error
     * @param args
     */
    public ShangDongApiException(IError error, Object[] args) {
        super(error.getCode(), PLATFORM_FREFIX + MessageFormat.format(error.getMessage(), args));
    }


    public ShangDongApiException(BizError error, String remark, boolean replace) {
        super(error.getCode(), PLATFORM_FREFIX + StrUtil.format(error.getMessage(), remark));
    }
}
