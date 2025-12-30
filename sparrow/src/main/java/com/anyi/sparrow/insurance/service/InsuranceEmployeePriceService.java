package com.anyi.sparrow.insurance.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.commission.enums.CommissionPackage;
import com.anyi.common.commission.service.CommissionSettleService;
import com.anyi.common.insurance.domain.DiInsurance;
import com.anyi.common.insurance.domain.DiSkuInsurance;
import com.anyi.common.insurance.domain.dto.DiProductInsurancePriceDTO;
import com.anyi.common.insurance.mapper.DiProductInsurancePriceMapper;
import com.anyi.common.insurance.req.InsuranceEmployeePriceReq;
import com.anyi.common.insurance.response.DiSkuInsuranceVO;
import com.anyi.common.insurance.service.DiInsuranceService;
import com.anyi.common.insurance.service.DiSkuInsuranceService;
import com.anyi.common.product.domain.Product;
import com.anyi.common.product.domain.ProductSku;
import com.anyi.common.product.service.ProductService;
import com.anyi.common.product.service.ProductSkuService;
import com.anyi.common.util.MoneyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-06-05
 */
@Service
@Slf4j
public class InsuranceEmployeePriceService {

    @Autowired
    DiSkuInsuranceService skuInsuranceService;
    @Autowired
    ProductSkuService productSkuService;
    @Autowired
    ProductService ProductService;
    @Autowired
    DiInsuranceService insuranceService;
    @Autowired
    DiProductInsurancePriceMapper insurancePriceMapper;
    @Autowired
    CommissionSettleService commissionSettleService;


    public DiSkuInsuranceVO insuranceBenefit(InsuranceEmployeePriceReq req){

        DiSkuInsuranceVO resultVo = new DiSkuInsuranceVO();
        List<DiSkuInsuranceVO.insuranceVO> insuranceList = new ArrayList<>();

        ProductSku productSku = productSkuService.getById(req.getSkuId());
        Product product = ProductService.getById(productSku.getProductId());
        if(ObjectUtil.isNull(product) || ObjectUtil.isNull(productSku)){
            throw new BusinessException(99999,"手机SKU不存在");
        }

        resultVo.setSkuId(productSku.getId());
        resultVo.setProductName(product.getName());
        resultVo.setRetailPrice(MoneyUtil.fenToYuan(productSku.getRetailPrice()));

        List<DiSkuInsurance> skuInsuranceList = skuInsuranceService.lambdaQuery()
                .eq(DiSkuInsurance::getSkuId,req.getSkuId()).list();

        if(CollUtil.isEmpty(skuInsuranceList)) return resultVo;


        for(DiSkuInsurance skuInsurance:skuInsuranceList){
            DiSkuInsuranceVO.insuranceVO vo = new DiSkuInsuranceVO.insuranceVO();

            DiInsurance insurance = insuranceService.getById(skuInsurance.getInsuranceId());
            vo.setInsuranceName(insurance.getName());
            vo.setDescription(insurance.getDescription());
            vo.setInsurancePeriod(insurance.getPeriod());
            vo.setDownPrice(MoneyUtil.convert(skuInsurance.getDownPrice()));
            DiProductInsurancePriceDTO priceDTO = new DiProductInsurancePriceDTO();
            priceDTO.setInsuranceId(skuInsurance.getInsuranceId());
            priceDTO.setQueryPrice(productSku.getRetailPrice());
            Integer normalPrice = insurancePriceMapper.getInfoByPriceInterval(priceDTO).getNormalPrice();
            vo.setNormalPrice(MoneyUtil.fenToYuan(normalPrice));

            //收益
            //分佣金额
            Long amount = normalPrice-skuInsurance.getDownPrice();
            Long benefitAmount = commissionSettleService.orderScaleByEmployee(amount, CommissionBizType.INSURANCE_SERVICE,
                    CommissionPackage.INSURANCE_SERVICE,req.getEmployeeId());
            vo.setBenefit(MoneyUtil.convert(benefitAmount));
            insuranceList.add(vo);
        }
        resultVo.setInsuranceList(insuranceList);
        return resultVo;

    }
}