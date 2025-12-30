package com.anyi.sparrow.cyx.resp;

import com.anyi.common.advice.BizError;
import com.anyi.sparrow.base.exception.ShangDongApiException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/11/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseCyxVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String errorCode;
    private String errorMessage;
    private String data;
    private String resultTime;
    private String traceId;

    public boolean isSuccess() {
        return this.errorCode.equals("00000");
    }

    public void checkSuccess() {
        if (!this.isSuccess()) {
            throw new ShangDongApiException(BizError.REMOTE_ERROR, this.errorMessage, true);
        }
    }
}