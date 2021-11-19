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
    public String getFullName() {
        return "Chuck Norris";
    }


    @Override
    public List<Deliverable> work() {
        List<Deliverable> deliverables = new ArrayList<>();

        new ShortCoffeeMachineTalk().consume(getFullName());
        new Sharp().consume(getFullName());
        deliverables.add(new Deliverable(this, "new feature"));
        deliverables.add(new Deliverable(this, "updated dependencies after touching the code"));
        new ShortCoffeeMachineTalk().consume(getFullName());
        deliverables.add(new Deliverable(this, "updated documentation"));
        deliverables.add(new Deliverable(this, "planned demo"));
        new Lunch().consume(getFullName());
        deliverables.add(new Deliverable(this, "refined 3 stories"));
        new ShortCoffeeMachineTalk().consume(getFullName());
        deliverables.add(new Deliverable(this, "detailed design after mob design session"));
        new ShortCoffeeMachineTalk().consume(getFullName());
        new OcpChapterEleven().consume(getFullName());
        deliverables.add(new Deliverable(this, "new feature"));
        deliverables.add(new Deliverable(this, "reviewed pull request"));

        return deliverables;
    }
}
