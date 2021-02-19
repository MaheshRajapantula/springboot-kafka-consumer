package com.learning.kafka.springbootkafkaconsumer.model;

public enum Status {

    S("Success"),
    F("Failed");

    private String value;

    Status(String value) { this.value = value; }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }
}
