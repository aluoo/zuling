package com.anyi.sparrow.assist.system.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportErrosExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ReportErrosExample() {
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

        public Criteria andErroCntIsNull() {
            addCriterion("erro_cnt is null");
            return (Criteria) this;
        }

        public Criteria andErroCntIsNotNull() {
            addCriterion("erro_cnt is not null");
            return (Criteria) this;
        }

        public Criteria andErroCntEqualTo(String value) {
            addCriterion("erro_cnt =", value, "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCntNotEqualTo(String value) {
            addCriterion("erro_cnt <>", value, "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCntGreaterThan(String value) {
            addCriterion("erro_cnt >", value, "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCntGreaterThanOrEqualTo(String value) {
            addCriterion("erro_cnt >=", value, "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCntLessThan(String value) {
            addCriterion("erro_cnt <", value, "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCntLessThanOrEqualTo(String value) {
            addCriterion("erro_cnt <=", value, "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCntLike(String value) {
            addCriterion("erro_cnt like", value, "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCntNotLike(String value) {
            addCriterion("erro_cnt not like", value, "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCntIn(List<String> values) {
            addCriterion("erro_cnt in", values, "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCntNotIn(List<String> values) {
            addCriterion("erro_cnt not in", values, "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCntBetween(String value1, String value2) {
            addCriterion("erro_cnt between", value1, value2, "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCntNotBetween(String value1, String value2) {
            addCriterion("erro_cnt not between", value1, value2, "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCodeIsNull() {
            addCriterion("erro_code is null");
            return (Criteria) this;
        }

        public Criteria andErroCodeIsNotNull() {
            addCriterion("erro_code is not null");
            return (Criteria) this;
        }

        public Criteria andErroCodeEqualTo(String value) {
            addCriterion("erro_code =", value, "erroCode");
            return (Criteria) this;
        }

        public Criteria andErroCodeNotEqualTo(String value) {
            addCriterion("erro_code <>", value, "erroCode");
            return (Criteria) this;
        }

        public Criteria andErroCodeGreaterThan(String value) {
            addCriterion("erro_code >", value, "erroCode");
            return (Criteria) this;
        }

        public Criteria andErroCodeGreaterThanOrEqualTo(String value) {
            addCriterion("erro_code >=", value, "erroCode");
            return (Criteria) this;
        }

        public Criteria andErroCodeLessThan(String value) {
            addCriterion("erro_code <", value, "erroCode");
            return (Criteria) this;
        }

        public Criteria andErroCodeLessThanOrEqualTo(String value) {
            addCriterion("erro_code <=", value, "erroCode");
            return (Criteria) this;
        }

        public Criteria andErroCodeLike(String value) {
            addCriterion("erro_code like", value, "erroCode");
            return (Criteria) this;
        }

        public Criteria andErroCodeNotLike(String value) {
            addCriterion("erro_code not like", value, "erroCode");
            return (Criteria) this;
        }

        public Criteria andErroCodeIn(List<String> values) {
            addCriterion("erro_code in", values, "erroCode");
            return (Criteria) this;
        }

        public Criteria andErroCodeNotIn(List<String> values) {
            addCriterion("erro_code not in", values, "erroCode");
            return (Criteria) this;
        }

        public Criteria andErroCodeBetween(String value1, String value2) {
            addCriterion("erro_code between", value1, value2, "erroCode");
            return (Criteria) this;
        }

        public Criteria andErroCodeNotBetween(String value1, String value2) {
            addCriterion("erro_code not between", value1, value2, "erroCode");
            return (Criteria) this;
        }

        public Criteria andClientCntIsNull() {
            addCriterion("client_cnt is null");
            return (Criteria) this;
        }

        public Criteria andClientCntIsNotNull() {
            addCriterion("client_cnt is not null");
            return (Criteria) this;
        }

        public Criteria andClientCntEqualTo(String value) {
            addCriterion("client_cnt =", value, "clientCnt");
            return (Criteria) this;
        }

        public Criteria andClientCntNotEqualTo(String value) {
            addCriterion("client_cnt <>", value, "clientCnt");
            return (Criteria) this;
        }

        public Criteria andClientCntGreaterThan(String value) {
            addCriterion("client_cnt >", value, "clientCnt");
            return (Criteria) this;
        }

        public Criteria andClientCntGreaterThanOrEqualTo(String value) {
            addCriterion("client_cnt >=", value, "clientCnt");
            return (Criteria) this;
        }

        public Criteria andClientCntLessThan(String value) {
            addCriterion("client_cnt <", value, "clientCnt");
            return (Criteria) this;
        }

        public Criteria andClientCntLessThanOrEqualTo(String value) {
            addCriterion("client_cnt <=", value, "clientCnt");
            return (Criteria) this;
        }

        public Criteria andClientCntLike(String value) {
            addCriterion("client_cnt like", value, "clientCnt");
            return (Criteria) this;
        }

        public Criteria andClientCntNotLike(String value) {
            addCriterion("client_cnt not like", value, "clientCnt");
            return (Criteria) this;
        }

        public Criteria andClientCntIn(List<String> values) {
            addCriterion("client_cnt in", values, "clientCnt");
            return (Criteria) this;
        }

        public Criteria andClientCntNotIn(List<String> values) {
            addCriterion("client_cnt not in", values, "clientCnt");
            return (Criteria) this;
        }

        public Criteria andClientCntBetween(String value1, String value2) {
            addCriterion("client_cnt between", value1, value2, "clientCnt");
            return (Criteria) this;
        }

        public Criteria andClientCntNotBetween(String value1, String value2) {
            addCriterion("client_cnt not between", value1, value2, "clientCnt");
            return (Criteria) this;
        }

        public Criteria andErroCountIsNull() {
            addCriterion("erro_count is null");
            return (Criteria) this;
        }

        public Criteria andErroCountIsNotNull() {
            addCriterion("erro_count is not null");
            return (Criteria) this;
        }

        public Criteria andErroCountEqualTo(Integer value) {
            addCriterion("erro_count =", value, "erroCount");
            return (Criteria) this;
        }

        public Criteria andErroCountNotEqualTo(Integer value) {
            addCriterion("erro_count <>", value, "erroCount");
            return (Criteria) this;
        }

        public Criteria andErroCountGreaterThan(Integer value) {
            addCriterion("erro_count >", value, "erroCount");
            return (Criteria) this;
        }

        public Criteria andErroCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("erro_count >=", value, "erroCount");
            return (Criteria) this;
        }

        public Criteria andErroCountLessThan(Integer value) {
            addCriterion("erro_count <", value, "erroCount");
            return (Criteria) this;
        }

        public Criteria andErroCountLessThanOrEqualTo(Integer value) {
            addCriterion("erro_count <=", value, "erroCount");
            return (Criteria) this;
        }

        public Criteria andErroCountIn(List<Integer> values) {
            addCriterion("erro_count in", values, "erroCount");
            return (Criteria) this;
        }

        public Criteria andErroCountNotIn(List<Integer> values) {
            addCriterion("erro_count not in", values, "erroCount");
            return (Criteria) this;
        }

        public Criteria andErroCountBetween(Integer value1, Integer value2) {
            addCriterion("erro_count between", value1, value2, "erroCount");
            return (Criteria) this;
        }

        public Criteria andErroCountNotBetween(Integer value1, Integer value2) {
            addCriterion("erro_count not between", value1, value2, "erroCount");
            return (Criteria) this;
        }

        public Criteria andErroCurCountIsNull() {
            addCriterion("erro_cur_count is null");
            return (Criteria) this;
        }

        public Criteria andErroCurCountIsNotNull() {
            addCriterion("erro_cur_count is not null");
            return (Criteria) this;
        }

        public Criteria andErroCurCountEqualTo(Integer value) {
            addCriterion("erro_cur_count =", value, "erroCurCount");
            return (Criteria) this;
        }

        public Criteria andErroCurCountNotEqualTo(Integer value) {
            addCriterion("erro_cur_count <>", value, "erroCurCount");
            return (Criteria) this;
        }

        public Criteria andErroCurCountGreaterThan(Integer value) {
            addCriterion("erro_cur_count >", value, "erroCurCount");
            return (Criteria) this;
        }

        public Criteria andErroCurCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("erro_cur_count >=", value, "erroCurCount");
            return (Criteria) this;
        }

        public Criteria andErroCurCountLessThan(Integer value) {
            addCriterion("erro_cur_count <", value, "erroCurCount");
            return (Criteria) this;
        }

        public Criteria andErroCurCountLessThanOrEqualTo(Integer value) {
            addCriterion("erro_cur_count <=", value, "erroCurCount");
            return (Criteria) this;
        }

        public Criteria andErroCurCountIn(List<Integer> values) {
            addCriterion("erro_cur_count in", values, "erroCurCount");
            return (Criteria) this;
        }

        public Criteria andErroCurCountNotIn(List<Integer> values) {
            addCriterion("erro_cur_count not in", values, "erroCurCount");
            return (Criteria) this;
        }

        public Criteria andErroCurCountBetween(Integer value1, Integer value2) {
            addCriterion("erro_cur_count between", value1, value2, "erroCurCount");
            return (Criteria) this;
        }

        public Criteria andErroCurCountNotBetween(Integer value1, Integer value2) {
            addCriterion("erro_cur_count not between", value1, value2, "erroCurCount");
            return (Criteria) this;
        }

        public Criteria andCanHandleIsNull() {
            addCriterion("can_handle is null");
            return (Criteria) this;
        }

        public Criteria andCanHandleIsNotNull() {
            addCriterion("can_handle is not null");
            return (Criteria) this;
        }

        public Criteria andCanHandleEqualTo(Byte value) {
            addCriterion("can_handle =", value, "canHandle");
            return (Criteria) this;
        }

        public Criteria andCanHandleNotEqualTo(Byte value) {
            addCriterion("can_handle <>", value, "canHandle");
            return (Criteria) this;
        }

        public Criteria andCanHandleGreaterThan(Byte value) {
            addCriterion("can_handle >", value, "canHandle");
            return (Criteria) this;
        }

        public Criteria andCanHandleGreaterThanOrEqualTo(Byte value) {
            addCriterion("can_handle >=", value, "canHandle");
            return (Criteria) this;
        }

        public Criteria andCanHandleLessThan(Byte value) {
            addCriterion("can_handle <", value, "canHandle");
            return (Criteria) this;
        }

        public Criteria andCanHandleLessThanOrEqualTo(Byte value) {
            addCriterion("can_handle <=", value, "canHandle");
            return (Criteria) this;
        }

        public Criteria andCanHandleIn(List<Byte> values) {
            addCriterion("can_handle in", values, "canHandle");
            return (Criteria) this;
        }

        public Criteria andCanHandleNotIn(List<Byte> values) {
            addCriterion("can_handle not in", values, "canHandle");
            return (Criteria) this;
        }

        public Criteria andCanHandleBetween(Byte value1, Byte value2) {
            addCriterion("can_handle between", value1, value2, "canHandle");
            return (Criteria) this;
        }

        public Criteria andCanHandleNotBetween(Byte value1, Byte value2) {
            addCriterion("can_handle not between", value1, value2, "canHandle");
            return (Criteria) this;
        }

        public Criteria andReasonHandleIsNull() {
            addCriterion("reason_handle is null");
            return (Criteria) this;
        }

        public Criteria andReasonHandleIsNotNull() {
            addCriterion("reason_handle is not null");
            return (Criteria) this;
        }

        public Criteria andReasonHandleEqualTo(String value) {
            addCriterion("reason_handle =", value, "reasonHandle");
            return (Criteria) this;
        }

        public Criteria andReasonHandleNotEqualTo(String value) {
            addCriterion("reason_handle <>", value, "reasonHandle");
            return (Criteria) this;
        }

        public Criteria andReasonHandleGreaterThan(String value) {
            addCriterion("reason_handle >", value, "reasonHandle");
            return (Criteria) this;
        }

        public Criteria andReasonHandleGreaterThanOrEqualTo(String value) {
            addCriterion("reason_handle >=", value, "reasonHandle");
            return (Criteria) this;
        }

        public Criteria andReasonHandleLessThan(String value) {
            addCriterion("reason_handle <", value, "reasonHandle");
            return (Criteria) this;
        }

        public Criteria andReasonHandleLessThanOrEqualTo(String value) {
            addCriterion("reason_handle <=", value, "reasonHandle");
            return (Criteria) this;
        }

        public Criteria andReasonHandleLike(String value) {
            addCriterion("reason_handle like", value, "reasonHandle");
            return (Criteria) this;
        }

        public Criteria andReasonHandleNotLike(String value) {
            addCriterion("reason_handle not like", value, "reasonHandle");
            return (Criteria) this;
        }

        public Criteria andReasonHandleIn(List<String> values) {
            addCriterion("reason_handle in", values, "reasonHandle");
            return (Criteria) this;
        }

        public Criteria andReasonHandleNotIn(List<String> values) {
            addCriterion("reason_handle not in", values, "reasonHandle");
            return (Criteria) this;
        }

        public Criteria andReasonHandleBetween(String value1, String value2) {
            addCriterion("reason_handle between", value1, value2, "reasonHandle");
            return (Criteria) this;
        }

        public Criteria andReasonHandleNotBetween(String value1, String value2) {
            addCriterion("reason_handle not between", value1, value2, "reasonHandle");
            return (Criteria) this;
        }

        public Criteria andCallCenterAvailIsNull() {
            addCriterion("call_center_avail is null");
            return (Criteria) this;
        }

        public Criteria andCallCenterAvailIsNotNull() {
            addCriterion("call_center_avail is not null");
            return (Criteria) this;
        }

        public Criteria andCallCenterAvailEqualTo(Byte value) {
            addCriterion("call_center_avail =", value, "callCenterAvail");
            return (Criteria) this;
        }

        public Criteria andCallCenterAvailNotEqualTo(Byte value) {
            addCriterion("call_center_avail <>", value, "callCenterAvail");
            return (Criteria) this;
        }

        public Criteria andCallCenterAvailGreaterThan(Byte value) {
            addCriterion("call_center_avail >", value, "callCenterAvail");
            return (Criteria) this;
        }

        public Criteria andCallCenterAvailGreaterThanOrEqualTo(Byte value) {
            addCriterion("call_center_avail >=", value, "callCenterAvail");
            return (Criteria) this;
        }

        public Criteria andCallCenterAvailLessThan(Byte value) {
            addCriterion("call_center_avail <", value, "callCenterAvail");
            return (Criteria) this;
        }

        public Criteria andCallCenterAvailLessThanOrEqualTo(Byte value) {
            addCriterion("call_center_avail <=", value, "callCenterAvail");
            return (Criteria) this;
        }

        public Criteria andCallCenterAvailIn(List<Byte> values) {
            addCriterion("call_center_avail in", values, "callCenterAvail");
            return (Criteria) this;
        }

        public Criteria andCallCenterAvailNotIn(List<Byte> values) {
            addCriterion("call_center_avail not in", values, "callCenterAvail");
            return (Criteria) this;
        }

        public Criteria andCallCenterAvailBetween(Byte value1, Byte value2) {
            addCriterion("call_center_avail between", value1, value2, "callCenterAvail");
            return (Criteria) this;
        }

        public Criteria andCallCenterAvailNotBetween(Byte value1, Byte value2) {
            addCriterion("call_center_avail not between", value1, value2, "callCenterAvail");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("status not between", value1, value2, "status");
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

        public Criteria andErroCntLikeInsensitive(String value) {
            addCriterion("upper(erro_cnt) like", value.toUpperCase(), "erroCnt");
            return (Criteria) this;
        }

        public Criteria andErroCodeLikeInsensitive(String value) {
            addCriterion("upper(erro_code) like", value.toUpperCase(), "erroCode");
            return (Criteria) this;
        }

        public Criteria andClientCntLikeInsensitive(String value) {
            addCriterion("upper(client_cnt) like", value.toUpperCase(), "clientCnt");
            return (Criteria) this;
        }

        public Criteria andReasonHandleLikeInsensitive(String value) {
            addCriterion("upper(reason_handle) like", value.toUpperCase(), "reasonHandle");
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