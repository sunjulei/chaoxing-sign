### 1、config.properties文件
主要用来设置cookie和添加课程
##### （1）设置cookie：打开浏览器，找到cookie，复制粘贴即可，注意不需要双引号
##### （2）添加课程方式（已默认使用了3门课程）：  
course_*=xxxxx		//课程名称，可任意输入  
courseId_*=xxxxx	//课程ID，在超星网页的地址栏可找到，务必保证输入正确  
classId_*=xxxxx		//班级ID，在超星网页的地址栏可找到，务必保证输入正确  
schedule_*=0 10/1 8 ? * 3 //执行签到时间表达式，越精细，占用内存越少，不懂怎么设置的可咨询  
注意：=号前面的*表示数字，务必按照顺序，比如course_1、course_2.....

### 2、start.bat文件
window系统双击启动即可，前提是要有jdk1.8以上版本的环境变量

### 3、cx-sign-1.0.jar
打包文件


author：sunjulei  
email：1374856829@qq.com
