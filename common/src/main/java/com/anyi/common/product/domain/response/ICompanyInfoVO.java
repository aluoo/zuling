package com.anyi.common.product.domain.response;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.company.domain.Company;

import java.util.Map;
import java.util.Optional;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/14
 * @Copyright
 * @Version 1.0
 */
public interface ICompanyInfoVO {
    Long getStoreCompanyId();
    void setStoreCompanyId(Long storeCompanyId);
    Long getRecyclerCompanyId();
    void setRecyclerCompanyId(Long recyclerCompanyId);
    String getStoreCompanyName();
    String getRecyclerCompanyName();
    void setRecyclerCompanyName(String recyclerCompanyName);
    void setStoreCompanyName(String storeCompanyName);

    default void setCompanyInfo(Map<Long, Company> map) {
        if (CollUtil.isEmpty(map)) {
            return;
        }
        Optional<Company> storeCompany = Optional.empty();
        Optional<Company> recyclerCompany = Optional.empty();
        if (this.getStoreCompanyId() != null) {
            storeCompany = Optional.ofNullable(map.get(this.getStoreCompanyId()));
        }
        if (this.getRecyclerCompanyId() != null) {
            recyclerCompany = Optional.ofNullable(map.get(this.getRecyclerCompanyId()));
        }
        String storeCompanyName = storeCompany.map(Company::getName).orElse(null);
        String recyclerCompanyName = recyclerCompany.map(Company::getName).orElse(null);
        this.setStoreCompanyName(storeCompanyName);
        this.setRecyclerCompanyName(recyclerCompanyName);
    }
}