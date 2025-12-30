package com.anyi.common.jdl.exception;

import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.IError;

import java.text.MessageFormat;

public class JdlBizException extends BaseException {

    private static final String PLATFORM_PREFIX = "京东物流异常-";

    public JdlBizException(IError error) {
        super(error.getCode(), PLATFORM_PREFIX + error.getMessage());

    }

    public JdlBizException(IError error, String extMessage) {
        super(error.getCode(), PLATFORM_PREFIX + error.getMessage(), extMessage, null);
    }

    public JdlBizException(IError error, String extMessage, String posMsg) {
        super(error.getCode(), PLATFORM_PREFIX + error.getMessage(), extMessage, posMsg);

    }

    /**
     * 消息格式化
     * @param error
     * @param args
     */
    public JdlBizException(IError error, Object[] args) {
        super(error.getCode(), PLATFORM_PREFIX + MessageFormat.format(error.getMessage(), args));
    }


    public JdlBizException(BizError error, String remark, boolean replace) {
        super(error.getCode(), PLATFORM_PREFIX + StrUtil.format(error.getMessage(), remark));
    }

    public JdlBizException(int code, String message) {
        super(code, PLATFORM_PREFIX + message);
    }

    public JdlBizException(int code, String message, Object... params) {
        super(code, PLATFORM_PREFIX + StrUtil.format(message, params));
    }
}