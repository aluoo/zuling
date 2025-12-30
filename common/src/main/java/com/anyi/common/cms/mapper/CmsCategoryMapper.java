package com.anyi.common.cms.mapper;

import com.anyi.common.cms.domain.CmsCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/9/12
 */
@Mapper
@Repository
public interface CmsCategoryMapper extends BaseMapper<CmsCategory> {
}