package com.anyi.sparrow.organize.employee.dao.mapper;

public interface ExtEmpCfgMapper {
    String getMax();

    String getByBankUserId(String bankUserId);
}