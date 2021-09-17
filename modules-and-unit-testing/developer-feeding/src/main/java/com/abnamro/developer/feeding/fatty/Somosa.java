package com.abnamro.developer.feeding.fatty;

import com.abnamro.developer.feeding.interfaces.Experience;
import com.abnamro.developer.feeding.interfaces.Snack;

public class Somosa implements Snack {
    public Experience eatIt() {
        System.out.println("Eating a somosa...");
        return Experience.MOREMORE;
    }
}
