package com.anyi.common.commission.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.commission.domain.CommissionSettleCheck;
import com.anyi.common.commission.mapper.CommissionSettleCheckMapper;
import com.anyi.common.commission.req.CommissionSettleCheckReq;
import com.anyi.common.commission.response.CommissionSettleCheckSumVO;
import com.anyi.common.commission.response.CommissionSettleCheckVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
@Slf4j
public class CommissionSettleCheckService extends ServiceImpl<CommissionSettleCheckMapper, CommissionSettleCheck> {

    public PageInfo<CommissionSettleCheckVO> listOrder(CommissionSettleCheckReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<CommissionSettleCheckVO> resultList = this.baseMapper.selectByParam(req);
        if (CollUtil.isEmpty(resultList)) {
            return PageInfo.emptyPageInfo();
        }
        PageInfo<CommissionSettleCheckVO> resp = PageInfo.of(resultList);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

    public CommissionSettleCheckSumVO SumByParam(CommissionSettleCheckReq req) {
        CommissionSettleCheckSumVO resultVo = this.baseMapper.SumByParam(req);
        if(ObjectUtil.isNull(resultVo)){
            return CommissionSettleCheckSumVO.builder().appTotal(0L).insuranceTotal(0L).mobileTotal(0L).build();
        }
        return resultVo;
    }


}