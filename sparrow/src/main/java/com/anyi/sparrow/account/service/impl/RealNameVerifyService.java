package com.anyi.sparrow.account.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.sparrow.account.domain.EmployeeRealNameVerification;
import com.anyi.sparrow.account.dto.response.EmployeeRealNameVerificationVO;
import com.anyi.sparrow.account.req.EmployeeRealNameVerificationReq;
import com.anyi.sparrow.third.shumai.ShuMaiRealNameVerifyApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/10/9
 */
@Slf4j
@Service
public class RealNameVerifyService {
    @Autowired
    private IEmployeeRealNameVerificationService verificationService;

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Value("${spring.profiles.active}")
    private String env;


    public EmployeeRealNameVerificationVO getInfoByEmployeeId(Long employeeId) {
        EmployeeRealNameVerificationVO vo = new EmployeeRealNameVerificationVO();
        EmployeeRealNameVerification bean = verificationService.getByEmployeeId(employeeId);
        if (bean != null) {
            BeanUtil.copyProperties(bean, vo);
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public EmployeeRealNameVerificationVO save(EmployeeRealNameVerificationReq req, Long employeeId) {
        EmployeeRealNameVerificationVO vo = new EmployeeRealNameVerificationVO();
        BeanUtil.copyProperties(req, vo);
        ShuMaiRealNameVerifyApi.Response verifyResp = null;
        if ("production".equals(env)) {
            verifyResp = ShuMaiRealNameVerifyApi.verify(req.getName(), req.getIdCard());
            if (!verifyResp.isSuccess()) {
                throw new BaseException(verifyResp.getCode(), verifyResp.getMessage());
            }
            vo.setVerifyResult(ShuMaiRealNameVerifyApi.Response.getVerifyRes(verifyResp));
            if (!ShuMaiRealNameVerifyApi.Response.checkVerifyRes(verifyResp)) {
                if (vo.getVerifyResult() == 3) {
                    throw new BaseException(-1, "无记录");
                }
                if (vo.getVerifyResult() == 2) {
                    throw new BaseException(-1, "不一致");
                }
                throw new BaseException(-1, "未知错误");
            }
        } else {
            vo.setVerifyResult(1);
        }
        EmployeeRealNameVerification old = verificationService.getByEmployeeId(employeeId);
        if (old != null) {
            verificationService.lambdaUpdate()
                    .set(EmployeeRealNameVerification::getDeleted, true)
                    .eq(EmployeeRealNameVerification::getEmployeeId, employeeId)
                    .eq(EmployeeRealNameVerification::getDeleted, false)
                    .eq(EmployeeRealNameVerification::getId, old.getId())
                    .update();
        }

        EmployeeRealNameVerification bean = EmployeeRealNameVerification.builder()
                .id(snowflakeIdService.nextId())
                .employeeId(employeeId)
                .name(req.getName())
                .idCard(req.getIdCard())
                .idUrlUp(req.getIdUrlUp())
                .idUrlDown(req.getIdUrlDown())
                .remoteResp("production".equals(env) ? JSONUtil.toJsonStr(verifyResp) : "")
                .build();
        verificationService.save(bean);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public EmployeeRealNameVerificationVO saveTemp(EmployeeRealNameVerificationReq req, Long employeeId) {
        EmployeeRealNameVerificationVO vo = new EmployeeRealNameVerificationVO();
        BeanUtil.copyProperties(req, vo);
        vo.setVerifyResult(1);
        EmployeeRealNameVerification old = verificationService.getByEmployeeId(employeeId);
        if (old != null) {
            verificationService.lambdaUpdate()
                    .set(EmployeeRealNameVerification::getDeleted, true)
                    .eq(EmployeeRealNameVerification::getEmployeeId, employeeId)
                    .eq(EmployeeRealNameVerification::getDeleted, false)
                    .eq(EmployeeRealNameVerification::getId, old.getId())
                    .update();
        }

        EmployeeRealNameVerification bean = EmployeeRealNameVerification.builder()
                .id(snowflakeIdService.nextId())
                .employeeId(employeeId)
                .name(req.getName())
                .idCard(req.getIdCard())
                .idUrlUp(req.getIdUrlUp())
                .idUrlDown(req.getIdUrlDown())
                .remoteResp("")
                .build();
        verificationService.save(bean);
        return vo;
    }
}