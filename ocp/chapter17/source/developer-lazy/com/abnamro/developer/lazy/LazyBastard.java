package com.abnamro.developer.lazy;

import com.abnamro.developer.api.Deliverable;
import com.abnamro.developer.api.Developer;
import com.abnamro.developer.feeding.meals.Lunch;
import com.abnamro.developer.meetups.content.external.LongCoffeeMachineTalk;
import com.abnamro.developer.meetups.content.external.Netflix;
import com.abnamro.developer.meetups.content.external.ShortCoffeeMachineTalk;

import java.util.ArrayList;
import java.util.List;

public class LazyBastard implements Developer {
    @Override
    public String getBlockId() {
        return "UPBLOCK";
    }

    @Override
    public List<Deliverable> work() {
        List<Deliverable> deliverables = new ArrayList<>();

        new ShortCoffeeMachineTalk().consume();
        deliverables.add(new Deliverable("logged in"));
        new LongCoffeeMachineTalk().consume();
        deliverables.add(new Deliverable("removed empty line from pom"));
        new LongCoffeeMachineTalk().consume();
        new Netflix().consume();
        deliverables.add(new Deliverable("... (nothing actually)"));
        new Lunch().consume();
        deliverables.add(new Deliverable("fix broken build: out-commented failing unit test"));
        new LongCoffeeMachineTalk().consume();
        deliverables.add(new Deliverable("removed another empty line from pom"));
        new ShortCoffeeMachineTalk().consume();
        deliverables.add(new Deliverable("pressed some approve button cause team mate asked me to"));
        new LongCoffeeMachineTalk().consume();

        return deliverables;
    }
}
