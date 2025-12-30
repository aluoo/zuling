package com.anyi.sparrow.assist.projects.dao;

import com.anyi.sparrow.assist.projects.dao.mapper.SysProjectsMapper;
import com.anyi.sparrow.assist.projects.domain.SysProjects;
import com.anyi.sparrow.assist.projects.domain.SysProjectsExample;
import com.anyi.sparrow.assist.projects.enums.ProjectStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public class SysProjectsDao {


    @Autowired
    private SysProjectsMapper sysProjectsMapper;


    //查询比buildCode更高且已经启用的版本
    public SysProjects getCurPrg(Integer projectCode, Integer deviceType) {

        SysProjectsExample sysProjectsExample = new SysProjectsExample();
        sysProjectsExample.createCriteria().
                andStatusEqualTo(ProjectStatusEnum.NOW.getCode()).
                andBuildCodeGreaterThan(0).
                andProjectCodeEqualTo(projectCode).
                andDeviceEqualTo(deviceType);
        List<SysProjects> lsSysProjects = sysProjectsMapper.selectByExample(sysProjectsExample);
        if ((null != lsSysProjects) && lsSysProjects.size() > 0)
            return lsSysProjects.get(0);
        return null;
    }


    public SysProjects getPrgsForceUpdateByBuildCode(Integer projectCode, Integer buildCodeMin, Integer buildCodeMax) {

        SysProjectsExample sysProjectsExample = new SysProjectsExample();
        sysProjectsExample.createCriteria().
                andBuildCodeGreaterThan(0).
                andForcedUpdatingEqualTo(1).//强制更新
                andProjectCodeEqualTo(projectCode).
                andBuildCodeGreaterThan(buildCodeMin).
                andBuildCodeLessThanOrEqualTo(buildCodeMax);
        List<SysProjects> lsSysProjects = sysProjectsMapper.selectByExample(sysProjectsExample);
        if ((null != lsSysProjects) && lsSysProjects.size() > 0)
            return lsSysProjects.get(0);
        return null;
    }



}
