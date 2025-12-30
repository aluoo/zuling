package com.anyi.common.insurance.mapper;

import com.anyi.common.insurance.domain.DiInsuranceOption;
import com.anyi.common.insurance.domain.DiOption;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数保产品关联选项表 Mapper 接口
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
public interface DiInsuranceOptionMapper extends BaseMapper<DiInsuranceOption> {

    List<DiOption> getByInsuranceId(@Param("insuranceId") Long insuranceId,@Param("code") String code);


    List<DiOption> getOptionByAncestors(@Param("insuranceId") Long insuranceId,@Param("ancestors") String ancestors,@Param("code") String code);


}
