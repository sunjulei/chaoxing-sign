package com.sunlee.Utils;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author sunlee
 * @date 2020/3/20 17:05
 * @Description:程序入口
 */
public class PropertiesUtil {

    public static Map<String, String> initData() {
        Properties prop = new Properties();
        Map<String, String> map = new HashMap<>();
        InputStream in = null;
        FileOutputStream oFile = null;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(System.getProperty("user.dir") + "/config.properties")));
            //prop.load(in);//直接这么写，如果properties文件中有汉子，则汉字会乱码。因为未设置编码格式。
             prop.load(new InputStreamReader(in, StandardCharsets.UTF_8));

            map.put("myCookie", prop.getProperty("myCookie"));

            map.put("userAgent", prop.getProperty("userAgent"));
            for (int i = 1; i <= 10; i++) {
                if (prop.getProperty("course_" + i)!=null) {
                    map.put("course_"+i, prop.getProperty("course_"+i));
                    map.put("courseId_"+i, prop.getProperty("courseId_"+i));
                    map.put("classId_"+i, prop.getProperty("classId_"+i));
                    map.put("schedule_"+i, prop.getProperty("schedule_"+i));
                }else {
                    return map;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return map;
    }
}