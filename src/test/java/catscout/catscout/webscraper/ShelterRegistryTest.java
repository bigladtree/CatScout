package catscout.catscout.webscraper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ShelterRegistryTest {

    @Test
    void shouldMatchShelterByCityOrStateText() {
        ShelterRegistry.ShelterConfig shelter = new ShelterRegistry.ShelterConfig(
                "shelterluv",
                "1863",
                "Because You Care",
                null,
                "Memphis, TN");

        assertTrue(ShelterRegistry.matchesLocation("memphis", shelter));
        assertTrue(ShelterRegistry.matchesLocation("tn", shelter));
        assertFalse(ShelterRegistry.matchesLocation("austin", shelter));
    }
}
