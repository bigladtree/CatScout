package catscout.catscout.webscraper;

import java.util.List;

public class ShelterRegistry {

    // each entry is: platform, shelterId, display name
    public static final List<ShelterConfig> SHELTERS = List.of(

            // shelterluv shelters
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
            new ShelterConfig("shelterluv", "13324", "Jacksonville Humane Society",
                    "https://new.shelterluv.com/embed/"),
            // https://jaxhumane.org/adopt/cats/
            // https://new.shelterluv.com/embed/13324?...
            new ShelterConfig("shelterluv", "100000846", "The Anti-Cruelty Society",
                    "https://new.shelterluv.com/embed/"),
            // https://new.shelterluv.com/embed/100000846

            // new ShelterConfig("shelterluv", "NVHS", "Nevada Humane Society")
            // can be parsed through jsoup - make sure scanner is able to read first

            // new ShelterConfig("shelterluv", null, "Lynchburg Humane Society")
            // use link instead of orgid
            // https://lynchburghumane.org/animal-listing/cats/

            // petango shelters
            // ex) new ShelterConfig("petango", "some-id", "Some Petango Shelter")

            // https://www.wilcotx.gov/164/Adopt
            new ShelterConfig("petango", "htr0d8cmdxn6kjq4i3brxlvgmx8e610khmut6wkjxayue3rdff",
                    "Williamson County Regional Animal Shelter")
    // can be parsed through jsoup alone

    );

    public record ShelterConfig(String platform, String shelterId, String shelterName, String baseUrl) {
        public ShelterConfig(String platform, String shelterId, String shelterName) {
            this(platform, shelterId, shelterName, null);
        }
    }
}