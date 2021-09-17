package com.abnamro.developer.hipo;

import com.abnamro.developer.api.Deliverable;
import com.abnamro.developer.api.Developer;
import com.abnamro.developer.feeding.meals.Lunch;
import com.abnamro.developer.meetups.content.external.OcpChapterEleven;
import com.abnamro.developer.meetups.content.external.Sharp;
import com.abnamro.developer.meetups.content.external.ShortCoffeeMachineTalk;

import java.util.ArrayList;
import java.util.List;

public class ProDev implements Developer {
    @Override
    public String getBlockId() {
        return "HPBLOCK";
    }

    @Override
    public List<Deliverable> work() {
        List<Deliverable> deliverables = new ArrayList<>();

        new ShortCoffeeMachineTalk().consume();
        new Sharp().consume();
        deliverables.add(new Deliverable("new feature"));
        new ShortCoffeeMachineTalk().consume();
        deliverables.add(new Deliverable("updated documentation"));
        deliverables.add(new Deliverable("planned demo"));
        new Lunch().eatIt();
        deliverables.add(new Deliverable("refined 3 stories"));
        new ShortCoffeeMachineTalk().consume();
        deliverables.add(new Deliverable("detailed design after mob design session"));
        new ShortCoffeeMachineTalk().consume();
        new OcpChapterEleven().consume();
        deliverables.add(new Deliverable("new feature"));
        deliverables.add(new Deliverable("reviewed pull request"));

        return deliverables;
    }
}
