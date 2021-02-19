package com.learning.kafka.springbootkafkaconsumer.model;

public class FinalKafkaTopic {

    private Urn urn;
    private Status status;

    public FinalKafkaTopic(Urn urn, Status status) {
        this.urn = urn;
        this.status = status;
    }

    public FinalKafkaTopic() {}

    public FinalKafkaTopic(Urn urn) {
        this.urn = urn;
    }

    public Urn getUrn() { return urn; }

    public void setUrn(Urn urn) { this.urn = urn; }

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }
}
