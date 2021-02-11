package com.learning.kafka.springbootkafkaconsumer.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka.springbootkafkaconsumer.model.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "Kafka_Topic", groupId = "group_id")
    public void consume(String message){
        System.out.println("Consumed message: " + message);
    }

    @KafkaListener(topics = "Kafka_Topic_Json", groupId = "group_json", containerFactory = "kafkaListenerContainerFactoryJson")
    public void consumeJson(User user) {
        System.out.println("Consumed JSON message: " + user);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonStr = mapper.writeValueAsString(user);
            System.out.println(jsonStr);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
