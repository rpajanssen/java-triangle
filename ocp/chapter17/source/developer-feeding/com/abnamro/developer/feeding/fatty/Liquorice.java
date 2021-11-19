package com.abnamro.developer.feeding.fatty;

import com.abnamro.developer.feeding.interfaces.Experience;
import com.abnamro.developer.feeding.interfaces.Snack;

public class Liquorice implements Snack {
    public Experience consume(String name) {
        System.out.println(name + " eating liquorice...");
        return Experience.OK;
    }
}
