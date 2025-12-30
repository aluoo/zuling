package com.anyi.common.account.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.account.dto.EmployeeAccoutLogStatDTO;
import com.anyi.common.account.req.PartnerAccountLogReq;
import com.anyi.common.account.vo.PartnerAccountLogVO;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.exchange.enums.PartnerChanelEnum;
import com.anyi.common.util.date.DateUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.anyi.common.account.constant.UserFocusTypeEnum;
import com.anyi.common.account.mapper.EmployeeAccountLogMapper;
import com.anyi.common.account.domain.EmployeeAccountLog;
import com.anyi.common.account.req.QueryAccountLogReq;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 个人账户变动明细表 服务实现类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
@Service
public class EmployeeAccountLogServiceImpl extends ServiceImpl<EmployeeAccountLogMapper, EmployeeAccountLog> implements IEmployeeAccountLogService {

    @Autowired
    EmployeeService employeeService;

    @Override
    public Page<EmployeeAccountLog> listUserFocusAccountLog(Long employeeId, QueryAccountLogReq req) {

        UserFocusTypeEnum userFocusTypeEnum = null;
        if (StringUtils.isNotEmpty(req.getTypeCode())){
            userFocusTypeEnum =  UserFocusTypeEnum.getByCode(req.getTypeCode());
        }

        if (StringUtils.isEmpty(req.getMonth())){
            throw new BusinessException(BizError.PARAM_ERROR);
        }
        Date startDate = DateUtils.strToDate(req.getMonth(),"yyyy年MM月");
        if (startDate == null){
            throw new BusinessException(BizError.PARAM_ERROR);
        }

        Date stopDate = DateUtils.adjustMonth(startDate,1);


        PageHelper.startPage(req.getPage(), req.getPageSize());

        LambdaQueryChainWrapper<EmployeeAccountLog> lambdaQuery = this.lambdaQuery();

        lambdaQuery.eq(EmployeeAccountLog::getEmployeeId,employeeId)
                .ge(EmployeeAccountLog::getCreateTime,LocalDateTimeUtil.of(startDate))
                .lt(EmployeeAccountLog::getCreateTime,LocalDateTimeUtil.of(stopDate))
                .eq(EmployeeAccountLog::getUserFocus,1)
        ;

        if (userFocusTypeEnum!=null){
            lambdaQuery.ge(EmployeeAccountLog::getChangeDetailType,userFocusTypeEnum.getChangeDetailTypeStart())
                    .le(EmployeeAccountLog::getChangeDetailType,userFocusTypeEnum.getChangeDetailTypeEnd());
        }
        lambdaQuery.orderByDesc(EmployeeAccountLog::getId);

        List<EmployeeAccountLog> childList = lambdaQuery.list();
        PageHelper.clearPage();

        Page page = (Page) childList;

        return page;
    }

    @Override
    public EmployeeAccoutLogStatDTO AccountLogSum(Long employeeId, QueryAccountLogReq req) {
        EmployeeAccoutLogStatDTO resultDTO = new EmployeeAccoutLogStatDTO();
        resultDTO.setIncome(0L);
        resultDTO.setExpend(0L);
        UserFocusTypeEnum userFocusTypeEnum = null;
        if (StringUtils.isNotEmpty(req.getTypeCode())) {
            userFocusTypeEnum = UserFocusTypeEnum.getByCode(req.getTypeCode());
        }

        if (StringUtils.isEmpty(req.getMonth())) {
            throw new BusinessException(BizError.PARAM_ERROR);
        }
        Date startDate = DateUtils.strToDate(req.getMonth(), "yyyy年MM月");
        if (startDate == null) {
            throw new BusinessException(BizError.PARAM_ERROR);
        }

        Date stopDate = DateUtils.adjustMonth(startDate, 1);

        LambdaQueryChainWrapper<EmployeeAccountLog> lambdaQuery = this.lambdaQuery();

        lambdaQuery.eq(EmployeeAccountLog::getEmployeeId, employeeId)
                .ge(EmployeeAccountLog::getCreateTime, LocalDateTimeUtil.of(startDate))
                .lt(EmployeeAccountLog::getCreateTime, LocalDateTimeUtil.of(stopDate))
                .eq(EmployeeAccountLog::getUserFocus, 1)
        ;

        if (userFocusTypeEnum != null) {
            lambdaQuery.ge(EmployeeAccountLog::getChangeDetailType, userFocusTypeEnum.getChangeDetailTypeStart())
                    .le(EmployeeAccountLog::getChangeDetailType, userFocusTypeEnum.getChangeDetailTypeEnd());
        }
        lambdaQuery.orderByDesc(EmployeeAccountLog::getId);

        List<EmployeeAccountLog> childList = lambdaQuery.list();
        if (CollUtil.isEmpty(childList)) {
            return resultDTO;
        }
        resultDTO.setIncome(childList.stream().filter(e -> e.getChangeMainType().intValue() == 2).mapToLong(EmployeeAccountLog::getChangeBalance).sum());
        resultDTO.setExpend(childList.stream().filter(e -> e.getChangeMainType().intValue() == 1).mapToLong(EmployeeAccountLog::getChangeBalance).sum());
        return resultDTO;
    }

    @Override
    public PageInfo<PartnerAccountLogVO> partnerAccountLog(PartnerAccountLogReq req) {

        if(StrUtil.isBlank(req.getChannelCode())){
            throw new BusinessException(99999,"渠道码不能为空");
        }

        if(!EnumUtil.contains(PartnerChanelEnum.class,req.getChannelCode())){
            throw new BusinessException(99999,"非合作渠道");
        }

        //精确看某个员工
        if(StrUtil.isNotBlank(req.getMobile())){
            Employee employee = employeeService.getByMobileStatus(req.getMobile());
            req.setEmployeeId(ObjectUtil.isNotNull(employee)?employee.getId():1L);
        }

        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<EmployeeAccountLog> list = this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(req.getEmployeeId()),EmployeeAccountLog::getEmployeeId,req.getEmployeeId())
                .in(EmployeeAccountLog::getEmployeeId,req.getEmployeeIds())
                .eq(EmployeeAccountLog::getChangeMainType,2)
                .ge(req.getBeginTime() != null, EmployeeAccountLog::getCreateTime, req.getBeginTime())
                .le(req.getEndTime() != null, EmployeeAccountLog::getCreateTime, req.getEndTime())
                .orderByDesc(EmployeeAccountLog::getCreateTime)
                .list();

        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }

        List<PartnerAccountLogVO> vos = BeanUtil.copyToList(list, PartnerAccountLogVO.class);
        List<Long> employeeIds = vos.stream().map(PartnerAccountLogVO::getEmployeeId).collect(Collectors.toList());
        Map<Long, Employee> employeeInfoMap = employeeService.getEmployeeInfoMap(employeeIds);

        vos.forEach(vo -> {
            vo.setEmployeeName(Optional.ofNullable(employeeInfoMap.get(vo.getEmployeeId())).map(Employee::getName).orElse(null));
            vo.setEmployeePhone(Optional.ofNullable(employeeInfoMap.get(vo.getEmployeeId())).map(Employee::getMobileNumber).orElse(null));
        });

        PageInfo<PartnerAccountLogVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

}
