package com.abnamro.developer.meetups.content.external;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * In-module test
 */
class CleanCodeTest {

    @Test
    void shouldProvideTitleAndDescriptionOfTheCleanCodeBook() {
        CleanCode content = new CleanCode();
        assertEquals("Clean Code", content.getTitle());
        assertEquals("How to write sustainable code.", content.getDescription());
    }
}
