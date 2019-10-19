package com.aaroom.persistence;


import com.aaroom.beans.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntentionStoreMapper {

    Integer addIntentionStoreSurvey(@Param(value = "isdata")IntentionStoreSurvey intentionStoreSurvey);

    IntentionStoreSurvey getIntentionStoreByHotelName(@Param(value = "hotelName")String hotelName);

    void saveIntentionStoreInfo(@Param(value = "isi")IntentionStoreInfo intentionStoreInfo);

    void saveIntentionStoreInfoBed(@Param(value = "isiId")String isiId,
                                   @Param(value = "ibs")List<IsiBed> isiBed);

    List<IntentionStoreSurvey> getIntentionStoreData(@Param(value = "hotelName")String hotelName,
                                                     @Param(value = "owernName")String owernName,
                                                     @Param(value = "status")Integer status);

    IntentionStoreSurvey getIntentionStoreSurvey(@Param(value = "surveyId")String surveyId);

    void updateIntentionStoreSurvey(@Param(value = "iss")IntentionStoreSurvey intentionStoreSurvey);

    void addIntentionStoreContract(@Param(value = "isc")IntentionStoreContract intentionStoreContract);

    IntentionStoreInfo getIntentionStoreInfo(@Param(value = "surveyId")String surveyId);

    List<IsiBed> getIsiBedByIsiId(@Param(value = "isiId")String isiId);

    IntentionStoreContract getIntentionStoreContract(@Param(value = "isiId")String isiId);

    void updateIntentionStoreContract(@Param(value = "isc")IntentionStoreContract intentionStoreContract);

    void updateIntentionStoreInfo(@Param(value = "isi")IntentionStoreInfo intentionStoreInfo);

    void updateIsiBed(@Param(value = "ib")IsiBed isiBed);

    Province getProvinceInfo(@Param(value = "id")String hotelProvince);

    City getCityInfo(@Param(value = "id")String hotelCity);

    County getCountyInfo(@Param(value = "id")String hotelCounty);
}
