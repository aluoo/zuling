package com.anyi.sparrow.assist.system.dao;

import com.anyi.sparrow.assist.system.dao.mapper.SysDictMapper;
import com.anyi.sparrow.common.vo.Dicts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysDictDao {
    @Autowired
    private SysDictMapper sysDictMapper;

    public String getByName(String name) {
        return sysDictMapper.getByName(name);
    }

    public List<Dicts> getByType(String type){
        return sysDictMapper.getByType(type);
    }


}
