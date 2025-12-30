package com.anyi.common.exchange.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.anyi.common.account.constant.EmployAccountChangeEnum;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.commission.enums.CommissionPackage;
import com.anyi.common.commission.service.CommissionSettleService;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.exchange.domain.*;
import com.anyi.common.exchange.dto.ExchangeApplyOrderDTO;
import com.anyi.common.exchange.dto.ExchangePartnerApplyDTO;
import com.anyi.common.exchange.dto.ExchangePhoneVerifyDTO;
import com.anyi.common.exchange.dto.OnekeyApplyOrderDTO;
import com.anyi.common.exchange.enums.*;
import com.anyi.common.exchange.mapper.MbExchangeOrderMapper;
import com.anyi.common.exchange.response.MbExchangeQrcodeVO;
import com.anyi.common.exchange.response.MbPartnerOrderQueryReq;
import com.anyi.common.exchange.response.MbPartnerOrderQueryVO;
import com.anyi.common.oss.FileUploader;
import com.anyi.common.product.service.OrderLogService;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.util.date.DateUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 换机晒单表 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Service
@Slf4j
public class MbExchangeOrderService extends ServiceImpl<MbExchangeOrderMapper, MbExchangeOrder> {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private FileUploader fileUploader;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private MbExchangeEmployeeService mbExchangeEmployeeService;
    @Autowired
    private MbExchangePicService mbExchangePicService;
    @Autowired
    private MbInstallService mbInstallService;
    @Autowired
    private MbExchangePhoneService mbExchangePhoneService;
    @Autowired
    private MbExchangeCustomService mbExchangeCustomService;
    @Autowired
    private MbExchangeDeviceService mbExchangeDeviceService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private CommonSysDictService dictService;
    @Autowired
    private MbExchangeVerifyEmployeeService verifyEmployeeService;
    @Autowired
    private MbVerifyInstallService verifyInstallService;
    @Autowired
    private MbExchangeEmployeeInfoService employeeInfoService;
    @Autowired
    private CommissionSettleService commissionSettleService;

    private final static String TMPDIR = System.getProperty("java.io.tmpdir");

    public MbExchangeQrcodeVO orderQrcode(Long employeeId) {
        MbExchangeQrcodeVO resultVo = new MbExchangeQrcodeVO();

        Employee employee = employeeService.getById(employeeId);
        if(ObjectUtil.isNull(employee)){
            throw new BusinessException(99999,"员工不存在");
        }

        String fileName = snowflakeIdService.nextId() + ".jpeg";
        File imgFile = new File(TMPDIR + File.separator + fileName);
        QrCodeUtil.generate(snowflakeIdService.nextId().toString(), 300, 300, imgFile);
        String url = fileUploader.upload(imgFile, fileName, "aycx-wechat");
        resultVo.setQrcodeUrl(url);
        return resultVo;
    }

    /**
     * 是否换机模式
     */
    public Boolean existExchange(Long companyId){
        Company company = companyService.getById(companyId);
        return ExchangeOrderTypeEnum.HUAN_JI.getCode().equals(company.getExchangeType());
    }

    @Transactional(rollbackFor = Exception.class)
    public Long oneKeyOrderApply(OnekeyApplyOrderDTO dto){

        if(CollUtil.isEmpty(dto.getPicList())){
            throw new BusinessException(99999,"晒单图片不能为空");
        }

        if(dto.getPicList().size()!=6){
            throw new BusinessException(99999,"晒单图片必须6张");
        }

        Employee employee = employeeService.getById(dto.getEmployeeId());

        //晒图校验
        List<Boolean> checkList = new ArrayList<>();
        for(OnekeyApplyOrderDTO.ApplyPicDTO applyPicDTO:dto.getPicList()){
            checkList.add(checkApply(applyPicDTO));
        }
        long picSize = dto.getPicList().size();
        long num = checkList.stream().filter(e -> e.equals(Boolean.TRUE)).count();

        String [] levels = employee.getAncestors().split(",");

        MbExchangeOrder order = new MbExchangeOrder();
        order.setStoreCompanyId(employee.getCompanyId());
        order.setStoreEmployeeId(employee.getId());
        order.setType(4);
        order.setSource(dto.getSource() != null ? dto.getSource() : 1);
        order.setBdId(Long.valueOf(levels[1]));
        order.setAreaId(Long.valueOf(levels[2]));
        order.setAgentId(employeeService.getAgent(employee.getId()));
        order.setStatus(num>=picSize-1? ExchangeStatus.SYS_PASS.getCode():ExchangeStatus.SYS_Fail.getCode());
        order.setSettleStatus(0);
        this.save(order);
        //保存晒单图片
        List<MbExchangePic> picList = new ArrayList<>();
        for(OnekeyApplyOrderDTO.ApplyPicDTO applyPicDTO:dto.getPicList()){
            MbExchangePic pic = new MbExchangePic();
            pic.setOrderId(order.getId());
            pic.setDid(applyPicDTO.getDid());
            pic.setUid(applyPicDTO.getUid());
            pic.setInstallChannel(applyPicDTO.getChannel());
            pic.setActTime(DateUtils.parseDate(applyPicDTO.getActTime()));
            pic.setImageUrl(applyPicDTO.getImageUrl());
            picList.add(pic);
        }
        mbExchangePicService.saveBatch(picList);
        orderLogService.addLog(employee.getId(),order.getId(),num>=picSize-1? ExchangeStatus.SYS_PASS.getCode():ExchangeStatus.SYS_Fail.getCode(),ExchangeStatus.SYS_PASS.getCode(),"晒单创建","晒单创建");
        return order.getId();
    }

    private Boolean checkApply(OnekeyApplyOrderDTO.ApplyPicDTO dto){

        if(StrUtil.isBlank(dto.getChannel())){
            log.info("{}渠道号为空",dto.getUid());
            return false;
        }

        if(dto.getChannel().contains("新设备")){
            return true;
        }

        boolean checkTime = true;

        String blackTech = dictService.getValueByNameFromStorage("exchange_active_black");
        if(StringUtils.isNotEmpty(blackTech)){
            List<String>  blackManItems  = Arrays.asList( blackTech.split(","));
            if(CollUtil.isNotEmpty(blackManItems) && blackManItems.stream().anyMatch(dto.getChannel()::contains)){
                checkTime = false;
            }
        }

        if(checkTime){
            Date applyDate = DateUtils.parseDate(dto.getActTime());
            if(ObjectUtil.isNull(applyDate)){
                log.info("{}作业时间格式解析错误",dto.getUid());
                return false;
            }
            String nowDate = DateUtils.getDate();
            String applyTime = DateUtils.parseDateToStr("yyyy-MM-dd",applyDate);

            //日期当天判断
            if(!applyTime.equals(nowDate)){
                log.info("{}日期不是当天的",dto.getUid());
                return false;
            }
        }

        //UID和DID判断
        List<MbExchangePic> picList =  mbExchangePicService.list(Wrappers.lambdaQuery(MbExchangePic.class)
                .eq(MbExchangePic::getUid,dto.getUid()).or().eq(MbExchangePic::getDid,dto.getDid()));

        if(CollUtil.isNotEmpty(picList)){
            log.info("{}设备信息已经存在",dto.getUid());
            return false;
        }

        List<String> channelList = mbInstallService.list(Wrappers.lambdaQuery(MbInstall.class).eq(MbInstall::getStatus,1))
                .stream().map(MbInstall::getChannelNo).collect(Collectors.toList());

        boolean contains = channelList.stream().anyMatch(dto.getChannel()::contains);
        if(!contains){
            log.info("{}渠道推广不是安逸",dto.getUid());
            return false;
        }
        return true;
    }

    private Boolean checkExchangeApply(ExchangeApplyOrderDTO dto,MbExchangeOrder order,Employee employee,String employeePhone){
        boolean flag = true;
        StringJoiner remark = new StringJoiner(",");

        log.info("换机订单可提交判断{}",order.getCustomPhone());

        //开机时长判断
        MbExchangeDevice device = mbExchangeDeviceService.lambdaQuery()
                .eq(MbExchangeDevice::getOaid,order.getOaid())
                .last("limit 1").one();

        //大于3小时
        if(ObjectUtil.isNull(device) || ObjectUtil.isNull(device.getOpenTime()) || device.getOpenTime()>10800){
            remark.add("开机时长超出范围");
            flag=false;
        }

        /*if(dto.getPicList().size()!=2 || StrUtil.isBlank(dto.getPicList().get(1).getImeiTwo()) || StrUtil.isBlank(dto.getPicList().get(1).getImei())
                || !dto.getPicList().get(1).getImeiTwo().equals(dto.getPicList().get(1).getImei())){
            remark.add("图二IMEI不匹配");
            flag=false;
        }*/

        order.setRemark(remark.toString());
        order.setSysRemark(remark.toString());
        return flag;
    }


    @Transactional(rollbackFor = Exception.class)
    public Long exchangeOrderApply(ExchangeApplyOrderDTO dto){

        if(CollUtil.isEmpty(dto.getPicList())){
            throw new BusinessException(99999,"晒单图片不能为空");
        }

        Employee employee = employeeService.getById(dto.getEmployeeId());
        if(ObjectUtil.isNull(employee)){
            throw new BusinessException(99999,"员工不存在");
        }

        //店员换机包
        List<Long> phoneIds = mbExchangeEmployeeService.employeePhoneId(employee.getId());
        if(CollUtil.isEmpty(phoneIds)){
            throw new BusinessException(99999,"换机包集合不存在");
        }

        MbExchangePhone exchangePhone = mbExchangePhoneService.getById(phoneIds.get(0));
        if(ObjectUtil.isNull(exchangePhone)){
            throw new BusinessException(99999,"换机包不存在");
        }

        if(StrUtil.isBlank(dto.getOrderSn())){
            //单号识别不到，放行到后台由审单员处理
            return jumpOrderApply(dto,employee,exchangePhone.getChannelNo());
        }

        List<MbExchangeCustom> customInstallList = mbExchangeCustomService.list(Wrappers.lambdaQuery(MbExchangeCustom.class)
                .eq(MbExchangeCustom::getOrderSn,dto.getOrderSn()));

        if(CollUtil.isEmpty(customInstallList)){
            //单号安装记录不存在,放行到后台由审单员处理
            return jumpOrderApply(dto,employee,exchangePhone.getChannelNo());
        }

        String customPhone = customInstallList.get(0).getCustomPhone();
        String employeePhone = customInstallList.get(0).getEmployeePhone();
        String oaid = customInstallList.get(0).getOaid();

        String [] levels = employee.getAncestors().split(",");
        MbExchangeOrder order = new MbExchangeOrder();
        order.setStoreEmployeeId(employee.getId());
        order.setStoreCompanyId(employee.getCompanyId());
        order.setType(3);
        order.setSource(dto.getSource() != null ? dto.getSource() : 1);
        order.setImeiNo(dto.getPicList().size()==2?dto.getPicList().get(1).getImei():null);
        order.setOaid(StrUtil.isNotBlank(oaid)?oaid:"");
        order.setCustomPhone(customPhone);
        order.setBdId(Long.valueOf(levels[1]));
        order.setAreaId(Long.valueOf(levels[2]));
        order.setAgentId(employeeService.getAgent(employee.getId()));
        order.setExchangePhoneNo(exchangePhone.getChannelNo());
        order.setSettleStatus(0);
        //根据合伙人的设置判断是平台审核还是自己审
        MbExchangeEmployeeInfo employeeInfo = employeeInfoService.getByEmployeeId(Long.valueOf(levels[1]));
        order.setPlatCheck(ObjectUtil.isNull(employeeInfo)?true:employeeInfo.getPlatCheck());
        //强规则
        undoCommit(dto,customPhone,oaid,employee,employeePhone,order);
        //若规则
        boolean passFlag = checkExchangeApply(dto,order,employee,employeePhone);
        order.setStatus(passFlag?ExchangeStatus.SYS_PASS.getCode():ExchangeStatus.SYS_Fail.getCode());
        order.setSysStatus(passFlag?ExchangeStatus.SYS_PASS.getCode():ExchangeStatus.SYS_Fail.getCode());
        this.save(order);
        List<MbExchangePic> picList = new ArrayList<>();
        for(ExchangeApplyOrderDTO.ApplyPicDTO picDTO:dto.getPicList()){
            MbExchangePic pic = new MbExchangePic();
            pic.setOrderId(order.getId());
            pic.setImageUrl(picDTO.getImageUrl());
            pic.setImeiNo(picDTO.getImei());
            picList.add(pic);
        }
        mbExchangePicService.saveBatch(picList);
        //更新客户安装记录,回填订单号
        mbExchangeCustomService.lambdaUpdate()
                .set(MbExchangeCustom::getOrderId, order.getId())
                .eq(MbExchangeCustom::getOaid, oaid)
                .update(new MbExchangeCustom());
        //更新客户安装设备记录,回填订单号
        mbExchangeDeviceService.lambdaUpdate()
                .set(MbExchangeDevice::getOrderId,order.getId())
                .eq(MbExchangeDevice::getOaid, oaid)
                .update(new MbExchangeDevice());
        orderLogService.addLog(employee.getId(),order.getId(),passFlag?ExchangeStatus.SYS_PASS.getCode():ExchangeStatus.SYS_Fail.getCode(),ExchangeStatus.SYS_PASS.getCode(),"晒单创建","晒单创建");
        return order.getId();
    }


    /**
     * 验新晒单
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public Long specialExchangeOrderApply(ExchangeApplyOrderDTO dto){

        if(CollUtil.isEmpty(dto.getPicList())){
            throw new BusinessException(99999,"晒单图片不能为空");
        }

        if(ExchangeOrderTypeEnum.LV_ZHOU.getCode().equals(dto.getType()) && dto.getPicList().size()!=2){
            throw new BusinessException(99999,"晒单必须两张图片");
        }
        Employee employee = employeeService.getById(dto.getEmployeeId());

        MbExchangeOrder order = new MbExchangeOrder();

        String typeCode = "";
        if(ExchangeOrderTypeEnum.IPHONE_DOUYIN.getCode().equals(dto.getType())){
            typeCode = VerifyInstallTypeEnum.IPHONE_DOUYIN.getCode();
            checkPgDouyinApply(dto,order,ExchangeOrderTypeEnum.IPHONE_DOUYIN.getCode());
        }else{
            typeCode = VerifyInstallTypeEnum.LV_ZHOU.getCode();
            checkLvZhouApply(dto,order,ExchangeOrderTypeEnum.LV_ZHOU.getCode());
        }

        //店员验新包
        List<MbExchangeVerifyEmployee> verifyEmployeeList = verifyEmployeeService.appCompanyVerify(employee.getId(),typeCode);
        if(CollUtil.isEmpty(verifyEmployeeList)){
            throw new BusinessException(99999,"店员验新包不存在");
        }

        MbVerifyInstall verifyInstall = verifyInstallService.getById(verifyEmployeeList.get(0).getExchangeVerifyId());
        if(verifyInstall.getStatus().intValue()!=1){
            throw new BusinessException(99999,"验新包状态异常");
        }

        String [] levels = employee.getAncestors().split(",");
        boolean passFlag = false;
        order.setStoreEmployeeId(employee.getId());
        order.setStoreCompanyId(employee.getCompanyId());
        order.setType(ObjectUtil.isNull(dto.getType())?ExchangeOrderTypeEnum.LV_ZHOU.getCode():dto.getType());
        order.setSource(dto.getSource() != null ? dto.getSource() : 1);
        order.setBdId(Long.valueOf(levels[1]));
        order.setAreaId(Long.valueOf(levels[2]));
        order.setAgentId(employeeService.getAgent(employee.getId()));
        order.setExchangePhoneNo(verifyInstall.getChannelNo());
        order.setStatus(passFlag?ExchangeStatus.SYS_PASS.getCode():ExchangeStatus.SYS_Fail.getCode());
        order.setSettleStatus(0);
        order.setSysStatus(passFlag?ExchangeStatus.SYS_PASS.getCode():ExchangeStatus.SYS_Fail.getCode());
        order.setPlatCheck(true);
        this.save(order);
        //保存晒单图片
        List<MbExchangePic> picList = new ArrayList<>();
        for(ExchangeApplyOrderDTO.ApplyPicDTO picDTO:dto.getPicList()){
            MbExchangePic pic = new MbExchangePic();
            pic.setUid(picDTO.getUid());
            pic.setDid(picDTO.getDid());
            pic.setOrderId(order.getId());
            pic.setImageUrl(picDTO.getImageUrl());
            picList.add(pic);
        }
        mbExchangePicService.saveBatch(picList);
        orderLogService.addLog(employee.getId(),order.getId(),passFlag?ExchangeStatus.SYS_PASS.getCode():ExchangeStatus.SYS_Fail.getCode(),ExchangeStatus.SYS_PASS.getCode(),"晒单创建","晒单创建");
        return order.getId();
    }


    private void checkLvZhouApply(ExchangeApplyOrderDTO dto,MbExchangeOrder order,Integer type){
        //从第一张图里面取快手号
        String imeiNo = dto.getPicList().get(1).getUid();
        if(StrUtil.isNotBlank(imeiNo)){
            List<MbExchangeOrder> orderList = this.lambdaQuery()
                    .eq(MbExchangeOrder::getType,type)
                    .ne(MbExchangeOrder::getStatus,ExchangeStatus.FAIL.getCode())
                    .eq(MbExchangeOrder::getImeiNo,imeiNo).list();
            if(CollUtil.isNotEmpty(orderList)){
                throw new BusinessException(99999,"快手号已存在晒单");
            }
            order.setImeiNo(imeiNo);
        }
    }

    private void checkPgDouyinApply(ExchangeApplyOrderDTO dto,MbExchangeOrder order,Integer type){
        //从第一张图里面取uid
        String uid = dto.getPicList().get(0).getUid();
        if(StrUtil.isNotBlank(uid)){
            List<MbExchangeOrder> orderList = this.lambdaQuery()
                    .eq(MbExchangeOrder::getType,type)
                    .ne(MbExchangeOrder::getStatus,ExchangeStatus.FAIL.getCode())
                    .eq(MbExchangeOrder::getImeiNo,uid).list();
            if(CollUtil.isNotEmpty(orderList)){
                throw new BusinessException(99999,"苹果抖音已存在晒单");
            }
            order.setImeiNo(uid);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Long jumpOrderApply(ExchangeApplyOrderDTO dto,Employee employee,String channelNo){

        log.info("跳过晒单接受到的订单号:{}",dto.getOrderSn());
        String [] levels = employee.getAncestors().split(",");

        //平台审核还是店铺审核
        //MbExchangeEmployeeInfo employeeInfo = employeeInfoService.getByEmployeeId(Long.valueOf(levels[1]));

        MbExchangeOrder order = new MbExchangeOrder();
        order.setStoreEmployeeId(employee.getId());
        order.setStoreCompanyId(employee.getCompanyId());
        order.setType(3);
        order.setSource(2);
        order.setImeiNo(dto.getPicList().size()==2?dto.getPicList().get(1).getImei():"");
        order.setBdId(Long.valueOf(levels[1]));
        order.setAreaId(Long.valueOf(levels[2]));
        order.setAgentId(employeeService.getAgent(employee.getId()));
        order.setExchangePhoneNo(channelNo);
        order.setSysStatus(ExchangeStatus.SYS_Fail.getCode());
        order.setSysRemark(StrUtil.isBlank(dto.getOrderSn())?"单号识别不到":"单号识别有误");
        order.setStatus(ExchangeStatus.SYS_Fail.getCode());
        order.setSettleStatus(0);
        //平台审核
        order.setPlatCheck(true);
        this.save(order);
        //保存晒单图片
        List<MbExchangePic> picList = new ArrayList<>();
        for(ExchangeApplyOrderDTO.ApplyPicDTO picDTO:dto.getPicList()){
            MbExchangePic pic = new MbExchangePic();
            pic.setOrderId(order.getId());
            pic.setImageUrl(picDTO.getImageUrl());
            pic.setImeiNo(picDTO.getImei());
            picList.add(pic);
        }
        mbExchangePicService.saveBatch(picList);
        orderLogService.addLog(employee.getId(),order.getId(),ExchangeStatus.SYS_Fail.getCode(),ExchangeStatus.SYS_Fail.getCode(),"晒单创建","晒单创建");
        return order.getId();
    }

    private void undoCommit(ExchangeApplyOrderDTO dto,String customPhone,String oaid,Employee employee,String employeePhone,MbExchangeOrder order){

        boolean flag = true;
        StringJoiner remark = new StringJoiner(",");
        List<MbExchangeOrder> customList = this.list(Wrappers.lambdaQuery(MbExchangeOrder.class)
                .eq(MbExchangeOrder::getCustomPhone,customPhone)
                .isNotNull(MbExchangeOrder::getBdId)
                .ne(MbExchangeOrder::getStatus,ExchangeStatus.FAIL.getCode()));

        if(CollUtil.isNotEmpty(customList)){
            throw new BusinessException(99999,"该账户已参加过换机");
        }

        long applyNum = this.count(Wrappers.lambdaQuery(MbExchangeOrder.class)
                .eq(MbExchangeOrder::getStoreEmployeeId,dto.getEmployeeId())
                .isNotNull(MbExchangeOrder::getBdId)
                .eq(MbExchangeOrder::getType,ExchangeOrderTypeEnum.HUAN_JI.getCode())
                .in(MbExchangeOrder::getStatus,
                        Arrays.asList(ExchangeStatus.SYS_Fail.getCode(),ExchangeStatus.TO_AUDIT.getCode(),ExchangeStatus.SYS_PASS.getCode())));

        int downNum = mbExchangeCustomService.list(Wrappers.lambdaQuery(MbExchangeCustom.class)
                .eq(MbExchangeCustom::getStoreEmployeeId,dto.getEmployeeId())
                .groupBy(MbExchangeCustom::getCustomPhone)).size();

        List<MbExchangeCustom> installList = mbExchangeCustomService.list(Wrappers.lambdaQuery(MbExchangeCustom.class)
                .eq(MbExchangeCustom::getCustomPhone,customPhone).orderByAsc(MbExchangeCustom::getCreateTime));

        if(CollUtil.isEmpty(installList)){
            throw new BusinessException(99999,"客户安装记录不存在");
        }

        if(applyNum-downNum>=0){
            throw new BusinessException(99999,"晒单数大于下载数");
        }

        Date applyDate = installList.get(0).getCreateTime();
        String nowDate = DateUtils.getDate();
        String applyTime = DateUtils.parseDateToStr("yyyy-MM-dd",applyDate);

        //日期当天判断
        if(!applyTime.equals(nowDate)){
            throw new BusinessException(99999,"换机日期不是当天的");
        }

        //OAID判断
        List<MbExchangeOrder> oaidList =  this.list(Wrappers.lambdaQuery(MbExchangeOrder.class)
                .eq(MbExchangeOrder::getOaid,oaid)
                .ne(MbExchangeOrder::getStatus,ExchangeStatus.FAIL.getCode()));
        if(CollUtil.isNotEmpty(oaidList)){
            throw new BusinessException(99999,"设备信息已经存在");
        }

        //打开应用市场判断
        if(installList.stream().anyMatch(e -> e.getMarketFlag().intValue()==1)){
            throw new BusinessException(99999,"去应用商店安装改订单无效");
        }

        //晒单员工和下载员工不一致
        if(!employee.getMobileNumber().equals(employeePhone)){
            throw new BusinessException(99999,"晒单员工和下载员工不一致");
        }

        //IMEI判断
        if(StrUtil.isNotBlank(order.getImeiNo())){
            List<MbExchangeOrder> imeiList =  this.list(Wrappers.lambdaQuery(MbExchangeOrder.class)
                    .eq(MbExchangeOrder::getImeiNo,order.getImeiNo())
                    .ne(MbExchangeOrder::getStatus,ExchangeStatus.FAIL.getCode()));
            if(CollUtil.isNotEmpty(imeiList)){
                throw new BusinessException(99999,"该设备已参加过换机");
            }
        }

        //打开换机助手时间和做单时间差1个小时以上
        MbExchangeDevice device = mbExchangeDeviceService.lambdaQuery()
                .eq(MbExchangeDevice::getOaid,order.getOaid())
                .last("limit 1").one();

        //大于1小时
        if(ObjectUtil.isNull(device) || DateUtils.differentMinuteByMillisecond(device.getCreateTime(),installList.get(0).getCreateTime())>60){
            throw new BusinessException(99999,"做单耗时超过规定时间");
        }

    }



    @Transactional(rollbackFor = Exception.class)
    public void check(ExchangePhoneVerifyDTO dto){
        MbExchangeOrder order = this.lambdaQuery()
                .eq(MbExchangeOrder::getId, dto.getId())
                .last("for update")
                .one();
        if(!Arrays.asList(ExchangeStatus.TO_AUDIT.getCode(),ExchangeStatus.SYS_PASS.getCode(),ExchangeStatus.SYS_Fail.getCode())
                .contains(order.getStatus())){
            throw new BusinessException(99999,"订单不是待审核状态");
        }

        CommissionPackage commissionPackage = getCommissionPackage(order.getType());
        EmployAccountChangeEnum employAccountChangeEnum = getAccountChangeEnum(order.getType());

        if(dto.getStatus().equals(ExchangeOrderLogStatus.PASS.getCode())){
            order.setStatus(ExchangeStatus.PASS.getCode());
            order.setSettleStatus(ExchangeSettleStatus.PASS.getCode());
            order.setTrialTime(new Date());
            order.setIllegal(order.getSysStatus().equals(ExchangeStatus.SYS_Fail.getCode())?true:false);
            this.updateById(order);
            orderLogService.addLog(order.getStoreEmployeeId(),order.getId(),ExchangeStatus.PASS.getCode(), ExchangeOrderLogStatus.PASS.getCode(),dto.getReason(),dto.getRemark());
            //拉新绑定订单
            commissionSettleService.orderBindSettleRule(dto.getId(), CommissionBizType.APP_NEW, commissionPackage.getType(),order.getStoreEmployeeId());
            commissionSettleService.waitSettleOrder(dto.getId(), CommissionBizType.APP_NEW, commissionPackage.getType(),order.getStoreEmployeeId(),null,employAccountChangeEnum.getRemark());
            commissionSettleService.settleOrder(dto.getId(), CommissionBizType.APP_NEW, commissionPackage.getType(),employAccountChangeEnum,employAccountChangeEnum.getRemark());
        }

        if(dto.getStatus().equals(ExchangeOrderLogStatus.FAIL.getCode())){
            order.setStatus(ExchangeStatus.FAIL.getCode());
            order.setTrialTime(new Date());
            this.updateById(order);
            // orderLogService.addLog(ShiroUtils.getUserId(),order.getId(),ExchangeStatus.FAIL.getCode(),ExchangeOrderLogStatus.FAIL.getCode(),dto.getReason(),dto.getRemark());
            orderLogService.addLog(
                    order.getStoreEmployeeId(),
                    order.getId(),
                    ExchangeStatus.FAIL.getCode(),ExchangeOrderLogStatus.FAIL.getCode(),
                    ExchangeOrderLogStatus.FAIL.getName(),
                    StrUtil.format("{} {}", dto.getReason(), dto.getRemark()));
        }

    }

    private CommissionPackage getCommissionPackage(Integer orderType){
        CommissionPackage commissionPackage = null;
        switch (EnumUtil.getBy(ExchangeOrderTypeEnum::getCode, orderType)) {
            case HUAN_JI: {
                commissionPackage = CommissionPackage.HUAN_JI;
                break;
            }
            case LV_ZHOU: {
                commissionPackage = CommissionPackage.KUAI_SHOU_LV_SHOU;
                break;
            }
            case IPHONE_DOUYIN: {
                commissionPackage = CommissionPackage.IPHONE_DOUYIN;
                break;
            }
            default: {
                commissionPackage = CommissionPackage.YJLX;
                break;
            }
        }
        return commissionPackage;
    }

    private EmployAccountChangeEnum getAccountChangeEnum(Integer orderType){
        EmployAccountChangeEnum employAccountChangeEnum = null;
        switch (EnumUtil.getBy(ExchangeOrderTypeEnum::getCode, orderType)) {
            case HUAN_JI: {
                employAccountChangeEnum = EmployAccountChangeEnum.app_lx_hjzs;
                break;
            }
            case LV_ZHOU: {
                employAccountChangeEnum = EmployAccountChangeEnum.app_lx_kslz;
                break;
            }
            case IPHONE_DOUYIN: {
                employAccountChangeEnum = EmployAccountChangeEnum.app_lx_pgdy;
                break;
            }
            default: {
                employAccountChangeEnum = EmployAccountChangeEnum.app_lx_yjlx;
                break;
            }
        }
        return employAccountChangeEnum;
    }

    /**
     * 三方渠道验新晒单
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void partnerApply(ExchangePartnerApplyDTO dto){

        if(StrUtil.isBlank(dto.getChannelCode())){
            throw new BusinessException(99999,"渠道码不能为空");
        }

        if(!EnumUtil.contains(PartnerChanelEnum.class,dto.getChannelCode())){
            throw new BusinessException(99999,"非合作渠道");
        }

        if(StrUtil.isBlank(dto.getChannelOrderNo())){
            throw new BusinessException(99999,"渠道订单号不能为空");
        }

        if(CollUtil.isEmpty(dto.getPicList())){
            throw new BusinessException(99999,"晒单图片不能为空");
        }

        if(ExchangeOrderTypeEnum.LV_ZHOU.getCode().equals(dto.getType()) && dto.getPicList().size()!=2){
            throw new BusinessException(99999,"晒单必须两张图片");
        }

        existOrder(dto);

        Employee employee = employeeService.getById(dto.getEmployeeId());

        MbExchangeOrder order = new MbExchangeOrder();

        String typeCode = "";
        if(ExchangeOrderTypeEnum.IPHONE_DOUYIN.getCode().equals(dto.getType())){
            typeCode = VerifyInstallTypeEnum.IPHONE_DOUYIN.getCode();
        }else{
            typeCode = VerifyInstallTypeEnum.LV_ZHOU.getCode();
        }

        //店员验新包
        List<MbExchangeVerifyEmployee> verifyEmployeeList = verifyEmployeeService.appCompanyVerify(employee.getId(),typeCode);
        if(CollUtil.isEmpty(verifyEmployeeList)){
            throw new BusinessException(99999,"店员验新包不存在");
        }

        MbVerifyInstall verifyInstall = verifyInstallService.getById(verifyEmployeeList.get(0).getExchangeVerifyId());
        if(verifyInstall.getStatus().intValue()!=1){
            throw new BusinessException(99999,"验新包状态异常");
        }

        String [] levels = employee.getAncestors().split(",");
        order.setStoreEmployeeId(employee.getId());
        order.setStoreCompanyId(employee.getCompanyId());
        order.setType(ObjectUtil.isNull(dto.getType())?ExchangeOrderTypeEnum.LV_ZHOU.getCode():dto.getType());
        order.setSource(dto.getSource() != null ? dto.getSource() : 1);
        order.setBdId(Long.valueOf(levels[1]));
        order.setAreaId(Long.valueOf(levels[2]));
        order.setAgentId(employeeService.getAgent(employee.getId()));
        order.setExchangePhoneNo(verifyInstall.getChannelNo());
        order.setStatus(ExchangeStatus.SYS_Fail.getCode());
        order.setSettleStatus(0);
        order.setSysStatus(ExchangeStatus.SYS_Fail.getCode());
        order.setPlatCheck(true);
        order.setChannelCode(dto.getChannelCode());
        order.setChannelName(EnumUtil.getBy(PartnerChanelEnum::getCode,dto.getChannelCode()).getDesc());
        order.setChannelOrderNo(dto.getChannelOrderNo());
        this.save(order);
        //保存晒单图片
        List<MbExchangePic> picList = new ArrayList<>();
        for(ExchangePartnerApplyDTO.ApplyPicDTO picDTO:dto.getPicList()){
            MbExchangePic pic = new MbExchangePic();
            pic.setOrderId(order.getId());
            pic.setImageUrl(picDTO.getImageUrl());
            picList.add(pic);
        }
        mbExchangePicService.saveBatch(picList);
        orderLogService.addLog(employee.getId(),order.getId(),ExchangeStatus.SYS_Fail.getCode(),ExchangeStatus.SYS_Fail.getCode(),"晒单创建","晒单创建");
    }

    /**
     * 三方渠道查看订单状态
     */
    public MbPartnerOrderQueryVO partnerOrderQuery(MbPartnerOrderQueryReq req){

        MbPartnerOrderQueryVO resultVo = new MbPartnerOrderQueryVO();
        List<MbPartnerOrderQueryVO.orderStatus> resultList = new ArrayList<>();

        if(CollUtil.isEmpty(req.getChannelOrderNo()) || req.getChannelOrderNo().size()>5000){
            throw new BusinessException(99999,"订单号不存在或超出限制范围");
        }

        List<MbExchangeOrder> orderList = this.lambdaQuery()
                .eq(MbExchangeOrder::getChannelCode,req.getChannelCode())
                .in(MbExchangeOrder::getChannelOrderNo,req.getChannelOrderNo()).list();
        if(ObjectUtil.isNull(orderList)){
            throw new BusinessException(99999,"订单不存在");
        }



        for(MbExchangeOrder order:orderList){
            MbPartnerOrderQueryVO.orderStatus orderDetail = new  MbPartnerOrderQueryVO.orderStatus();
            orderDetail.setStatus(order.getStatus());
            orderDetail.setRemark(StrUtil.isBlank(order.getRemark())?"":order.getRemark());
            orderDetail.setChannelOrderNo(StrUtil.isBlank(order.getChannelOrderNo())?"":order.getChannelOrderNo());
            resultList.add(orderDetail);
        }
        resultVo.setOrderList(resultList);
        return resultVo;
    }

    private void existOrder(ExchangePartnerApplyDTO dto){
        List<MbExchangeOrder> orderList = this.lambdaQuery()
                .eq(MbExchangeOrder::getChannelCode,dto.getChannelCode())
                .eq(MbExchangeOrder::getChannelOrderNo,dto.getChannelOrderNo()).list();
        if(CollUtil.isNotEmpty(orderList)){
            throw new BusinessException(99999,"订单已存在");
        }
    }
}