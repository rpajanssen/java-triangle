package com.abnamro.developer.meetups.content.internal;

import com.abnamro.developer.meetups.content.external.Sharp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * In-module test
 */
class SharpTest {
    @Test
    void shouldProvideTitleAndDescriptionOfTheCleanCodeBook() {
        Sharp content = new Sharp();
        assertEquals("Sharp: Bank rules made fun", content.getTitle());
        assertEquals("How to learn a developer about stuff he'll never have to do.", content.getDescription());
    }
}
