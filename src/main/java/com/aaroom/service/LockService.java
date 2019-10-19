package com.aaroom.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by sosoda on 2017/9/28.
 */
@Service
public class LockService {

    @Autowired
    private RestTemplate restTemplate;

    private final static String DES = "DES";

    /**
     *
     */
    public String desEncode(String password) {

        String encryptString= encrypt(password,"K54hd4ke");
        return encryptString;
    }


    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key){
        byte[] bt = new byte[0];
        try {
            bt = encrypt(data.getBytes(), key.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String strs = DatatypeConverter.printHexBinary(bt);
        return strs;
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    /**
     * 访问果加所需要的http头
     */
    public HttpHeaders createHttpHeader(){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add("version", "1.1");
        requestHeaders.add("s_id", UUID.randomUUID().toString());
        requestHeaders.add("access_token", loginHuohe());
        return requestHeaders;
    }

    /**
     * 获取access_token
     * @return
     */
    public String loginHuohe(){
        if(StringUtils.isNotEmpty(access_token)&& new Date().getTime()- endTime.getTime()<18000000) {
            return access_token;
        }

        //拿到header信息
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add("version", "1.1");
        requestHeaders.add("s_id", UUID.randomUUID().toString());
        JSONObject content = new JSONObject();
        content.put("account", account);
        content.put("password",  desEncode(password));
        HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(content, requestHeaders);
        String result = restTemplate.postForObject(loginAddress, requestEntity, String.class);
        JSONObject resultObject = JSON.parseObject(result);
        access_token = resultObject.getJSONObject("data").getString("access_token");


        endTime = new Date();
        return access_token;
    }


    /**
     * 生成房间密码
     */
    public void pwdAdd(){
        JSONObject content = new JSONObject();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        content.put("valid_time_start", new Date().getTime());
        content.put("valid_time_end", cal.getTime());
        content.put("pwd_user_mobile", "13998420803");
        Random random = new Random();
            //先增加房间密码
        content.put("lock_no", "10.135.66.165");
        Integer room_key = random.nextInt(8999999) + 1000000;
        content.put("pwd_text", desEncode(room_key.toString()));
        HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(content, createHttpHeader());
        String responseText = restTemplate.postForObject(pwdAddAddress, requestEntity, String.class);

    }


    public void lockList(){

        JSONObject content = new JSONObject();
        HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(content, createHttpHeader());
        String responseText = restTemplate.postForObject(lockListAddress, requestEntity, String.class);
    }

    private String account = "18618380375";
    private String password = "hyt18618380375";
    private String loginAddress = "http://ops.huohetech.com/login";
    private String pwdAddAddress = "http://ops.huohetech.com/pwd/add";
    private String lockListAddress = "http://ops.huohetech.com/lock/list";


    private Date endTime;
    private String access_token;
}
