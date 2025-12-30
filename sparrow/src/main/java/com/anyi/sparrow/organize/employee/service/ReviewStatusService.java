package com.anyi.sparrow.organize.employee.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.domain.dto.AppReviewControlConfigDTO;
import com.anyi.sparrow.assist.system.service.SysDictService;
import com.anyi.sparrow.organize.employee.dao.mapper.EmployeeLoginMapper;
import com.anyi.sparrow.organize.employee.domain.EmployeeLogin;
import com.anyi.sparrow.organize.employee.vo.EmployeeLoginVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/9/5
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ReviewStatusService {
    @Autowired
    private SysDictService dictService;
    @Autowired
    private EmployeeLoginMapper employeeLoginMapper;

    public boolean getReStatus(Long employeeId) {
        String key = "app_review_control_config";
        String value = dictService.getByNameWithCache(key);
        if (StrUtil.isBlank(value)) {
            return false;
        }
        LambdaQueryWrapper<EmployeeLogin> qw = new LambdaQueryWrapper<EmployeeLogin>().eq(EmployeeLogin::getUserId, employeeId).orderByDesc(EmployeeLogin::getLoginTime).last("limit 1");
        EmployeeLogin vo = employeeLoginMapper.selectOne(qw);
        AppReviewControlConfigDTO config = JSONUtil.toBean(value, AppReviewControlConfigDTO.class);
        if (config.getOs().equals(AppReviewControlConfigDTO.OS.ALL.getName())) {
            if (config.getAppVersion().equals(AppReviewControlConfigDTO.OS.ALL.getName())) {
                return true;
            }
            if (vo.getAppVersion().equals(config.getAppVersion())) {
                return true;
            }
        }
        if (vo.getOs().equals(config.getOs())) {
            if (config.getAppVersion().equals(AppReviewControlConfigDTO.OS.ALL.getName())) {
                return true;
            }
            if (vo.getAppVersion().equals(config.getAppVersion())) {
                return true;
            }
        }
        return false;
    }

    public boolean getMobileExchangeReviewStatus(EmployeeLoginVO req) {
        if (req == null || StrUtil.isBlank(req.getAppVersion())) {
            return false;
        }
        String key = "mobile_exchange_app_review_control_config";
        String value = dictService.getByNameWithCache(key);
        if (StrUtil.isBlank(value)) {
            return false;
        }
        // 换机助手默认隐藏登录入口
        // 审核的版本，状态为true，保持隐藏
        // 不是审核的版本，状态为false，放开入口
        AppReviewControlConfigDTO config = JSONUtil.toBean(value, AppReviewControlConfigDTO.class);
        if (config == null || StrUtil.isBlank(config.getAppVersion())) {
            return false;
        }
        // 请求与配置版本一致，表示该版本正在审核，返回true保持登录入口隐藏
        return req.getAppVersion().equals(config.getAppVersion());
    }
}