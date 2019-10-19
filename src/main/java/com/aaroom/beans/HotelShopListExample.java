package com.aaroom.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotelShopListExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public HotelShopListExample() {
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

        public Criteria andProprietorIdIsNull() {
            addCriterion("proprietor_id is null");
            return (Criteria) this;
        }

        public Criteria andProprietorIdIsNotNull() {
            addCriterion("proprietor_id is not null");
            return (Criteria) this;
        }

        public Criteria andProprietorIdEqualTo(Integer value) {
            addCriterion("proprietor_id =", value, "proprietorId");
            return (Criteria) this;
        }

        public Criteria andProprietorIdNotEqualTo(Integer value) {
            addCriterion("proprietor_id <>", value, "proprietorId");
            return (Criteria) this;
        }

        public Criteria andProprietorIdGreaterThan(Integer value) {
            addCriterion("proprietor_id >", value, "proprietorId");
            return (Criteria) this;
        }

        public Criteria andProprietorIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("proprietor_id >=", value, "proprietorId");
            return (Criteria) this;
        }

        public Criteria andProprietorIdLessThan(Integer value) {
            addCriterion("proprietor_id <", value, "proprietorId");
            return (Criteria) this;
        }

        public Criteria andProprietorIdLessThanOrEqualTo(Integer value) {
            addCriterion("proprietor_id <=", value, "proprietorId");
            return (Criteria) this;
        }

        public Criteria andProprietorIdIn(List<Integer> values) {
            addCriterion("proprietor_id in", values, "proprietorId");
            return (Criteria) this;
        }

        public Criteria andProprietorIdNotIn(List<Integer> values) {
            addCriterion("proprietor_id not in", values, "proprietorId");
            return (Criteria) this;
        }

        public Criteria andProprietorIdBetween(Integer value1, Integer value2) {
            addCriterion("proprietor_id between", value1, value2, "proprietorId");
            return (Criteria) this;
        }

        public Criteria andProprietorIdNotBetween(Integer value1, Integer value2) {
            addCriterion("proprietor_id not between", value1, value2, "proprietorId");
            return (Criteria) this;
        }

        public Criteria andSignatoryTimeIsNull() {
            addCriterion("signatory_time is null");
            return (Criteria) this;
        }

        public Criteria andSignatoryTimeIsNotNull() {
            addCriterion("signatory_time is not null");
            return (Criteria) this;
        }

        public Criteria andSignatoryTimeEqualTo(Date value) {
            addCriterion("signatory_time =", value, "signatoryTime");
            return (Criteria) this;
        }

        public Criteria andSignatoryTimeNotEqualTo(Date value) {
            addCriterion("signatory_time <>", value, "signatoryTime");
            return (Criteria) this;
        }

        public Criteria andSignatoryTimeGreaterThan(Date value) {
            addCriterion("signatory_time >", value, "signatoryTime");
            return (Criteria) this;
        }

        public Criteria andSignatoryTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("signatory_time >=", value, "signatoryTime");
            return (Criteria) this;
        }

        public Criteria andSignatoryTimeLessThan(Date value) {
            addCriterion("signatory_time <", value, "signatoryTime");
            return (Criteria) this;
        }

        public Criteria andSignatoryTimeLessThanOrEqualTo(Date value) {
            addCriterion("signatory_time <=", value, "signatoryTime");
            return (Criteria) this;
        }

        public Criteria andSignatoryTimeIn(List<Date> values) {
            addCriterion("signatory_time in", values, "signatoryTime");
            return (Criteria) this;
        }

        public Criteria andSignatoryTimeNotIn(List<Date> values) {
            addCriterion("signatory_time not in", values, "signatoryTime");
            return (Criteria) this;
        }

        public Criteria andSignatoryTimeBetween(Date value1, Date value2) {
            addCriterion("signatory_time between", value1, value2, "signatoryTime");
            return (Criteria) this;
        }

        public Criteria andSignatoryTimeNotBetween(Date value1, Date value2) {
            addCriterion("signatory_time not between", value1, value2, "signatoryTime");
            return (Criteria) this;
        }

        public Criteria andExpandIdIsNull() {
            addCriterion("expand_id is null");
            return (Criteria) this;
        }

        public Criteria andExpandIdIsNotNull() {
            addCriterion("expand_id is not null");
            return (Criteria) this;
        }

        public Criteria andExpandIdEqualTo(Integer value) {
            addCriterion("expand_id =", value, "expandId");
            return (Criteria) this;
        }

        public Criteria andExpandIdNotEqualTo(Integer value) {
            addCriterion("expand_id <>", value, "expandId");
            return (Criteria) this;
        }

        public Criteria andExpandIdGreaterThan(Integer value) {
            addCriterion("expand_id >", value, "expandId");
            return (Criteria) this;
        }

        public Criteria andExpandIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("expand_id >=", value, "expandId");
            return (Criteria) this;
        }

        public Criteria andExpandIdLessThan(Integer value) {
            addCriterion("expand_id <", value, "expandId");
            return (Criteria) this;
        }

        public Criteria andExpandIdLessThanOrEqualTo(Integer value) {
            addCriterion("expand_id <=", value, "expandId");
            return (Criteria) this;
        }

        public Criteria andExpandIdIn(List<Integer> values) {
            addCriterion("expand_id in", values, "expandId");
            return (Criteria) this;
        }

        public Criteria andExpandIdNotIn(List<Integer> values) {
            addCriterion("expand_id not in", values, "expandId");
            return (Criteria) this;
        }

        public Criteria andExpandIdBetween(Integer value1, Integer value2) {
            addCriterion("expand_id between", value1, value2, "expandId");
            return (Criteria) this;
        }

        public Criteria andExpandIdNotBetween(Integer value1, Integer value2) {
            addCriterion("expand_id not between", value1, value2, "expandId");
            return (Criteria) this;
        }

        public Criteria andShopManagerIdIsNull() {
            addCriterion("shop_manager_id is null");
            return (Criteria) this;
        }

        public Criteria andShopManagerIdIsNotNull() {
            addCriterion("shop_manager_id is not null");
            return (Criteria) this;
        }

        public Criteria andShopManagerIdEqualTo(Integer value) {
            addCriterion("shop_manager_id =", value, "shopManagerId");
            return (Criteria) this;
        }

        public Criteria andShopManagerIdNotEqualTo(Integer value) {
            addCriterion("shop_manager_id <>", value, "shopManagerId");
            return (Criteria) this;
        }

        public Criteria andShopManagerIdGreaterThan(Integer value) {
            addCriterion("shop_manager_id >", value, "shopManagerId");
            return (Criteria) this;
        }

        public Criteria andShopManagerIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("shop_manager_id >=", value, "shopManagerId");
            return (Criteria) this;
        }

        public Criteria andShopManagerIdLessThan(Integer value) {
            addCriterion("shop_manager_id <", value, "shopManagerId");
            return (Criteria) this;
        }

        public Criteria andShopManagerIdLessThanOrEqualTo(Integer value) {
            addCriterion("shop_manager_id <=", value, "shopManagerId");
            return (Criteria) this;
        }

        public Criteria andShopManagerIdIn(List<Integer> values) {
            addCriterion("shop_manager_id in", values, "shopManagerId");
            return (Criteria) this;
        }

        public Criteria andShopManagerIdNotIn(List<Integer> values) {
            addCriterion("shop_manager_id not in", values, "shopManagerId");
            return (Criteria) this;
        }

        public Criteria andShopManagerIdBetween(Integer value1, Integer value2) {
            addCriterion("shop_manager_id between", value1, value2, "shopManagerId");
            return (Criteria) this;
        }

        public Criteria andShopManagerIdNotBetween(Integer value1, Integer value2) {
            addCriterion("shop_manager_id not between", value1, value2, "shopManagerId");
            return (Criteria) this;
        }

        public Criteria andShopStatusIsNull() {
            addCriterion("shop_status is null");
            return (Criteria) this;
        }

        public Criteria andShopStatusIsNotNull() {
            addCriterion("shop_status is not null");
            return (Criteria) this;
        }

        public Criteria andShopStatusEqualTo(Integer value) {
            addCriterion("shop_status =", value, "shopStatus");
            return (Criteria) this;
        }

        public Criteria andShopStatusNotEqualTo(Integer value) {
            addCriterion("shop_status <>", value, "shopStatus");
            return (Criteria) this;
        }

        public Criteria andShopStatusGreaterThan(Integer value) {
            addCriterion("shop_status >", value, "shopStatus");
            return (Criteria) this;
        }

        public Criteria andShopStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("shop_status >=", value, "shopStatus");
            return (Criteria) this;
        }

        public Criteria andShopStatusLessThan(Integer value) {
            addCriterion("shop_status <", value, "shopStatus");
            return (Criteria) this;
        }

        public Criteria andShopStatusLessThanOrEqualTo(Integer value) {
            addCriterion("shop_status <=", value, "shopStatus");
            return (Criteria) this;
        }

        public Criteria andShopStatusIn(List<Integer> values) {
            addCriterion("shop_status in", values, "shopStatus");
            return (Criteria) this;
        }

        public Criteria andShopStatusNotIn(List<Integer> values) {
            addCriterion("shop_status not in", values, "shopStatus");
            return (Criteria) this;
        }

        public Criteria andShopStatusBetween(Integer value1, Integer value2) {
            addCriterion("shop_status between", value1, value2, "shopStatus");
            return (Criteria) this;
        }

        public Criteria andShopStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("shop_status not between", value1, value2, "shopStatus");
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

        public Criteria andCreateIdIsNull() {
            addCriterion("create_id is null");
            return (Criteria) this;
        }

        public Criteria andCreateIdIsNotNull() {
            addCriterion("create_id is not null");
            return (Criteria) this;
        }

        public Criteria andCreateIdEqualTo(Integer value) {
            addCriterion("create_id =", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotEqualTo(Integer value) {
            addCriterion("create_id <>", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdGreaterThan(Integer value) {
            addCriterion("create_id >", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("create_id >=", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdLessThan(Integer value) {
            addCriterion("create_id <", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdLessThanOrEqualTo(Integer value) {
            addCriterion("create_id <=", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdIn(List<Integer> values) {
            addCriterion("create_id in", values, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotIn(List<Integer> values) {
            addCriterion("create_id not in", values, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdBetween(Integer value1, Integer value2) {
            addCriterion("create_id between", value1, value2, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotBetween(Integer value1, Integer value2) {
            addCriterion("create_id not between", value1, value2, "createId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdIsNull() {
            addCriterion("update_id is null");
            return (Criteria) this;
        }

        public Criteria andUpdateIdIsNotNull() {
            addCriterion("update_id is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateIdEqualTo(Integer value) {
            addCriterion("update_id =", value, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdNotEqualTo(Integer value) {
            addCriterion("update_id <>", value, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdGreaterThan(Integer value) {
            addCriterion("update_id >", value, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("update_id >=", value, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdLessThan(Integer value) {
            addCriterion("update_id <", value, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdLessThanOrEqualTo(Integer value) {
            addCriterion("update_id <=", value, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdIn(List<Integer> values) {
            addCriterion("update_id in", values, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdNotIn(List<Integer> values) {
            addCriterion("update_id not in", values, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdBetween(Integer value1, Integer value2) {
            addCriterion("update_id between", value1, value2, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdNotBetween(Integer value1, Integer value2) {
            addCriterion("update_id not between", value1, value2, "updateId");
            return (Criteria) this;
        }

        public Criteria andIsActiveIsNull() {
            addCriterion("is_active is null");
            return (Criteria) this;
        }

        public Criteria andIsActiveIsNotNull() {
            addCriterion("is_active is not null");
            return (Criteria) this;
        }

        public Criteria andIsActiveEqualTo(Integer value) {
            addCriterion("is_active =", value, "isActive");
            return (Criteria) this;
        }

        public Criteria andIsActiveNotEqualTo(Integer value) {
            addCriterion("is_active <>", value, "isActive");
            return (Criteria) this;
        }

        public Criteria andIsActiveGreaterThan(Integer value) {
            addCriterion("is_active >", value, "isActive");
            return (Criteria) this;
        }

        public Criteria andIsActiveGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_active >=", value, "isActive");
            return (Criteria) this;
        }

        public Criteria andIsActiveLessThan(Integer value) {
            addCriterion("is_active <", value, "isActive");
            return (Criteria) this;
        }

        public Criteria andIsActiveLessThanOrEqualTo(Integer value) {
            addCriterion("is_active <=", value, "isActive");
            return (Criteria) this;
        }

        public Criteria andIsActiveIn(List<Integer> values) {
            addCriterion("is_active in", values, "isActive");
            return (Criteria) this;
        }

        public Criteria andIsActiveNotIn(List<Integer> values) {
            addCriterion("is_active not in", values, "isActive");
            return (Criteria) this;
        }

        public Criteria andIsActiveBetween(Integer value1, Integer value2) {
            addCriterion("is_active between", value1, value2, "isActive");
            return (Criteria) this;
        }

        public Criteria andIsActiveNotBetween(Integer value1, Integer value2) {
            addCriterion("is_active not between", value1, value2, "isActive");
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