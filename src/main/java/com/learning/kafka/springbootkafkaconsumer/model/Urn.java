package com.learning.kafka.springbootkafkaconsumer.model;

public class Urn {

    private String urn;

    public Urn() {}
    public Urn(String urn) {
        this.urn = urn;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }
}
