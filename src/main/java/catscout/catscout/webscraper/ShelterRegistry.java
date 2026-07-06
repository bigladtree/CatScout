package catscout.catscout.webscraper;

import java.util.List;

public class ShelterRegistry {

    // each entry is: platform, shelterId, display name
    public static final List<ShelterConfig> SHELTERS = List.of(
        new ShelterConfig("shelterluv", "1863", "Because You Care"),
        // add more shelterluv shelters
        
        // and petpoint shelters 
        // ex) new ShelterConfig("petpoint", "some-id", "Some Petpoint Shelter")

    );

    public record ShelterConfig(String platform, String shelterId, String shelterName) {
    }
}