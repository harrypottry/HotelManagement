package com.aaroom;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AaRoomApplicationTests {

    @Autowired
    private RestTemplate restTemplate;

    /*@Autowired
    private RedisCacheService redisCacheService;
*/
	/*@Test
    public void contextLoads() {
	}

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ProfitLogService profitLogService;

	@Autowired
	private HttpSenderService httpSenderService;

	@Test
	public void regCover(){
		String url = "https://testsjz.ihotels.cc//ethank-sjz-web/rest/memberResource/v1.1/memberRegister3";
		//String url = "https://sjz.ihotels.cc//ethank-sjz-web/rest/memberResource/v1.1/memberRegister3";
		//url += "?channel="+5+"&deviceType="+"a"+"&memberId="+"17701352042"+"&memberName="+"aaa"+"&memberPwd="+"fdsafdsafas"+"&memberShortMsg="+"1213"+"&tagVersion="+"3.4.2";

		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("channel",17);
		jsonMap.put("deviceType", "5");
		jsonMap.put("memberId", "13998420803");
		jsonMap.put("memberName", "aaa");
		jsonMap.put("memberPwd", "dafafaf");
		jsonMap.put("memberShortMsg", "403715");
		jsonMap.put("tagVersion", "3.4.2");

		String s = restTemplate.postForObject(url, jsonMap, String.class);
		Object ret = JSON.parse(s);
		System.out.println(ret);
	}


    @Test
    public void subStrTest(){
        String redirectUrl = "/console/login.html";
        int i = redirectUrl.indexOf("/", 2);
        System.out.println(i);
        System.out.println(redirectUrl.substring(0,i));
    }


	@Test
	public void getMemberInfoTest(){
		Map<String, Object> memberInfoArgs = new HashMap<>();
		memberInfoArgs.put("versionCode",68410);
		memberInfoArgs.put("memberId","17701352042");
		memberInfoArgs.put("ct","ethank");
		memberInfoArgs.put("appSign","A99C1B8E45E40DB1D1579F8703C4D966");
		memberInfoArgs.put("deviceID","869465023416543");
		memberInfoArgs.put("tagVersion","4.1.0");
		memberInfoArgs.put("channel","20");

		String s = restTemplate.postForObject("http://testsjz.ihotels.cc/ethank-sjz-web/rest/memberResource/v1.1/memberInfo", memberInfoArgs, String.class);
		JSONObject jsonObject = JSON.parseObject(s, JSONObject.class);
		JSONObject retValue = (JSONObject)jsonObject.get("retValue");
		String memberCardId = (String)retValue.get("memberCardId");
		System.out.println(s);
		System.out.println(memberCardId);

	}

	@Test
	public void testProfit(){
		profitLogService.profitCreatebyOrderId("JD00145171227000201");
	}


	@Test
	public void testSendSms() throws UnsupportedEncodingException {
		httpSenderService.sendMsgByPhone("13998420803","【AA酒店】您的注册验证码是：292927");
	}*/

/*    @Test
    public void generatePassword() {

        List list=new ArrayList();
        list.add(1);
        list.add(3);
        list.add(5);
        list.add(7);
        list.add(9);
        list.add(11);

        for (Object num:list) {
            System.out.print(num);
        }

        String[] pswdStr = {"qwertyuiopasdfghjklzxcvbnm",
                "QWERTYUIOPASDFGHJKLZXCVBNM", "0123456789",
                "!@#$%^&*()_+-="};

        int pswdLen = 30;
        String pswd = " ";
        char[] chs = new char[pswdLen];
        for (int i = 0; i < pswdStr.length; i++) {

            int idx = (int) (Math.random() * pswdStr[i].length());
            chs[i] = pswdStr[i].charAt(idx);

        }

        for (int i = pswdStr.length; i < pswdLen; i++) {

            int arrIdx = (int) (Math.random() * pswdStr.length);
            int strIdx = (int) (Math.random() * pswdStr[arrIdx].length());

            chs[i] = pswdStr[arrIdx].charAt(strIdx);
        }

        for (int i = 0; i < 1000; i++) {
            int idx1 = (int) (Math.random() * chs.length);
            int idx2 = (int) (Math.random() * chs.length);

            if (idx1 == idx2) {
                continue;
            }

            char tempChar = chs[idx1];
            chs[idx1] = chs[idx2];
            chs[idx2] = tempChar;
        }

        pswd = new String(chs);
        System.out.println(pswd);
    }*/

    @Test
    public void testSuccess(){
        System.out.println("success test");
    }

    @Test
    public void getAuth(){
        String plainCreds = "aahotel:aahotel849$";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "basic " + base64Creds);
        headers.add("Content-Type","application/json");

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange("http://120.92.148.25:8099/rest/oauth/authorize", HttpMethod.GET, entity, String.class);
        String account = response.getBody();
        System.out.println(account);
    }

}


