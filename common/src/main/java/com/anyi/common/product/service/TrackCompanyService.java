package com.anyi.common.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.anyi.common.product.domain.TrackCompany;
import com.anyi.common.product.domain.dto.TrackCompanyDTO;
import com.anyi.common.product.mapper.TrackCompanyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/8
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class TrackCompanyService extends ServiceImpl<TrackCompanyMapper, TrackCompany> {

    public List<TrackCompanyDTO> listAllTrackCompanyDTO() {
        List<TrackCompany> list = this.listAllTrackCompany();
        return CollUtil.isNotEmpty(list) ? BeanUtil.copyToList(list, TrackCompanyDTO.class) : null;
    }

    private List<TrackCompany> listAllTrackCompany() {
        return this.lambdaQuery()
                .orderByDesc(TrackCompany::getOdrIdx)
                .list();
    }
}