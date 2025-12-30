package com.anyi.common.hk.service;

import com.anyi.common.hk.domain.HkProductEmployee;
import com.anyi.common.hk.mapper.HkProductEmployeeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class HkProductEmployeeService extends ServiceImpl<HkProductEmployeeMapper, HkProductEmployee>  {

        public List<Long> getProductByCompanyId(Long companyId){
            return this.lambdaQuery().eq(HkProductEmployee::getCompanyId, companyId).list()
                    .stream().map(HkProductEmployee::getProductId).collect(Collectors.toList());
        }
}
