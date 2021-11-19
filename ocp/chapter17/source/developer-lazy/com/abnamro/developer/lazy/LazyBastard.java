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
    public String getFullName() {
        return "Caf Febreak";
    }

    @Override
    public List<Deliverable> work() {
        List<Deliverable> deliverables = new ArrayList<>();

        new ShortCoffeeMachineTalk().consume(getFullName());
        deliverables.add(new Deliverable(this, "logged in"));
        new LongCoffeeMachineTalk().consume(getFullName());
        deliverables.add(new Deliverable(this, "removed empty line from pom"));
        new LongCoffeeMachineTalk().consume(getFullName());
        new Netflix().consume(getFullName());
        deliverables.add(new Deliverable(this, "... (nothing actually)"));
        new Lunch().consume(getFullName());
        deliverables.add(new Deliverable(this, "fix broken build: out-commented failing unit test"));
        new LongCoffeeMachineTalk().consume(getFullName());
        deliverables.add(new Deliverable(this, "removed another empty line from pom"));
        new ShortCoffeeMachineTalk().consume(getFullName());
        deliverables.add(new Deliverable(this, "pressed some approve button cause team mate asked me to"));
        new LongCoffeeMachineTalk().consume(getFullName());

        return deliverables;
    }
}
