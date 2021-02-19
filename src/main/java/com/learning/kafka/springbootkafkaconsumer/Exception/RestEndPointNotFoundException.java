package com.learning.kafka.springbootkafkaconsumer.Exception;

import org.apache.kafka.common.protocol.types.Field;

public class RestEndPointNotFoundException extends Exception{


    public RestEndPointNotFoundException(String message) {
        super(message);
    }

    public  RestEndPointNotFoundException(String message, Exception e){
        super(message, e);
    }

    public RestEndPointNotFoundException(Exception e) {
        super(e);
    }

}
