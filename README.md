Kafka version：0.10

Spark version：2.11

Elasticsearch version: 5.6.10

Fluentd version: 0.12

Kibana version: 5.6.10


Description：

    Main Function:
        1.kafka集成log4j2发送日志与sparkStream对接做日志准实时批处理 
        2.EFK日志收集
        
    Design ideas:
        1.App.simulationUserBehavior()模拟生成用户信息
        2.SparkStreamingKafka.reveiveKafkaStream() 流计算结果放入本地缓存
        3.AjaxController.getData()对外提供查询接口

Echart-ui:  [如图](https://github.com/Joeysin/kafka-spark/blob/master/images/kafka_spark_ui.png?raw=true)  

    Function：Show quasi-real-time calculation results 
    URL： http://127.0.0.1:8080
   
kinaba:  [Init Kibana](https://github.com/Joeysin/kafka-spark/blob/master/images/Kibana_init.png?raw=true)    [Visualize](https://github.com/Joeysin/kafka-spark/blob/master/images/Kibana.png?raw=true) 
    
    Function：Visualize and manage your log  
    URL: localhost:http://127.0.0.1:5601
    
Spark-ui:

    Function：Spark-Admin ui page
    URL: localhost:http://127.0.0.1:4040
    
   

How to Start: [必须调大Docker内存](https://github.com/Joeysin/kafka-spark/blob/master/images/Docker_memery_fix.png?raw=true)  

    1.首先打开本地Docker,需要把Docker内存设置的大一点 
    3.进入工程目录执行 mvn clean package -DskipTests
    4.进入工程目录执行 docker-compose up -d
    5.启动后浏览器可访问上面几个ui页（Echart-ui && Spark-ui && kibana-ui）
    
    
How to stop：

    1.进入工程目录执行 docker-compose down
