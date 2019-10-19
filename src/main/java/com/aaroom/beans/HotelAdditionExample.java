package com.aaroom.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class HotelAdditionExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public HotelAdditionExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
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

        public Criteria andLongitudIsNull() {
            addCriterion("longitud is null");
            return (Criteria) this;
        }

        public Criteria andLongitudIsNotNull() {
            addCriterion("longitud is not null");
            return (Criteria) this;
        }

        public Criteria andLongitudEqualTo(String value) {
            addCriterion("longitud =", value, "longitud");
            return (Criteria) this;
        }

        public Criteria andLongitudNotEqualTo(String value) {
            addCriterion("longitud <>", value, "longitud");
            return (Criteria) this;
        }

        public Criteria andLongitudGreaterThan(String value) {
            addCriterion("longitud >", value, "longitud");
            return (Criteria) this;
        }

        public Criteria andLongitudGreaterThanOrEqualTo(String value) {
            addCriterion("longitud >=", value, "longitud");
            return (Criteria) this;
        }

        public Criteria andLongitudLessThan(String value) {
            addCriterion("longitud <", value, "longitud");
            return (Criteria) this;
        }

        public Criteria andLongitudLessThanOrEqualTo(String value) {
            addCriterion("longitud <=", value, "longitud");
            return (Criteria) this;
        }

        public Criteria andLongitudLike(String value) {
            addCriterion("longitud like", value, "longitud");
            return (Criteria) this;
        }

        public Criteria andLongitudNotLike(String value) {
            addCriterion("longitud not like", value, "longitud");
            return (Criteria) this;
        }

        public Criteria andLongitudIn(List<String> values) {
            addCriterion("longitud in", values, "longitud");
            return (Criteria) this;
        }

        public Criteria andLongitudNotIn(List<String> values) {
            addCriterion("longitud not in", values, "longitud");
            return (Criteria) this;
        }

        public Criteria andLongitudBetween(String value1, String value2) {
            addCriterion("longitud between", value1, value2, "longitud");
            return (Criteria) this;
        }

        public Criteria andLongitudNotBetween(String value1, String value2) {
            addCriterion("longitud not between", value1, value2, "longitud");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNull() {
            addCriterion("latitude is null");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNotNull() {
            addCriterion("latitude is not null");
            return (Criteria) this;
        }

        public Criteria andLatitudeEqualTo(String value) {
            addCriterion("latitude =", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotEqualTo(String value) {
            addCriterion("latitude <>", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThan(String value) {
            addCriterion("latitude >", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThanOrEqualTo(String value) {
            addCriterion("latitude >=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThan(String value) {
            addCriterion("latitude <", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThanOrEqualTo(String value) {
            addCriterion("latitude <=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLike(String value) {
            addCriterion("latitude like", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotLike(String value) {
            addCriterion("latitude not like", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeIn(List<String> values) {
            addCriterion("latitude in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotIn(List<String> values) {
            addCriterion("latitude not in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeBetween(String value1, String value2) {
            addCriterion("latitude between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotBetween(String value1, String value2) {
            addCriterion("latitude not between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andFaxesIsNull() {
            addCriterion("faxes is null");
            return (Criteria) this;
        }

        public Criteria andFaxesIsNotNull() {
            addCriterion("faxes is not null");
            return (Criteria) this;
        }

        public Criteria andFaxesEqualTo(String value) {
            addCriterion("faxes =", value, "faxes");
            return (Criteria) this;
        }

        public Criteria andFaxesNotEqualTo(String value) {
            addCriterion("faxes <>", value, "faxes");
            return (Criteria) this;
        }

        public Criteria andFaxesGreaterThan(String value) {
            addCriterion("faxes >", value, "faxes");
            return (Criteria) this;
        }

        public Criteria andFaxesGreaterThanOrEqualTo(String value) {
            addCriterion("faxes >=", value, "faxes");
            return (Criteria) this;
        }

        public Criteria andFaxesLessThan(String value) {
            addCriterion("faxes <", value, "faxes");
            return (Criteria) this;
        }

        public Criteria andFaxesLessThanOrEqualTo(String value) {
            addCriterion("faxes <=", value, "faxes");
            return (Criteria) this;
        }

        public Criteria andFaxesLike(String value) {
            addCriterion("faxes like", value, "faxes");
            return (Criteria) this;
        }

        public Criteria andFaxesNotLike(String value) {
            addCriterion("faxes not like", value, "faxes");
            return (Criteria) this;
        }

        public Criteria andFaxesIn(List<String> values) {
            addCriterion("faxes in", values, "faxes");
            return (Criteria) this;
        }

        public Criteria andFaxesNotIn(List<String> values) {
            addCriterion("faxes not in", values, "faxes");
            return (Criteria) this;
        }

        public Criteria andFaxesBetween(String value1, String value2) {
            addCriterion("faxes between", value1, value2, "faxes");
            return (Criteria) this;
        }

        public Criteria andFaxesNotBetween(String value1, String value2) {
            addCriterion("faxes not between", value1, value2, "faxes");
            return (Criteria) this;
        }

        public Criteria andPostalCodeIsNull() {
            addCriterion("postal_code is null");
            return (Criteria) this;
        }

        public Criteria andPostalCodeIsNotNull() {
            addCriterion("postal_code is not null");
            return (Criteria) this;
        }

        public Criteria andPostalCodeEqualTo(String value) {
            addCriterion("postal_code =", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeNotEqualTo(String value) {
            addCriterion("postal_code <>", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeGreaterThan(String value) {
            addCriterion("postal_code >", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeGreaterThanOrEqualTo(String value) {
            addCriterion("postal_code >=", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeLessThan(String value) {
            addCriterion("postal_code <", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeLessThanOrEqualTo(String value) {
            addCriterion("postal_code <=", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeLike(String value) {
            addCriterion("postal_code like", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeNotLike(String value) {
            addCriterion("postal_code not like", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeIn(List<String> values) {
            addCriterion("postal_code in", values, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeNotIn(List<String> values) {
            addCriterion("postal_code not in", values, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeBetween(String value1, String value2) {
            addCriterion("postal_code between", value1, value2, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeNotBetween(String value1, String value2) {
            addCriterion("postal_code not between", value1, value2, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressIsNull() {
            addCriterion("place_address is null");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressIsNotNull() {
            addCriterion("place_address is not null");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressEqualTo(String value) {
            addCriterion("place_address =", value, "placeAddress");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressNotEqualTo(String value) {
            addCriterion("place_address <>", value, "placeAddress");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressGreaterThan(String value) {
            addCriterion("place_address >", value, "placeAddress");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressGreaterThanOrEqualTo(String value) {
            addCriterion("place_address >=", value, "placeAddress");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressLessThan(String value) {
            addCriterion("place_address <", value, "placeAddress");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressLessThanOrEqualTo(String value) {
            addCriterion("place_address <=", value, "placeAddress");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressLike(String value) {
            addCriterion("place_address like", value, "placeAddress");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressNotLike(String value) {
            addCriterion("place_address not like", value, "placeAddress");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressIn(List<String> values) {
            addCriterion("place_address in", values, "placeAddress");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressNotIn(List<String> values) {
            addCriterion("place_address not in", values, "placeAddress");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressBetween(String value1, String value2) {
            addCriterion("place_address between", value1, value2, "placeAddress");
            return (Criteria) this;
        }

        public Criteria andPlaceAddressNotBetween(String value1, String value2) {
            addCriterion("place_address not between", value1, value2, "placeAddress");
            return (Criteria) this;
        }

        public Criteria andOpeningTimeIsNull() {
            addCriterion("opening_time is null");
            return (Criteria) this;
        }

        public Criteria andOpeningTimeIsNotNull() {
            addCriterion("opening_time is not null");
            return (Criteria) this;
        }

        public Criteria andOpeningTimeEqualTo(Date value) {
            addCriterionForJDBCDate("opening_time =", value, "openingTime");
            return (Criteria) this;
        }

        public Criteria andOpeningTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("opening_time <>", value, "openingTime");
            return (Criteria) this;
        }

        public Criteria andOpeningTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("opening_time >", value, "openingTime");
            return (Criteria) this;
        }

        public Criteria andOpeningTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("opening_time >=", value, "openingTime");
            return (Criteria) this;
        }

        public Criteria andOpeningTimeLessThan(Date value) {
            addCriterionForJDBCDate("opening_time <", value, "openingTime");
            return (Criteria) this;
        }

        public Criteria andOpeningTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("opening_time <=", value, "openingTime");
            return (Criteria) this;
        }

        public Criteria andOpeningTimeIn(List<Date> values) {
            addCriterionForJDBCDate("opening_time in", values, "openingTime");
            return (Criteria) this;
        }

        public Criteria andOpeningTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("opening_time not in", values, "openingTime");
            return (Criteria) this;
        }

        public Criteria andOpeningTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("opening_time between", value1, value2, "openingTime");
            return (Criteria) this;
        }

        public Criteria andOpeningTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("opening_time not between", value1, value2, "openingTime");
            return (Criteria) this;
        }

        public Criteria andRenovationTimeIsNull() {
            addCriterion("renovation_time is null");
            return (Criteria) this;
        }

        public Criteria andRenovationTimeIsNotNull() {
            addCriterion("renovation_time is not null");
            return (Criteria) this;
        }

        public Criteria andRenovationTimeEqualTo(Date value) {
            addCriterionForJDBCDate("renovation_time =", value, "renovationTime");
            return (Criteria) this;
        }

        public Criteria andRenovationTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("renovation_time <>", value, "renovationTime");
            return (Criteria) this;
        }

        public Criteria andRenovationTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("renovation_time >", value, "renovationTime");
            return (Criteria) this;
        }

        public Criteria andRenovationTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("renovation_time >=", value, "renovationTime");
            return (Criteria) this;
        }

        public Criteria andRenovationTimeLessThan(Date value) {
            addCriterionForJDBCDate("renovation_time <", value, "renovationTime");
            return (Criteria) this;
        }

        public Criteria andRenovationTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("renovation_time <=", value, "renovationTime");
            return (Criteria) this;
        }

        public Criteria andRenovationTimeIn(List<Date> values) {
            addCriterionForJDBCDate("renovation_time in", values, "renovationTime");
            return (Criteria) this;
        }

        public Criteria andRenovationTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("renovation_time not in", values, "renovationTime");
            return (Criteria) this;
        }

        public Criteria andRenovationTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("renovation_time between", value1, value2, "renovationTime");
            return (Criteria) this;
        }

        public Criteria andRenovationTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("renovation_time not between", value1, value2, "renovationTime");
            return (Criteria) this;
        }

        public Criteria andHotelBuildIsNull() {
            addCriterion("hotel_build is null");
            return (Criteria) this;
        }

        public Criteria andHotelBuildIsNotNull() {
            addCriterion("hotel_build is not null");
            return (Criteria) this;
        }

        public Criteria andHotelBuildEqualTo(Integer value) {
            addCriterion("hotel_build =", value, "hotelBuild");
            return (Criteria) this;
        }

        public Criteria andHotelBuildNotEqualTo(Integer value) {
            addCriterion("hotel_build <>", value, "hotelBuild");
            return (Criteria) this;
        }

        public Criteria andHotelBuildGreaterThan(Integer value) {
            addCriterion("hotel_build >", value, "hotelBuild");
            return (Criteria) this;
        }

        public Criteria andHotelBuildGreaterThanOrEqualTo(Integer value) {
            addCriterion("hotel_build >=", value, "hotelBuild");
            return (Criteria) this;
        }

        public Criteria andHotelBuildLessThan(Integer value) {
            addCriterion("hotel_build <", value, "hotelBuild");
            return (Criteria) this;
        }

        public Criteria andHotelBuildLessThanOrEqualTo(Integer value) {
            addCriterion("hotel_build <=", value, "hotelBuild");
            return (Criteria) this;
        }

        public Criteria andHotelBuildIn(List<Integer> values) {
            addCriterion("hotel_build in", values, "hotelBuild");
            return (Criteria) this;
        }

        public Criteria andHotelBuildNotIn(List<Integer> values) {
            addCriterion("hotel_build not in", values, "hotelBuild");
            return (Criteria) this;
        }

        public Criteria andHotelBuildBetween(Integer value1, Integer value2) {
            addCriterion("hotel_build between", value1, value2, "hotelBuild");
            return (Criteria) this;
        }

        public Criteria andHotelBuildNotBetween(Integer value1, Integer value2) {
            addCriterion("hotel_build not between", value1, value2, "hotelBuild");
            return (Criteria) this;
        }

        public Criteria andReceptionStatusIsNull() {
            addCriterion("reception_status is null");
            return (Criteria) this;
        }

        public Criteria andReceptionStatusIsNotNull() {
            addCriterion("reception_status is not null");
            return (Criteria) this;
        }

        public Criteria andReceptionStatusEqualTo(Integer value) {
            addCriterion("reception_status =", value, "receptionStatus");
            return (Criteria) this;
        }

        public Criteria andReceptionStatusNotEqualTo(Integer value) {
            addCriterion("reception_status <>", value, "receptionStatus");
            return (Criteria) this;
        }

        public Criteria andReceptionStatusGreaterThan(Integer value) {
            addCriterion("reception_status >", value, "receptionStatus");
            return (Criteria) this;
        }

        public Criteria andReceptionStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("reception_status >=", value, "receptionStatus");
            return (Criteria) this;
        }

        public Criteria andReceptionStatusLessThan(Integer value) {
            addCriterion("reception_status <", value, "receptionStatus");
            return (Criteria) this;
        }

        public Criteria andReceptionStatusLessThanOrEqualTo(Integer value) {
            addCriterion("reception_status <=", value, "receptionStatus");
            return (Criteria) this;
        }

        public Criteria andReceptionStatusIn(List<Integer> values) {
            addCriterion("reception_status in", values, "receptionStatus");
            return (Criteria) this;
        }

        public Criteria andReceptionStatusNotIn(List<Integer> values) {
            addCriterion("reception_status not in", values, "receptionStatus");
            return (Criteria) this;
        }

        public Criteria andReceptionStatusBetween(Integer value1, Integer value2) {
            addCriterion("reception_status between", value1, value2, "receptionStatus");
            return (Criteria) this;
        }

        public Criteria andReceptionStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("reception_status not between", value1, value2, "receptionStatus");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationIsNull() {
            addCriterion("channel_cooperation is null");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationIsNotNull() {
            addCriterion("channel_cooperation is not null");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationEqualTo(String value) {
            addCriterion("channel_cooperation =", value, "channelCooperation");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationNotEqualTo(String value) {
            addCriterion("channel_cooperation <>", value, "channelCooperation");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationGreaterThan(String value) {
            addCriterion("channel_cooperation >", value, "channelCooperation");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationGreaterThanOrEqualTo(String value) {
            addCriterion("channel_cooperation >=", value, "channelCooperation");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationLessThan(String value) {
            addCriterion("channel_cooperation <", value, "channelCooperation");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationLessThanOrEqualTo(String value) {
            addCriterion("channel_cooperation <=", value, "channelCooperation");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationLike(String value) {
            addCriterion("channel_cooperation like", value, "channelCooperation");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationNotLike(String value) {
            addCriterion("channel_cooperation not like", value, "channelCooperation");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationIn(List<String> values) {
            addCriterion("channel_cooperation in", values, "channelCooperation");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationNotIn(List<String> values) {
            addCriterion("channel_cooperation not in", values, "channelCooperation");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationBetween(String value1, String value2) {
            addCriterion("channel_cooperation between", value1, value2, "channelCooperation");
            return (Criteria) this;
        }

        public Criteria andChannelCooperationNotBetween(String value1, String value2) {
            addCriterion("channel_cooperation not between", value1, value2, "channelCooperation");
            return (Criteria) this;
        }

        public Criteria andPaymentIsNull() {
            addCriterion("payment is null");
            return (Criteria) this;
        }

        public Criteria andPaymentIsNotNull() {
            addCriterion("payment is not null");
            return (Criteria) this;
        }

        public Criteria andPaymentEqualTo(String value) {
            addCriterion("payment =", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentNotEqualTo(String value) {
            addCriterion("payment <>", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentGreaterThan(String value) {
            addCriterion("payment >", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentGreaterThanOrEqualTo(String value) {
            addCriterion("payment >=", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentLessThan(String value) {
            addCriterion("payment <", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentLessThanOrEqualTo(String value) {
            addCriterion("payment <=", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentLike(String value) {
            addCriterion("payment like", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentNotLike(String value) {
            addCriterion("payment not like", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentIn(List<String> values) {
            addCriterion("payment in", values, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentNotIn(List<String> values) {
            addCriterion("payment not in", values, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentBetween(String value1, String value2) {
            addCriterion("payment between", value1, value2, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentNotBetween(String value1, String value2) {
            addCriterion("payment not between", value1, value2, "payment");
            return (Criteria) this;
        }

        public Criteria andHotelIntroIsNull() {
            addCriterion("hotel_intro is null");
            return (Criteria) this;
        }

        public Criteria andHotelIntroIsNotNull() {
            addCriterion("hotel_intro is not null");
            return (Criteria) this;
        }

        public Criteria andHotelIntroEqualTo(String value) {
            addCriterion("hotel_intro =", value, "hotelIntro");
            return (Criteria) this;
        }

        public Criteria andHotelIntroNotEqualTo(String value) {
            addCriterion("hotel_intro <>", value, "hotelIntro");
            return (Criteria) this;
        }

        public Criteria andHotelIntroGreaterThan(String value) {
            addCriterion("hotel_intro >", value, "hotelIntro");
            return (Criteria) this;
        }

        public Criteria andHotelIntroGreaterThanOrEqualTo(String value) {
            addCriterion("hotel_intro >=", value, "hotelIntro");
            return (Criteria) this;
        }

        public Criteria andHotelIntroLessThan(String value) {
            addCriterion("hotel_intro <", value, "hotelIntro");
            return (Criteria) this;
        }

        public Criteria andHotelIntroLessThanOrEqualTo(String value) {
            addCriterion("hotel_intro <=", value, "hotelIntro");
            return (Criteria) this;
        }

        public Criteria andHotelIntroLike(String value) {
            addCriterion("hotel_intro like", value, "hotelIntro");
            return (Criteria) this;
        }

        public Criteria andHotelIntroNotLike(String value) {
            addCriterion("hotel_intro not like", value, "hotelIntro");
            return (Criteria) this;
        }

        public Criteria andHotelIntroIn(List<String> values) {
            addCriterion("hotel_intro in", values, "hotelIntro");
            return (Criteria) this;
        }

        public Criteria andHotelIntroNotIn(List<String> values) {
            addCriterion("hotel_intro not in", values, "hotelIntro");
            return (Criteria) this;
        }

        public Criteria andHotelIntroBetween(String value1, String value2) {
            addCriterion("hotel_intro between", value1, value2, "hotelIntro");
            return (Criteria) this;
        }

        public Criteria andHotelIntroNotBetween(String value1, String value2) {
            addCriterion("hotel_intro not between", value1, value2, "hotelIntro");
            return (Criteria) this;
        }

        public Criteria andGuideBookIsNull() {
            addCriterion("guide_book is null");
            return (Criteria) this;
        }

        public Criteria andGuideBookIsNotNull() {
            addCriterion("guide_book is not null");
            return (Criteria) this;
        }

        public Criteria andGuideBookEqualTo(String value) {
            addCriterion("guide_book =", value, "guideBook");
            return (Criteria) this;
        }

        public Criteria andGuideBookNotEqualTo(String value) {
            addCriterion("guide_book <>", value, "guideBook");
            return (Criteria) this;
        }

        public Criteria andGuideBookGreaterThan(String value) {
            addCriterion("guide_book >", value, "guideBook");
            return (Criteria) this;
        }

        public Criteria andGuideBookGreaterThanOrEqualTo(String value) {
            addCriterion("guide_book >=", value, "guideBook");
            return (Criteria) this;
        }

        public Criteria andGuideBookLessThan(String value) {
            addCriterion("guide_book <", value, "guideBook");
            return (Criteria) this;
        }

        public Criteria andGuideBookLessThanOrEqualTo(String value) {
            addCriterion("guide_book <=", value, "guideBook");
            return (Criteria) this;
        }

        public Criteria andGuideBookLike(String value) {
            addCriterion("guide_book like", value, "guideBook");
            return (Criteria) this;
        }

        public Criteria andGuideBookNotLike(String value) {
            addCriterion("guide_book not like", value, "guideBook");
            return (Criteria) this;
        }

        public Criteria andGuideBookIn(List<String> values) {
            addCriterion("guide_book in", values, "guideBook");
            return (Criteria) this;
        }

        public Criteria andGuideBookNotIn(List<String> values) {
            addCriterion("guide_book not in", values, "guideBook");
            return (Criteria) this;
        }

        public Criteria andGuideBookBetween(String value1, String value2) {
            addCriterion("guide_book between", value1, value2, "guideBook");
            return (Criteria) this;
        }

        public Criteria andGuideBookNotBetween(String value1, String value2) {
            addCriterion("guide_book not between", value1, value2, "guideBook");
            return (Criteria) this;
        }

        public Criteria andWarmPromptIsNull() {
            addCriterion("warm_prompt is null");
            return (Criteria) this;
        }

        public Criteria andWarmPromptIsNotNull() {
            addCriterion("warm_prompt is not null");
            return (Criteria) this;
        }

        public Criteria andWarmPromptEqualTo(String value) {
            addCriterion("warm_prompt =", value, "warmPrompt");
            return (Criteria) this;
        }

        public Criteria andWarmPromptNotEqualTo(String value) {
            addCriterion("warm_prompt <>", value, "warmPrompt");
            return (Criteria) this;
        }

        public Criteria andWarmPromptGreaterThan(String value) {
            addCriterion("warm_prompt >", value, "warmPrompt");
            return (Criteria) this;
        }

        public Criteria andWarmPromptGreaterThanOrEqualTo(String value) {
            addCriterion("warm_prompt >=", value, "warmPrompt");
            return (Criteria) this;
        }

        public Criteria andWarmPromptLessThan(String value) {
            addCriterion("warm_prompt <", value, "warmPrompt");
            return (Criteria) this;
        }

        public Criteria andWarmPromptLessThanOrEqualTo(String value) {
            addCriterion("warm_prompt <=", value, "warmPrompt");
            return (Criteria) this;
        }

        public Criteria andWarmPromptLike(String value) {
            addCriterion("warm_prompt like", value, "warmPrompt");
            return (Criteria) this;
        }

        public Criteria andWarmPromptNotLike(String value) {
            addCriterion("warm_prompt not like", value, "warmPrompt");
            return (Criteria) this;
        }

        public Criteria andWarmPromptIn(List<String> values) {
            addCriterion("warm_prompt in", values, "warmPrompt");
            return (Criteria) this;
        }

        public Criteria andWarmPromptNotIn(List<String> values) {
            addCriterion("warm_prompt not in", values, "warmPrompt");
            return (Criteria) this;
        }

        public Criteria andWarmPromptBetween(String value1, String value2) {
            addCriterion("warm_prompt between", value1, value2, "warmPrompt");
            return (Criteria) this;
        }

        public Criteria andWarmPromptNotBetween(String value1, String value2) {
            addCriterion("warm_prompt not between", value1, value2, "warmPrompt");
            return (Criteria) this;
        }

        public Criteria andHotelServeIsNull() {
            addCriterion("hotel_serve is null");
            return (Criteria) this;
        }

        public Criteria andHotelServeIsNotNull() {
            addCriterion("hotel_serve is not null");
            return (Criteria) this;
        }

        public Criteria andHotelServeEqualTo(String value) {
            addCriterion("hotel_serve =", value, "hotelServe");
            return (Criteria) this;
        }

        public Criteria andHotelServeNotEqualTo(String value) {
            addCriterion("hotel_serve <>", value, "hotelServe");
            return (Criteria) this;
        }

        public Criteria andHotelServeGreaterThan(String value) {
            addCriterion("hotel_serve >", value, "hotelServe");
            return (Criteria) this;
        }

        public Criteria andHotelServeGreaterThanOrEqualTo(String value) {
            addCriterion("hotel_serve >=", value, "hotelServe");
            return (Criteria) this;
        }

        public Criteria andHotelServeLessThan(String value) {
            addCriterion("hotel_serve <", value, "hotelServe");
            return (Criteria) this;
        }

        public Criteria andHotelServeLessThanOrEqualTo(String value) {
            addCriterion("hotel_serve <=", value, "hotelServe");
            return (Criteria) this;
        }

        public Criteria andHotelServeLike(String value) {
            addCriterion("hotel_serve like", value, "hotelServe");
            return (Criteria) this;
        }

        public Criteria andHotelServeNotLike(String value) {
            addCriterion("hotel_serve not like", value, "hotelServe");
            return (Criteria) this;
        }

        public Criteria andHotelServeIn(List<String> values) {
            addCriterion("hotel_serve in", values, "hotelServe");
            return (Criteria) this;
        }

        public Criteria andHotelServeNotIn(List<String> values) {
            addCriterion("hotel_serve not in", values, "hotelServe");
            return (Criteria) this;
        }

        public Criteria andHotelServeBetween(String value1, String value2) {
            addCriterion("hotel_serve between", value1, value2, "hotelServe");
            return (Criteria) this;
        }

        public Criteria andHotelServeNotBetween(String value1, String value2) {
            addCriterion("hotel_serve not between", value1, value2, "hotelServe");
            return (Criteria) this;
        }

        public Criteria andParkPriceIsNull() {
            addCriterion("park_price is null");
            return (Criteria) this;
        }

        public Criteria andParkPriceIsNotNull() {
            addCriterion("park_price is not null");
            return (Criteria) this;
        }

        public Criteria andParkPriceEqualTo(Double value) {
            addCriterion("park_price =", value, "parkPrice");
            return (Criteria) this;
        }

        public Criteria andParkPriceNotEqualTo(Double value) {
            addCriterion("park_price <>", value, "parkPrice");
            return (Criteria) this;
        }

        public Criteria andParkPriceGreaterThan(Double value) {
            addCriterion("park_price >", value, "parkPrice");
            return (Criteria) this;
        }

        public Criteria andParkPriceGreaterThanOrEqualTo(Double value) {
            addCriterion("park_price >=", value, "parkPrice");
            return (Criteria) this;
        }

        public Criteria andParkPriceLessThan(Double value) {
            addCriterion("park_price <", value, "parkPrice");
            return (Criteria) this;
        }

        public Criteria andParkPriceLessThanOrEqualTo(Double value) {
            addCriterion("park_price <=", value, "parkPrice");
            return (Criteria) this;
        }

        public Criteria andParkPriceIn(List<Double> values) {
            addCriterion("park_price in", values, "parkPrice");
            return (Criteria) this;
        }

        public Criteria andParkPriceNotIn(List<Double> values) {
            addCriterion("park_price not in", values, "parkPrice");
            return (Criteria) this;
        }

        public Criteria andParkPriceBetween(Double value1, Double value2) {
            addCriterion("park_price between", value1, value2, "parkPrice");
            return (Criteria) this;
        }

        public Criteria andParkPriceNotBetween(Double value1, Double value2) {
            addCriterion("park_price not between", value1, value2, "parkPrice");
            return (Criteria) this;
        }

        public Criteria andBreakfastPriceIsNull() {
            addCriterion("breakfast_price is null");
            return (Criteria) this;
        }

        public Criteria andBreakfastPriceIsNotNull() {
            addCriterion("breakfast_price is not null");
            return (Criteria) this;
        }

        public Criteria andBreakfastPriceEqualTo(Double value) {
            addCriterion("breakfast_price =", value, "breakfastPrice");
            return (Criteria) this;
        }

        public Criteria andBreakfastPriceNotEqualTo(Double value) {
            addCriterion("breakfast_price <>", value, "breakfastPrice");
            return (Criteria) this;
        }

        public Criteria andBreakfastPriceGreaterThan(Double value) {
            addCriterion("breakfast_price >", value, "breakfastPrice");
            return (Criteria) this;
        }

        public Criteria andBreakfastPriceGreaterThanOrEqualTo(Double value) {
            addCriterion("breakfast_price >=", value, "breakfastPrice");
            return (Criteria) this;
        }

        public Criteria andBreakfastPriceLessThan(Double value) {
            addCriterion("breakfast_price <", value, "breakfastPrice");
            return (Criteria) this;
        }

        public Criteria andBreakfastPriceLessThanOrEqualTo(Double value) {
            addCriterion("breakfast_price <=", value, "breakfastPrice");
            return (Criteria) this;
        }

        public Criteria andBreakfastPriceIn(List<Double> values) {
            addCriterion("breakfast_price in", values, "breakfastPrice");
            return (Criteria) this;
        }

        public Criteria andBreakfastPriceNotIn(List<Double> values) {
            addCriterion("breakfast_price not in", values, "breakfastPrice");
            return (Criteria) this;
        }

        public Criteria andBreakfastPriceBetween(Double value1, Double value2) {
            addCriterion("breakfast_price between", value1, value2, "breakfastPrice");
            return (Criteria) this;
        }

        public Criteria andBreakfastPriceNotBetween(Double value1, Double value2) {
            addCriterion("breakfast_price not between", value1, value2, "breakfastPrice");
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