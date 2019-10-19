package com.aaroom.utils;

/**
 * @className Page
 * @Description   分页查询--常量
 * @Author 张赢
 * @Date 2018/11/5 19:27
 * @Version 1.0
 **/

public class Constants {
    //默认显示第一页
    public static final int DEFAULT_PAGE_NO = 1;
    //默认一页30条
    public static final int DEFAULT_PAGE_SIZE = 30;

    public static final String CLOTH = "C:";

    public static final String CLOTHRFID = "CRFID:";

    public static final String CLOTHTYPEIDS = "CTIDS";

    public static final String ALLCLOTHTYPEIDS = "ACTIDS";

    public static final String CLOTHTYPE = "CT:";

    public static final String MISSION = "M:";

    public static final String HOTEL = "H:";

    public static final String TEMPIDS = "TEMPIDS:";

    public static final String RESOURCE = "RES:";

    public static final String SHANGMEIOTATOKEN = "SMT";


    public enum ClothKind{

        //名称
        Type,

        //规格
        Size,

        //品牌
        Brand,

        //材质
        Material,

        //工艺
        Craft,

        //织纱
        Woven,

        //密度
        Density
    }


    public enum ClothErrorType{

        //丢失
        Lost,

        //破损
        Broken,

        //二维码破损
        QRBroken

    }

    public interface Session {

        String key = "employee_id";

    }
}
