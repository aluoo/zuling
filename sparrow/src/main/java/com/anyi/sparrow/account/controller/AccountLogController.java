package com.anyi.sparrow.account.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.account.req.QueryAccountReq;
import com.anyi.common.account.service.IEmployeeAccountService;
import com.anyi.common.account.vo.EmployeeAccountVO;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.github.pagehelper.Page;
import com.anyi.common.account.constant.UserFocusTypeEnum;
import com.anyi.common.account.domain.EmployeeAccountLog;
import com.anyi.common.account.dto.EmployeeAccoutLogDTO;
import com.anyi.common.account.req.QueryAccountLogReq;
import com.anyi.common.account.service.IEmployeeAccountLogService;
import com.anyi.common.domain.param.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 账户模块相关接口控制器
 * </p>
 *
 * @author shenbh
 * @since 2023-03-24
 */
@Slf4j
@Api(tags = "账户余额变动模块相关接口")
@RestController
@RequestMapping("/eapp/v1.0/account")
public class AccountLogController {

    @Autowired
    private IEmployeeAccountLogService employeeAccountLogService;
    @Autowired
    private IEmployeeAccountService employeeAccountService;

    @ApiOperation("获取资金明细类型接口")
    @PostMapping("getAccontLogType")
    public Response<List<Map<String, String>>> getAccontLogType() {
        List<Map<String, String>> list = UserFocusTypeEnum.getTypesForUserSelect();
        return Response.ok(list);
    }

    @ApiOperation("获取资金钱包接口")
    @PostMapping("getAccont")
    public Response<EmployeeAccountVO> getAccont(@RequestBody QueryAccountReq req) {
        Long employeeId = LoginUserContext.getUser().getId();
        if (ObjectUtil.isNotNull(req.getEmployeeId())) {
            employeeId = req.getEmployeeId();
        }
        return Response.ok(employeeAccountService.getByEmployee(employeeId));
    }


    @ApiOperation("资金明细列表接口")
    @PostMapping("listAccontLog")
    public Response<List<EmployeeAccoutLogDTO>> listAccontLog(@RequestBody QueryAccountLogReq req) {
        Long employeeId = LoginUserContext.getUser().getId();
        if (ObjectUtil.isNotNull(req.getEmployeeId())) {
            employeeId = req.getEmployeeId();
        }
        Page<EmployeeAccountLog> pageDto = employeeAccountLogService.listUserFocusAccountLog(employeeId, req);
        List<EmployeeAccountLog> list = pageDto.getResult();

        List<EmployeeAccoutLogDTO> result = null;
        if (CollectionUtil.isNotEmpty(list)) {
            result = list.stream().map(item -> mapppingToDTO(item)).collect(Collectors.toList());
        } else {
            result = new ArrayList<>();
        }
        Response<List<EmployeeAccoutLogDTO>> response = Response.ok(result);
        response.setCount((int) pageDto.getTotal());
        return response;
    }

    public EmployeeAccoutLogDTO mapppingToDTO(EmployeeAccountLog employeeAccountLog) {
        if (employeeAccountLog == null) {
            return null;
        }
        EmployeeAccoutLogDTO employeeAccoutLogDTO = new EmployeeAccoutLogDTO();
        employeeAccoutLogDTO.setLogId(employeeAccountLog.getId());
        employeeAccoutLogDTO.setChangeMainType(employeeAccountLog.getChangeMainType());
        employeeAccoutLogDTO.setChangeDetailType(employeeAccountLog.getChangeDetailType());
        employeeAccoutLogDTO.setAbleBalanceChangeAfter(employeeAccountLog.getAbleBalanceAfter());
        employeeAccoutLogDTO.setAbleBalanceChange(employeeAccountLog.getAbleBalanceChange());
        employeeAccoutLogDTO.setChangeBalance(employeeAccountLog.getChangeBalance());
        employeeAccoutLogDTO.setCorrelationId(employeeAccountLog.getCorrelationId());
        employeeAccoutLogDTO.setCreateTime(employeeAccountLog.getCreateTime());
        employeeAccoutLogDTO.setTitle(employeeAccountLog.getRemark());

        return employeeAccoutLogDTO;
    }




}
