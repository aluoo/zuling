package com.anyi.sparrow.assist.system.dao.mapper;

import com.anyi.sparrow.common.vo.Dicts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SysDictMapper extends BaseMapper<Dicts> {
    String getByName(String name);

    List<Dicts> getByType(String type);
}