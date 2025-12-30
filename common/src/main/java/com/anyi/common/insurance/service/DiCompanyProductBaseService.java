package com.anyi.common.insurance.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.insurance.domain.DiCompanyProductBase;
import com.anyi.common.insurance.domain.DiInsurance;
import com.anyi.common.insurance.domain.DiProductBaseConfig;
import com.anyi.common.insurance.mapper.DiCompanyProductBaseMapper;
import com.anyi.common.insurance.req.DiInsuranceBaseDTO;
import com.anyi.common.insurance.response.DiBaseConfigVO;
import com.anyi.common.product.domain.Product;
import com.anyi.common.product.service.ProductService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 门店手机-数保产品价格基础配置表 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Service
public class DiCompanyProductBaseService extends ServiceImpl<DiCompanyProductBaseMapper, DiCompanyProductBase>  {

    @Autowired
    ProductService productService;
    @Autowired
    DiInsuranceService diInsuranceService;
    @Autowired
    DiProductBaseConfigService productBaseConfigService;
    @Autowired
    private DiTypeService diTypeService;
    @Autowired
    private DiPackageService diPackageService;

    /**
     * 手机产品基础配置列表
     */
    public List<DiBaseConfigVO> baseList(Long productId,Long companyId){
        List<DiCompanyProductBase> resultList = this.list(
                Wrappers.lambdaQuery(DiCompanyProductBase.class)
                        .eq(DiCompanyProductBase::getCompanyId,companyId)
                        .eq(DiCompanyProductBase::getProductId,productId));

        if(CollUtil.isEmpty(resultList)){
            return productBaseConfigService.baseList(productId);
        }

        List<DiBaseConfigVO> resp = BeanUtil.copyToList(resultList, DiBaseConfigVO.class);

        if(CollUtil.isNotEmpty(resp)){
            resp.forEach(vo ->{
                vo.setCompanyBaseId(vo.getId());
                vo.setTypeName(diTypeService.getById(vo.getTypeId()).getName());
                vo.setPackageName(diPackageService.getById(vo.getPackageId()).getName());
            });
        }
        return resp;
    }

    public void saveBase(DiInsuranceBaseDTO dto){
        if(CollUtil.isEmpty(dto.getBaseList())){
            throw new BusinessException(99999, "配置列表不能为空");
        }
        List<DiCompanyProductBase> baseConfigList = new ArrayList<>();
        for(DiInsuranceBaseDTO.InsuranceBaseDetailDTO detailDTO:dto.getBaseList()){

            DiCompanyProductBase config = new DiCompanyProductBase();
            BeanUtil.copyProperties(detailDTO,config, CopyOptions.create().ignoreNullValue());
            config.setCompanyId(dto.getCompanyId());
            //修改
            if(detailDTO.getCompanyBaseId()!=null){
                config.setId(detailDTO.getCompanyBaseId());
            }
            Product product = productService.getById(detailDTO.getProductId());
            DiInsurance diInsurance = diInsuranceService.getById(detailDTO.getInsuranceId());
            config.setCategoryId(product.getCategoryId());
            config.setBrandId(product.getBrandId());
            config.setTypeId(diInsurance.getTypeId());
            config.setPackageId(diInsurance.getPackageId());
            baseConfigList.add(config);
        }
        this.saveOrUpdateBatch(baseConfigList);
    }


}
