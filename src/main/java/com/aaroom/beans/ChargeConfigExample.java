package com.aaroom.beans;

import java.util.ArrayList;
import java.util.List;

public class ChargeConfigExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ChargeConfigExample() {
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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andHotelIdIsNull() {
            addCriterion("hotel_id is null");
            return (Criteria) this;
        }

        public Criteria andHotelIdIsNotNull() {
            addCriterion("hotel_id is not null");
            return (Criteria) this;
        }

        public Criteria andHotelIdEqualTo(Integer value) {
            addCriterion("hotel_id =", value, "hotelId");
            return (Criteria) this;
        }

        public Criteria andHotelIdNotEqualTo(Integer value) {
            addCriterion("hotel_id <>", value, "hotelId");
            return (Criteria) this;
        }

        public Criteria andHotelIdGreaterThan(Integer value) {
            addCriterion("hotel_id >", value, "hotelId");
            return (Criteria) this;
        }

        public Criteria andHotelIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("hotel_id >=", value, "hotelId");
            return (Criteria) this;
        }

        public Criteria andHotelIdLessThan(Integer value) {
            addCriterion("hotel_id <", value, "hotelId");
            return (Criteria) this;
        }

        public Criteria andHotelIdLessThanOrEqualTo(Integer value) {
            addCriterion("hotel_id <=", value, "hotelId");
            return (Criteria) this;
        }

        public Criteria andHotelIdIn(List<Integer> values) {
            addCriterion("hotel_id in", values, "hotelId");
            return (Criteria) this;
        }

        public Criteria andHotelIdNotIn(List<Integer> values) {
            addCriterion("hotel_id not in", values, "hotelId");
            return (Criteria) this;
        }

        public Criteria andHotelIdBetween(Integer value1, Integer value2) {
            addCriterion("hotel_id between", value1, value2, "hotelId");
            return (Criteria) this;
        }

        public Criteria andHotelIdNotBetween(Integer value1, Integer value2) {
            addCriterion("hotel_id not between", value1, value2, "hotelId");
            return (Criteria) this;
        }

        public Criteria andChargeItemIsNull() {
            addCriterion("charge_item is null");
            return (Criteria) this;
        }

        public Criteria andChargeItemIsNotNull() {
            addCriterion("charge_item is not null");
            return (Criteria) this;
        }

        public Criteria andChargeItemEqualTo(Integer value) {
            addCriterion("charge_item =", value, "chargeItem");
            return (Criteria) this;
        }

        public Criteria andChargeItemNotEqualTo(Integer value) {
            addCriterion("charge_item <>", value, "chargeItem");
            return (Criteria) this;
        }

        public Criteria andChargeItemGreaterThan(Integer value) {
            addCriterion("charge_item >", value, "chargeItem");
            return (Criteria) this;
        }

        public Criteria andChargeItemGreaterThanOrEqualTo(Integer value) {
            addCriterion("charge_item >=", value, "chargeItem");
            return (Criteria) this;
        }

        public Criteria andChargeItemLessThan(Integer value) {
            addCriterion("charge_item <", value, "chargeItem");
            return (Criteria) this;
        }

        public Criteria andChargeItemLessThanOrEqualTo(Integer value) {
            addCriterion("charge_item <=", value, "chargeItem");
            return (Criteria) this;
        }

        public Criteria andChargeItemIn(List<Integer> values) {
            addCriterion("charge_item in", values, "chargeItem");
            return (Criteria) this;
        }

        public Criteria andChargeItemNotIn(List<Integer> values) {
            addCriterion("charge_item not in", values, "chargeItem");
            return (Criteria) this;
        }

        public Criteria andChargeItemBetween(Integer value1, Integer value2) {
            addCriterion("charge_item between", value1, value2, "chargeItem");
            return (Criteria) this;
        }

        public Criteria andChargeItemNotBetween(Integer value1, Integer value2) {
            addCriterion("charge_item not between", value1, value2, "chargeItem");
            return (Criteria) this;
        }

        public Criteria andChargeRateIsNull() {
            addCriterion("charge_rate is null");
            return (Criteria) this;
        }

        public Criteria andChargeRateIsNotNull() {
            addCriterion("charge_rate is not null");
            return (Criteria) this;
        }

        public Criteria andChargeRateEqualTo(Integer value) {
            addCriterion("charge_rate =", value, "chargeRate");
            return (Criteria) this;
        }

        public Criteria andChargeRateNotEqualTo(Integer value) {
            addCriterion("charge_rate <>", value, "chargeRate");
            return (Criteria) this;
        }

        public Criteria andChargeRateGreaterThan(Integer value) {
            addCriterion("charge_rate >", value, "chargeRate");
            return (Criteria) this;
        }

        public Criteria andChargeRateGreaterThanOrEqualTo(Integer value) {
            addCriterion("charge_rate >=", value, "chargeRate");
            return (Criteria) this;
        }

        public Criteria andChargeRateLessThan(Integer value) {
            addCriterion("charge_rate <", value, "chargeRate");
            return (Criteria) this;
        }

        public Criteria andChargeRateLessThanOrEqualTo(Integer value) {
            addCriterion("charge_rate <=", value, "chargeRate");
            return (Criteria) this;
        }

        public Criteria andChargeRateIn(List<Integer> values) {
            addCriterion("charge_rate in", values, "chargeRate");
            return (Criteria) this;
        }

        public Criteria andChargeRateNotIn(List<Integer> values) {
            addCriterion("charge_rate not in", values, "chargeRate");
            return (Criteria) this;
        }

        public Criteria andChargeRateBetween(Integer value1, Integer value2) {
            addCriterion("charge_rate between", value1, value2, "chargeRate");
            return (Criteria) this;
        }

        public Criteria andChargeRateNotBetween(Integer value1, Integer value2) {
            addCriterion("charge_rate not between", value1, value2, "chargeRate");
            return (Criteria) this;
        }

        public Criteria andChargeStandardIsNull() {
            addCriterion("charge_standard is null");
            return (Criteria) this;
        }

        public Criteria andChargeStandardIsNotNull() {
            addCriterion("charge_standard is not null");
            return (Criteria) this;
        }

        public Criteria andChargeStandardEqualTo(String value) {
            addCriterion("charge_standard =", value, "chargeStandard");
            return (Criteria) this;
        }

        public Criteria andChargeStandardNotEqualTo(String value) {
            addCriterion("charge_standard <>", value, "chargeStandard");
            return (Criteria) this;
        }

        public Criteria andChargeStandardGreaterThan(String value) {
            addCriterion("charge_standard >", value, "chargeStandard");
            return (Criteria) this;
        }

        public Criteria andChargeStandardGreaterThanOrEqualTo(String value) {
            addCriterion("charge_standard >=", value, "chargeStandard");
            return (Criteria) this;
        }

        public Criteria andChargeStandardLessThan(String value) {
            addCriterion("charge_standard <", value, "chargeStandard");
            return (Criteria) this;
        }

        public Criteria andChargeStandardLessThanOrEqualTo(String value) {
            addCriterion("charge_standard <=", value, "chargeStandard");
            return (Criteria) this;
        }

        public Criteria andChargeStandardLike(String value) {
            addCriterion("charge_standard like", value, "chargeStandard");
            return (Criteria) this;
        }

        public Criteria andChargeStandardNotLike(String value) {
            addCriterion("charge_standard not like", value, "chargeStandard");
            return (Criteria) this;
        }

        public Criteria andChargeStandardIn(List<String> values) {
            addCriterion("charge_standard in", values, "chargeStandard");
            return (Criteria) this;
        }

        public Criteria andChargeStandardNotIn(List<String> values) {
            addCriterion("charge_standard not in", values, "chargeStandard");
            return (Criteria) this;
        }

        public Criteria andChargeStandardBetween(String value1, String value2) {
            addCriterion("charge_standard between", value1, value2, "chargeStandard");
            return (Criteria) this;
        }

        public Criteria andChargeStandardNotBetween(String value1, String value2) {
            addCriterion("charge_standard not between", value1, value2, "chargeStandard");
            return (Criteria) this;
        }

        public Criteria andChargeRuleIsNull() {
            addCriterion("charge_rule is null");
            return (Criteria) this;
        }

        public Criteria andChargeRuleIsNotNull() {
            addCriterion("charge_rule is not null");
            return (Criteria) this;
        }

        public Criteria andChargeRuleEqualTo(Double value) {
            addCriterion("charge_rule =", value, "chargeRule");
            return (Criteria) this;
        }

        public Criteria andChargeRuleNotEqualTo(Double value) {
            addCriterion("charge_rule <>", value, "chargeRule");
            return (Criteria) this;
        }

        public Criteria andChargeRuleGreaterThan(Double value) {
            addCriterion("charge_rule >", value, "chargeRule");
            return (Criteria) this;
        }

        public Criteria andChargeRuleGreaterThanOrEqualTo(Double value) {
            addCriterion("charge_rule >=", value, "chargeRule");
            return (Criteria) this;
        }

        public Criteria andChargeRuleLessThan(Double value) {
            addCriterion("charge_rule <", value, "chargeRule");
            return (Criteria) this;
        }

        public Criteria andChargeRuleLessThanOrEqualTo(Double value) {
            addCriterion("charge_rule <=", value, "chargeRule");
            return (Criteria) this;
        }

        public Criteria andChargeRuleIn(List<Double> values) {
            addCriterion("charge_rule in", values, "chargeRule");
            return (Criteria) this;
        }

        public Criteria andChargeRuleNotIn(List<Double> values) {
            addCriterion("charge_rule not in", values, "chargeRule");
            return (Criteria) this;
        }

        public Criteria andChargeRuleBetween(Double value1, Double value2) {
            addCriterion("charge_rule between", value1, value2, "chargeRule");
            return (Criteria) this;
        }

        public Criteria andChargeRuleNotBetween(Double value1, Double value2) {
            addCriterion("charge_rule not between", value1, value2, "chargeRule");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
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

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
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