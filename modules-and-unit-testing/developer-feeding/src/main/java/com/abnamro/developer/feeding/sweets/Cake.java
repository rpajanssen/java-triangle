package com.abnamro.developer.feeding.sweets;

import com.abnamro.developer.feeding.interfaces.Experience;
import com.abnamro.developer.feeding.interfaces.Snack;

public class Cake implements Snack {
    @Override
    public Experience eatIt() {
        System.out.println("Eating cake...");
        return Experience.MOREMORE;
    }
}
