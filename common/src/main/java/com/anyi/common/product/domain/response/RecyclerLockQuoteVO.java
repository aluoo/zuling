package com.anyi.common.product.domain.response;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/11
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecyclerLockQuoteVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Date createTime;
    @ApiModelProperty(value = "订单报价过期时间", notes = "超过该时间关闭报价功能")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date quoteExpiredTime;

    public void setQuoteExpiredTime(int quoteExpiredMinutes) {
        if (this.createTime == null) {
            return;
        }
        DateTime expiredTime = DateUtil.offset(DateUtil.date(this.createTime), DateField.MINUTE, quoteExpiredMinutes);
        this.quoteExpiredTime = expiredTime.toJdkDate();
    }
}