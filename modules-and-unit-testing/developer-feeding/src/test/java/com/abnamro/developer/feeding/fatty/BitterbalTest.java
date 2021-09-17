package com.abnamro.developer.feeding.fatty;

import com.abnamro.developer.feeding.interfaces.Experience;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * In-module unit testing
 */
class BitterbalTest {
    @Test
    void shouldEatBitterbal() {
        Bitterbal bitterbal = new Bitterbal();
        assertEquals(Experience.DELICIOUS, bitterbal.eatIt());
    }
}
