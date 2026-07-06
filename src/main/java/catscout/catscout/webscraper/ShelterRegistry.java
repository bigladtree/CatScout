package catscout.catscout.webscraper;

import java.util.List;

public class ShelterRegistry {

    // each entry is: platform, shelterId, display name
    public static final List<ShelterConfig> SHELTERS = List.of(
            new ShelterConfig("shelterluv", "1863", "Because You Care"),

            new ShelterConfig("shelterluv", "14449", "Memphis Animal Services"),
            // https://memphisanimalservices.com/adopt/available-pets/cats-at-mas/
            // 2782 might be code to filter for cats only at this shelter tbd
            new ShelterConfig("shelterluv", "464", "Austin Pets Alive!"),
            // https://www.austinpetsalive.org/adopt/cats
            new ShelterConfig("shelterluv", "12603", "Best Friends Animal Society"),
            // https://bestfriends.org/adopt/adopt-our-sanctuary/cats
            new ShelterConfig("shelterluv", "13324", "Jacksonville Humane Society"),
            // https://jaxhumane.org/adopt/cats/
            new ShelterConfig("shelterluv", "3827", "Lynchburg Humane Society"),
            // https://lynchburghumane.org/animal-listing/cats/
            new ShelterConfig("shelterluv", "12815", "Southern Pines Animal Shelter")
    // https://www.southernpinesanimalshelter.org/spasdogs-294453.html

    // and petpoint shelters
    // ex) new ShelterConfig("petpoint", "some-id", "Some Petpoint Shelter")

    );

    public record ShelterConfig(String platform, String shelterId, String shelterName) {
    }
}