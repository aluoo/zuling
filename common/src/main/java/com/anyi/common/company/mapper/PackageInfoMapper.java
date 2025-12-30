package com.anyi.common.company.mapper;


import com.anyi.common.company.domain.PackageInfo;
import com.anyi.common.company.dto.PackageInfoDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 套餐信息 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2024-02-26
 */
public interface PackageInfoMapper extends BaseMapper<PackageInfo> {
    /**
     * 传入价格，找出对应价格区间的套餐信息
     * @param req PackageInfoDTO
     * @return PackageInfoDTO
     */
    PackageInfoDTO getPackageInfoByPriceInterval(@Param("req") PackageInfoDTO req);
}