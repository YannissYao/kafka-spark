package com.joeysin.consumer;

import com.joeysin.App;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import scala.Serializable;
import scala.Tuple2;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class SparkStreamingKafka implements Serializable {

    private static Logger LOGGER = LogManager.getLogger(SparkStreamingKafka.class);
    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBroker;
    @Value("${spring.kafka.consumer.group-id}")
    private String kafkaGroup;
    //实际上可以多个topic，逗号分割
    @Value("${spring.kafka.topic}")
    private String TOPIC;
    //批处理时间窗口（s）
    private static final Integer DURATION_SECOND = 10;
    private static String now[] = {""};
    private ScheduledExecutorService scheduledhreadPool = Executors.newScheduledThreadPool(2);

    Thread refreshTime = new Thread(new Runnable() {
        @Override
        public void run() {
//            ZoneId zoneId = ZoneId.of(ZoneId.SHORT_IDS.get("ACT"));
//            LOGGER.info("=========================================================" + ZonedDateTime.of(LocalDateTime.now(), zoneId));
            now[0] = LocalTime.now().toString().intern();
        }
    });


    /**
     * Created by Joeysin on  2018/7/11  下午5:11.
     * Describe：sparkStream对接kafkaStream
     */
    public void reveiveKafkaStream() throws InterruptedException {
        String brokers = kafkaBroker;
        String topics = TOPIC;
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("streaming word count");
        JavaSparkContext sc = new JavaSparkContext(conf);
//        sc.setLogLevel("WARN");
        JavaStreamingContext ssc = new JavaStreamingContext(sc, Durations.seconds(DURATION_SECOND));

        Collection<String> topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));
        //kafka相关参数，必要！缺了会报错
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", brokers);
        kafkaParams.put("bootstrap.servers", brokers);
        kafkaParams.put("group.id", kafkaGroup);
        kafkaParams.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //Topic分区
        Map<TopicPartition, Long> offsets = new HashMap<>();
        offsets.put(new TopicPartition(TOPIC, 0), 2L);
        //通过KafkaUtils.createDirectStream(...)获得kafka数据，kafka相关参数由kafkaParams指定
        JavaInputDStream<ConsumerRecord<Object, Object>> lines = KafkaUtils.createDirectStream(
                ssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topicsSet, kafkaParams, offsets)
        );
        scheduledhreadPool.scheduleAtFixedRate(refreshTime, 0, 3, TimeUnit.SECONDS);
        //可以打印所有信息，看下ConsumerRecord的结构
//        lines.foreachRDD(rdd -> {
//            rdd.foreach(x -> {
//                System.out.println("==================> " + x);
//            });
//        });
        AtomicLong count = new AtomicLong(0L);
        JavaPairDStream<String, Integer> counts =
                lines.flatMap(x -> {
                    String val = x.value().toString();
                    int start = val.indexOf("Joeysin_") == -1 ? 0 : val.indexOf("Joeysin_");
                    val = val.substring(start, (val.length() - 1));
                    return Arrays.asList(val).iterator();
                }).mapToPair(x -> {
                    App.localCache().put(now[0], count.incrementAndGet());
                    return new Tuple2<String, Integer>(x, 1);
                }).reduceByKey((x, y) -> x + y);

        counts.print();
        ssc.start();
        ssc.awaitTermination();
        ssc.close();
        scheduledhreadPool.shutdown();
    }


    @PostConstruct
    public void threadRun() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reveiveKafkaStream();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        scheduledhreadPool.execute(thread);
    }
}
