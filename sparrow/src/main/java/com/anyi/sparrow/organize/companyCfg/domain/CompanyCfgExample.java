package com.anyi.sparrow.organize.companyCfg.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompanyCfgExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CompanyCfgExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIsNull() {
            addCriterion("company_id is null");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIsNotNull() {
            addCriterion("company_id is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyIdEqualTo(Long value) {
            addCriterion("company_id =", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotEqualTo(Long value) {
            addCriterion("company_id <>", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdGreaterThan(Long value) {
            addCriterion("company_id >", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdGreaterThanOrEqualTo(Long value) {
            addCriterion("company_id >=", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdLessThan(Long value) {
            addCriterion("company_id <", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdLessThanOrEqualTo(Long value) {
            addCriterion("company_id <=", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIn(List<Long> values) {
            addCriterion("company_id in", values, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotIn(List<Long> values) {
            addCriterion("company_id not in", values, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdBetween(Long value1, Long value2) {
            addCriterion("company_id between", value1, value2, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotBetween(Long value1, Long value2) {
            addCriterion("company_id not between", value1, value2, "companyId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNull() {
            addCriterion("operator_id is null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNotNull() {
            addCriterion("operator_id is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdEqualTo(String value) {
            addCriterion("operator_id =", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotEqualTo(String value) {
            addCriterion("operator_id <>", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThan(String value) {
            addCriterion("operator_id >", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThanOrEqualTo(String value) {
            addCriterion("operator_id >=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThan(String value) {
            addCriterion("operator_id <", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThanOrEqualTo(String value) {
            addCriterion("operator_id <=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLike(String value) {
            addCriterion("operator_id like", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotLike(String value) {
            addCriterion("operator_id not like", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIn(List<String> values) {
            addCriterion("operator_id in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotIn(List<String> values) {
            addCriterion("operator_id not in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdBetween(String value1, String value2) {
            addCriterion("operator_id between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotBetween(String value1, String value2) {
            addCriterion("operator_id not between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andWxEtcTypeIdIsNull() {
            addCriterion("wx_etc_type_id is null");
            return (Criteria) this;
        }

        public Criteria andWxEtcTypeIdIsNotNull() {
            addCriterion("wx_etc_type_id is not null");
            return (Criteria) this;
        }

        public Criteria andWxEtcTypeIdEqualTo(Long value) {
            addCriterion("wx_etc_type_id =", value, "wxEtcTypeId");
            return (Criteria) this;
        }

        public Criteria andWxEtcTypeIdNotEqualTo(Long value) {
            addCriterion("wx_etc_type_id <>", value, "wxEtcTypeId");
            return (Criteria) this;
        }

        public Criteria andWxEtcTypeIdGreaterThan(Long value) {
            addCriterion("wx_etc_type_id >", value, "wxEtcTypeId");
            return (Criteria) this;
        }

        public Criteria andWxEtcTypeIdGreaterThanOrEqualTo(Long value) {
            addCriterion("wx_etc_type_id >=", value, "wxEtcTypeId");
            return (Criteria) this;
        }

        public Criteria andWxEtcTypeIdLessThan(Long value) {
            addCriterion("wx_etc_type_id <", value, "wxEtcTypeId");
            return (Criteria) this;
        }

        public Criteria andWxEtcTypeIdLessThanOrEqualTo(Long value) {
            addCriterion("wx_etc_type_id <=", value, "wxEtcTypeId");
            return (Criteria) this;
        }

        public Criteria andWxEtcTypeIdIn(List<Long> values) {
            addCriterion("wx_etc_type_id in", values, "wxEtcTypeId");
            return (Criteria) this;
        }

        public Criteria andWxEtcTypeIdNotIn(List<Long> values) {
            addCriterion("wx_etc_type_id not in", values, "wxEtcTypeId");
            return (Criteria) this;
        }

        public Criteria andWxEtcTypeIdBetween(Long value1, Long value2) {
            addCriterion("wx_etc_type_id between", value1, value2, "wxEtcTypeId");
            return (Criteria) this;
        }

        public Criteria andWxEtcTypeIdNotBetween(Long value1, Long value2) {
            addCriterion("wx_etc_type_id not between", value1, value2, "wxEtcTypeId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andEtcTypeIdIsNull() {
            addCriterion("etc_type_id is null");
            return (Criteria) this;
        }

        public Criteria andEtcTypeIdIsNotNull() {
            addCriterion("etc_type_id is not null");
            return (Criteria) this;
        }

        public Criteria andEtcTypeIdEqualTo(Long value) {
            addCriterion("etc_type_id =", value, "etcTypeId");
            return (Criteria) this;
        }

        public Criteria andEtcTypeIdNotEqualTo(Long value) {
            addCriterion("etc_type_id <>", value, "etcTypeId");
            return (Criteria) this;
        }

        public Criteria andEtcTypeIdGreaterThan(Long value) {
            addCriterion("etc_type_id >", value, "etcTypeId");
            return (Criteria) this;
        }

        public Criteria andEtcTypeIdGreaterThanOrEqualTo(Long value) {
            addCriterion("etc_type_id >=", value, "etcTypeId");
            return (Criteria) this;
        }

        public Criteria andEtcTypeIdLessThan(Long value) {
            addCriterion("etc_type_id <", value, "etcTypeId");
            return (Criteria) this;
        }

        public Criteria andEtcTypeIdLessThanOrEqualTo(Long value) {
            addCriterion("etc_type_id <=", value, "etcTypeId");
            return (Criteria) this;
        }

        public Criteria andEtcTypeIdIn(List<Long> values) {
            addCriterion("etc_type_id in", values, "etcTypeId");
            return (Criteria) this;
        }

        public Criteria andEtcTypeIdNotIn(List<Long> values) {
            addCriterion("etc_type_id not in", values, "etcTypeId");
            return (Criteria) this;
        }

        public Criteria andEtcTypeIdBetween(Long value1, Long value2) {
            addCriterion("etc_type_id between", value1, value2, "etcTypeId");
            return (Criteria) this;
        }

        public Criteria andEtcTypeIdNotBetween(Long value1, Long value2) {
            addCriterion("etc_type_id not between", value1, value2, "etcTypeId");
            return (Criteria) this;
        }

        public Criteria andQlNumberIsNull() {
            addCriterion("ql_number is null");
            return (Criteria) this;
        }

        public Criteria andQlNumberIsNotNull() {
            addCriterion("ql_number is not null");
            return (Criteria) this;
        }

        public Criteria andQlNumberEqualTo(String value) {
            addCriterion("ql_number =", value, "qlNumber");
            return (Criteria) this;
        }

        public Criteria andQlNumberNotEqualTo(String value) {
            addCriterion("ql_number <>", value, "qlNumber");
            return (Criteria) this;
        }

        public Criteria andQlNumberGreaterThan(String value) {
            addCriterion("ql_number >", value, "qlNumber");
            return (Criteria) this;
        }

        public Criteria andQlNumberGreaterThanOrEqualTo(String value) {
            addCriterion("ql_number >=", value, "qlNumber");
            return (Criteria) this;
        }

        public Criteria andQlNumberLessThan(String value) {
            addCriterion("ql_number <", value, "qlNumber");
            return (Criteria) this;
        }

        public Criteria andQlNumberLessThanOrEqualTo(String value) {
            addCriterion("ql_number <=", value, "qlNumber");
            return (Criteria) this;
        }

        public Criteria andQlNumberLike(String value) {
            addCriterion("ql_number like", value, "qlNumber");
            return (Criteria) this;
        }

        public Criteria andQlNumberNotLike(String value) {
            addCriterion("ql_number not like", value, "qlNumber");
            return (Criteria) this;
        }

        public Criteria andQlNumberIn(List<String> values) {
            addCriterion("ql_number in", values, "qlNumber");
            return (Criteria) this;
        }

        public Criteria andQlNumberNotIn(List<String> values) {
            addCriterion("ql_number not in", values, "qlNumber");
            return (Criteria) this;
        }

        public Criteria andQlNumberBetween(String value1, String value2) {
            addCriterion("ql_number between", value1, value2, "qlNumber");
            return (Criteria) this;
        }

        public Criteria andQlNumberNotBetween(String value1, String value2) {
            addCriterion("ql_number not between", value1, value2, "qlNumber");
            return (Criteria) this;
        }

        public Criteria andSkipLicIsNull() {
            addCriterion("skip_lic is null");
            return (Criteria) this;
        }

        public Criteria andSkipLicIsNotNull() {
            addCriterion("skip_lic is not null");
            return (Criteria) this;
        }

        public Criteria andSkipLicEqualTo(Integer value) {
            addCriterion("skip_lic =", value, "skipLic");
            return (Criteria) this;
        }

        public Criteria andSkipLicNotEqualTo(Integer value) {
            addCriterion("skip_lic <>", value, "skipLic");
            return (Criteria) this;
        }

        public Criteria andSkipLicGreaterThan(Integer value) {
            addCriterion("skip_lic >", value, "skipLic");
            return (Criteria) this;
        }

        public Criteria andSkipLicGreaterThanOrEqualTo(Integer value) {
            addCriterion("skip_lic >=", value, "skipLic");
            return (Criteria) this;
        }

        public Criteria andSkipLicLessThan(Integer value) {
            addCriterion("skip_lic <", value, "skipLic");
            return (Criteria) this;
        }

        public Criteria andSkipLicLessThanOrEqualTo(Integer value) {
            addCriterion("skip_lic <=", value, "skipLic");
            return (Criteria) this;
        }

        public Criteria andSkipLicIn(List<Integer> values) {
            addCriterion("skip_lic in", values, "skipLic");
            return (Criteria) this;
        }

        public Criteria andSkipLicNotIn(List<Integer> values) {
            addCriterion("skip_lic not in", values, "skipLic");
            return (Criteria) this;
        }

        public Criteria andSkipLicBetween(Integer value1, Integer value2) {
            addCriterion("skip_lic between", value1, value2, "skipLic");
            return (Criteria) this;
        }

        public Criteria andSkipLicNotBetween(Integer value1, Integer value2) {
            addCriterion("skip_lic not between", value1, value2, "skipLic");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLikeInsensitive(String value) {
            addCriterion("upper(operator_id) like", value.toUpperCase(), "operatorId");
            return (Criteria) this;
        }

        public Criteria andQlNumberLikeInsensitive(String value) {
            addCriterion("upper(ql_number) like", value.toUpperCase(), "qlNumber");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}