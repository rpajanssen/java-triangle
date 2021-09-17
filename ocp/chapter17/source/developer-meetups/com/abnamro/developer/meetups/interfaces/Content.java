package com.abnamro.developer.meetups.interfaces;

public interface Content {
    String getTitle();
    String getDescription();

    default void consume() {
        System.out.println("Consuming " + getTitle());
    }
}
