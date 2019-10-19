package com.aaroom.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClaimAccountExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ClaimAccountExample() {
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

        public Criteria andFlowNumberIsNull() {
            addCriterion("flow_number is null");
            return (Criteria) this;
        }

        public Criteria andFlowNumberIsNotNull() {
            addCriterion("flow_number is not null");
            return (Criteria) this;
        }

        public Criteria andFlowNumberEqualTo(String value) {
            addCriterion("flow_number =", value, "flowNumber");
            return (Criteria) this;
        }

        public Criteria andFlowNumberNotEqualTo(String value) {
            addCriterion("flow_number <>", value, "flowNumber");
            return (Criteria) this;
        }

        public Criteria andFlowNumberGreaterThan(String value) {
            addCriterion("flow_number >", value, "flowNumber");
            return (Criteria) this;
        }

        public Criteria andFlowNumberGreaterThanOrEqualTo(String value) {
            addCriterion("flow_number >=", value, "flowNumber");
            return (Criteria) this;
        }

        public Criteria andFlowNumberLessThan(String value) {
            addCriterion("flow_number <", value, "flowNumber");
            return (Criteria) this;
        }

        public Criteria andFlowNumberLessThanOrEqualTo(String value) {
            addCriterion("flow_number <=", value, "flowNumber");
            return (Criteria) this;
        }

        public Criteria andFlowNumberLike(String value) {
            addCriterion("flow_number like", value, "flowNumber");
            return (Criteria) this;
        }

        public Criteria andFlowNumberNotLike(String value) {
            addCriterion("flow_number not like", value, "flowNumber");
            return (Criteria) this;
        }

        public Criteria andFlowNumberIn(List<String> values) {
            addCriterion("flow_number in", values, "flowNumber");
            return (Criteria) this;
        }

        public Criteria andFlowNumberNotIn(List<String> values) {
            addCriterion("flow_number not in", values, "flowNumber");
            return (Criteria) this;
        }

        public Criteria andFlowNumberBetween(String value1, String value2) {
            addCriterion("flow_number between", value1, value2, "flowNumber");
            return (Criteria) this;
        }

        public Criteria andFlowNumberNotBetween(String value1, String value2) {
            addCriterion("flow_number not between", value1, value2, "flowNumber");
            return (Criteria) this;
        }

        public Criteria andTransactionDateIsNull() {
            addCriterion("transaction_date is null");
            return (Criteria) this;
        }

        public Criteria andTransactionDateIsNotNull() {
            addCriterion("transaction_date is not null");
            return (Criteria) this;
        }

        public Criteria andTransactionDateEqualTo(Date value) {
            addCriterion("transaction_date =", value, "transactionDate");
            return (Criteria) this;
        }

        public Criteria andTransactionDateNotEqualTo(Date value) {
            addCriterion("transaction_date <>", value, "transactionDate");
            return (Criteria) this;
        }

        public Criteria andTransactionDateGreaterThan(Date value) {
            addCriterion("transaction_date >", value, "transactionDate");
            return (Criteria) this;
        }

        public Criteria andTransactionDateGreaterThanOrEqualTo(Date value) {
            addCriterion("transaction_date >=", value, "transactionDate");
            return (Criteria) this;
        }

        public Criteria andTransactionDateLessThan(Date value) {
            addCriterion("transaction_date <", value, "transactionDate");
            return (Criteria) this;
        }

        public Criteria andTransactionDateLessThanOrEqualTo(Date value) {
            addCriterion("transaction_date <=", value, "transactionDate");
            return (Criteria) this;
        }

        public Criteria andTransactionDateIn(List<Date> values) {
            addCriterion("transaction_date in", values, "transactionDate");
            return (Criteria) this;
        }

        public Criteria andTransactionDateNotIn(List<Date> values) {
            addCriterion("transaction_date not in", values, "transactionDate");
            return (Criteria) this;
        }

        public Criteria andTransactionDateBetween(Date value1, Date value2) {
            addCriterion("transaction_date between", value1, value2, "transactionDate");
            return (Criteria) this;
        }

        public Criteria andTransactionDateNotBetween(Date value1, Date value2) {
            addCriterion("transaction_date not between", value1, value2, "transactionDate");
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

        public Criteria andDraweeIsNull() {
            addCriterion("drawee is null");
            return (Criteria) this;
        }

        public Criteria andDraweeIsNotNull() {
            addCriterion("drawee is not null");
            return (Criteria) this;
        }

        public Criteria andDraweeEqualTo(String value) {
            addCriterion("drawee =", value, "drawee");
            return (Criteria) this;
        }

        public Criteria andDraweeNotEqualTo(String value) {
            addCriterion("drawee <>", value, "drawee");
            return (Criteria) this;
        }

        public Criteria andDraweeGreaterThan(String value) {
            addCriterion("drawee >", value, "drawee");
            return (Criteria) this;
        }

        public Criteria andDraweeGreaterThanOrEqualTo(String value) {
            addCriterion("drawee >=", value, "drawee");
            return (Criteria) this;
        }

        public Criteria andDraweeLessThan(String value) {
            addCriterion("drawee <", value, "drawee");
            return (Criteria) this;
        }

        public Criteria andDraweeLessThanOrEqualTo(String value) {
            addCriterion("drawee <=", value, "drawee");
            return (Criteria) this;
        }

        public Criteria andDraweeLike(String value) {
            addCriterion("drawee like", value, "drawee");
            return (Criteria) this;
        }

        public Criteria andDraweeNotLike(String value) {
            addCriterion("drawee not like", value, "drawee");
            return (Criteria) this;
        }

        public Criteria andDraweeIn(List<String> values) {
            addCriterion("drawee in", values, "drawee");
            return (Criteria) this;
        }

        public Criteria andDraweeNotIn(List<String> values) {
            addCriterion("drawee not in", values, "drawee");
            return (Criteria) this;
        }

        public Criteria andDraweeBetween(String value1, String value2) {
            addCriterion("drawee between", value1, value2, "drawee");
            return (Criteria) this;
        }

        public Criteria andDraweeNotBetween(String value1, String value2) {
            addCriterion("drawee not between", value1, value2, "drawee");
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

        public Criteria andPaymentAccountIsNull() {
            addCriterion("payment_account is null");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountIsNotNull() {
            addCriterion("payment_account is not null");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountEqualTo(String value) {
            addCriterion("payment_account =", value, "paymentAccount");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountNotEqualTo(String value) {
            addCriterion("payment_account <>", value, "paymentAccount");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountGreaterThan(String value) {
            addCriterion("payment_account >", value, "paymentAccount");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountGreaterThanOrEqualTo(String value) {
            addCriterion("payment_account >=", value, "paymentAccount");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountLessThan(String value) {
            addCriterion("payment_account <", value, "paymentAccount");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountLessThanOrEqualTo(String value) {
            addCriterion("payment_account <=", value, "paymentAccount");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountLike(String value) {
            addCriterion("payment_account like", value, "paymentAccount");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountNotLike(String value) {
            addCriterion("payment_account not like", value, "paymentAccount");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountIn(List<String> values) {
            addCriterion("payment_account in", values, "paymentAccount");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountNotIn(List<String> values) {
            addCriterion("payment_account not in", values, "paymentAccount");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountBetween(String value1, String value2) {
            addCriterion("payment_account between", value1, value2, "paymentAccount");
            return (Criteria) this;
        }

        public Criteria andPaymentAccountNotBetween(String value1, String value2) {
            addCriterion("payment_account not between", value1, value2, "paymentAccount");
            return (Criteria) this;
        }

        public Criteria andTransactionAmountIsNull() {
            addCriterion("transaction_amount is null");
            return (Criteria) this;
        }

        public Criteria andTransactionAmountIsNotNull() {
            addCriterion("transaction_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTransactionAmountEqualTo(Double value) {
            addCriterion("transaction_amount =", value, "transactionAmount");
            return (Criteria) this;
        }

        public Criteria andTransactionAmountNotEqualTo(Double value) {
            addCriterion("transaction_amount <>", value, "transactionAmount");
            return (Criteria) this;
        }

        public Criteria andTransactionAmountGreaterThan(Double value) {
            addCriterion("transaction_amount >", value, "transactionAmount");
            return (Criteria) this;
        }

        public Criteria andTransactionAmountGreaterThanOrEqualTo(Double value) {
            addCriterion("transaction_amount >=", value, "transactionAmount");
            return (Criteria) this;
        }

        public Criteria andTransactionAmountLessThan(Double value) {
            addCriterion("transaction_amount <", value, "transactionAmount");
            return (Criteria) this;
        }

        public Criteria andTransactionAmountLessThanOrEqualTo(Double value) {
            addCriterion("transaction_amount <=", value, "transactionAmount");
            return (Criteria) this;
        }

        public Criteria andTransactionAmountIn(List<Double> values) {
            addCriterion("transaction_amount in", values, "transactionAmount");
            return (Criteria) this;
        }

        public Criteria andTransactionAmountNotIn(List<Double> values) {
            addCriterion("transaction_amount not in", values, "transactionAmount");
            return (Criteria) this;
        }

        public Criteria andTransactionAmountBetween(Double value1, Double value2) {
            addCriterion("transaction_amount between", value1, value2, "transactionAmount");
            return (Criteria) this;
        }

        public Criteria andTransactionAmountNotBetween(Double value1, Double value2) {
            addCriterion("transaction_amount not between", value1, value2, "transactionAmount");
            return (Criteria) this;
        }

        public Criteria andSummaryIsNull() {
            addCriterion("summary is null");
            return (Criteria) this;
        }

        public Criteria andSummaryIsNotNull() {
            addCriterion("summary is not null");
            return (Criteria) this;
        }

        public Criteria andSummaryEqualTo(String value) {
            addCriterion("summary =", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotEqualTo(String value) {
            addCriterion("summary <>", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryGreaterThan(String value) {
            addCriterion("summary >", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryGreaterThanOrEqualTo(String value) {
            addCriterion("summary >=", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryLessThan(String value) {
            addCriterion("summary <", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryLessThanOrEqualTo(String value) {
            addCriterion("summary <=", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryLike(String value) {
            addCriterion("summary like", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotLike(String value) {
            addCriterion("summary not like", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryIn(List<String> values) {
            addCriterion("summary in", values, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotIn(List<String> values) {
            addCriterion("summary not in", values, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryBetween(String value1, String value2) {
            addCriterion("summary between", value1, value2, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotBetween(String value1, String value2) {
            addCriterion("summary not between", value1, value2, "summary");
            return (Criteria) this;
        }

        public Criteria andReceivingBankIsNull() {
            addCriterion("receiving_bank is null");
            return (Criteria) this;
        }

        public Criteria andReceivingBankIsNotNull() {
            addCriterion("receiving_bank is not null");
            return (Criteria) this;
        }

        public Criteria andReceivingBankEqualTo(String value) {
            addCriterion("receiving_bank =", value, "receivingBank");
            return (Criteria) this;
        }

        public Criteria andReceivingBankNotEqualTo(String value) {
            addCriterion("receiving_bank <>", value, "receivingBank");
            return (Criteria) this;
        }

        public Criteria andReceivingBankGreaterThan(String value) {
            addCriterion("receiving_bank >", value, "receivingBank");
            return (Criteria) this;
        }

        public Criteria andReceivingBankGreaterThanOrEqualTo(String value) {
            addCriterion("receiving_bank >=", value, "receivingBank");
            return (Criteria) this;
        }

        public Criteria andReceivingBankLessThan(String value) {
            addCriterion("receiving_bank <", value, "receivingBank");
            return (Criteria) this;
        }

        public Criteria andReceivingBankLessThanOrEqualTo(String value) {
            addCriterion("receiving_bank <=", value, "receivingBank");
            return (Criteria) this;
        }

        public Criteria andReceivingBankLike(String value) {
            addCriterion("receiving_bank like", value, "receivingBank");
            return (Criteria) this;
        }

        public Criteria andReceivingBankNotLike(String value) {
            addCriterion("receiving_bank not like", value, "receivingBank");
            return (Criteria) this;
        }

        public Criteria andReceivingBankIn(List<String> values) {
            addCriterion("receiving_bank in", values, "receivingBank");
            return (Criteria) this;
        }

        public Criteria andReceivingBankNotIn(List<String> values) {
            addCriterion("receiving_bank not in", values, "receivingBank");
            return (Criteria) this;
        }

        public Criteria andReceivingBankBetween(String value1, String value2) {
            addCriterion("receiving_bank between", value1, value2, "receivingBank");
            return (Criteria) this;
        }

        public Criteria andReceivingBankNotBetween(String value1, String value2) {
            addCriterion("receiving_bank not between", value1, value2, "receivingBank");
            return (Criteria) this;
        }

        public Criteria andClaimedAmountIsNull() {
            addCriterion("claimed_amount is null");
            return (Criteria) this;
        }

        public Criteria andClaimedAmountIsNotNull() {
            addCriterion("claimed_amount is not null");
            return (Criteria) this;
        }

        public Criteria andClaimedAmountEqualTo(Double value) {
            addCriterion("claimed_amount =", value, "claimedAmount");
            return (Criteria) this;
        }

        public Criteria andClaimedAmountNotEqualTo(Double value) {
            addCriterion("claimed_amount <>", value, "claimedAmount");
            return (Criteria) this;
        }

        public Criteria andClaimedAmountGreaterThan(Double value) {
            addCriterion("claimed_amount >", value, "claimedAmount");
            return (Criteria) this;
        }

        public Criteria andClaimedAmountGreaterThanOrEqualTo(Double value) {
            addCriterion("claimed_amount >=", value, "claimedAmount");
            return (Criteria) this;
        }

        public Criteria andClaimedAmountLessThan(Double value) {
            addCriterion("claimed_amount <", value, "claimedAmount");
            return (Criteria) this;
        }

        public Criteria andClaimedAmountLessThanOrEqualTo(Double value) {
            addCriterion("claimed_amount <=", value, "claimedAmount");
            return (Criteria) this;
        }

        public Criteria andClaimedAmountIn(List<Double> values) {
            addCriterion("claimed_amount in", values, "claimedAmount");
            return (Criteria) this;
        }

        public Criteria andClaimedAmountNotIn(List<Double> values) {
            addCriterion("claimed_amount not in", values, "claimedAmount");
            return (Criteria) this;
        }

        public Criteria andClaimedAmountBetween(Double value1, Double value2) {
            addCriterion("claimed_amount between", value1, value2, "claimedAmount");
            return (Criteria) this;
        }

        public Criteria andClaimedAmountNotBetween(Double value1, Double value2) {
            addCriterion("claimed_amount not between", value1, value2, "claimedAmount");
            return (Criteria) this;
        }

        public Criteria andTobeClaimedAmountIsNull() {
            addCriterion("tobe_claimed_amount is null");
            return (Criteria) this;
        }

        public Criteria andTobeClaimedAmountIsNotNull() {
            addCriterion("tobe_claimed_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTobeClaimedAmountEqualTo(Double value) {
            addCriterion("tobe_claimed_amount =", value, "tobeClaimedAmount");
            return (Criteria) this;
        }

        public Criteria andTobeClaimedAmountNotEqualTo(Double value) {
            addCriterion("tobe_claimed_amount <>", value, "tobeClaimedAmount");
            return (Criteria) this;
        }

        public Criteria andTobeClaimedAmountGreaterThan(Double value) {
            addCriterion("tobe_claimed_amount >", value, "tobeClaimedAmount");
            return (Criteria) this;
        }

        public Criteria andTobeClaimedAmountGreaterThanOrEqualTo(Double value) {
            addCriterion("tobe_claimed_amount >=", value, "tobeClaimedAmount");
            return (Criteria) this;
        }

        public Criteria andTobeClaimedAmountLessThan(Double value) {
            addCriterion("tobe_claimed_amount <", value, "tobeClaimedAmount");
            return (Criteria) this;
        }

        public Criteria andTobeClaimedAmountLessThanOrEqualTo(Double value) {
            addCriterion("tobe_claimed_amount <=", value, "tobeClaimedAmount");
            return (Criteria) this;
        }

        public Criteria andTobeClaimedAmountIn(List<Double> values) {
            addCriterion("tobe_claimed_amount in", values, "tobeClaimedAmount");
            return (Criteria) this;
        }

        public Criteria andTobeClaimedAmountNotIn(List<Double> values) {
            addCriterion("tobe_claimed_amount not in", values, "tobeClaimedAmount");
            return (Criteria) this;
        }

        public Criteria andTobeClaimedAmountBetween(Double value1, Double value2) {
            addCriterion("tobe_claimed_amount between", value1, value2, "tobeClaimedAmount");
            return (Criteria) this;
        }

        public Criteria andTobeClaimedAmountNotBetween(Double value1, Double value2) {
            addCriterion("tobe_claimed_amount not between", value1, value2, "tobeClaimedAmount");
            return (Criteria) this;
        }

        public Criteria andClaimTimesIsNull() {
            addCriterion("claim_times is null");
            return (Criteria) this;
        }

        public Criteria andClaimTimesIsNotNull() {
            addCriterion("claim_times is not null");
            return (Criteria) this;
        }

        public Criteria andClaimTimesEqualTo(Integer value) {
            addCriterion("claim_times =", value, "claimTimes");
            return (Criteria) this;
        }

        public Criteria andClaimTimesNotEqualTo(Integer value) {
            addCriterion("claim_times <>", value, "claimTimes");
            return (Criteria) this;
        }

        public Criteria andClaimTimesGreaterThan(Integer value) {
            addCriterion("claim_times >", value, "claimTimes");
            return (Criteria) this;
        }

        public Criteria andClaimTimesGreaterThanOrEqualTo(Integer value) {
            addCriterion("claim_times >=", value, "claimTimes");
            return (Criteria) this;
        }

        public Criteria andClaimTimesLessThan(Integer value) {
            addCriterion("claim_times <", value, "claimTimes");
            return (Criteria) this;
        }

        public Criteria andClaimTimesLessThanOrEqualTo(Integer value) {
            addCriterion("claim_times <=", value, "claimTimes");
            return (Criteria) this;
        }

        public Criteria andClaimTimesIn(List<Integer> values) {
            addCriterion("claim_times in", values, "claimTimes");
            return (Criteria) this;
        }

        public Criteria andClaimTimesNotIn(List<Integer> values) {
            addCriterion("claim_times not in", values, "claimTimes");
            return (Criteria) this;
        }

        public Criteria andClaimTimesBetween(Integer value1, Integer value2) {
            addCriterion("claim_times between", value1, value2, "claimTimes");
            return (Criteria) this;
        }

        public Criteria andClaimTimesNotBetween(Integer value1, Integer value2) {
            addCriterion("claim_times not between", value1, value2, "claimTimes");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIsNull() {
            addCriterion("update_date is null");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIsNotNull() {
            addCriterion("update_date is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateDateEqualTo(Date value) {
            addCriterion("update_date =", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotEqualTo(Date value) {
            addCriterion("update_date <>", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateGreaterThan(Date value) {
            addCriterion("update_date >", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("update_date >=", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateLessThan(Date value) {
            addCriterion("update_date <", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateLessThanOrEqualTo(Date value) {
            addCriterion("update_date <=", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIn(List<Date> values) {
            addCriterion("update_date in", values, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotIn(List<Date> values) {
            addCriterion("update_date not in", values, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateBetween(Date value1, Date value2) {
            addCriterion("update_date between", value1, value2, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotBetween(Date value1, Date value2) {
            addCriterion("update_date not between", value1, value2, "updateDate");
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