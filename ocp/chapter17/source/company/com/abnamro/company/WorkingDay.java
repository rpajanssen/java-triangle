package com.abnamro.company;

import com.abnamro.developer.api.Deliverable;
import com.abnamro.developer.pool.DeveloperPool;

import java.util.List;

public class WorkingDay {
    private static List<String> teams = List.of( "HPBLOCK", "UPBLOCK");

    public static void main(String... args) {
        teams.forEach(
                team -> DeveloperPool.getDevelopers(team).parallelStream().forEach(
                        developer -> {
                            System.out.println("Developer: " + developer.getClass().getSimpleName());
                            developer.work().forEach(
                                    deliverable -> {
                                        System.out.println("   completed: " + deliverable.getTask());
                                    }
                            );
                            System.out.println();
                        }
                )
        );
    }
}
