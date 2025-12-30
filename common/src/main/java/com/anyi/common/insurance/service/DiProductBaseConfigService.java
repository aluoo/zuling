package com.anyi.common.insurance.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.anyi.common.insurance.domain.DiProductBaseConfig;
import com.anyi.common.insurance.mapper.DiProductBaseConfigMapper;
import com.anyi.common.insurance.response.DiBaseConfigVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 手机-数保产品价格基础配置表 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Service
public class DiProductBaseConfigService extends ServiceImpl<DiProductBaseConfigMapper, DiProductBaseConfig>  {

    @Autowired
    private DiTypeService diTypeService;
    @Autowired
    private DiPackageService diPackageService;

    /**
     * 手机产品基础配置列表
     */
    public List<DiBaseConfigVO> baseList(Long productId){

        List<DiProductBaseConfig> resultList = this.list(
                Wrappers.lambdaQuery(DiProductBaseConfig.class).eq(DiProductBaseConfig::getProductId,productId));
        List<DiBaseConfigVO> resp = BeanUtil.copyToList(resultList, DiBaseConfigVO.class);
        if(CollUtil.isNotEmpty(resp)){
            resp.forEach(vo ->{
                vo.setTypeName(diTypeService.getById(vo.getTypeId()).getName());
                vo.setPackageName(diPackageService.getById(vo.getPackageId()).getName());
            });
        }
        return resp;
    }

}
