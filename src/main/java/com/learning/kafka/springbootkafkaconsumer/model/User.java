package com.learning.kafka.springbootkafkaconsumer.model;

import java.time.LocalDateTime;

public class User {

    private String name;
    private String id;
    private String salary;
    private LocalDateTime time;
    private Urn urn;

    public User(){}
    public User(String name, String id, String salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public Urn getUrn() { return urn; }

    public void setUrn(Urn urn) { this.urn = urn; }
}
