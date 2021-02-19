package com.learning.kafka.springbootkafkaconsumer.utility;

import com.learning.kafka.springbootkafkaconsumer.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Verify {

    @Bean(name = "verifyUsed")
    public Verify verify(){
        return  new Verify();
    }

    public boolean check(User user){
        System.out.println(Thread.currentThread());
        if (Integer.parseInt(user.getSalary()) < 100000) { return true; }
        else{ return false; }
    }
}
