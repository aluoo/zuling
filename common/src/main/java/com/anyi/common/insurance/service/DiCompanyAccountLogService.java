package com.anyi.common.insurance.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.anyi.common.account.constant.UserFocusTypeEnum;
import com.anyi.common.account.domain.EmployeeAccountLog;
import com.anyi.common.account.dto.EmployeeAccoutLogStatDTO;
import com.anyi.common.account.req.QueryAccountLogReq;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.insurance.constant.CompanyUserFocusTypeEnum;
import com.anyi.common.insurance.domain.DiCompanyAccountLog;
import com.anyi.common.insurance.mapper.DiCompanyAccountLogMapper;
import com.anyi.common.util.date.DateUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数保门店账户变动明细表 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Service
public class DiCompanyAccountLogService extends ServiceImpl<DiCompanyAccountLogMapper, DiCompanyAccountLog>  {

    public Page<DiCompanyAccountLog> listUserFocusAccountLog(Long companyId, QueryAccountLogReq req) {

        CompanyUserFocusTypeEnum userFocusTypeEnum = null;
        if (StringUtils.isNotEmpty(req.getTypeCode())){
            userFocusTypeEnum =  CompanyUserFocusTypeEnum.getByCode(req.getTypeCode());
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

        LambdaQueryChainWrapper<DiCompanyAccountLog> lambdaQuery = this.lambdaQuery();

        lambdaQuery.eq(DiCompanyAccountLog::getCompanyId,companyId)
                .ge(DiCompanyAccountLog::getCreateTime, LocalDateTimeUtil.of(startDate))
                .lt(DiCompanyAccountLog::getCreateTime,LocalDateTimeUtil.of(stopDate))
                .eq(DiCompanyAccountLog::getUserFocus,1)
        ;

        if (userFocusTypeEnum!=null){
            lambdaQuery.ge(DiCompanyAccountLog::getChangeDetailType,userFocusTypeEnum.getChangeDetailTypeStart())
                    .le(DiCompanyAccountLog::getChangeDetailType,userFocusTypeEnum.getChangeDetailTypeEnd());
        }
        lambdaQuery.orderByDesc(DiCompanyAccountLog::getId);

        List<DiCompanyAccountLog> childList = lambdaQuery.list();
        PageHelper.clearPage();

        Page page = (Page) childList;

        return page;
    }

    public EmployeeAccoutLogStatDTO AccountLogSum(Long companyId, QueryAccountLogReq req) {
        EmployeeAccoutLogStatDTO resultDTO = new EmployeeAccoutLogStatDTO();
        resultDTO.setIncome(0L);
        resultDTO.setExpend(0L);
        CompanyUserFocusTypeEnum userFocusTypeEnum = null;
        if (StringUtils.isNotEmpty(req.getTypeCode())) {
            userFocusTypeEnum = CompanyUserFocusTypeEnum.getByCode(req.getTypeCode());
        }

        if (StringUtils.isEmpty(req.getMonth())) {
            throw new BusinessException(BizError.PARAM_ERROR);
        }
        Date startDate = DateUtils.strToDate(req.getMonth(), "yyyy年MM月");
        if (startDate == null) {
            throw new BusinessException(BizError.PARAM_ERROR);
        }

        Date stopDate = DateUtils.adjustMonth(startDate, 1);

        LambdaQueryChainWrapper<DiCompanyAccountLog> lambdaQuery = this.lambdaQuery();

        lambdaQuery.eq(DiCompanyAccountLog::getCompanyId, companyId)
                .ge(DiCompanyAccountLog::getCreateTime, LocalDateTimeUtil.of(startDate))
                .lt(DiCompanyAccountLog::getCreateTime, LocalDateTimeUtil.of(stopDate))
                .eq(DiCompanyAccountLog::getUserFocus, 1)
        ;

        if (userFocusTypeEnum != null) {
            lambdaQuery.ge(DiCompanyAccountLog::getChangeDetailType, userFocusTypeEnum.getChangeDetailTypeStart())
                    .le(DiCompanyAccountLog::getChangeDetailType, userFocusTypeEnum.getChangeDetailTypeEnd());
        }
        lambdaQuery.orderByDesc(DiCompanyAccountLog::getId);

        List<DiCompanyAccountLog> childList = lambdaQuery.list();
        if (CollUtil.isEmpty(childList)) {
            return resultDTO;
        }
        resultDTO.setIncome(childList.stream().filter(e -> e.getChangeMainType().intValue() == 2).mapToLong(DiCompanyAccountLog::getChangeBalance).sum());
        resultDTO.setExpend(childList.stream().filter(e -> e.getChangeMainType().intValue() == 1).mapToLong(DiCompanyAccountLog::getChangeBalance).sum());
        return resultDTO;
    }

}
