package com.anyi.sparrow.organize.employee.service;

import cn.hutool.core.collection.CollectionUtil;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.company.mapper.CompanyMapper;
import com.anyi.sparrow.assist.system.service.SysDictService;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.Constants;
import com.anyi.sparrow.organize.employee.dao.CompanyDao;
import com.anyi.common.company.domain.Company;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.company.enums.CompanyStatus;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.sparrow.organize.employee.vo.CreateChannelReq;
import com.anyi.common.snowWork.SnowflakeIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CompanyProcessService {
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    public Company getByName(String name) {
        return companyDao.getByName(name);
    }

    public Company createCompany(LoginUser user, CreateChannelReq req, String pCode) {
        Company company = buildCompany(user, req, pCode);
        companyMapper.insert(company);
        return company;
    }

    private Company buildCompany(LoginUser user, CreateChannelReq req, String pCode) {
        Company company = new Company();
        company.setCreateTime(new Date());
        company.setCreator(user.getName());
        company.setId(snowflakeIdService.nextId());
        company.setPId(user.getCompanyId());
        company.setName(req.getName());
        company.setContact(req.getManagerName());
        company.setContactMobile(req.getMobile());
        company.setStatus(CompanyStatus.TO_AUDIT.getCode());
        company.setType(CompanyType.STORE.getCode());
        company.setUpdator(user.getName());
        company.setUpdateTime(new Date());
        company.setAplId(user.getId());
        company.setCode(generateCompanyCode(user.getCompanyId(), pCode));

        return company;
    }

    private String generateCompanyCode(Long pId, String pCode){
        int maxCode = companyDao.selectMaxCode(pId);
        return pCode + "-" + (++maxCode);
    }

    public Company getById(Long channelId) {
        return companyMapper.selectById(channelId);
    }

    public Employee getManager(Long id) {
        return companyDao.getManager(id);
    }

    public void updateCompany(Company company) {
        LoginUser loginUser = LoginUserContext.getUser();
        company.setUpdator(loginUser.getName());
        company.setUpdateTime(new Date());
        companyMapper.updateById(company);
    }

    public void deleteChannel(String code, String updator) {

        companyDao.deleteChannel(code, updator);
    }

    public void verifyLevel(String code) {
        String[] split = code.split("-");
        int configLevel = getConfigLevel();
        if (split.length >= configLevel + 1){
            throw new BusinessException(BizError.LEVEL_ERROR);
        }

    }

    private int getConfigLevel(){
        String level = sysDictService.getByName(Constants.Level_key);
        if (level == null){
            return  1;
        }
        return Integer.valueOf(level);
    }

    public void updatePdept(Long pDeptId, List<Long> cmpIds){
        companyDao.updatePdept(pDeptId, cmpIds);
    }

    public boolean checkContactMobileExist(String mobile) {
        List<Company> list =  companyDao.getListByContactMobile(mobile);
        return CollectionUtil.isNotEmpty(list);
    }

    /**
     * 查询自动结算的公司或渠道
     * @return
     */
    public List<Long> queryAutoSettleCompanyIds() {
        List<Long> list =  companyDao.queryAutoSettleCompanyIds();
        return list;
    }
}
