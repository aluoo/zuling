package com.anyi.sparrow.insurance.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.commission.domain.CommissionPlanConf;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.insurance.domain.*;
import com.anyi.common.insurance.enums.*;
import com.anyi.common.insurance.req.*;
import com.anyi.common.insurance.response.*;
import com.anyi.common.insurance.service.*;
import com.anyi.common.product.domain.Product;
import com.anyi.common.product.domain.ProductSku;
import com.anyi.common.product.domain.dto.OrderLogDTO;
import com.anyi.common.product.domain.enums.OrderOperationEnum;
import com.anyi.common.product.domain.response.ProductSkuVO;
import com.anyi.common.product.service.OrderLogService;
import com.anyi.common.product.service.ProductService;
import com.anyi.common.product.service.ProductSkuService;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.utils.DateUtils;
import com.anyi.sparrow.organize.employee.service.EmService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class InsuranceGzhManageService {

    @Autowired
    private DiInsuranceOrderService insuranceOrderService;
    @Autowired
    private DiInsuranceService insuranceService;
    @Autowired
    private EmService emService;
    @Autowired
    private DiInsuranceUserAccountService insuranceUserService;
    @Autowired
    private DiInsuranceOptionService insuranceOptionService;
    @Autowired
    private DiOptionService optionService;
    @Autowired
    private DiInsuranceFixOrderService fixOrderService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CommonSysDictService commonSysDictService;
    @Autowired
    private DiInsuranceFixOrderOptionService fixOrderOptionService;
    @Autowired
    private DiOrderNoProcessService orderNoProcessService;


    public PageInfo<GzhInsuranceOrderVO> orderList(GzhOrderQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<DiInsuranceOrder> list = insuranceOrderService.lambdaQuery()
                .eq(StrUtil.isNotBlank(req.getIdCard()),DiInsuranceOrder::getIdCard,req.getIdCard())
                .eq(StrUtil.isNotBlank(req.getMobile()),DiInsuranceOrder::getCustomPhone,req.getMobile())
                .eq(ObjectUtil.isNotNull(req.getInsuranceOrderId()),DiInsuranceOrder::getId,req.getInsuranceOrderId())
                .eq(ObjectUtil.isNotNull(req.getCompanyId()),DiInsuranceOrder::getStoreCompanyId,req.getCompanyId())
                .eq( ObjectUtil.isNotNull(req.getStoreEmployeeId()), DiInsuranceOrder::getStoreEmployeeId, req.getStoreEmployeeId())
                .eq(DiInsuranceOrder::getStatus, DiOrderStatusEnum.FINISHED.getCode())
                .eq(StrUtil.isNotBlank(req.getImei()),DiInsuranceOrder::getImeiNo,req.getImei())
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }

        List<GzhInsuranceOrderVO> vos = BeanUtil.copyToList(list, GzhInsuranceOrderVO.class);
        vos.forEach(vo -> {
            vo.setOrderId(vo.getOrderId());
            vo.setPhoneName(vo.getProductName()+" ("+vo.getProductSpec()+")");
            vo.setRemark(vo.getInsuranceName()+" ("+vo.getInsurancePeriod()+"年)");
            vo.setExpiredTime(DateUtils.adjustYear(DateUtils.adjustDay(vo.getCreateTime(),1),vo.getInsurancePeriod()));
            vo.setFixButton(DiOrderInsuranceStatusEnum.EFFECTIVE_FINISHED.getCode().equals(vo.getInsuranceStatus()) && DateUtils.adjustDay(vo.getCreateTime(),7).compareTo(new Date())<0);
        });

        PageInfo<GzhInsuranceOrderVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

    @Transactional(rollbackFor = Exception.class)
    public void passWord(GzhPassWordReq req){

        DiInsuranceUserAccount userAccount = insuranceUserService.lambdaQuery()
                .eq(DiInsuranceUserAccount::getMobile,req.getMobile()).last("limit 1").one();

        if(ObjectUtil.isNull(userAccount)){
            throw new BusinessException(99999,"数保用户不存在");
        }

        // 校验密码是否正确
        if (!SecureUtil.md5(req.getOldPass()).equals(userAccount.getPassWord())) {
            throw new BusinessException(BizError.ERROR_TEMPORARY_EMPLOYEE_PASSWORD);
        }

        userAccount.setPassWord(SecureUtil.md5(req.getNewPass()));
        insuranceUserService.updateById(userAccount);
        emService.logoutForInsurance(userAccount.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveFix(InsuranceFixOrderDTO req){
        DiInsuranceOrder insuranceOrder = insuranceOrderService.getById(req.getOrderId());
        if(ObjectUtil.isNull(insuranceOrder)){
            throw new BusinessException(99990,"保单不存在");
        }

        //保存报险订单
        DiInsuranceFixOrder order = new DiInsuranceFixOrder();
        BeanUtil.copyProperties(req,order, CopyOptions.create().ignoreNullValue());
        order.setId(null);
        order.setStoreCompanyId(insuranceOrder.getStoreCompanyId());
        order.setStoreEmployeeId(insuranceOrder.getStoreEmployeeId());
        order.setMobile(insuranceOrder.getCustomPhone());
        order.setStatus(DiFixOrderStatusEnum.WAIT.getCode());
        order.setOrderNo(orderNoProcessService.nextOrderNo());
        order.setOrderId(req.getOrderId());
        order.setOldSkuRetailPrice(StrUtil.isNotBlank(req.getOldSkuRetailPrice())?new BigDecimal(req.getOldSkuRetailPrice()).multiply(new BigDecimal(100)).intValue():0);
        order.setProductSkuRetailPrice(StrUtil.isNotBlank(req.getProductSkuRetailPrice())?new BigDecimal(req.getProductSkuRetailPrice()).multiply(new BigDecimal(100)).intValue():0);
        order.setSuppleAmount(StrUtil.isNotBlank(req.getSuppleAmount())?new BigDecimal(req.getSuppleAmount()).multiply(new BigDecimal(100)).intValue():0);
        order.setDiscountAmount(StrUtil.isNotBlank(req.getDiscountAmount())?new BigDecimal(req.getDiscountAmount()).multiply(new BigDecimal(100)).intValue():0);
        fixOrderService.save(order);

        //保存保险订单图片
        List<DiInsuranceFixOrderOption> resultList = new ArrayList<>();
        if(CollUtil.isNotEmpty(req.getFixPictureList())){
            req.getFixPictureList().forEach(e -> {
                DiInsuranceFixOrderOption fixOrderOption = new DiInsuranceFixOrderOption();
                fixOrderOption.setOptionId(e.getOptionId());
                fixOrderOption.setOrderId(order.getId());
                fixOrderOption.setCode(e.getCode());
                fixOrderOption.setTitle(e.getTitle());
                fixOrderOption.setValue(e.getValue());
                resultList.add(fixOrderOption);
            });
        }
        if(CollUtil.isNotEmpty(req.getSupplePictureList())){
            req.getSupplePictureList().forEach(e -> {
                DiInsuranceFixOrderOption fixOrderOption = new DiInsuranceFixOrderOption();
                fixOrderOption.setOptionId(e.getOptionId());
                fixOrderOption.setOrderId(order.getId());
                fixOrderOption.setCode(e.getCode());
                fixOrderOption.setTitle(e.getTitle());
                fixOrderOption.setValue(e.getValue());
                resultList.add(fixOrderOption);
            });
        }

        fixOrderOptionService.saveBatch(resultList);

        //保存日志记录
        orderLogService.addLog(LoginUserContext.getUser().getId(),
                order.getId(),
                DiFixOrderStatusEnum.WAIT.getCode(),
                FixOrderOperationEnum.CREATE.getCode(),
                FixOrderOperationEnum.CREATE.getDesc(),
                FixOrderOperationEnum.CREATE.getRemark());
    }

    public PageInfo<GzhInsuranceOrderVO> fixList(GzhOrderQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<DiInsuranceFixOrder> list = fixOrderService.lambdaQuery()
                .eq(StrUtil.isNotBlank(req.getMobile()),DiInsuranceFixOrder::getMobile,req.getMobile())
                .eq(ObjectUtil.isNotNull(req.getStatus()),DiInsuranceFixOrder::getStatus,req.getStatus())
                .eq(ObjectUtil.isNotNull(req.getCompanyId()),DiInsuranceFixOrder::getStoreCompanyId,req.getCompanyId())
                .eq(!req.getManage() && ObjectUtil.isNotNull(req.getStoreEmployeeId()), DiInsuranceFixOrder::getStoreEmployeeId, req.getStoreEmployeeId())
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }

        Set<Long> insuranceIds = list.stream().map(DiInsuranceFixOrder::getOrderId).collect(Collectors.toSet());

        Set<Long> serviceTypeIds = list.stream().map(DiInsuranceFixOrder::getServiceType).collect(Collectors.toSet());
        Set<Long> claimIds = list.stream().map(DiInsuranceFixOrder::getClaimItem).collect(Collectors.toSet());
        Set<Long> optionIds = Stream.concat(serviceTypeIds.stream(),claimIds.stream()).collect(Collectors.toSet());

        Map<Long, DiInsuranceOrder> insuranceMap = insuranceOrderService.getInsuranceInfoMap(insuranceIds);
        Map<Long, DiOption> optionMap = optionService.getOptionInfoMap(optionIds);

        List<GzhInsuranceOrderVO> vos = BeanUtil.copyToList(list, GzhInsuranceOrderVO.class);
        vos.forEach(vo -> {
            vo.setCustomName(insuranceMap.get(vo.getOrderId()).getCustomName());
            vo.setCustomPhone(insuranceMap.get(vo.getOrderId()).getCustomPhone());
            vo.setImeiNo(insuranceMap.get(vo.getOrderId()).getImeiNo());
            vo.setInsuranceName(insuranceMap.get(vo.getOrderId()).getInsuranceName());
            vo.setPhoneName(insuranceMap.get(vo.getOrderId()).getProductName()+" ("+insuranceMap.get(vo.getOrderId()).getProductSpec()+")");
            vo.setRemark(vo.getInsuranceName()+" ("+insuranceMap.get(vo.getOrderId()).getInsurancePeriod()+"年)");
            vo.setStatusName(EnumUtil.getBy(DiFixOrderStatusEnum::getCode,vo.getStatus()).getDesc());
            vo.setFixButton(DiFixOrderStatusEnum.ONE_FAIL.getCode().equals(vo.getStatus()));
            vo.setSaveDataButton(DiFixOrderStatusEnum.ONE_PASS.getCode().equals(vo.getStatus()));
            vo.setUpdateDataButton(DiFixOrderStatusEnum.DATA_EDIT.getCode().equals(vo.getStatus()));
            vo.setExpiredTime(DateUtils.adjustYear(DateUtils.adjustDay(vo.getCreateTime(),1),insuranceMap.get(vo.getOrderId()).getInsurancePeriod()));
            vo.setNewPhoneName(vo.getProductName()+" ("+vo.getProductSpec()+")");
            vo.setServiceTypeName(optionMap.get(vo.getServiceType()).getName());
            vo.setClaimItemName(optionMap.get(vo.getClaimItem()).getName());
            if(vo.getUpProduct()!=null){
                vo.setUpProductName(EnumUtil.getBy(DiUpProductEnum::getCode,vo.getUpProduct()).getDesc());
            }
            if(vo.getImeiRead()!=null){
                vo.setImeiReadName(EnumUtil.getBy(DiReadImeiEnum::getCode,vo.getImeiRead()).getDesc());
            }
        });

        PageInfo<GzhInsuranceOrderVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

    public DiInsuranceFixOrderVO fixDetail(Long id){

        List<InsuranceDemoImageVO> fixDataList = new ArrayList<>();
        List<InsuranceDemoImageVO> suppleDataList = new ArrayList<>();
        List<InsuranceDemoImageVO> settleDataList = new ArrayList<>();

        DiInsuranceFixOrderVO resultVo = new DiInsuranceFixOrderVO();
        DiInsuranceFixOrder fixOrder = fixOrderService.getById(id);

        resultVo.setId(fixOrder.getId());
        resultVo.setOrderId(fixOrder.getOrderId());
        resultVo.setOrderNo(fixOrder.getOrderNo());
        resultVo.setStatus(fixOrder.getStatus());
        resultVo.setStatusName(EnumUtil.getBy(DiFixOrderStatusEnum::getCode,resultVo.getStatus()).getDesc());

        resultVo.setFixButton(DiFixOrderStatusEnum.ONE_FAIL.getCode().equals(resultVo.getStatus()));
        resultVo.setSaveDataButton(DiFixOrderStatusEnum.ONE_PASS.getCode().equals(resultVo.getStatus()));
        resultVo.setUpdateDataButton(DiFixOrderStatusEnum.DATA_EDIT.getCode().equals(resultVo.getStatus()));

        resultVo.setFixName(fixOrder.getFixName());
        resultVo.setFixAlipay(fixOrder.getFixAlipay());

        DiInsuranceOrder insuranceOrder = insuranceOrderService.getById(fixOrder.getOrderId());

        ProductSku productSku = Optional.ofNullable(productSkuService.getById(insuranceOrder.getProductSkuId())).orElse(new ProductSku());
        Product product = productService.getById(productSku.getProductId());
        //机器类型
        resultVo.setMobileType(Optional.ofNullable(product).map(Product::getType).orElse(0));
        resultVo.setOldSkuRetailPrice(productSku.getRetailPrice());
        if(ObjectUtil.isNotNull(insuranceOrder.getProductSkuPurchaseInvoicePrice())){
            resultVo.setOldSkuRetailPrice(insuranceOrder.getProductSkuPurchaseInvoicePrice());
        }

        //服务类型
        resultVo.setServiceType(optionService.getById(fixOrder.getServiceType()));

        //数保服务类型对应的理赔项目
        List<DiOption> optionList = insuranceOptionService.getOptionIdsByPid(fixOrder.getServiceType());

        //保险材料
        List<DiInsuranceFixOrderOption> fixOrderOptionList = fixOrderOptionService.list(Wrappers.lambdaQuery(DiInsuranceFixOrderOption.class)
                .eq(DiInsuranceFixOrderOption::getOrderId,fixOrder.getId()));

        Map<Long,DiInsuranceFixOrderOption> fixOptionMap = fixOrderOptionList.stream().collect(Collectors.toMap(DiInsuranceFixOrderOption::getOptionId, Function.identity()));


        if(CollUtil.isNotEmpty(optionList)){
            for(DiOption option:optionList){
                //报险资料
                List<DiOption> fixDataImages =insuranceOptionService.getOptionByAncestors(insuranceOrder.getInsuranceId(),option.getAncestors(),"FIXDATA");
                //List<DiOption> fixDataImages = insuranceOptionService.getCodeImages(option.getAncestors(),"FIXDATA");
                if(CollUtil.isNotEmpty(fixDataImages)){
                    List<DiOptionVO> fixDataImageVo = BeanUtil.copyToList(fixDataImages, DiOptionVO.class);
                    InsuranceDemoImageVO fixDataVo = new InsuranceDemoImageVO();
                    fixDataVo.setItemName(option.getName());
                    //示例图用上传的图片替换
                    fixDataImageVo.stream().forEach(e ->{
                        if(fixOptionMap!=null && fixOptionMap.get(e.getId())!=null){
                            e.setValue(fixOptionMap.get(e.getId()).getValue());
                        }

                    });
                    fixDataVo.setImageList(fixDataImageVo.stream().filter(e->e.getType().intValue()==3 || e.getType().intValue()==4).collect(Collectors.toList()));
                    fixDataList.add(fixDataVo);
                }

                //补充资料
                List<DiOption> suppleImages = insuranceOptionService.getOptionByAncestors(insuranceOrder.getInsuranceId(),option.getAncestors(),"SUPPLEDATA");
                if(CollUtil.isNotEmpty(suppleImages)){
                    List<DiOptionVO> suppleImageVo = BeanUtil.copyToList(suppleImages, DiOptionVO.class);
                    InsuranceDemoImageVO fixDataVo = new InsuranceDemoImageVO();
                    fixDataVo.setItemName(option.getName());
                    //示例图用上传的图片替换
                    suppleImageVo.stream().forEach(e ->{
                        if(fixOptionMap!=null && fixOptionMap.get(e.getId())!=null){
                            e.setValue(fixOptionMap.get(e.getId()).getValue());
                        }

                    });
                    fixDataVo.setImageList(suppleImageVo.stream().filter(e->e.getType().intValue()==3 || e.getType().intValue()==4).collect(Collectors.toList()));
                    suppleDataList.add(fixDataVo);
                }

                //理赔资料
                List<DiOption> settleImages = insuranceOptionService.getOptionByAncestors(insuranceOrder.getInsuranceId(),option.getAncestors(),"SETTLEDATA");
                if(CollUtil.isNotEmpty(settleImages)){
                    List<DiOptionVO> settleImageVo = BeanUtil.copyToList(settleImages, DiOptionVO.class);
                    InsuranceDemoImageVO fixDataVo = new InsuranceDemoImageVO();
                    fixDataVo.setItemName(option.getName());
                    //示例图用上传的图片替换
                    settleImageVo.stream().forEach(e ->{
                        if(fixOptionMap!=null && fixOptionMap.get(e.getId())!=null){
                            e.setValue(fixOptionMap.get(e.getId()).getValue());
                        }
                    });
                    fixDataVo.setImageList(settleImageVo.stream().filter(e->e.getType().intValue()==3 || e.getType().intValue()==4).collect(Collectors.toList()));
                    settleDataList.add(fixDataVo);
                }

            }
        }

        resultVo.setFixItemList(optionList);
        resultVo.setFixDataList(fixDataList);
        resultVo.setSuppleDataList(suppleDataList);
        resultVo.setSettleDataList(settleDataList);

        //折抵金额计算
        int days = 0;
        try {
            days = DateUtils.daysBetween(new Date(),insuranceOrder.getEffectiveDate());
        } catch (ParseException e) {
            throw new BusinessException(99999,"日期转换异常");
        }

        if(days<=365){
            resultVo.setDiscountAmount(new BigDecimal(productSku.getRetailPrice()).multiply(new BigDecimal("0.60")).intValue());
            resultVo.setDiscountAmountDesc("(第一年按购机价60%折抵)");
        }else{
            resultVo.setDiscountAmount(new BigDecimal(productSku.getRetailPrice()).multiply(new BigDecimal("0.50")).intValue());
            resultVo.setDiscountAmountDesc("(第二年按购机价40%折抵)");
        }

        resultVo.setBadFixInsurancePrice(commonSysDictService.getBadFixInsurancePrice());
        resultVo.setGoodFixInsurancePrice(commonSysDictService.getGoodFixInsurancePrice());


        resultVo.setProductName(insuranceOrder.getProductName());
        resultVo.setProductSpec(insuranceOrder.getProductSpec());
        resultVo.setImeiNo(insuranceOrder.getImeiNo());
        resultVo.setCustomName(insuranceOrder.getCustomName());
        resultVo.setCustomPhone(insuranceOrder.getCustomPhone());
        resultVo.setIdCard(insuranceOrder.getIdCard());
        resultVo.setInsuranceName(insuranceOrder.getInsuranceName());
        resultVo.setInsurancePeriod(insuranceOrder.getInsurancePeriod());

        resultVo.setNewProductName(fixOrder.getProductName());
        resultVo.setNewProductSpec(fixOrder.getProductSpec());
        resultVo.setNewProductSkuRetailPrice(fixOrder.getProductSkuRetailPrice());
        resultVo.setProductSkuId(fixOrder.getProductSkuId());
        resultVo.setSuppleAmount(fixOrder.getSuppleAmount());
        resultVo.setSettleAmount(fixOrder.getSettleAmount());
        resultVo.setCreateTime(fixOrder.getCreateTime());
        resultVo.setUpProduct(fixOrder.getUpProduct());
        resultVo.setImeiRead(fixOrder.getImeiRead());
        resultVo.setBreakDown(fixOrder.getBreakDown());
        resultVo.setFixCity(fixOrder.getFixCity());
        resultVo.setNewImei(fixOrder.getNewImei());
        resultVo.setRemark(fixOrder.getRemark());
        resultVo.setContactMobile(fixOrder.getContactMobile());

        resultVo.setExpiredTime(DateUtils.adjustYear(DateUtils.adjustDay(resultVo.getCreateTime(),1),insuranceOrder.getInsurancePeriod()));


        if(resultVo.getUpProduct()!=null){
            resultVo.setUpProductName(EnumUtil.getBy(DiUpProductEnum::getCode,resultVo.getUpProduct()).getDesc());
        }
        if(resultVo.getImeiRead()!=null){
            resultVo.setImeiReadName(EnumUtil.getBy(DiReadImeiEnum::getCode,resultVo.getImeiRead()).getDesc());
        }

        DiOption serviceTypeOption = optionService.getById(fixOrder.getServiceType());
        DiOption claimItemOption = optionService.getById(fixOrder.getClaimItem());


        resultVo.setServiceTypeName(serviceTypeOption.getName());
        resultVo.setClaimItemName(claimItemOption.getName());

        List<Integer> customerStatus = Arrays.asList(fixOrder.getStatus());

        List<OrderLogDTO> logs = orderLogService.listLog(fixOrder.getId(), customerStatus);
        if(CollUtil.isNotEmpty(logs) &&
                Arrays.asList(DiFixOrderStatusEnum.ONE_FAIL.getCode(),DiFixOrderStatusEnum.DATA_EDIT.getCode()).contains(fixOrder.getStatus())){
            resultVo.setFailRemark(logs.get(0).getRemark());
        }
        return resultVo;
    }

    public DiFixInsuranceDetailVO insuranceOrderDetail(InsuranceOrderDetailReq req){
        DiFixInsuranceDetailVO resultVo = new DiFixInsuranceDetailVO();

        List<InsuranceDemoImageVO> fixDataList = new ArrayList<>();
        List<InsuranceDemoImageVO> suppleDataList = new ArrayList<>();

        DiInsuranceOrder insuranceOrder = insuranceOrderService.getById(req.getOrderId());
        resultVo.setOrderId(insuranceOrder.getId());
        resultVo.setProductName(insuranceOrder.getProductName());
        resultVo.setProductSpec(insuranceOrder.getProductSpec());
        resultVo.setImeiNo(insuranceOrder.getImeiNo());
        resultVo.setCustomName(insuranceOrder.getCustomName());
        resultVo.setCustomPhone(insuranceOrder.getCustomPhone());
        resultVo.setIdCard(insuranceOrder.getIdCard());
        resultVo.setInsuranceName(insuranceOrder.getInsuranceName());
        resultVo.setInsurancePeriod(insuranceOrder.getInsurancePeriod());
        ProductSku productSku = Optional.ofNullable(productSkuService.getById(insuranceOrder.getProductSkuId())).orElse(new ProductSku());
        Product product = productService.getById(productSku.getProductId());
        //机器类型
        resultVo.setMobileType(Optional.ofNullable(product).map(Product::getType).orElse(0));
        resultVo.setOldSkuRetailPrice(productSku.getRetailPrice());
        if(ObjectUtil.isNotNull(insuranceOrder.getProductSkuPurchaseInvoicePrice())){
            resultVo.setOldSkuRetailPrice(insuranceOrder.getProductSkuPurchaseInvoicePrice());
        }


        //服务类型时候返回
        if(ObjectUtil.isNotNull(req.getOptionId())){
            resultVo.setServiceType(optionService.getById(req.getOptionId()));
        }else{
            //没选择服务类型的时候
            List<DiOption> serviceTypeList = insuranceOptionService.getOptionByProductId(insuranceOrder.getInsuranceId(),"FIXSERVICE");
            if(CollUtil.isNotEmpty(serviceTypeList)){
                resultVo.setServiceType(serviceTypeList.get(0));
                req.setOptionId(serviceTypeList.get(0).getId());
            }
        }

        //数保服务类型对应的理赔项目
        List<DiOption> optionList = insuranceOptionService.getOptionIdsByPid(req.getOptionId());
        if(CollUtil.isNotEmpty(optionList)){
            for(DiOption option:optionList){
                //报险资料
                List<DiOption> fixDataImages = insuranceOptionService.getOptionByAncestors(insuranceOrder.getInsuranceId(),option.getAncestors(),"FIXDATA");
                if(CollUtil.isNotEmpty(fixDataImages)){
                    List<DiOptionVO> fixDataImageVo = BeanUtil.copyToList(fixDataImages, DiOptionVO.class);
                    InsuranceDemoImageVO fixDataVo = new InsuranceDemoImageVO();
                    fixDataVo.setItemName(option.getName());
                    fixDataVo.setImageList(fixDataImageVo.stream().filter(e->e.getType().intValue()==3 || e.getType().intValue()==4).collect(Collectors.toList()));
                    fixDataList.add(fixDataVo);
                }

                //补充资料
                List<DiOption> suppleImages = insuranceOptionService.getOptionByAncestors(insuranceOrder.getInsuranceId(),option.getAncestors(),"SUPPLEDATA");
                if(CollUtil.isNotEmpty(suppleImages)){
                    List<DiOptionVO> suppleImageVo = BeanUtil.copyToList(suppleImages, DiOptionVO.class);
                    InsuranceDemoImageVO fixDataVo = new InsuranceDemoImageVO();
                    fixDataVo.setItemName(option.getName());
                    fixDataVo.setImageList(suppleImageVo.stream().filter(e->e.getType().intValue()==3 || e.getType().intValue()==4).collect(Collectors.toList()));
                    suppleDataList.add(fixDataVo);
                }

            }
        }
        resultVo.setFixItemList(optionList);
        resultVo.setFixDataList(fixDataList);
        resultVo.setSuppleDataList(suppleDataList);

        //折抵金额计算
        int days = 0;
        try {
             days = DateUtils.daysBetween(new Date(),insuranceOrder.getEffectiveDate());
        } catch (ParseException e) {
            throw new BusinessException(99999,"日期转换异常");
        }

        if(days<=365){
            resultVo.setDiscountAmount(new BigDecimal(productSku.getRetailPrice()).multiply(new BigDecimal("0.60")).intValue());
            resultVo.setDiscountAmountDesc("(第一年按购机价60%折抵)");
        }else{
            resultVo.setDiscountAmount(new BigDecimal(productSku.getRetailPrice()).multiply(new BigDecimal("0.50")).intValue());
            resultVo.setDiscountAmountDesc("(第一年按购机价40%折抵)");
        }

        resultVo.setBadFixInsurancePrice(commonSysDictService.getBadFixInsurancePrice());
        resultVo.setGoodFixInsurancePrice(commonSysDictService.getGoodFixInsurancePrice());
        return resultVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateFix(InsuranceFixOrderDTO req){
        DiInsuranceFixOrder fixOrder = fixOrderService.getById(req.getId());
        if(ObjectUtil.isNull(fixOrder)){
            throw new BusinessException(99999,"报修订单不存在");
        }
        if(!DiFixOrderStatusEnum.ONE_FAIL.getCode().equals(fixOrder.getStatus())){
            throw new BusinessException(99999,"订单状态不支持修改报险材料");
        }
        BeanUtil.copyProperties(req,fixOrder, CopyOptions.create().ignoreNullValue());

        fixOrder.setOldSkuRetailPrice(StrUtil.isNotBlank(req.getOldSkuRetailPrice())?new BigDecimal(req.getOldSkuRetailPrice()).multiply(new BigDecimal(100)).intValue():0);
        fixOrder.setProductSkuRetailPrice(StrUtil.isNotBlank(req.getProductSkuRetailPrice())?new BigDecimal(req.getProductSkuRetailPrice()).multiply(new BigDecimal(100)).intValue():0);
        fixOrder.setSuppleAmount(StrUtil.isNotBlank(req.getSuppleAmount())?new BigDecimal(req.getSuppleAmount()).multiply(new BigDecimal(100)).intValue():0);
        fixOrder.setDiscountAmount(StrUtil.isNotBlank(req.getDiscountAmount())?new BigDecimal(req.getDiscountAmount()).multiply(new BigDecimal(100)).intValue():0);
        fixOrder.setStatus(DiFixOrderStatusEnum.WAIT.getCode());
        fixOrderService.updateById(fixOrder);

        //删除旧的报险材料
        fixOrderOptionService.remove(Wrappers.lambdaQuery(DiInsuranceFixOrderOption.class)
                .eq(DiInsuranceFixOrderOption::getOrderId,fixOrder.getId()));

        //保存保险订单图片
        List<DiInsuranceFixOrderOption> resultList = new ArrayList<>();
        if(CollUtil.isNotEmpty(req.getFixPictureList())){
            req.getFixPictureList().forEach(e -> {
                DiInsuranceFixOrderOption fixOrderOption = new DiInsuranceFixOrderOption();
                fixOrderOption.setOptionId(e.getOptionId());
                fixOrderOption.setOrderId(fixOrder.getId());
                fixOrderOption.setCode(e.getCode());
                fixOrderOption.setTitle(e.getTitle());
                fixOrderOption.setValue(e.getValue());
                resultList.add(fixOrderOption);
            });
        }
        if(CollUtil.isNotEmpty(req.getSupplePictureList())){
            req.getSupplePictureList().forEach(e -> {
                DiInsuranceFixOrderOption fixOrderOption = new DiInsuranceFixOrderOption();
                fixOrderOption.setOptionId(e.getOptionId());
                fixOrderOption.setOrderId(fixOrder.getId());
                fixOrderOption.setCode(e.getCode());
                fixOrderOption.setTitle(e.getTitle());
                fixOrderOption.setValue(e.getValue());
                resultList.add(fixOrderOption);
            });
        }

        fixOrderOptionService.saveBatch(resultList);

        //保存日志记录
        orderLogService.addLog(LoginUserContext.getUser().getId(),
                fixOrder.getId(),
                DiFixOrderStatusEnum.WAIT.getCode(),
                FixOrderOperationEnum.ONE_FAIL.getCode(),
                FixOrderOperationEnum.ONE_FAIL.getDesc(),
                FixOrderOperationEnum.ONE_FAIL.getRemark());

    }



    @Transactional(rollbackFor = Exception.class)
    public void saveData(InsuranceFixSettleDTO req){

        DiInsuranceFixOrder fixOrder = fixOrderService.getById(req.getId());
        if(ObjectUtil.isNull(fixOrder)){
            throw new BusinessException(99999,"报修订单不存在");
        }
        if(!Arrays.asList(DiFixOrderStatusEnum.ONE_PASS.getCode(),DiFixOrderStatusEnum.DATA_EDIT.getCode()).contains(fixOrder.getStatus())){
            throw new BusinessException(99999,"订单状态不支持维修资料变动");
        }
        BeanUtil.copyProperties(req,fixOrder, CopyOptions.create().ignoreNullValue());
        fixOrder.setStatus(DiFixOrderStatusEnum.DATA_WAIT.getCode());
        fixOrderService.updateById(fixOrder);

        //删除旧的理赔材料
        fixOrderOptionService.remove(Wrappers.lambdaQuery(DiInsuranceFixOrderOption.class)
                        .eq(DiInsuranceFixOrderOption::getCode,"SETTLEDATA")
                .eq(DiInsuranceFixOrderOption::getOrderId,fixOrder.getId()));

        //保存保险订单图片
        List<DiInsuranceFixOrderOption> resultList = new ArrayList<>();
        if(CollUtil.isNotEmpty(req.getSettlePictureList())){
            req.getSettlePictureList().forEach(e -> {
                DiInsuranceFixOrderOption fixOrderOption = new DiInsuranceFixOrderOption();
                fixOrderOption.setOptionId(e.getOptionId());
                fixOrderOption.setOrderId(fixOrder.getId());
                fixOrderOption.setCode(e.getCode());
                fixOrderOption.setTitle(e.getTitle());
                fixOrderOption.setValue(e.getValue());
                resultList.add(fixOrderOption);
            });
        }
        fixOrderOptionService.saveBatch(resultList);

        //保存日志记录
        orderLogService.addLog(LoginUserContext.getUser().getId(),
                fixOrder.getId(),
                DiFixOrderStatusEnum.DATA_WAIT.getCode(),
                FixOrderOperationEnum.DATA_EDIT.getCode(),
                FixOrderOperationEnum.DATA_EDIT.getDesc(),
                FixOrderOperationEnum.DATA_EDIT.getRemark());
    }

}
