package com.anyi.sparrow.assist.system.service;

import com.anyi.sparrow.common.Constants;
import com.anyi.sparrow.common.utils.TimeUtil;
import com.anyi.sparrow.common.vo.Dicts;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.anyi.sparrow.assist.system.dao.SysDictDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import java.util.List;

@Component
public class SysDictService {
    @Autowired
    private SysDictDao sysDictDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public String getByName(String name){
        return sysDictDao.getByName(name);
    }

    public String getByNameWithCache(String name){
        String s = redisTemplate.opsForValue().get(Constants.dict_name_cache_prefix + name);
        if (s != null){
            return s;
        }
        String byName = getByName(name);
        if (byName != null){
            redisTemplate.opsForValue().set(Constants.dict_name_cache_prefix + name, byName, TimeUtil.thisDaySecond(), TimeUnit.SECONDS);
        }
        return byName;
    }

    public List<Dicts> getByTypeWithCache(String type){
        String data = redisTemplate.opsForValue().get(Constants.dict_type_cache_prefix + type);
        if (data != null){
            return JSONArray.parseArray(data, Dicts.class);
        }
        List<Dicts> dicts = sysDictDao.getByType(type);
        if (dicts.size() > 0){
            redisTemplate.opsForValue().set(Constants.dict_type_cache_prefix + type, JSON.toJSONString(dicts), TimeUtil.thisDaysMinute(), TimeUnit.MINUTES);
        }
        return dicts;
    }

    public List<Dicts> getByType(String type){
        List<Dicts> dicts = sysDictDao.getByType(type);
        return dicts;
    }
}
