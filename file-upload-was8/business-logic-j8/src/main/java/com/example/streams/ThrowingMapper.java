package com.example.streams;

@FunctionalInterface
public interface ThrowingMapper<A, R, E extends Exception> {
    R apply(A a) throws E;
}
