package com.anyi.miniapp.service.impl;

import com.anyi.common.company.dto.CompanyDTO;
import com.anyi.common.company.dto.EmployeeApplyDTO;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.miniapp.MiniApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = {MiniApplication.class})
@Slf4j
public class CompanyServiceTest {

    @Autowired
    CompanyService companyService;
    @Autowired
    EmployeeService employeeService;

    @Test
    public void test() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setProvince("福建省");
        companyDTO.setCity("厦门市");
        companyDTO.setRegion("思明区");
        companyDTO.setProvinceCode("350000");
        companyDTO.setCityCode("350200");
        companyDTO.setRegionCode("350202");
        companyDTO.setContact("包收一");
        companyDTO.setName("安逸回收商一");
        companyDTO.setContactMobile("15333333333");
        companyDTO.setAddress("福建省厦门市集美区软件园三期");
        companyDTO.setFrontUrl("https://aycx-vehicle.oss-cn-hangzhou.aliyuncs.com/1682403091529IMG_20230107_142531.jpg");
        companyDTO.setBusLicense("https://aycx-vehicle.oss-cn-hangzhou.aliyuncs.com/1682403091529IMG_20230107_142531.jpg");
        companyDTO.setUserId(1232132L);
        companyDTO.setAplId(1L);
        companyDTO.setType(4);
        companyService.joinCompany(companyDTO);
    }

    @Test
    public void test2() {
        EmployeeApplyDTO applyDTO = new EmployeeApplyDTO();
        applyDTO.setEmployeeId(1200022210855878656L);
        applyDTO.setMobileNumber("15260685799");
        applyDTO.setName("陈璐一二");
        employeeService.createEmployee(applyDTO);
    }

}