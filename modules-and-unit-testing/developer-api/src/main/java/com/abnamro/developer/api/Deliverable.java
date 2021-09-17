package com.abnamro.developer.api;

public class Deliverable {
    private String task;

    public Deliverable(String task) {
        System.out.println("Completing " + task);
        this.task = task;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
