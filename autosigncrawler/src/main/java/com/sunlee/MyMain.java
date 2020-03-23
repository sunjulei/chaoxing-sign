package com.sunlee;

import com.sunlee.Utils.PropertiesUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author sunlee
 * @date 2020/3/20 16:37
 * @Description:定时器启动类
 */
public class MyMain {

    public static void start() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> map = PropertiesUtil.initData();
        String cookie = map.get("myCookie").trim();
        String userAgent = map.get("userAgent").trim();

        if ("".equals(cookie) && "".equals(userAgent)) {
            System.out.println("myCookie或userAgent不能为空,请修改后重新运行");
            System.exit(0);
        }

        //1.创建Scheduler的工厂
        SchedulerFactory sf = new StdSchedulerFactory();
        //2.从工厂中获取调度器实例
        Scheduler scheduler = sf.getScheduler();
        System.out.println("----------课程开始录入----------");
        for (int i = 1; i <= (map.size() - 2) / 4; i++) {


            //3.创建JobDetail(作业信息)
            JobDetail jb = JobBuilder.newJob(AutoSignJob.class)
                    .withDescription("desc:" + map.get("course_" + i)) //job的描述
                    .withIdentity("group:" + map.get("course_" + i)) //job 的name和group
                    .build();
            //向任务传递数据
            JobDataMap jobDataMap = jb.getJobDataMap();
            jobDataMap.put("cookie", cookie);
            jobDataMap.put("userAgent", userAgent);


            //触发时间
            String schedule = map.get("schedule_" + i).trim();
            //课程参数
            jobDataMap.put("courseName", map.get("course_" + i));
            jobDataMap.put("courseId", map.get("courseId_" + i));
            jobDataMap.put("classId", map.get("classId_" + i));


            //任务运行的时间，SimpleSchedle类型触发器有效
            long time = System.currentTimeMillis(); //3秒后启动任务
            Date statTime = new Date(time);

            //4.创建Trigger
            //使用SimpleScheduleBuilder或者CronScheduleBuilder
            Trigger t = null;
            try {
                t = TriggerBuilder.newTrigger()
                        .withDescription("")
                        .withIdentity("signTrigger:" + map.get("course_" + i), "signGroupTrigger:" + map.get("course_" + i))
                        .startAt(statTime)  //默认当前时间启动
                        //普通计时器
                        //.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10))//间隔3秒，重复3次
                        //表达式计时器
                        .withSchedule(CronScheduleBuilder.cronSchedule(schedule))
                        .build();
            } catch (Exception e) {
                System.err.println("----------执行时间表有误，程序结束----------");
                System.exit(0);
            }

            //5.注册任务和定时器
            scheduler.scheduleJob(jb, t);

            //6.启动 调度器
            scheduler.start();

            if (map.get("course_" + i) != null) {
                System.out.println(
                        String.format("%s 已录入课程：《%s》",
                                sdf.format(new Date()) + "", map.get("course_" + i)));
            }

        }
        System.out.println("----------课程录入结束----------");
        System.out.println("----------已开始执行自动签到----------");

    }
}