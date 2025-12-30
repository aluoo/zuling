package com.anyi.common.exchange.response;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 拉新安装包
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Data
public class MbExchangeQrcodeVO implements Serializable {

    private static final long serialVersionUID = 1L;

   private Long orderId;

   private String qrcodeUrl;

}
