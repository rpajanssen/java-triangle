package com.abnamro.developer.meetups.interfaces;

public interface Content {
    String getTitle();
    String getDescription();

    default void consume(String name) {
        System.out.println(name + " consuming " + getTitle());
    }
}
