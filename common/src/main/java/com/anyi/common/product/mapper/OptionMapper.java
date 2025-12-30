package com.anyi.common.product.mapper;

import com.anyi.common.product.domain.Option;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface OptionMapper extends BaseMapper<Option> {

    /**
     * 根据产品ID查询关联的选项列表
     *
     * @param productId 产品ID
     * @return 选项列表
     */
    @Select("select op.* from mb_option op inner join mb_product_option rel on rel.option_id = op.id and rel.product_id = #{productId} where op.deleted = 0 order by op.level asc, op.sort asc")
    List<Option> listOptionsByProductId(@Param("productId") Long productId);
}