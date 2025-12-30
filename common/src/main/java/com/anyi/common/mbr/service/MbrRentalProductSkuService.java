package com.anyi.common.mbr.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.mbr.domain.MbrRentalProductSku;
import com.anyi.common.mbr.mapper.MbrRentalProductSkuMapper;
import com.anyi.common.mbr.response.MbrProductDTO;
import com.anyi.common.product.domain.dto.ProductDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class MbrRentalProductSkuService extends ServiceImpl<MbrRentalProductSkuMapper, MbrRentalProductSku>  {

    /**
     * 获取产品信息映射表
     *
     * @param ids 产品ID列表
     * @return 产品信息映射表
     */
    public Map<Long, MbrProductDTO> getProductInfoMap(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<MbrProductDTO> products = baseMapper.listProduct(ids);
        if (CollUtil.isEmpty(products)) {
            return Collections.emptyMap();
        }
        return products.stream()
                .filter(Objects::nonNull)
                .filter(o -> StrUtil.isNotBlank(o.getBrandLogo()))
                .collect(Collectors.toMap(MbrProductDTO::getSkuId, Function.identity()));
    }

}
