package com.sunlee;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sunlee
 * @date 2020/3/20 16:21
 * @Description:程序入口
 */
@SpringBootApplication
public class AutosigncrawlerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication app =new SpringApplication(AutosigncrawlerApplication.class);
        app.setBannerMode(Banner.Mode.OFF);

        new MyMain().start();
    }

}
