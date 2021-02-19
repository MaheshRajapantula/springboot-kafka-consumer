package com.learning.kafka.springbootkafkaconsumer.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka.springbootkafkaconsumer.Exception.RestEndPointInaccessibleException;
import com.learning.kafka.springbootkafkaconsumer.Exception.RestEndPointNotFoundException;
import com.learning.kafka.springbootkafkaconsumer.model.FinalKafkaTopic;
import com.learning.kafka.springbootkafkaconsumer.model.Status;
import com.learning.kafka.springbootkafkaconsumer.model.Urn;
import com.learning.kafka.springbootkafkaconsumer.model.User;
import com.learning.kafka.springbootkafkaconsumer.restendpoint.RestEndPointCaller;
import com.learning.kafka.springbootkafkaconsumer.utility.Verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class KafkaConsumer {

    @Autowired
    private RestEndPointCaller restEndPointCaller;
    @Autowired
    KafkaTemplate<String, User> kafkaTemplate;
    @Autowired
    KafkaTemplate<String, FinalKafkaTopic> kafkaFinalTemplate;
    private Verify verify;
    private User user;
    private static final String TOPIC_5M = "Kafka_Retry_5m";
    private static final String TOPIC_10M = "Kafka_Retry_10m";
    private static final String FINAL_TOPIC = "Kafka_Final_Topic";

    public KafkaConsumer (@Qualifier("verifyUsed") Verify verify){
        this.verify = verify;
    }

    @KafkaListener(topics = "Kafka_Topic", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consumeUrn(Urn urn, Acknowledgment acknowledgment) throws RestEndPointInaccessibleException, RestEndPointNotFoundException {
        System.out.println("Consumed message: " + urn.getUrn());
//        wait(20000);
        user = restEndPointCaller.GetUserData(urn);
        printJson(user);
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        user.setTime(localDateTime);
        user.setUrn(urn);
//        System.out.println(Thread.currentThread());
        boolean failed = verify.check(user);
        if(failed){
            handleRetry5m(user);
            System.out.println(user.getName() + " : Failed and sent to Retry 1");
            acknowledgment.acknowledge();
        }
        else {
            System.out.println(user.getName() + " : Succeeded");
            handleSuccess(user.getUrn());
            acknowledgment.acknowledge();
        }

    }

//    @KafkaListener(topics = "Kafka_Topic_Json", groupId = "group_json", containerFactory = "kafkaListenerContainerFactoryJson")
//    public void consumeJson(User user) {
//        printJson(user);
//        LocalDateTime localDateTime = LocalDateTime.now();
//        System.out.println(localDateTime);
//        user.setTime(localDateTime);
////        System.out.println(Thread.currentThread());
//        boolean failed = verify.check(user);
//        if(failed){
//            handleRetry5m(user);
//            System.out.println(user.getName() + " : Failed and sent to Retry 1");
//        }
//        else {
//            System.out.println(user.getName() + " : Succeeded");
//        }
//    }

    @KafkaListener(topics = TOPIC_5M, groupId = "group_json", containerFactory = "KafkaListenerContainerFactoryRetry5m")
    public void retry_5m(User user, Acknowledgment acknowledgment){
        LocalDateTime waittime = user.getTime().plusMinutes(1);
        System.out.println(user.getTime());
        System.out.println(waittime);
        KafkaConsumer.wait(59000);
        while(LocalDateTime.now().isBefore(waittime)){
            wait(1000);
        }
        printJson(user);
//        System.out.println(Thread.currentThread());
        boolean failed = verify.check(user);
        if(failed){
            handleRetry10m(user);
            System.out.println(user.getName() + " : Failed and sent to Retry 2");
            acknowledgment.acknowledge();
        }
        else {
            handleSuccess(user.getUrn());
            System.out.println(user.getName() + " : Succeeded");
            acknowledgment.acknowledge();
        }

    }

    @KafkaListener(topics = "Kafka_Retry_10m", groupId = "group_json", containerFactory = "KafkaListenerContainerFactoryRetry10m")
    public void retry_10m(User user, Acknowledgment acknowledgment){
        LocalDateTime waittime = user.getTime().plusMinutes(2);
        System.out.println(waittime);
        KafkaConsumer.wait(119000);
        while(LocalDateTime.now().isBefore(waittime)){
            wait(1000);
        }
        printJson(user);
//        System.out.println(Thread.currentThread());
        boolean failed = verify.check(user);
        if(failed){
            System.out.println(user.getName() + " : Failed");
            handleFailed(user.getUrn());
            acknowledgment.acknowledge();
        }
        else {
            System.out.println(user.getName() + " : Succeeded");
            handleSuccess(user.getUrn());
            acknowledgment.acknowledge();
        }
    }

    public void handleRetry5m(User user){
        user.setSalary(String.valueOf(Integer.parseInt(user.getSalary())+ 10000));
        kafkaTemplate.send(TOPIC_5M,user);
    }

    public void handleRetry10m(User user){
        user.setSalary(String.valueOf(Integer.parseInt(user.getSalary())+ 20000));
        kafkaTemplate.send(TOPIC_10M,user);
    }

    public void handleFailed(Urn urn) {
        FinalKafkaTopic finalKafkaTopic = new FinalKafkaTopic(urn, Status.F);
        kafkaFinalTemplate.send(FINAL_TOPIC, finalKafkaTopic);
    }

    public void handleSuccess(Urn urn) {
        FinalKafkaTopic finalKafkaTopic = new FinalKafkaTopic(urn, Status.S);
        kafkaFinalTemplate.send(FINAL_TOPIC, finalKafkaTopic);
    }

    public static void wait(int ms) {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public void printJson(User user){
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
