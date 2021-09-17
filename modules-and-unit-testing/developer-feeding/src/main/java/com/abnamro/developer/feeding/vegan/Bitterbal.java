package com.abnamro.developer.feeding.vegan;

import com.abnamro.developer.feeding.interfaces.Experience;
import com.abnamro.developer.feeding.interfaces.Snack;

public class Bitterbal implements Snack {
    public Experience eatIt() {
        System.out.println("Eating a vegan bitterbal...");
        return Experience.DELICIOUS;
    }
}
