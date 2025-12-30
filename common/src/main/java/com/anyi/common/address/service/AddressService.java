package com.anyi.common.address.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.address.vo.AddrSearchReq;
import com.anyi.common.address.vo.AddressVO;
import com.anyi.common.address.vo.DeleteReq;
import com.anyi.common.address.vo.GetLatestReq;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.address.mapper.AddressDao;
import com.anyi.common.address.mapper.LatestAddressDao;
import com.anyi.common.address.domain.Address;
import com.anyi.common.address.domain.LatestUseAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressDao addressDao;

    @Autowired
    private LatestAddressDao latestAddressDao;

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    public List<AddressVO> getAddressList(Long employeeId) {
        List<Address> addressList = addressDao.selectByEmpId(employeeId);
        List<AddressVO> voList = new ArrayList<>();
        if (CollUtil.isNotEmpty(addressList)) {
            for (Address a : addressList) {
                AddressVO vo = new AddressVO();
                BeanUtils.copyProperties(a, vo);
                setAddressVO(a, vo);
                voList.add(vo);
            }
        }
        return voList;
    }

    public List<AddressVO> getAddressList(AddrSearchReq addrSearchReq, Long employeeId) {
        List<Address> addressList = null;
        if (StringUtils.isNotEmpty(addrSearchReq.getSearchKey()) && StringUtils.isNotBlank(addrSearchReq.getSearchKey())) {
            addressList = addressDao.selectByEmpIdAndSearch(employeeId, addrSearchReq.getSearchKey());
        } else {
            addressList = addressDao.selectByEmpId(employeeId);
        }
        List<AddressVO> voList = new ArrayList<>();
        if (CollUtil.isNotEmpty(addressList)) {
            for (Address a : addressList) {
                AddressVO vo = new AddressVO();
                BeanUtils.copyProperties(a, vo);
                setAddressVO(a, vo);
                voList.add(vo);
            }
        }
        return voList;
    }

    public void add(AddressVO addressVO, Long employeeId, String name) {
        Address address = new Address();
        BeanUtils.copyProperties(addressVO, address);
        address.setId(snowflakeIdService.nextId());
        address.setEmpId(employeeId);
        address.setCreator(name);
        address.setUpdator(name);
        setAddress(address, addressVO);
        addressDao.insert(address);
    }

    public void update(AddressVO addressVO, Long employeeId, String name) {
        Address address = new Address();
        BeanUtils.copyProperties(addressVO, address);
        address.setCreator(name);
        address.setUpdator(name);
        setAddress(address, addressVO);
        addressDao.update(address);
    }

    public void setAddress(Address address, AddressVO addressVO) {
        if (StringUtils.isEmpty(addressVO.getProvince())
                || StringUtils.isEmpty(addressVO.getCity()) || StringUtils.isEmpty(addressVO.getRegion())) {
            throw new BusinessException(BizError.ADDRESS_INCORRECT);
        }
        address.setAddress(addressVO.getProvince() + "," + addressVO.getCity() + "," + addressVO.getRegion());
//        if(StringUtils.isNotEmpty(addressVO.getProvince())
//                && StringUtils.isNotEmpty(addressVO.getCity()) && StringUtils.isNotEmpty(addressVO.getRegion())) {
//            address.setAddress(addressVO.getProvince() + "," + addressVO.getCity() + "," + addressVO.getRegion());
//        }
    }

    public void setAddressVO(Address address, AddressVO addressVO) {
        if (addressVO.getAddress().contains(",")) {
            String pcr = address.getAddress();
            String[] arr = pcr.split(",");
            addressVO.setProvince(arr[0]);
            addressVO.setCity(arr[1]);
            addressVO.setRegion(arr[2]);
        }
        if (StringUtils.isNotEmpty(address.getAddress())) {
            addressVO.setAddress(address.getAddress().replaceAll(",", ""));
        }
    }

    /**
     * 更新最近使用地址
     *
     * @param useAddress
     */
    public void updateLatestUse(LatestUseAddress useAddress) {
        if (latestAddressDao.update(useAddress) < 1) {
            useAddress.setId(snowflakeIdService.nextId());
            latestAddressDao.insert(useAddress);
        }
    }

    /**
     * 获取最新使用地址
     *
     * @param req
     * @return
     */
    public AddressVO getLatestUse(GetLatestReq req, Long employeeId) {
        long reqEmpId = employeeId;
        long rspEmpId = req.getRspEmpId();
        LatestUseAddress latest = latestAddressDao.getLatest(
                req.getBizType().getCode(), reqEmpId, rspEmpId);
        if (latest != null) {
            Address address = addressDao.selectById(latest.getAddressId());
            if (address != null) {
                AddressVO addressVO = new AddressVO();
                BeanUtils.copyProperties(address, addressVO);
                setAddressVO(address, addressVO);
                return addressVO;
            }
        }
        return null;
    }

    /**
     * 根据id获取地址
     *
     * @return
     */
    public AddressVO getById(Long addressId) {
        Address address = addressDao.selectById(addressId);
        if (address != null) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(address, addressVO);
            setAddressVO(address, addressVO);
            return addressVO;
        }
        return null;
    }

    public AddressVO getByIdNoFormat(Long addressId) {
        Address address = addressDao.selectById(addressId);
        if (address != null) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(address, addressVO);
            return addressVO;
        }
        return null;
    }

    public void delete(DeleteReq req) {
        addressDao.delete(req.getAddressId());
    }
}
