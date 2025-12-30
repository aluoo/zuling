package com.anyi.sparrow.organize.employee.service;

import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.sparrow.assist.system.service.SysDictService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.organize.companyCfg.service.CompanyCfgService;
import com.anyi.sparrow.organize.employee.dao.mapper.CfgPairMapper;
import com.anyi.sparrow.organize.employee.dao.mapper.EmpCfgMapper;
import com.anyi.sparrow.organize.employee.dao.mapper.ExtCfgPairMapper;
import com.anyi.sparrow.organize.employee.domain.CfgPair;
import com.anyi.sparrow.organize.employee.domain.CfgPairExample;
import com.anyi.sparrow.organize.employee.domain.EmpCfg;
import com.anyi.sparrow.organize.employee.domain.EmpCfgExample;
import com.anyi.sparrow.organize.employee.vo.EmpCfgVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class EmpCfgService {
    @Autowired
    private EmpCfgMapper empCfgMapper;
    @Autowired
    private CfgPairMapper cfgPairMapper;
    @Autowired
    private ExtCfgPairMapper extCfgPairMapper;
    @Autowired
    private CompanyCfgService companyCfgService;
    @Autowired
    private SysDictService dictService;

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    private static final Logger logger = LoggerFactory.getLogger(EmpCfgService.class);

    @Transactional
    public EmpCfgVo inert(EmpCfgVo empCfgVo) {
        EmpCfg empCfg = new EmpCfg();
        BeanUtils.copyProperties(empCfgVo, empCfg);
        empCfg.setId(snowflakeIdService.nextId());
        empCfg.setEmpId(LoginUserContext.getUser().getId());
        empCfg.setCreateTime(new Date());
        empCfg.setUpdateTime(new Date());
        empCfgMapper.insert(empCfg);
        String pairNo = generatePair(empCfg);
        empCfgVo.setPairNo(pairNo);
        return empCfgVo;
    }

    private String generatePair(EmpCfg empCfg) {
        switch (empCfg.getBiz()) {
            case 1:
                return generateIcbcPair(empCfg, 3);
        }
        return null;
    }

    private String generateIcbcPair(EmpCfg empCfg, int times) {
        CfgPairExample example = new CfgPairExample();
        example.createCriteria().andBizEqualTo(empCfg.getBiz()).andNoEqualTo(empCfg.getNo());
        List<CfgPair> cfgPairs = cfgPairMapper.selectByExample(example);
        if (cfgPairs.size() > 0) {
            return cfgPairs.get(0).getPairNo();
        }
        String generate = generate(empCfg.getBiz());
        CfgPair cfgPair = new CfgPair();
        cfgPair.setBiz(empCfg.getBiz());
        cfgPair.setCreateTime(new Date());
        cfgPair.setId(snowflakeIdService.nextId());
        cfgPair.setNo(empCfg.getNo());
        cfgPair.setPairNo(generate);
        try {
            cfgPairMapper.insert(cfgPair);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            if (times < 0) {
                throw new BusinessException(BizError.Create_EMP_CFG);
            }
            return generateIcbcPair(empCfg, --times);
        }
        return generate;
    }


    private String generate(Integer biz) {
        String max = extCfgPairMapper.getMax(biz);
        if (max == null) {
            max = "Z00031001";
        }
        int i = Integer.valueOf(max.substring(max.length() - 4)) + 1;
        String greet = dictService.getByNameWithCache("ql_bank_greet");
        if (greet != null){
            List<String> greets = Arrays.asList(greet.split(","));
            boolean contains = greets.contains(String.valueOf(i));
            logger.info("i:{},contains:{}, greets:{}", i, contains, greets);
            while (greets.contains(String.valueOf(i)) && i <= 9999){
                ++i;
            }
        }
        if (i > 9999) {
            throw new BusinessException(BizError.OUT_INDEX);
        }
        return max.substring(0, max.length() - 4) + i;
    }


    public EmpCfgVo query(Integer biz) {
        return query(biz, LoginUserContext.getUser().getId());
    }

    public EmpCfgVo query(Integer biz, Long empId) {
        return extCfgPairMapper.getByIdAndBiz(empId, biz);
    }


    @Transactional
    public EmpCfgVo update(EmpCfgVo vo) {
        EmpCfgExample example = new EmpCfgExample();
        example.createCriteria().andEmpIdEqualTo(LoginUserContext.getUser().getId())
                .andBizEqualTo(vo.getBiz());
        List<EmpCfg> empCfgs = empCfgMapper.selectByExample(example);
        if (empCfgs.size() == 0) {
            throw new BusinessException(BizError.CONFIG_NOT_EXIST);
        }
        EmpCfg empCfg = empCfgs.get(0);
        empCfg.setName(vo.getName());
        empCfg.setNo(vo.getNo());
        empCfg.setUpdateTime(new Date());
        empCfgMapper.updateByPrimaryKeySelective(empCfg);
        String s = generatePair(empCfg);
        vo.setPairNo(s);
        return vo;
    }
}
