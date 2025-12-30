package com.anyi.common.company.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.company.domain.PackageInfo;
import com.anyi.common.company.dto.PackageInfoApplyDTO;
import com.anyi.common.company.dto.PackageInfoDTO;
import com.anyi.common.company.mapper.PackageInfoMapper;
import com.anyi.common.company.req.CompanyPackageReq;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 套餐信息 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-02-26
 */
@Service
public class PackageInfoService extends ServiceImpl<PackageInfoMapper, PackageInfo> {
    public PackageInfoDTO getPackageInfoByPriceInterval(PackageInfoDTO req) {
        return this.baseMapper.getPackageInfoByPriceInterval(req);
    }

    public PackageInfoDTO getHighestPackageInfoByPriceInterval(PackageInfoDTO req) {
        PackageInfo highest = this.lambdaQuery()
                .eq(PackageInfo::getStatus, 1)
                .eq(PackageInfo::getBizTypeId, req.getBizTypeId())
                .eq(PackageInfo::getCompanyId, req.getCompanyId())
                .orderByDesc(PackageInfo::getPriceHigh)
                .last("limit 1")
                .one();
        return highest != null ? BeanUtil.copyProperties(highest, PackageInfoDTO.class) : null;
    }

    public PackageInfoDTO getLowestPackageInfoByPriceInterval(PackageInfoDTO req) {
        PackageInfo highest = this.lambdaQuery()
                .eq(PackageInfo::getStatus, 1)
                .eq(PackageInfo::getBizTypeId, req.getBizTypeId())
                .eq(PackageInfo::getCompanyId, req.getCompanyId())
                .orderByAsc(PackageInfo::getPriceLow)
                .last("limit 1")
                .one();
        return highest != null ? BeanUtil.copyProperties(highest, PackageInfoDTO.class) : null;
    }

    public List<PackageInfoDTO> packageInfoList(CompanyPackageReq req) {
        List<PackageInfoDTO> resultList = new ArrayList<>();
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<PackageInfo> packageInfoList = this.lambdaQuery()
                .eq(PackageInfo::getCompanyId, req.getCompanyId())
                .eq(PackageInfo::getBizTypeId, 3)
                .eq(PackageInfo::getStatus, 1)
                .orderByAsc(PackageInfo::getId)
                .list();
        resultList = BeanUtil.copyToList(packageInfoList, PackageInfoDTO.class);
        return resultList;
    }

    public void packageSet(List<PackageInfoApplyDTO> packageInfoApplyDTOList) {
        if (CollUtil.isEmpty(packageInfoApplyDTOList)) return;
        for (PackageInfoApplyDTO applyDTO : packageInfoApplyDTOList) {
            PackageInfo packageInfo = this.getById(applyDTO.getId());
            if (applyDTO.getRealCommissionFee() - packageInfo.getPlatCommissionFee() > 0) {
                throw new BusinessException(99999, packageInfo.getName() + "压价上限金额超过范围");
            }
            if (applyDTO.getRealCommissionScale().compareTo(packageInfo.getPlatCommissionScale()) > 0) {
                throw new BusinessException(99999, packageInfo.getName() + "压价比例超过范围");
            }
            packageInfo.setRealCommissionFee(applyDTO.getRealCommissionFee());
            packageInfo.setRealCommissionScale(applyDTO.getRealCommissionScale());
            this.updateById(packageInfo);
        }
    }


}