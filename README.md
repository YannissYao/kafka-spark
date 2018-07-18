kafka版本：0.10

spark版本：2.11


工程描述：

    主要功能:
        kafka集成log4j发送日志与sparkStream对接做日志准实时批处理&附带监控页面
    实现过程:
        1.App.simulationUserBehavior()模拟生成用户信息
        2.SparkStreamingKafka.reveiveKafkaStream() 流计算结果放入本地缓存
        3.AjaxController.getData()对外提供查询接口


Echart-ui:

    功能：实时计算结果显示
    URL： http://127.0.0.1:8080
   

Spark-ui:

    功能：Spark-Admin管理界面
    URL: localhost:http://127.0.0.1:4040


启动过程：

    1.打开本地Docker
    2.打开log4j.properties配置文件把第七行ip改成自己本地ip（properties文件无法解析Docker link 容器的ip）
    3.进入工程目录执行 mvn clean package -DskipTests
    4.进入工程目录执行 docker-compose up
    5.启动后浏览器可访问上面两个ui页（Echart-ui & Spark-ui）
    
    
关闭过程：

    1.进入工程目录执行 docker-compose down
