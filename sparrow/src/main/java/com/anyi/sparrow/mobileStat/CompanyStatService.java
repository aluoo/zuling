package com.anyi.sparrow.mobileStat;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.enums.CompanyStatus;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.common.company.req.CompanyReq;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.company.vo.AgencyCompanyVO;
import com.anyi.common.constant.RedisLockKeyConstants;
import com.anyi.common.dept.service.DeptService;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.employee.mapper.EmployeeMapper;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.employee.vo.AgencyCalVO;
import com.anyi.common.employee.vo.AgencyVO;
import com.anyi.common.enums.PayEnterEnum;
import com.anyi.common.mobileStat.dto.CompanyStatDTO;
import com.anyi.common.mobileStat.response.CompanyStatVO;
import com.anyi.common.mobileStat.service.CompanyDataDailyBaseService;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.OrderCustomerReceivePayment;
import com.anyi.common.product.domain.OrderCustomerRefundPayment;
import com.anyi.common.product.domain.enums.OrderOperationEnum;
import com.anyi.common.product.domain.enums.OrderStatusEnum;
import com.anyi.common.product.domain.enums.PaymentTypeEnum;
import com.anyi.common.product.domain.enums.RefundPaymentStatusEnum;
import com.anyi.common.product.domain.request.RefundPaymentApplyReq;
import com.anyi.common.product.domain.response.ReceivePaymentInfoVO;
import com.anyi.common.product.domain.response.RefundPaymentInfoVO;
import com.anyi.common.product.service.OrderCustomerRefundPaymentService;
import com.anyi.common.product.service.OrderLogService;
import com.anyi.common.result.WxPayVO;
import com.anyi.common.service.AbstractRefundPaymentManage;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.common.service.PayApplyRecordService;
import com.anyi.common.service.ProductWxPayService;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.user.domain.UserAccount;
import com.anyi.common.util.DistributionLockUtil;
import com.anyi.common.wx.MchIdService;
import com.anyi.sparrow.applet.user.service.UserAccountProcessService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.utils.WxUtils;
import com.anyi.sparrow.mobileStat.req.CompanyStatReq;
import com.anyi.sparrow.mobileStat.response.CompanyDirectListVO;
import com.anyi.sparrow.mobileStat.response.CompanyStatAgentVO;
import com.anyi.sparrow.mobileStat.response.DirectCompanyStatVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsQueryRequest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsRequest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsResult;
import com.github.binarywang.wxpay.bean.ecommerce.TransactionsResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/14
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class CompanyStatService  {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    DeptService deptService;
    @Autowired
    CompanyDataDailyBaseService companyDataDailyBaseService;
    @Autowired
    CompanyService companyService;

    public CompanyStatAgentVO agencyFirst(CompanyStatReq req) {

        Employee current = employeeService.getOne(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getAncestors,req.getAncestors()).eq(Employee::getLevel,req.getLevel()));

        //获取晒单总数
        CompanyStatDTO exchangeDto = new CompanyStatDTO();
        exchangeDto.setAncestors(req.getAncestors());
        exchangeDto.setStartTime(req.getStartTime());
        exchangeDto.setEndTime(req.getEndTime());
        CompanyStatVO exchangeAllVo = companyDataDailyBaseService.companyStatToday(exchangeDto);


        //当前登陆人下级的代理
        List<Employee> employeeList = employeeService.list(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getLevel, req.getLevel() + 1)
                .eq(Employee::getCompanyType, CompanyType.COMPANY.getCode())
                .likeRight(Employee::getAncestors, req.getAncestors()));

        List<CompanyStatAgentVO.AgencyStatVO> childVOList = new ArrayList<>();

        if(CollUtil.isNotEmpty(employeeList)){
            for (Employee employee : employeeList) {
                exchangeDto = new CompanyStatDTO();
                exchangeDto.setAncestors(employee.getAncestors());
                exchangeDto.setStartTime(req.getStartTime());
                exchangeDto.setEndTime(req.getEndTime());
                CompanyStatVO exchangeVo = companyDataDailyBaseService.companyStatToday(exchangeDto);
                CompanyStatAgentVO.AgencyStatVO childVO = CompanyStatAgentVO.AgencyStatVO.builder()
                        .deptName(deptService.getById(employee.getDeptId()).getName())
                        .employeeId(employee.getId())
                        .employeeName(employee.getName())
                        .phone(employee.getMobileNumber())
                        .ancestors(employee.getAncestors())
                        .exchangeAllNum(exchangeVo.getExchangeAllNum())
                        .exchangePassNum(exchangeVo.getExchangePassNum())
                        .huanjiNum(exchangeVo.getHuanjiNum())
                        .huanjiPassNum(exchangeVo.getHuanjiPassNum())
                        .lvzhouNum(exchangeVo.getLvzhouNum())
                        .lvzhouPassNum(exchangeVo.getLvzhouPassNum())
                        .appleNum(exchangeVo.getAppleNum())
                        .applePassNum(exchangeVo.getApplePassNum())
                        .nextAgency(nextAgency(employee.getAncestors(), employee.getLevel()))
                        .nextCompany(nextCompany(employee.getAncestors(), employee.getLevel()))
                        .level(employee.getLevel()).build();

                childVOList.add(childVO);
            }
        }


        childVOList = childVOList.stream().filter(e -> e.getExchangePassNum()>0).collect(Collectors.toList());
        CompanyStatAgentVO vo = CompanyStatAgentVO.builder().exchangeNum(exchangeAllVo.getExchangeAllNum()).agencyList(childVOList).build();

        //直营门店数目
        CompanyReq companyReq = new CompanyReq();
        companyReq.setAplId(current.getId());
        List<AgencyCompanyVO> companyList = companyService.dictCompany(companyReq);
        //直营门店晒单统计
        List<CompanyStatVO> directList = directCompanyStat(companyList,req);
        DirectCompanyStatVO directStatVO =new DirectCompanyStatVO();
        directStatVO.setCompanyNum(CollUtil.isNotEmpty(companyList)?companyList.size():0);
        directStatVO.setExchangeAllNum(directList.stream().mapToInt(CompanyStatVO::getExchangeAllNum).sum());
        directStatVO.setHuanjiNum(directList.stream().mapToInt(CompanyStatVO::getHuanjiNum).sum());
        directStatVO.setHuanjiPassNum(directList.stream().mapToInt(CompanyStatVO::getHuanjiPassNum).sum());
        directStatVO.setLvzhouNum(directList.stream().mapToInt(CompanyStatVO::getLvzhouNum).sum());
        directStatVO.setLvzhouPassNum(directList.stream().mapToInt(CompanyStatVO::getLvzhouPassNum).sum());
        directStatVO.setAppleNum(directList.stream().mapToInt(CompanyStatVO::getAppleNum).sum());
        directStatVO.setApplePassNum(directList.stream().mapToInt(CompanyStatVO::getApplePassNum).sum());
        directStatVO.setExchangePassNum(directList.stream().mapToInt(CompanyStatVO::getExchangePassNum).sum());
        directStatVO.setEmployeeId(current.getId());
        vo.setDirectStatVO(directStatVO);
        return vo;
    }

    private List<CompanyStatVO> directCompanyStat(List<AgencyCompanyVO> companyList,CompanyStatReq req){

        List<CompanyStatVO> statVOList = new ArrayList<>();

        if(CollUtil.isEmpty(companyList)){
            CompanyStatVO statVO = new CompanyStatVO();
            statVO.setExchangeAllNum(0);
            statVO.setHuanjiNum(0);
            statVO.setHuanjiPassNum(0);
            statVO.setLvzhouNum(0);
            statVO.setLvzhouPassNum(0);
            statVO.setAppleNum(0);
            statVO.setApplePassNum(0);
            statVOList.add(statVO);
            return statVOList;
        }


        for(AgencyCompanyVO companyVO:companyList){
            CompanyStatDTO exchangeDto = new CompanyStatDTO();
            exchangeDto.setAncestors(companyVO.getAncestors());
            exchangeDto.setCompanyId(companyVO.getId());
            exchangeDto.setStartTime(req.getStartTime());
            exchangeDto.setEndTime(req.getEndTime());
            CompanyStatVO exchangeVo = companyDataDailyBaseService.companyStatToday(exchangeDto);
            statVOList.add(exchangeVo);
        }

       return statVOList;
    }


    /**
     * 下一步是否是代理
     */
    private Boolean nextAgency(String ancestors, int currentLevel) {
        List<Employee> employeeList = employeeService.list(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getLevel, currentLevel + 1)
                .likeRight(Employee::getAncestors, ancestors)
                .eq(Employee::getCompanyType, CompanyType.COMPANY.getCode())
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode()));
        return !CollUtil.isEmpty(employeeList);
    }

    /**
     * 下一步门店
     *
     */
    private Boolean nextCompany(String ancestors, int currentLevel) {
        List<Employee> employeeList = employeeService.list(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getLevel, currentLevel + 1)
                .likeRight(Employee::getAncestors, ancestors)
                .in(Employee::getCompanyType, Arrays.asList(CompanyType.STORE.getCode(), CompanyType.CHAIN.getCode()))
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode()));
        if (CollUtil.isEmpty(employeeList) || nextAgency(ancestors, currentLevel)) {
            return false;
        }else{
            return true;
        }
    }

    public CompanyDirectListVO dictCompany(CompanyStatReq req) {

        CompanyDirectListVO directListVO = new CompanyDirectListVO();
        List<DirectCompanyStatVO> companyStatVOList = new ArrayList<>();

        //直营门店数目
        CompanyReq companyReq = new CompanyReq();
        companyReq.setAplId(req.getAplId());
        List<AgencyCompanyVO> companyList = companyService.dictCompany(companyReq);

        if(CollUtil.isEmpty(companyList)) return directListVO;

        for(AgencyCompanyVO companyVO : companyList){
            DirectCompanyStatVO vo = new DirectCompanyStatVO();
            //直营门店晒单统计
            List<CompanyStatVO> directList = directCompanyStat(Arrays.asList(companyVO),req);
            BeanUtil.copyProperties(directList.get(0),vo);
            vo.setCompanyId(companyVO.getId());
            vo.setCompanyName(companyVO.getName());
            vo.setContact(companyVO.getContact());
            vo.setContactMobile(companyVO.getContactMobile());
            vo.setEmployeeId(companyVO.getEmployeeId());
            companyStatVOList.add(vo);
        }
        directListVO.setCompanyStatVOList(companyStatVOList.stream().filter(e -> e.getExchangePassNum()>0).collect(Collectors.toList()));
        return directListVO;

    }

    public CompanyDirectListVO groupCompany(CompanyStatReq req) {

        CompanyDirectListVO groupVo = new CompanyDirectListVO();
        List<DirectCompanyStatVO> companyStatVOList = new ArrayList<>();

        //团队门店数
        CompanyReq companyReq = new CompanyReq();
        companyReq.setAncestors(req.getAncestors());
        companyReq.setPage(1);
        companyReq.setPageSize(10000);
        List<AgencyCompanyVO> companyList = companyService.ancestorsCompany(companyReq);

        if(CollUtil.isEmpty(companyList)) return groupVo;

        for(AgencyCompanyVO companyVO : companyList){
            DirectCompanyStatVO vo = new DirectCompanyStatVO();
            //直营门店晒单统计
            List<CompanyStatVO> directList = directCompanyStat(Arrays.asList(companyVO),req);
            BeanUtil.copyProperties(directList.get(0),vo);
            vo.setCompanyId(companyVO.getId());
            vo.setCompanyName(companyVO.getName());
            vo.setContact(companyVO.getContact());
            vo.setContactMobile(companyVO.getContactMobile());
            vo.setEmployeeId(companyVO.getEmployeeId());
            companyStatVOList.add(vo);
        }
        groupVo.setCompanyStatVOList(companyStatVOList.stream().filter(e -> e.getExchangePassNum()>0).collect(Collectors.toList()));
        return groupVo;

    }


    public CompanyStatAgentVO companyFirst(CompanyStatReq req) {

        Employee current = employeeService.getOne(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getAncestors,req.getAncestors()).eq(Employee::getLevel,req.getLevel()));

        CompanyStatDTO exchangeDto = new CompanyStatDTO();
        exchangeDto.setAncestors(req.getAncestors());
        exchangeDto.setStartTime(req.getStartTime());
        exchangeDto.setEndTime(req.getEndTime());
        CompanyStatVO exchangeAllVo = companyDataDailyBaseService.companyStatToday(exchangeDto);

        //当前登陆人直接下级
        List<Employee> employeeList = employeeService.list(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getLevel, req.getLevel() + 1)
                .likeRight(Employee::getAncestors, req.getAncestors()));
        if (CollUtil.isEmpty(employeeList)) {
            return CompanyStatAgentVO.builder().exchangeNum(exchangeAllVo.getExchangeAllNum()).build();
        }

        List<CompanyStatAgentVO.AgencyStatVO> childVOList = new ArrayList<>();
        for (Employee employee : employeeList) {
            exchangeDto = new CompanyStatDTO();
            exchangeDto.setAncestors(employee.getAncestors());
            exchangeDto.setStartTime(req.getStartTime());
            exchangeDto.setEndTime(req.getEndTime());
            CompanyStatVO exchangeVo = companyDataDailyBaseService.companyStatToday(exchangeDto);
            CompanyStatAgentVO.AgencyStatVO childVO = CompanyStatAgentVO.AgencyStatVO.builder()
                    .deptName(deptService.getById(employee.getDeptId()).getName())
                    .employeeId(employee.getId())
                    .employeeName(employee.getName())
                    .companyId(employee.getCompanyId())
                    .phone(employee.getMobileNumber())
                    .ancestors(employee.getAncestors())
                    .exchangeAllNum(exchangeVo.getExchangeAllNum())
                    .huanjiNum(exchangeVo.getHuanjiNum())
                    .huanjiPassNum(exchangeVo.getHuanjiPassNum())
                    .lvzhouNum(exchangeVo.getLvzhouNum())
                    .lvzhouPassNum(exchangeVo.getLvzhouPassNum())
                    .appleNum(exchangeVo.getAppleNum())
                    .applePassNum(exchangeVo.getApplePassNum())
                    .exchangePassNum(exchangeVo.getExchangePassNum())
                    .nextAgency(!employee.getIsLeaf())
                    .level(employee.getLevel()).build();

            childVOList.add(childVO);
        }

        childVOList = childVOList.stream().filter(e -> e.getExchangePassNum()>0).collect(Collectors.toList());
        CompanyStatAgentVO vo = CompanyStatAgentVO.builder().exchangeNum(exchangeAllVo.getExchangeAllNum()).agencyList(childVOList).build();
        return vo;
    }

}