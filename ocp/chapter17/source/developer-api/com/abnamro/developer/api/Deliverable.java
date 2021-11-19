package com.abnamro.developer.api;

public class Deliverable {
    private String task;
    private Developer developer;

    public Deliverable(Developer developer, String task) {
        System.out.println(developer.getFullName() + " completed " + task);
        this.task = task;
        this.developer = developer;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
