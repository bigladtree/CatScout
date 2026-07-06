package catscout.catscout.webscraper;

import java.util.List;

public class ShelterRegistry {

    // each entry is: platform, shelterId, display name
    public static final List<ShelterConfig> SHELTERS = List.of(
            new ShelterConfig("shelterluv", "1863", "Because You Care"),

            new ShelterConfig("shelterluv", "14449", "Memphis Animal Services"),
            // https://memphisanimalservices.com/adopt/available-pets/cats-at-mas/
            // saved_query=2781 is the "Cats at MAS" filter
            new ShelterConfig("shelterluv", "APA", "Austin Pets Alive!"),
            // https://www.austinpetsalive.org/adopt/cats
            new ShelterConfig("shelterluv", "12815", "Southern Pines Animal Shelter"),
            // https://www.southernpinesanimalshelter.org/spasdogs-294453.html
            // https://www.shelterluv.com/embed/12815?
            new ShelterConfig("shelterluv", "HST", "Humane Society of Tulsa"),
            // https://www.shelterluv.com/matchme/adopt/HST/Cat
            new ShelterConfig("shelterluv", "13324", "Jacksonville Humane Society")
    // https://jaxhumane.org/adopt/cats/
    // https://new.shelterluv.com/embed/13324?...

    // new ShelterConfig("shelterluv", null, "Lynchburg Humane Society")
    // use link instead of orgid
    // https://lynchburghumane.org/animal-listing/cats/

    // and petpoint shelters
    // ex) new ShelterConfig("petpoint", "some-id", "Some Petpoint Shelter")

    );

    public record ShelterConfig(String platform, String shelterId, String shelterName) {
    }
}