package com.anyi.common.insurance.service;

import com.anyi.common.insurance.domain.DiInsuranceOption;
import com.anyi.common.insurance.domain.DiOption;
import com.anyi.common.insurance.mapper.DiInsuranceOptionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 数保产品关联选项表 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Service
public class DiInsuranceOptionService extends ServiceImpl<DiInsuranceOptionMapper, DiInsuranceOption>  {

    @Autowired
    DiOptionService diOptionService;


    public List<Long> getOptionIdsByProductId(Long insuranceId) {
        if (insuranceId == null) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<DiInsuranceOption> qw = new LambdaQueryWrapper<DiInsuranceOption>().eq(DiInsuranceOption::getInsuranceId, insuranceId);
        return this.list(qw).stream().map(DiInsuranceOption::getOptionId).collect(Collectors.toList());
    }

    /**
     * 根据数保产品和编码获取配置
     * @param insuranceId
     * @param code
     * @return
     */
    public List<DiOption> getOptionByProductId(Long insuranceId,String code) {
        return this.baseMapper.getByInsuranceId(insuranceId,code);
    }

    /**
     * 根据数保产品，层级，编码获取配置
     * @param insuranceId
     * @param code
     * @return
     */
    public List<DiOption> getOptionByAncestors(Long insuranceId,String ancestors,String code) {
        return this.baseMapper.getOptionByAncestors(insuranceId,ancestors,code);
    }

    /**
     * 查找下级配置选项
     * @param pid
     * @return
     */
    public List<DiOption> getOptionIdsByPid(Long pid) {
       return  diOptionService.lambdaQuery().eq(DiOption::getParentId,pid).eq(DiOption::getDeleted,false).list();
    }

    /**
     * 获取理赔项目某项的资料示例图
     * @return
     */
    public List<DiOption> getCodeImages(String ancestors,String code) {
        return  diOptionService.lambdaQuery().eq(DiOption::getCode,code).eq(DiOption::getDeleted,false).likeRight(DiOption::getAncestors,ancestors).list();
    }

}
