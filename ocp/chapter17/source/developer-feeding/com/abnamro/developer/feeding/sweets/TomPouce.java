package com.abnamro.developer.feeding.sweets;

import com.abnamro.developer.feeding.interfaces.Experience;
import com.abnamro.developer.feeding.interfaces.Snack;

public class TomPouce implements Snack {
    @Override
    public Experience consume(String name) {
        System.out.println(name + " eating tompouce...");
        return Experience.DELICIOUS;
    }
}
