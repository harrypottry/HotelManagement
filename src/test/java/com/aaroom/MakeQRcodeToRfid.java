package com.aaroom;

import com.aaroom.beans.Mission;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @className MakeQRcodeToRfid
 * @Description 这个类主要是干 把二维码id转换成rfid
 * @Author 张赢
 * @Date 2019/1/8 0008上午 10:14
 * @Version 1.0
 **/
public class MakeQRcodeToRfid {

    public static final Integer WRITE_MAX_LEN = 12;

    public static byte[] long2byte(long res) {
        byte[] buffer = new byte[WRITE_MAX_LEN];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i + WRITE_MAX_LEN - 8] = (byte) ((res >> offset) & 0xff);
        }
        return buffer;
    }

    public static String byteArrayToString(byte[] btAryHex, int nIndex, int nLen) {
        if (nIndex + nLen > btAryHex.length) {
            nLen = btAryHex.length - nIndex;
        }

        String strResult = String.format("%02X", btAryHex[nIndex]);
        for (int nloop = nIndex + 1; nloop < nIndex + nLen; nloop++) {
            String strTemp = String.format(" %02X", btAryHex[nloop]);

            strResult += strTemp;
        }

        return strResult;
    }


    //制作rfid 1-500
    public static void main(String[] args) {
//        Map<Integer, Integer> map=new HashMap();
//        System.out.println("++++++");
//        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//            System.out.println("++++++");
//        }

//        String[] QRCodeArray= new String[]{"1"};
//        for (String QRCode:
//        QRCodeArray) {
//            long qrcode_long = Long.parseLong(QRCode);
//            byte[] epcarray;
//            epcarray = long2byte(qrcode_long);
//            String content = byteArrayToString(epcarray,0,WRITE_MAX_LEN).replaceAll(" ", "");
//            System.out.println("二维码ID:  "+QRCode+"            "+"Rfid:  "+content);
//        }

//        ArrayList list=new ArrayList();
//        int num=0;
//        for (int i = 1775; i < 2000; i++) {
//            list.add(i);
//            num++;
//        }
//        System.out.println(num);
//
//        for (int i = 0; i < 225; i = i + 9) {
//            List<Integer> listsublist = list.subList(i, i + 9);
//            System.out.println(StringUtils.strip(listsublist.toString(),"[]"));
//        }
//        if(list.contains(null)){
//            System.out.println("contains null会进来");
//        }else {
//            System.out.println("contains null不会进来！！！！！！！");
//        }

        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //获取前月的第一天
//        Calendar cal_1 = Calendar.getInstance();//获取当前日期
//        cal_1.add(Calendar.MONTH, -1);
//        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));//上月最后一天
        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND,59);

        System.out.println(c.getTime());

    }

}
