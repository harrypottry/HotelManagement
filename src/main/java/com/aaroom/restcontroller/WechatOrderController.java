package com.aaroom.restcontroller;

import com.aaroom.model.ShangmeiRetView;
import com.aaroom.service.HotelBaseService;
import com.aaroom.service.ShangmeiOTAService;
import com.aaroom.wechat.pay.MyWXpayConfig;
import com.aaroom.wechat.pay.WXPay;
import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by sosoda on 2019/2/20.
 * C端小程序后台接口命名规则：
 * /WeChat/console/api/+XXX
 */
@RestController
public class WechatOrderController {

    @Autowired
    private ShangmeiOTAService shangmeiOTAService;
    
    @Autowired
    private HotelBaseService hotelBaseService;

    private final static String Provinces ="http://120.92.148.25:8099/rest/Geo/Provinces";

    private final static String cities ="http://120.92.148.25:8099/rest/Geo/Cities";

    private final static String TENCENTKEY ="HXXBZ-NM7WJ-FH3FA-KHY6Y-B4ONF-APFJ7";

    private final static String TENCENTMAP ="http://apis.map.qq.com/ws/geocoder/v1/?location=";

    private final static String AVAIL = "http://120.92.148.25:8099/rest/PMSGateway/AAWECHAT/avail"; //查询房态

    @GetMapping("/Provinces")
    public Object Provinces(){
        return shangmeiOTAService.exchange(Provinces, HttpMethod.GET, null, ShangmeiRetView.class);
    }


    @GetMapping("/cities")
    public Object cities(@RequestParam(value = "province_code",required = false) String province_code,
                         @RequestParam(value = "city_code",required = false)  String city_code){
        Map<String, String> params = new HashMap<>();
        params.put("province_code",province_code);
        params.put("city_code",city_code);
       return shangmeiOTAService.exchange(cities, HttpMethod.GET, params, ShangmeiRetView.class);
    }


    @GetMapping("/hotels")
    public Object hotels(HttpServletRequest request){
        Map<String, String> params = new HashMap<>();
        params.put("page","1");
        params.put("per_page","20");

        ShangmeiRetView exchange = shangmeiOTAService.exchange("http://120.92.148.25:8099/rest/static/hotel/list", HttpMethod.GET, params, ShangmeiRetView.class);
        return exchange;
    }
    //房态，查询近30天内的房量以及价格
    @GetMapping("/hotel")
    public Object hotel(){
        Map<String, String> params = new HashMap<>();
        params.put("hotel_code","66667");
        params.put("start","2019-03-01");
        params.put("end","2019-03-02");
        String exchange = shangmeiOTAService.exchange(AVAIL, HttpMethod.POST, params, String.class);
        return exchange;
    }

    @PostMapping("/order")
    public String order(HttpServletRequest request){
        Map<String, String> params = new HashMap<>();
        params.put("channelorderno","02732");
        params.put("customername","2019-02-20");
        params.put("contactname","2019-02-21");
        params.put("contactphone","2019-02-21");
        params.put("hotelcode","2019-02-21");
        params.put("roomnum","2019-02-21");
        params.put("start","2019-02-21");
        params.put("end","2019-02-21");
        params.put("arrival","2019-02-21");
        params.put("pricetotal","2019-02-21");
        params.put("pricelist","2019-02-21");


        String exchange = shangmeiOTAService.exchange("http://120.92.148.25:8099/rest/PMSGateway/AAWECHAT/avail", HttpMethod.POST, params, String.class);
        return exchange;
    }

    @PostMapping("/selectorder")
    public ShangmeiRetView selectorder(HttpServletRequest request){
        Map<String, String> params = new HashMap<>();
        params.put("order_no","JD01313160506003001");
        //params.put("room_type_code","012733");
      //  params.put("channel_order_no","2019-02-20");

        ShangmeiRetView exchange = shangmeiOTAService.exchange("http://120.92.148.25:8099/rest/PMSGateway/AAWECHAT/order/query", HttpMethod.POST, params, ShangmeiRetView.class);
        return exchange;
    }


    @PostMapping("/roomtype")
    public ShangmeiRetView roomtype(){
        Map<String, String> params = new HashMap<>();
        params.put("hotelcode","01273");
        params.put("roomtype_code","012731");

        ShangmeiRetView exchange = shangmeiOTAService.exchange("http://120.92.148.25:8099/rest/static/hotel/roomtype/:hotel_code/:room_type_code", HttpMethod.POST, JSON.toJSONString(params), ShangmeiRetView.class);
        return exchange;
    }

    @PostMapping("/roomtypelist")
    public ShangmeiRetView roomtypelist(){
        Map<String, String> params = new HashMap<>();
        params.put("hotelcode","01273");


        ShangmeiRetView exchange = shangmeiOTAService.exchange("http://120.92.148.25:8099/rest/static/hotel/roomtype/:hotel_code", HttpMethod.POST, JSON.toJSONString(params), ShangmeiRetView.class);
        return exchange;
    }

    @PostMapping("/createOrder")
    public Object createOrder(HttpServletRequest request
                                ,@RequestBody Map<String, String> data) throws Exception {

        WXPay wxPay = new WXPay(new MyWXpayConfig());
        data.put("body","测试订单");
        data.put("out_trade_no",new Date().getTime()+"");
        data.put("notify_url", "https://uc.aaroom.cc");
        data.put("trade_type", "JSAPI");
        data.put("total_fee", "0.01");
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        data.put("spbill_create_ip",ip);
        Map<String, String> stringStringMap = wxPay.unifiedOrder(data);
        return stringStringMap;

    }

    @GetMapping("/WeChat/console/api/getCityNameByLatLng")
    public Object getCityNameByLatLng(@RequestParam(value = "lng")String lng,
                                      @RequestParam(value = "lat") String lat) {
        Map<String, Object> resultMap = new HashMap<>();
        String result = "";
        //测试数据格式
        // https://apis.map.qq.com/ws/geocoder/v1/?location=39.984154,116.307490&key=HXXBZ-NM7WJ-FH3FA-KHY6Y-B4ONF-APFJ7
        // 参数解释：lng：经度，lat：维度。KEY：腾讯地图key，get_poi：返回状态。1返回，0不返回
        String urlString = TENCENTMAP + lat + "," + lng + "&key=" + TENCENTKEY + "&get_poi=1";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true); // 腾讯地图使用GET
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line; // 获取地址解析结果
            while ((line = in.readLine()) != null) {
                 result += line + "\n";
            }
            in.close();
        } catch (Exception e) {
            e.getMessage();
        }
        // 转JSON格式
        JSONObject jsonObject = JSONObject.fromObject(result).getJSONObject("result"); // 获取地址（行政区划信息） 包含有国籍，省份，城市
        if (jsonObject.size() != 0){
            JSONObject adInfo = jsonObject.getJSONObject("ad_info");
            resultMap.put("city", adInfo.get("city"));
        }else {
            resultMap = JSONObject.fromObject(result);
        }
        return resultMap;
    }
    @GetMapping("/WeChat/console/api/queryHotelList")
    public Object queryHotelList(@RequestParam(value = "cityName",defaultValue = "青岛",required = false)String cityName,
                                 @RequestParam(value = "beginDate")String beginDate,
                                 @RequestParam(value = "endDate")String endDate,
                                 @RequestParam(value = "sortOpt",defaultValue = "",required = false)Integer sortOpt,
                                 @RequestParam(value = "sortType",defaultValue = "",required = false)Integer sortType) {
        //sortType : 默认空，2：价格3：距离 1：不知道
        //.按距离：1：远到近排序2：近到远排序
        //.按价格：1：高到低 2：低到高
        Map<String,Object> ret = new LinkedHashMap();
        ret.put("hotelList",new ArrayList<>());

        //1.获取所有aaroom酒店
        Map param = new HashMap();
        param.put("cityName",cityName);
        List<Map<String, Object>> hotelList = hotelBaseService.queryHotelList(param);
        if (hotelList.size()==0){
            ret.put("hotelCount ",0);
            return ret;
        }

        List usefulHotelList = new ArrayList(); //存储所有可用酒店
        for (Map<String, Object> hotel:hotelList){
            String smhotel_id = (String)hotel.get("smhotel_id");

            //2.通过OTA的api校验用户指定的入住时间段内是否有可用酒店房间
            Map<String, String> params = new HashMap<>();
            params.put("hotel_code",smhotel_id);
            //日期校验：最近30天的数据
            params.put("start",beginDate);
            params.put("end",endDate);
            Map hotelPriceMap = shangmeiOTAService.exchange(AVAIL, HttpMethod.POST, params, Map.class);

            //查询当前酒店所有房型是否全部可用，全部可用房型为0，则不推荐此酒店
            List<Map> priceList = (List) hotelPriceMap.get("prices");
            List tempList = new ArrayList();
            List roomPriceList = new ArrayList();
            int usefulRoom =0 ;
            for (Map map:priceList){
                List<Map> room_type_prices = (List<Map>)map.get("room_type_prices");
                Map finalmap = room_type_prices.get(0);
                int num = (int)finalmap.get("num"); //此房型的可用数量
                int price =(int) finalmap.get("price");//此房型房价
                usefulRoom+=num;
                tempList.add(num);
                roomPriceList.add(price);
            }
            if (!tempList.contains(0) && tempList.size()>0){
                usefulHotelList.add(smhotel_id);//说明此酒店有可用房间，添加到可用酒店列表中
                //找出此酒店所有房型最低价格
                Collections.sort(roomPriceList);
                List myList = (List)ret.get("hotelList");
                Map dataMap = new LinkedHashMap();
                dataMap.put("brandName","AA ROOM");
                dataMap.put("areaName",hotel.get("county"));
                dataMap.put("minPrice",roomPriceList.get(0));
                dataMap.put("hotelName",hotel.get("hotel_name"));
                dataMap.put("restMsg","仅剩下"+usefulRoom+"间客房");
                myList.add(dataMap);
            }
        }
        ret.put("hotelCount",usefulHotelList.size());
         //3.通过pms数据库查询酒店订房情况，房价信息(暂时不写)
        return ret;
    }
}
