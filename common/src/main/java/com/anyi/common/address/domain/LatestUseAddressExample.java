package com.anyi.common.address.domain;

import java.util.ArrayList;
import java.util.List;

public class LatestUseAddressExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LatestUseAddressExample() {
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

        public Criteria andBizIsNull() {
            addCriterion("biz is null");
            return (Criteria) this;
        }

        public Criteria andBizIsNotNull() {
            addCriterion("biz is not null");
            return (Criteria) this;
        }

        public Criteria andBizEqualTo(String value) {
            addCriterion("biz =", value, "biz");
            return (Criteria) this;
        }

        public Criteria andBizNotEqualTo(String value) {
            addCriterion("biz <>", value, "biz");
            return (Criteria) this;
        }

        public Criteria andBizGreaterThan(String value) {
            addCriterion("biz >", value, "biz");
            return (Criteria) this;
        }

        public Criteria andBizGreaterThanOrEqualTo(String value) {
            addCriterion("biz >=", value, "biz");
            return (Criteria) this;
        }

        public Criteria andBizLessThan(String value) {
            addCriterion("biz <", value, "biz");
            return (Criteria) this;
        }

        public Criteria andBizLessThanOrEqualTo(String value) {
            addCriterion("biz <=", value, "biz");
            return (Criteria) this;
        }

        public Criteria andBizLike(String value) {
            addCriterion("biz like", value, "biz");
            return (Criteria) this;
        }

        public Criteria andBizNotLike(String value) {
            addCriterion("biz not like", value, "biz");
            return (Criteria) this;
        }

        public Criteria andBizIn(List<String> values) {
            addCriterion("biz in", values, "biz");
            return (Criteria) this;
        }

        public Criteria andBizNotIn(List<String> values) {
            addCriterion("biz not in", values, "biz");
            return (Criteria) this;
        }

        public Criteria andBizBetween(String value1, String value2) {
            addCriterion("biz between", value1, value2, "biz");
            return (Criteria) this;
        }

        public Criteria andBizNotBetween(String value1, String value2) {
            addCriterion("biz not between", value1, value2, "biz");
            return (Criteria) this;
        }

        public Criteria andAddressIdIsNull() {
            addCriterion("address_id is null");
            return (Criteria) this;
        }

        public Criteria andAddressIdIsNotNull() {
            addCriterion("address_id is not null");
            return (Criteria) this;
        }

        public Criteria andAddressIdEqualTo(Long value) {
            addCriterion("address_id =", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotEqualTo(Long value) {
            addCriterion("address_id <>", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdGreaterThan(Long value) {
            addCriterion("address_id >", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdGreaterThanOrEqualTo(Long value) {
            addCriterion("address_id >=", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdLessThan(Long value) {
            addCriterion("address_id <", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdLessThanOrEqualTo(Long value) {
            addCriterion("address_id <=", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdIn(List<Long> values) {
            addCriterion("address_id in", values, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotIn(List<Long> values) {
            addCriterion("address_id not in", values, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdBetween(Long value1, Long value2) {
            addCriterion("address_id between", value1, value2, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotBetween(Long value1, Long value2) {
            addCriterion("address_id not between", value1, value2, "addressId");
            return (Criteria) this;
        }

        public Criteria andReqEmpIdIsNull() {
            addCriterion("req_emp_id is null");
            return (Criteria) this;
        }

        public Criteria andReqEmpIdIsNotNull() {
            addCriterion("req_emp_id is not null");
            return (Criteria) this;
        }

        public Criteria andReqEmpIdEqualTo(Long value) {
            addCriterion("req_emp_id =", value, "reqEmpId");
            return (Criteria) this;
        }

        public Criteria andReqEmpIdNotEqualTo(Long value) {
            addCriterion("req_emp_id <>", value, "reqEmpId");
            return (Criteria) this;
        }

        public Criteria andReqEmpIdGreaterThan(Long value) {
            addCriterion("req_emp_id >", value, "reqEmpId");
            return (Criteria) this;
        }

        public Criteria andReqEmpIdGreaterThanOrEqualTo(Long value) {
            addCriterion("req_emp_id >=", value, "reqEmpId");
            return (Criteria) this;
        }

        public Criteria andReqEmpIdLessThan(Long value) {
            addCriterion("req_emp_id <", value, "reqEmpId");
            return (Criteria) this;
        }

        public Criteria andReqEmpIdLessThanOrEqualTo(Long value) {
            addCriterion("req_emp_id <=", value, "reqEmpId");
            return (Criteria) this;
        }

        public Criteria andReqEmpIdIn(List<Long> values) {
            addCriterion("req_emp_id in", values, "reqEmpId");
            return (Criteria) this;
        }

        public Criteria andReqEmpIdNotIn(List<Long> values) {
            addCriterion("req_emp_id not in", values, "reqEmpId");
            return (Criteria) this;
        }

        public Criteria andReqEmpIdBetween(Long value1, Long value2) {
            addCriterion("req_emp_id between", value1, value2, "reqEmpId");
            return (Criteria) this;
        }

        public Criteria andReqEmpIdNotBetween(Long value1, Long value2) {
            addCriterion("req_emp_id not between", value1, value2, "reqEmpId");
            return (Criteria) this;
        }

        public Criteria andRspEmpIdIsNull() {
            addCriterion("rsp_emp_id is null");
            return (Criteria) this;
        }

        public Criteria andRspEmpIdIsNotNull() {
            addCriterion("rsp_emp_id is not null");
            return (Criteria) this;
        }

        public Criteria andRspEmpIdEqualTo(Long value) {
            addCriterion("rsp_emp_id =", value, "rspEmpId");
            return (Criteria) this;
        }

        public Criteria andRspEmpIdNotEqualTo(Long value) {
            addCriterion("rsp_emp_id <>", value, "rspEmpId");
            return (Criteria) this;
        }

        public Criteria andRspEmpIdGreaterThan(Long value) {
            addCriterion("rsp_emp_id >", value, "rspEmpId");
            return (Criteria) this;
        }

        public Criteria andRspEmpIdGreaterThanOrEqualTo(Long value) {
            addCriterion("rsp_emp_id >=", value, "rspEmpId");
            return (Criteria) this;
        }

        public Criteria andRspEmpIdLessThan(Long value) {
            addCriterion("rsp_emp_id <", value, "rspEmpId");
            return (Criteria) this;
        }

        public Criteria andRspEmpIdLessThanOrEqualTo(Long value) {
            addCriterion("rsp_emp_id <=", value, "rspEmpId");
            return (Criteria) this;
        }

        public Criteria andRspEmpIdIn(List<Long> values) {
            addCriterion("rsp_emp_id in", values, "rspEmpId");
            return (Criteria) this;
        }

        public Criteria andRspEmpIdNotIn(List<Long> values) {
            addCriterion("rsp_emp_id not in", values, "rspEmpId");
            return (Criteria) this;
        }

        public Criteria andRspEmpIdBetween(Long value1, Long value2) {
            addCriterion("rsp_emp_id between", value1, value2, "rspEmpId");
            return (Criteria) this;
        }

        public Criteria andRspEmpIdNotBetween(Long value1, Long value2) {
            addCriterion("rsp_emp_id not between", value1, value2, "rspEmpId");
            return (Criteria) this;
        }

        public Criteria andBizLikeInsensitive(String value) {
            addCriterion("upper(biz) like", value.toUpperCase(), "biz");
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