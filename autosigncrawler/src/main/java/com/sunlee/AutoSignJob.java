package com.sunlee;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sunlee
 * @date 2020/3/20 16:45
 * @Description:定时器执行方法类
 */
public class AutoSignJob implements Job {

    public void setData(String courseName, String courseId, String classId, String cookie, String userAgent) {
        this.courseId = courseId;
        this.classId = classId;
        this.cookie = cookie;
        this.courseName = courseName;
        this.userAgent = userAgent;

        try {
            this.fid = cookie.substring(cookie.indexOf("fid") + 4);
            this.fid = this.fid.substring(0, (this.fid.indexOf(";")));
        } catch (Exception e) {
            System.out.println("cookie输入有误,结束即将程序，再次修改后运行");
        }
    }

    private String courseId;
    private String classId;
    private String cookie;
    private String fid;
    private String activeId;
    private String courseName;

    private String userAgent;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public void toSign() {
        getActiveId();
        if ("".equals(activeId) || activeId == null) {
            return;
        }
//        System.out.println((sdf.format(new Date()) + "——即将进入签到"));
        String url = String.format("https://mobilelearn.chaoxing.com/widget/sign/pcStuSignController/preSign?activeId=%s&classId=%s&fid=%s&courseId=%s", activeId, classId, fid, courseId);
        String referer = String.format("https://mobilelearn.chaoxing.com/widget/pcpick/stu/index?courseId=%s&jclassId=%s", courseId, classId);
        try {
            Document document = Jsoup.connect(url)
                    .cookie("Cookie", cookie)
                    .header("User-Agent", userAgent)
                    .timeout(5000)
                    .header("Referer", referer)
                    .get();

            String isNeedSign = document.select("span.greenColor").text();

            System.err.println(sdf.format(new Date()) + " 《" + courseName + "》——" + isNeedSign);
        } catch (Exception e) {
//            System.out.println(sdf.format(new Date()) + "——签到失败");
            e.printStackTrace();
        }
    }

    public void getActiveId() {
        String signUrl = String.format("https://mobilelearn.chaoxing.com/widget/pcpick/stu/index?courseId=%s&jclassId=%s", courseId, classId);
        Document document;
        String result = "";
        try {
            document = Jsoup.connect(signUrl)
                    .userAgent(userAgent)
                    .timeout(5000)
                    .cookie("Cookie", cookie)
                    .get();

            if (document.toString().contains("用户登录")) {
                System.out.println("cookie输入有误,程序结束");
                System.exit(0);
            }
            if (document.toString().contains("进行中(0)") && document.toString().contains("已结束(0)")) {

                System.err.println("《" + courseName + "》课程可能存在异常，如果确定参数无误，请忽视");
            }

            result = document.select("div#startList > div >div.Mct").attr("onclick");
            if (!result.equals("")) {
                activeId = result.substring(result.indexOf("(") + 1, result.indexOf(","));
                System.out.println(sdf.format(new Date()) + " 检测到《" + courseName + "》课需要签到");
            } else {
                System.out.println(sdf.format(new Date()) + " 检测到《" + courseName + "》课暂时不需要签到");
            }

        } catch (Exception e) {
            System.err.println("《" + courseName + "》课程不存在，请检查参数是否有误");
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap map = jobExecutionContext.getMergedJobDataMap();

        try {
            AutoSignJob sign = new AutoSignJob();
            sign.setData(
                    map.get("courseName").toString(),
                    map.get("courseId").toString(),
                    map.get("classId").toString(),
                    map.get("cookie").toString(),
                    map.get("userAgent").toString());
            sign.toSign();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
