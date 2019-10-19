package com.aaroom.utils;

import com.aaroom.exception.RestError;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @className FileUtils
 * @Description   文件上传工具包
 * @Author 张赢
 * @Date 2018/11/1 20:17
 * @Version 1.0
 **/
@Component
public class FileUtils {


    private static String host;


    private static String staticSource;

    @Value("${server.remotenew}")
    public void setHost(String hostValue){
        host = hostValue;
    }

    @Value("${server.host.staticSource}")
    public void setStaticSource(String staticSourceValue) {
        staticSource = staticSourceValue;
    }


    public static Object writeUploadFile(HttpServletRequest request, MultipartFile file, String module) {
        //先非空判断一下
        if (!file.isEmpty()) {
            //设置日期格式
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            // new Date()为获取当前系统时间，也可使用当前时间戳
            String date = df.format(new Date());
            //读本地图片名字 加时间戳保存图片名称
            String filename =date + file.getOriginalFilename();
            //存到工程中 有就存没有就创建文件夹  先不用 File.separator
            String realpathXD =host+staticSource+"/"+module;
            String realpath=staticSource+"/"+module;
            if(!SystemUtils.IS_OS_LINUX) {
                realpath=request.getSession().getServletContext().getRealPath(realpath);
            }
            File fileDir = new File(realpath);
            if (!fileDir.exists())
                fileDir.mkdirs();
            //检查合不合格则格式
            String extname = FilenameUtils.getExtension(filename);
            String allowImgFormat = "gif,jpg,jpeg,png";
            if (!allowImgFormat.contains(extname.toLowerCase())) {
                return "NOT_IMAGE";
            }

            InputStream input = null;
            FileOutputStream fos = null;
            try {
                input = file.getInputStream();
                fos = new FileOutputStream(realpath + "/" + filename);
                IOUtils.copy(input, fos);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(fos);
            }
            return (realpathXD + "/" + filename);
        }
        return null;
    }


}
