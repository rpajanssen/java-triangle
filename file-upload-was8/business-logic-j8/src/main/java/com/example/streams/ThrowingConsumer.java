package com.example.streams;

@FunctionalInterface
public interface ThrowingConsumer<A, E extends Exception> {
    void apply(A a) throws E;
}
