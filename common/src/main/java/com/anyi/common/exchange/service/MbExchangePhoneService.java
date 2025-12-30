package com.anyi.common.exchange.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.anyi.common.exchange.domain.MbExchangeInstall;
import com.anyi.common.exchange.domain.MbExchangePhone;
import com.anyi.common.exchange.domain.MbInstall;
import com.anyi.common.exchange.mapper.MbExchangePhoneMapper;
import com.anyi.common.exchange.response.MbInstallVO;
import com.anyi.common.product.domain.ShippingOrderAddress;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 拉新换机包 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Service
public class MbExchangePhoneService extends ServiceImpl<MbExchangePhoneMapper, MbExchangePhone>  {

    @Autowired
    MbInstallService mbInstallService;
    @Autowired
    MbExchangeInstallService mbExchangeInstallService;

    public List<MbInstallVO> getInstallList(List<Long> phoneIds){
        List<MbInstallVO> resultVo = new ArrayList<>();
        //启用状态的换机包
        List<MbExchangePhone> phoneList = this.list(Wrappers.lambdaQuery(MbExchangePhone.class)
                .eq(MbExchangePhone::getStatus,1).in(MbExchangePhone::getId,phoneIds));
        if(CollUtil.isEmpty(phoneList)) return resultVo;

        List<Long> ids = phoneList.stream().map(MbExchangePhone::getId).collect(Collectors.toList());
        //换机包对应的安装包
        List<MbExchangeInstall> exchangeInstallList = mbExchangeInstallService.list(
                Wrappers.lambdaQuery(MbExchangeInstall.class)
                .in(MbExchangeInstall::getExchangePhoneId,ids)).stream().collect(Collectors.toList());
        if(CollUtil.isEmpty(exchangeInstallList)) return resultVo;

        Map<Long,MbExchangeInstall> installMap = exchangeInstallList.stream().collect(Collectors.toMap(MbExchangeInstall::getInstallId, Function.identity()));

        List<Long>installIds = exchangeInstallList.stream().map(MbExchangeInstall::getInstallId).collect(Collectors.toList());

        List<MbInstall> installList = mbInstallService.list(Wrappers.lambdaQuery(MbInstall.class)
                .eq(MbInstall::getStatus,1)
                .in(MbInstall::getId,installIds));

        List<MbInstallVO> resultList = new ArrayList<>();
        for(MbInstall install:installList){
            MbInstallVO vo = new MbInstallVO();
            BeanUtil.copyProperties(install,vo);
            vo.setExchangePhoneId(Optional.ofNullable(installMap.get(install.getId())).map(MbExchangeInstall::getExchangePhoneId).orElse(null));
            resultList.add(vo);
        }

        return resultList;

    }

}
