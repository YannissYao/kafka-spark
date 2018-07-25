//package com.joeysin.consumer;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class KafkaConsumer<S, S1> {
//
//    @Value("${spring.kafka.topic}")
//    private String TOPIC;
//
//    @KafkaListener(topics = "{TOPIC}")
//    public void listen(ConsumerRecord<?, ?> record) {
//
//        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
//
//        if (kafkaMessage.isPresent()) {
//            Object message = kafkaMessage.get();
//            System.out.println("---->" + record);
//            System.out.println("---->" + message);
//
//        }
//    }
//}
