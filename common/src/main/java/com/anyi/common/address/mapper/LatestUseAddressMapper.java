package com.anyi.common.address.mapper;


import com.anyi.common.address.domain.LatestUseAddress;
import com.anyi.common.address.domain.LatestUseAddressExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface LatestUseAddressMapper {
    long countByExample(LatestUseAddressExample example);

    int deleteByExample(LatestUseAddressExample example);

    int deleteByPrimaryKey(Long id);

    int insert(LatestUseAddress record);

    int insertSelective(LatestUseAddress record);

    List<LatestUseAddress> selectByExampleWithRowbounds(LatestUseAddressExample example, RowBounds rowBounds);

    List<LatestUseAddress> selectByExample(LatestUseAddressExample example);

    LatestUseAddress selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") LatestUseAddress record, @Param("example") LatestUseAddressExample example);

    int updateByExample(@Param("record") LatestUseAddress record, @Param("example") LatestUseAddressExample example);

    int updateByPrimaryKeySelective(LatestUseAddress record);

    int updateByPrimaryKey(LatestUseAddress record);
}