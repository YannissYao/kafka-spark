package com.joeysin;


import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

//import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class);
//        @Autowired
//    KafkaTemplate kafkaTemplate;
//    @Autowired
//    RedisTemplate redisTemplate;

    @Value("${spring.kafka.topic}")
    private String TOPIC;

    private static Map<Object, Object> localCache;


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }


    public static Map<Object, Object> localCache() {
        if (Objects.equals(localCache, null)) {
            synchronized (App.class) {
                if (Objects.equals(localCache, null)) {
                    //这里使用有序&线程安全的Map
                    localCache = Collections.synchronizedMap(Maps.newLinkedHashMap());
                }
            }
        }
        return localCache;
    }

    /**
     * Created by Joeysin on  2018/7/11  下午3:15.
     * Describe：模拟用户行为
     */
    @PostConstruct
    public void simulationUserBehavior() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    int random = new Random().nextInt(5000);
                    String payload = "Joeysin_" + String.valueOf(random);
//                    kafkaTemplate.send(TOPIC, payload);
                    LOGGER.info(payload);
                    try {
                        Thread.sleep(random);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
    }
}
