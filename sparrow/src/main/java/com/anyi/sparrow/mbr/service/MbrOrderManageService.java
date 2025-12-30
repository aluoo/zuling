package com.anyi.sparrow.mbr.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.domain.param.IdQueryReq;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.huiwanzu.HuiWanZuApiUtil;
import com.anyi.common.huiwanzu.dto.HuiWanZuCommonResp;
import com.anyi.common.huiwanzu.enums.HuiWanZuApiEnum;
import com.anyi.common.mbr.domain.*;
import com.anyi.common.mbr.enums.*;
import com.anyi.common.mbr.req.MbrPreOrderApplyReq;
import com.anyi.common.mbr.req.PreOrderQueryReq;
import com.anyi.common.mbr.req.ShopSuppleReq;
import com.anyi.common.mbr.response.*;
import com.anyi.common.mbr.service.*;
import com.anyi.common.product.domain.OrderLog;
import com.anyi.common.product.domain.Product;
import com.anyi.common.product.service.OrderLogService;
import com.anyi.common.product.service.ProductService;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.util.SpringContextUtil;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjian
 * @Description
 * @Date 2025/4/21
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class MbrOrderManageService {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MbrRentalProductSkuService skuService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private MbrPreOrderQuoteLogService mbrPreOrderQuoteLogService;
    @Autowired
    private MbrPreOrderService mbrPreOrderService;
    @Autowired
    private MbrOrderService mbrOrderService;
    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private CommonSysDictService dictService;
    @Autowired
    private MbrShopCodeService mbrShopCodeService;

    @Transactional(rollbackFor = Exception.class)
    public MbrPreOrderApplyVO applyOrder(MbrPreOrderApplyReq req) {
        MbrRentalProductSku sku = skuService.getById(req.getSkuId());
        Product product = productService.getById(sku.getProductId());
        Long orderId = snowflakeIdService.nextId();
        //第一期自己先创建个租机商户使用
        Company company = companyService.getById(1914510187644907521L);
        //进件单初始化
        MbrPreOrder preOrder = MbrPreOrder.builder()
                .id(orderId)
                .storeEmployeeId(LoginUserContext.getUser().getId())
                .storeCompanyId(LoginUserContext.getUser().getCompanyId())
                .productSkuId(sku.getId())
                .productName(product.getName())
                .productSpec(sku.getSpec())
                .productType(req.getProductType())
                .period(req.getPeriod())
                .status(MbrPreOrderStatusEnum.FINISHED.getCode())
                .subStatus(MbrPreOrderSubStatusEnum.PASS.getCode())
                .customName(req.getCustomName())
                .customPhone(req.getCustomPhone())
                .idCard(req.getIdCard())
                .quotable(true)
                .recyclerCompanyId(company.getId())
                .recyclerEmployeeId(company.getEmployeeId())
                .build();
        //第一期默认自动审核通过，默认租机商户自动报价
        MbrPreOrderQuoteLog quoteLog = MbrPreOrderQuoteLog.builder()
                .orderId(orderId)
                .quoteTime(new Date())
                .recyclerCompanyId(company.getId())
                .recyclerEmployeeId(company.getEmployeeId())
                .quoted(true)
                .status(MbrQuoteLogStatusEnum.FINISHED.getCode())
                .build();
        mbrPreOrderQuoteLogService.save(quoteLog);
        //保存进件单
        preOrder.setQuoteLogId(quoteLog.getId());
        preOrder.setFinishQuoteTime(new Date());
        mbrPreOrderService.save(preOrder);
        //进件单日志
        orderLogService.addLog(
                LoginUserContext.getUser().getId(),
                orderId,
                MbrPreOrderStatusEnum.FINISHED.getCode(),
                MbrPreOrderStatusEnum.FINISHED.getCode(),
                MbrPreOrderStatusEnum.FINISHED.getDesc(),
                MbrPreOrderStatusEnum.FINISHED.getDesc()
        );
        return MbrPreOrderApplyVO.builder().preOrderId(orderId).build();
    }

    public MbrPreOrderDetailVO preOrderDetail(IdQueryReq req) {
        //进件单
        MbrPreOrder preOrder = mbrPreOrderService.getById(req.getId());
        MbrPreOrderDetailVO vo = new MbrPreOrderDetailVO();
        BeanUtil.copyProperties(preOrder, vo);
        // 设置过期时间
        vo.setExpiredTime(dictService.getProductOrderExpiredMinutes(), dictService.getQuoteExpiredMinutes());

        //审核通过的租机商
        List<MbrPreOrderQuoteLog> quotePassLogList = mbrPreOrderQuoteLogService.lambdaQuery().eq(MbrPreOrderQuoteLog::getOrderId, preOrder.getId())
                .eq(MbrPreOrderQuoteLog::getStatus,MbrPreOrderStatusEnum.FINISHED.getCode())
                .eq(MbrPreOrderQuoteLog::getSubStatus,MbrPreOrderSubStatusEnum.PASS.getCode()).list();
        if(CollUtil.isEmpty(quotePassLogList)) {
            vo.setPassNum(0);
        }else{
            vo.setPassNum(quotePassLogList.size());
        }

        //租机商审核状态列表
        vo.setQuoteLogList(listQuoteInfoByOrder(IdQueryReq.builder().id(req.getId()).build()));
        return vo;
    }

    public List<MbrPlatQuoteLogVO> listQuoteInfoByOrder(IdQueryReq req){
        //租机商审核状态列表
        List<MbrPreOrderQuoteLog> quoteLogList = mbrPreOrderQuoteLogService.lambdaQuery()
                .eq(MbrPreOrderQuoteLog::getOrderId, req.getId()).list();

        List<Long> companyIds = quoteLogList.stream().map(MbrPreOrderQuoteLog::getRecyclerCompanyId)
                .collect(Collectors.toList());

        Map<Long, Company> companyMap =  companyService.getCompanyInfoMap(companyIds);

        List<MbrPlatQuoteLogVO> quoteLogVOList = new ArrayList<>();
        quoteLogList.stream().forEach(quoteLog -> {
            MbrPlatQuoteLogVO quoteLogVO = new MbrPlatQuoteLogVO();
            quoteLogVO.setRecyclerCompanyName(companyMap.get(quoteLog.getRecyclerCompanyId()).getName());
            quoteLogVO.setQuoteTime(quoteLog.getQuoteTime());
            quoteLogVO.setCreateTime(quoteLog.getCreateTime());
            quoteLogVO.setStatus(quoteLog.getStatus());
            quoteLogVO.setStatusName(EnumUtil.getBy(MbrQuoteLogStatusEnum::getCode,quoteLog.getStatus()).name());
            quoteLogVOList.add(quoteLogVO);
        });
       return quoteLogVOList;
    }

    public MbrOrderDetailVO rent(IdQueryReq req) {
        MbrPreOrder preOrder = mbrPreOrderService.getById(req.getId());
        //生成租机单
        MbrOrder mbrOrder = MbrOrder.builder()
                .storeEmployeeId(LoginUserContext.getUser().getId())
                .storeCompanyId(LoginUserContext.getUser().getCompanyId())
                .productName(preOrder.getProductName())
                .productSpec(preOrder.getProductSpec())
                .period(preOrder.getPeriod())
                .subStatus(MbrOrderSubStatusEnum.ORDERING.getCode())
                .customName(preOrder.getCustomName())
                .customPhone(preOrder.getCustomPhone())
                .idCard(preOrder.getIdCard())
                .settleAmount(0L).build();
        mbrOrderService.save(mbrOrder);
        return rentDetail(mbrOrder.getId());
    }


    public MbrOrderDetailVO rentDetail(Long orderId) {
        MbrOrder order = mbrOrderService.getById(orderId);
        MbrOrderDetailVO vo = new MbrOrderDetailVO();
        BeanUtil.copyProperties(order, vo);
        vo.setIdCard(DesensitizedUtil.idCardNum(vo.getIdCard(), 4, 4));
        vo.setStoreCompanyName(companyService.getById(order.getStoreCompanyId()).getName());
        vo.setStoreEmployeeName(employeeService.getById(order.getStoreEmployeeId()).getName());
        vo.setStoreEmployeeMobile(employeeService.getById(order.getStoreEmployeeId()).getMobileNumber());
        OrderLog orderLog = orderLogService.getLatestLogByOrderId(orderId);
        vo.setRemark(ObjectUtil.isNull(orderLog)?"":orderLog.getRemark());
        return vo;
    }

    public PageInfo<MbrPreOrderDetailVO> preListOrder(PreOrderQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<MbrPreOrder> list = mbrPreOrderService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(MbrPreOrder::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(req.getOnlySelf() != null && req.getOnlySelf(), MbrPreOrder::getStoreEmployeeId, LoginUserContext.getUser().getId())
                .eq(req.getStatus() != null, MbrPreOrder::getStatus, req.getStatus())
                .ge(req.getBeginTime() != null, MbrPreOrder::getCreateTime, req.getBeginTime())
                .le(req.getEndTime() != null, MbrPreOrder::getCreateTime, req.getEndTime())
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }
        List<MbrPreOrderDetailVO> vos = BeanUtil.copyToList(list, MbrPreOrderDetailVO.class);
        List<Long> recyclerCompanyIds = vos.stream().map(MbrPreOrderDetailVO::getRecyclerCompanyId).collect(Collectors.toList());
        List<Long> orderIds = vos.stream().map(MbrPreOrderDetailVO::getId).collect(Collectors.toList());
        Map<Long, Company> companyMap = companyService.getCompanyInfoMap(recyclerCompanyIds);
        Map<Long, PreOrderQuoteLogCountDTO> orderQuoteLogCountMap = mbrPreOrderQuoteLogService.countGroupByOrderIds(orderIds);
        vos.forEach(vo -> {
            // 设置租机商户名称
            vo.setRecyclerCompanyName(companyMap.get(vo.getRecyclerCompanyId()).getName());
            // 设置已经报价的回收商数量
            vo.setPassNum(orderQuoteLogCountMap.getOrDefault(vo.getId(), new PreOrderQuoteLogCountDTO()).getFinishNum());
            // 设置待报价的回收商数量
            vo.setDealNum(orderQuoteLogCountMap.getOrDefault(vo.getId(), new PreOrderQuoteLogCountDTO()).getDealNum());
        });
        PageInfo<MbrPreOrderDetailVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

    public PageInfo<MbrOrderDetailVO> listOrder(PreOrderQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<MbrOrder> list = mbrOrderService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .likeRight(MbrOrder::getAncestors, LoginUserContext.getUser().getAncestors())
                .eq(req.getOnlySelf() != null && req.getOnlySelf(), MbrOrder::getStoreEmployeeId, LoginUserContext.getUser().getId())
                .eq(req.getStatus() != null, MbrOrder::getStatus, req.getStatus())
                .ge(req.getBeginTime() != null, MbrOrder::getCreateTime, req.getBeginTime())
                .le(req.getEndTime() != null, MbrOrder::getCreateTime, req.getEndTime())
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }
        List<MbrOrderDetailVO> vos = BeanUtil.copyToList(list, MbrOrderDetailVO.class);
        List<Long> companyIds = vos.stream().map(MbrOrderDetailVO::getStoreCompanyId).collect(Collectors.toList());
        List<Long> employeeIds = vos.stream().map(MbrOrderDetailVO::getStoreEmployeeId).collect(Collectors.toList());
        Map<Long, Company> companyMap = companyService.getCompanyInfoMap(companyIds);
        Map<Long, Employee> employeeMap = employeeService.getEmployeeInfoMap(employeeIds);
        vos.forEach(vo -> {
           vo.setStoreCompanyName(companyMap.get(vo.getStoreCompanyId()).getName());
            vo.setStoreEmployeeName(employeeMap.get(vo.getStoreEmployeeId()).getName());
            vo.setStoreEmployeeMobile(employeeMap.get(vo.getStoreEmployeeId()).getMobileNumber());
            vo.setIdCard(DesensitizedUtil.idCardNum(vo.getIdCard(), 4, 4));

        });
        PageInfo<MbrOrderDetailVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

    public MbrShopCodeVO shopCode() {
        Long employeeId = LoginUserContext.getUser().getId();
        MbrShopCode mbrShopCode = mbrShopCodeService.lambdaQuery()
                .eq(MbrShopCode::getEmployeeId, employeeId)
                .eq(MbrShopCode::getDeleted, 0).one();
        if(ObjectUtil.isNotNull(mbrShopCode)){
            MbrShopCodeVO mbrShopCodeVO = new MbrShopCodeVO();
            mbrShopCodeVO.setOutShopId(String.valueOf(mbrShopCode.getEmployeeId()));
            mbrShopCodeVO.setShopId(mbrShopCode.getShopId());
            mbrShopCodeVO.setQrcodeUrl(mbrShopCode.getQrCodeUrl());
            mbrShopCodeVO.setShopName(mbrShopCode.getShopName());
            return mbrShopCodeVO;
        }
        return createShop();
    }

    public MbrShopCodeVO createShop() {
        Employee employee = employeeService.getById(LoginUserContext.getUser().getId());
        Company company = companyService.getById(employee.getCompanyId());
        MbrCreateShopDTO createShopDTO = new MbrCreateShopDTO();
        //地推
        if(CompanyType.COMPANY.getCode()==employee.getCompanyType().intValue() || company.getMbrType()==0){
            createShopDTO.setShopName(employee.getName());
            createShopDTO.setContactName(employee.getName());
            createShopDTO.setEmployeeMobile(employee.getMobileNumber());
            createShopDTO.setOutShopId(employee.getId().toString());
            createShopDTO.setType("salesman");
        }else if(Arrays.asList(CompanyType.STORE.getCode(),CompanyType.CHAIN.getCode())
                .contains(employee.getCompanyType()) && company.getEmployeeId().equals(employee.getId()) && company.getMbrType()==1){
            //门店店长创建二维码
            createShopDTO.setShopName(company.getName());
            createShopDTO.setContactName(company.getName());
            createShopDTO.setEmployeeMobile(company.getContactMobile());
            createShopDTO.setOutShopId(company.getId().toString());
            createShopDTO.setType("shop");
        }else if(Arrays.asList(CompanyType.STORE.getCode(),CompanyType.CHAIN.getCode())
                .contains(employee.getCompanyType()) && !company.getEmployeeId().equals(employee.getId()) && company.getMbrType()==1){
            //店员创建二维码
            createShopDTO.setShopName(company.getName());
            createShopDTO.setContactName(company.getName());
            createShopDTO.setEmployeeMobile(employee.getMobileNumber());
            createShopDTO.setOutShopId(employee.getId().toString());
            createShopDTO.setType("employee");
            createShopDTO.setBelongToOutShopId(company.getId().toString());
        }
        HuiWanZuCommonResp commonResp = HuiWanZuApiUtil.execBase(createShopDTO,HuiWanZuApiUtil.getHost(SpringContextUtil.getProfile()), HuiWanZuApiEnum.SHOPS_CREATE);
        MbrShopCodeVO resultVO = JSONUtil.toBean(JSONUtil.toJsonStr(commonResp.getData()), MbrShopCodeVO.class);

        MbrShopCode mbrShopCode = new MbrShopCode();
        mbrShopCode.setShopId(resultVO.getShopId());
        mbrShopCode.setEmployeeId(LoginUserContext.getUser().getId());
        mbrShopCode.setQrCodeUrl(resultVO.getQrcodeUrl());
        mbrShopCode.setShopName(resultVO.getShopName());
        mbrShopCodeService.save(mbrShopCode);
        return resultVO;
    }

    public void supple(ShopSuppleReq req) {
        Employee employee = employeeService.getById(LoginUserContext.getUser().getId());
        Company company = companyService.getById(employee.getCompanyId());
        req.setOutShopId(employee.getCompanyId());
        HuiWanZuCommonResp commonResp = HuiWanZuApiUtil.execBase(req,HuiWanZuApiUtil.getHost(SpringContextUtil.getProfile()), HuiWanZuApiEnum.SHOPS_UPDATE);
        //更新门店信息
        mbrShopCodeService.lambdaUpdate()
                .eq(MbrShopCode::getEmployeeId,company.getEmployeeId())
                .set(MbrShopCode::getLicenseName,req.getBusinessLicenseName())
                .set(MbrShopCode::getLicenseNo,req.getBusinessLicenseNo())
                .set(MbrShopCode::getShopLegalName,req.getShopLegalName())
                .set(MbrShopCode::getPayee,req.getPayee())
                .set(MbrShopCode::getAccount,req.getAccount()).update();
    }

    public String settleLink() {
        Employee employee = employeeService.getById(LoginUserContext.getUser().getId());
        Company company = companyService.getById(employee.getCompanyId());

        MbrShopCode shopCode = mbrShopCodeService.lambdaQuery().eq(MbrShopCode::getEmployeeId,company.getEmployeeId())
                .isNotNull(MbrShopCode::getPayee).one();

        if(ObjectUtil.isNull(shopCode)){
            throw new BusinessException(99999,"请先去上传门店结算补充资料");
        }

        if(StrUtil.isBlank(shopCode.getSettleLink())){
            throw new BusinessException(99999,"暂无结算链接地址");
        }

        return shopCode.getSettleLink();
    }
}