package com.anyi.sparrow.assist.projects.service;

import com.anyi.sparrow.assist.projects.dao.SysProjectsDao;
import com.anyi.sparrow.assist.projects.domain.SysProjects;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anyi.sparrow.assist.projects.vo.SysProjectsRps;


@Service
public class ProjectsService {

    @Autowired
    private SysProjectsDao sysProjectsDao;


    public SysProjectsRps getCurPrg(Integer projectCode, Integer deviceType) {

        SysProjectsRps sysProjectsRps = null;
        SysProjects sysProjects = sysProjectsDao.getCurPrg(projectCode, deviceType);
        if (null != sysProjects) {
            sysProjectsRps = new SysProjectsRps();
            BeanUtils.copyProperties(sysProjects, sysProjectsRps);
        }
        return sysProjectsRps;
    }




    public SysProjectsRps  getPrgsForceUpdateByBuildCode(Integer projectCode, Integer buildCodeMin, Integer buildCodeMax) {

        SysProjectsRps sysProjectsRps = null;
        SysProjects sysProjects = sysProjectsDao.getPrgsForceUpdateByBuildCode(projectCode,buildCodeMin,buildCodeMax);
        if (null != sysProjects) {
            sysProjectsRps = new SysProjectsRps();
            BeanUtils.copyProperties(sysProjects, sysProjectsRps);
        }
        return sysProjectsRps;
    }


}
