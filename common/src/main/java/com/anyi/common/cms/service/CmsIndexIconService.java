package com.anyi.common.cms.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.cms.domain.CmsIndexIcon;
import com.anyi.common.cms.domain.enums.IconPlaceEnum;
import com.anyi.common.cms.domain.response.CmsIndexIconVO;
import com.anyi.common.cms.mapper.CmsIndexIconMapper;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.exchange.service.MbExchangeOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/5/17
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class CmsIndexIconService extends ServiceImpl<CmsIndexIconMapper, CmsIndexIcon> {
    @Autowired
    MbExchangeOrderService mbExchangeOrderService;

    public List<CmsIndexIconVO> listIconsByPlace(Integer place, Long companyId, boolean reStatus,Integer companyType) {
        if (place == null) {
            return new ArrayList<>();
        }
        List<CmsIndexIcon> list = this.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(CmsIndexIcon::getActivated, true)
                .eq(CmsIndexIcon::getPlace, place)
                .orderByAsc(CmsIndexIcon::getSort)
                .list();

        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }

        //非门店类型都只有租机业务展示
        if(CompanyType.COMPANY.getCode()== companyType.intValue()){
            list = list.stream().filter(e -> e.getName().contains("租机")).collect(Collectors.toList());
        }

        List<CmsIndexIconVO> vos = BeanUtil.copyToList(list, CmsIndexIconVO.class);

        if (place.equals(IconPlaceEnum.INDEX.getType())) {
            vos.forEach(o -> {
                o.setIsTest(StrUtil.isNotBlank(o.getDescription()) && o.getDescription().equals("test"));
            });
            if (reStatus) {
                Set<Long> filterIds = vos.stream().filter(o -> StrUtil.isNotBlank(o.getDescription()) && o.getDescription().equals("4re")).map(CmsIndexIconVO::getId).collect(Collectors.toSet());
                if (CollUtil.isNotEmpty(filterIds)) {
                    vos = vos.stream().filter(o -> !filterIds.contains(o.getId())).collect(Collectors.toList());
                }
            }
        }

        if (place.equals(IconPlaceEnum.EXCHANGE.getType())) {
            boolean existExchange = mbExchangeOrderService.existExchange(companyId);
            vos.forEach(o -> {
                if (StrUtil.isNotBlank(o.getDescription()) && o.getDescription().equals("exchange")) {
                    List<String> urls = StrUtil.split(o.getLinkUrl(), ",");
                    o.setLinkUrl(existExchange ? urls.get(0) : urls.get(1));
                }
            });
        }


        return vos;
    }
}