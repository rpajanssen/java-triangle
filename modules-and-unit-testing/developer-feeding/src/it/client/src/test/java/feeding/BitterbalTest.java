package feeding;

import com.abnamro.developer.feeding.interfaces.Experience;
import com.abnamro.developer.feeding.fatty.Bitterbal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Extra-Unit testing example
 */
class BitterbalTest {

    @Test
    void shouldEatBitterbal() {
        Bitterbal bitterbal = new Bitterbal();
        assertEquals(Experience.DELICIOUS, bitterbal.eatIt());
    }
}
