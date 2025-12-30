package com.anyi.common.address.mapper;


import com.anyi.common.address.domain.AddressExample;
import com.anyi.common.address.domain.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class AddressDao {
    @Autowired
    private AddressMapper mapper;

    public List<Address> selectByEmpId(Long empId) {
        AddressExample example = new AddressExample();
        example.createCriteria().andEmpIdEqualTo(empId);
        example.setOrderByClause(" update_time desc");
        return mapper.selectByExample(example);
    }

    public List<Address> selectByEmpIdAndSearch(Long empId, String searchKey) {
//        AddressExample example = new AddressExample();
//        example.createCriteria().andEmpIdEqualTo(empId).andContactLike(searchKey);
//        example.setOrderByClause(" update_time desc");

        return mapper.selectByEmpIdAndSearchKey(empId, searchKey);
//        return mapper.selectByExample(example);
    }

    public Address selectById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    public int insert(Address record) {
        Date now = new Date();
        record.setCreateTime(now);
        record.setUpdateTime(now);
        return mapper.insertSelective(record);
    }

    public int update(Address record) {
        record.setUpdateTime(new Date());
        return mapper.updateByPrimaryKeySelective(record);
    }

    public int delete(Long addressId) {
        return mapper.deleteByPrimaryKey(addressId);
    }
}
