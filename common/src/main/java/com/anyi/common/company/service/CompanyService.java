package com.anyi.common.company.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.dto.CompanyDTO;
import com.anyi.common.company.enums.CompanyStatus;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.common.company.mapper.CompanyMapper;
import com.anyi.common.company.mapper.ExtCompanyMapper;
import com.anyi.common.company.req.CompanyReq;
import com.anyi.common.company.vo.AgencyCompanyVO;
import com.anyi.common.company.vo.RecycleCompanyVO;
import com.anyi.common.constant.Constants;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.employee.service.EmployeeService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class CompanyService extends ServiceImpl<CompanyMapper, Company> {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    ExtCompanyMapper extCompanyMapper;

    public Collection<Long> getSingleStoreIdsByChainStoreId(Long chainStoreId) {
        Collection<Long> storeIds = new ArrayList<>();
        if (chainStoreId == null) {
            return storeIds;
        }
        List<Company> list = this.lambdaQuery()
                .select(Company::getId)
                .eq(Company::getPId, chainStoreId)
                .eq(Company::getType, CompanyType.STORE.getCode())
                .list();

        if (CollUtil.isNotEmpty(list)) {
            storeIds = list.stream().map(Company::getId).collect(Collectors.toSet());
        }
        return storeIds;
    }

    public Employee getCompanyManager(Long companyId) {
        Long managerId = this.getCompanyManagerId(companyId);
        return employeeService.lambdaQuery().eq(Employee::getId, managerId).one();
    }

    public Long getCompanyManagerId(Long companyId) {
        if (companyId == null) {
            return null;
        }
        return Optional.ofNullable(this.lambdaQuery()
                        .select(Company::getId, Company::getEmployeeId)
                        .eq(Company::getId, companyId)
                        .one()
                )
                .map(Company::getEmployeeId)
                .orElse(null);
    }

    public List<Long> listAllRecyclersIds() {
        List<Company> list = listAllRecyclers();
        return CollUtil.isEmpty(list)
                ? new ArrayList<>()
                : list.stream().map(Company::getId).collect(Collectors.toList());
    }

    public List<Company> listAllRecyclers() {
        return this.lambdaQuery()
                .eq(Company::getType, CompanyType.RECYCLE.getCode())
                .eq(Company::getStatus, CompanyStatus.NORMAL.getCode())
                .list();
    }

    public Map<Long, Company> getCompanyInfoMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<Company> list = this.lambdaQuery().in(Company::getId, ids).list();
        return CollUtil.isEmpty(list)
                ? Collections.emptyMap()
                : list.stream().collect(Collectors.toMap(Company::getId, Function.identity()));
    }

    public void joinCompany(CompanyDTO companyDTO) {
        Employee applyEmployee = employeeService.lambdaQuery().eq(Employee::getId,companyDTO.getAplId())
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode()).one();
        if (applyEmployee == null) {
            throw new BusinessException(BizError.RVT_EMP_NOT_EXIST);
        }

        Employee em = employeeService.getByMobileStatus(companyDTO.getContactMobile());
        if (em != null) {
            throw new BusinessException(BizError.EM_CANT_CANNEL);
        }

        Company company = getByMobile(companyDTO.getContactMobile());
        if (company != null) {
            throw new BusinessException(BizError.EM_CANT_CANNEL);
        }

        Company emCompany = this.getById(applyEmployee.getCompanyId());
        if (ObjectUtil.isNull(emCompany)) {
            throw new BusinessException(BizError.COMPANY_NOT_EXIST);
        }

        company = new Company();
        BeanUtil.copyProperties(companyDTO, company);
        company.setPId(applyEmployee.getCompanyId());
        company.setCode(generateCompanyCode(applyEmployee.getCompanyId(), emCompany.getCode()));
        company.setPDeptId(applyEmployee.getDeptId());
        company.setStatus(CompanyStatus.TO_AUDIT.getCode());
        this.save(company);
    }


    public Company getByMobile(String mobile) {
        List<Company> companyList = this.list(Wrappers.lambdaQuery(Company.class)
                .eq(Company::getContactMobile, mobile)
                .in(Company::getStatus, Arrays.asList(CompanyStatus.TO_AUDIT.getCode(), CompanyStatus.NORMAL.getCode(), CompanyStatus.FREEZE.getCode())));
        if (CollUtil.isNotEmpty(companyList)) {
            return companyList.get(0);
        }
        return null;
    }

    private String generateCompanyCode(Long pId, String pCode) {
        Integer maxCode = extCompanyMapper.selectMaxCode(pId);
        if (maxCode == null) {
            maxCode = 0;
        }
        return pCode + "-" + (++maxCode);
    }

    /**
     * 某一部门的下级门店
     *
     * @param req
     * @return
     */
    public List<AgencyCompanyVO> ancestorsCompany(CompanyReq req) {
        List<AgencyCompanyVO> resultVo = new ArrayList<>();
        //部门底下门店管理员的员工ID
        List<Long> empIds = employeeService.getAncestorsEmployee(req.getAncestors());
        if (CollUtil.isEmpty(empIds)) {
            return resultVo;
        }
        req.setEmpIds(empIds);
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<AgencyCompanyVO> resultList = this.baseMapper.getByEmployee(req);
        if(CollUtil.isEmpty(resultList)) return resultVo;

        for (AgencyCompanyVO vo : resultList) {
            vo.setAddress(vo.getProvince() + vo.getCity() + vo.getRegion() + vo.getAddress());
            vo.setStatusName(CompanyStatus.getNameByCode(vo.getStatus().byteValue()));
            vo.setScore(5);
            vo.setChainFlag(CompanyType.CHAIN.getCode() == vo.getType());
            resultVo.add(vo);
        }
        return resultVo;
    }

    /**
     * 门店详情
     *
     * @param companyId
     * @return
     */
    public AgencyCompanyVO companyDetail(Long companyId) {
        AgencyCompanyVO resultVo = new AgencyCompanyVO();
        Company company = this.getById(companyId);
        BeanUtil.copyProperties(company, resultVo);
        return resultVo;
    }

    public void updateCompany(CompanyDTO companyDTO) {
        Company company = this.getById(companyDTO.getId());
        BeanUtil.copyProperties(companyDTO, company, CopyOptions.create().ignoreNullValue());
        this.updateById(company);
    }

    public List<RecycleCompanyVO> recycleList(CompanyReq req) {
        List<RecycleCompanyVO> resultVo = new ArrayList<>();

        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<RecycleCompanyVO> resultList = this.baseMapper.recycleList(req);
        if(CollUtil.isEmpty(resultList)) return resultVo;

        for (RecycleCompanyVO vo : resultList) {
            vo.setAddress(vo.getProvince() + vo.getCity() + vo.getRegion() + vo.getAddress());
            vo.setStatusName(CompanyStatus.getNameByCode(vo.getStatus().byteValue()));
            vo.setScore(95);
            resultVo.add(vo);
        }
        return resultVo;
    }

    /**
     * 直营连锁或者门店
     *
     * @param
     * @return
     */
    public List<AgencyCompanyVO> dictCompany(CompanyReq req) {
        List<AgencyCompanyVO> resultVo = new ArrayList<>();
        //直营连锁
        List<Company> chainList = this.list(Wrappers.lambdaQuery(Company.class).eq(Company::getAplId,req.getAplId())
                .eq(Company::getType,CompanyType.CHAIN.getCode()));
        if(CollUtil.isNotEmpty(chainList)){
            req.setParentIds(chainList.stream().map(Company::getId).collect(Collectors.toList()));
        }
        req.setAplId(req.getAplId());
        req.setPage(1);
        req.setPageSize(10000);
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<AgencyCompanyVO> resultList = this.baseMapper.getByEmployee(req);
        if(CollUtil.isEmpty(resultList)) return resultVo;

        for (AgencyCompanyVO vo : resultList) {
            vo.setAddress(vo.getProvince() + vo.getCity() + vo.getRegion() + vo.getAddress());
            vo.setStatusName(CompanyStatus.getNameByCode(vo.getStatus().byteValue()));
            vo.setScore(5);
            vo.setChainFlag(CompanyType.CHAIN.getCode() == vo.getType());
            resultVo.add(vo);
        }
        return resultVo;
    }

    /**
     * 是否是连锁店的负责人，或者不是连锁的单店负责人
     */
    public boolean isDirectManager(Long companyId, Long employeeId) {
        Long managerId = Optional.ofNullable(this.lambdaQuery()
                        .select(Company::getId, Company::getEmployeeId)
                        .eq(Company::getId, companyId)
                        .eq(Company::getPId, Constants.LAN_HAI_CMP_ID)
                        .one()
                )
                .map(Company::getEmployeeId)
                .orElse(null);
        return managerId != null && managerId.equals(employeeId);
    }
}