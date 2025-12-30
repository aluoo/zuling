package com.anyi.common.snowWork;

import com.anyi.common.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeIdService {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;



    public Long nextId(){
        return this.snowflakeIdWorker.nextId();
    }


}
