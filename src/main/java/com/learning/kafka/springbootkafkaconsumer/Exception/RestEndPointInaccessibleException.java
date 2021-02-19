package com.learning.kafka.springbootkafkaconsumer.Exception;

public class RestEndPointInaccessibleException extends Exception{

    public RestEndPointInaccessibleException(String message) {
        super(message);
    }

    public  RestEndPointInaccessibleException(String message, Exception e){
        super(message, e);
    }

    public RestEndPointInaccessibleException(Exception e) {
        super(e);
    }
}
