package com.abnamro.company;

import com.abnamro.developer.api.Deliverable;
import com.abnamro.developer.pool.DeveloperPool;

import java.util.List;

public class WorkingDay {
    private final static List<String> teams = List.of( "HPBLOCK", "UPBLOCK");

    public static void main(String... args) {
        System.out.println("Start working");

        teams.forEach(
                team -> DeveloperPool.getDevelopers(team).parallelStream().forEach(
                        developer -> {
                            System.out.println("Developer: " + developer.getFullName());
                            developer.work().forEach(
                                    deliverable -> {
                                        System.out.println("   completed: " + deliverable.getTask());
                                    }
                            );
                            System.out.println();
                        }
                )
        );

        System.out.println("Yeah... feet on the table and a beer");
    }
}
