package com.anyi.sparrow.organize.invite.service;

import com.anyi.common.advice.BusinessException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.oss.FileUploader;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.utils.WxUtils;
import com.anyi.sparrow.organize.invite.vo.SpreadCodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * ETC办理员推广二维码 服务实现类
 * </p>
 *
 * @author L
 * @since 2023-07-08
 */
@Service
@Slf4j
public class SpreadCodeService{

    @Autowired
    private WxUtils wxUtils;
    private String tmpdir = System.getProperty("java.io.tmpdir");
    @Autowired
    private FileUploader fileUploader;
    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Value("${tokenExpire:24}")
    private int tokenExpire;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    CompanyService companyService;

    public SpreadCodeVo companyCreate() {
        SpreadCodeVo resultVo = new SpreadCodeVo();
        //生成二维码
        Map<String, String> params = new HashMap<>();
        params.put("employeeId", LoginUserContext.getUser().getId().toString());
        params.put("companyType", LoginUserContext.getUser().getCompanyType().toString());
        String qrUrl = wxUtils.genQrCode("pages/invite/agent?", params, 60);
        //回填二维码地址
        resultVo.setCodeUrl(qrUrl);
        resultVo.setQrCodeUrl(qrUrl);
        return resultVo;
    }

    public SpreadCodeVo recycleCreate() {
        SpreadCodeVo resultVo = new SpreadCodeVo();
        //生成二维码
        Map<String, String> params = new HashMap<>();
        params.put("employeeId", LoginUserContext.getUser().getId().toString());
        String qrUrl = wxUtils.genQrCode("pages/invite/service?", params, 60);
        //回填二维码地址
        resultVo.setCodeUrl(qrUrl);
        resultVo.setQrCodeUrl(qrUrl);
        return resultVo;
    }

    public SpreadCodeVo serviceEmployee() {
        SpreadCodeVo resultVo = new SpreadCodeVo();
        String mobile = LoginUserContext.getUser().getMobileNumber();
        Employee applyEmployee = employeeService.getByMobileStatus(mobile);
        if (Objects.isNull(applyEmployee)) {
            throw new BusinessException(99999, "服务商员工不存在");
        }
        Company company = companyService.getById(applyEmployee.getCompanyId());
        resultVo.setCompanyName(company.getName());
        //生成二维码
        Map<String, String> params = new HashMap<>();
        params.put("employeeId", applyEmployee.getId().toString());
        String qrUrl = wxUtils.genQrCode("pages/invite/staffInvite?", params, 60);
        //回填二维码地址
        resultVo.setCodeUrl(qrUrl);
        return resultVo;
    }

    public SpreadCodeVo companyStaffCreate() {
        SpreadCodeVo resultVo = new SpreadCodeVo();
        //生成二维码
        Map<String, String> params = new HashMap<>();
        params.put("companyId", LoginUserContext.getUser().getCompanyId().toString());
        params.put("employeeId", LoginUserContext.getUser().getId().toString());
        params.put("companyType", LoginUserContext.getUser().getCompanyType().toString());
        String qrUrl = wxUtils.genQrCode("pages/invite/worker?", params, 60);
        //回填二维码地址
        resultVo.setCodeUrl(qrUrl);
        resultVo.setQrCodeUrl(qrUrl);
        return resultVo;
    }
}