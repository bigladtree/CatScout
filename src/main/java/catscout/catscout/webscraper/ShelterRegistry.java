package catscout.catscout.webscraper;

import java.util.List;
import java.util.Locale;

public class ShelterRegistry {

    // each entry is: platform, shelterId, display name, optional base URL, optional
    // location text
    public static final List<ShelterConfig> SHELTERS = List.of(

            // shelterluv shelters
            new ShelterConfig("shelterluv", "1863", "Because You Care", null, "Memphis, TN"),
            new ShelterConfig("shelterluv", "14449", "Memphis Animal Services", null, "Memphis, TN"),
            // https://memphisanimalservices.com/adopt/available-pets/cats-at-mas/
            // saved_query=2781 is the "Cats at MAS" filter
            //new ShelterConfig("shelterluv", "APA", "Austin Pets Alive!", null, "Austin, TX"),
            // https://www.austinpetsalive.org/adopt/cats
            new ShelterConfig("shelterluv", "12815", "Southern Pines Animal Shelter", null, "Southern Pines, NC"),
            // https://www.southernpinesanimalshelter.org/spasdogs-294453.html
            // https://www.shelterluv.com/embed/12815?
            //new ShelterConfig("shelterluv", "HST", "Humane Society of Tulsa", null, "Tulsa, OK"),
            // https://www.shelterluv.com/matchme/adopt/HST/Cat
            new ShelterConfig("shelterluv", "13324", "Jacksonville Humane Society",
                    "https://new.shelterluv.com/embed/", "Jacksonville, FL"),
            // https://jaxhumane.org/adopt/cats/
            // https://new.shelterluv.com/embed/13324?...
            new ShelterConfig("shelterluv", "100000846", "The Anti-Cruelty Society",
                    "https://new.shelterluv.com/embed/", "Chicago, IL"),
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
                    "Williamson County Regional Animal Shelter", null, "Georgetown, TX")
    // can be parsed through jsoup alone

    );

    public record ShelterConfig(String platform, String shelterId, String shelterName, String baseUrl,
            String locationText) {
        public ShelterConfig(String platform, String shelterId, String shelterName) {
            this(platform, shelterId, shelterName, null, null);
        }

        public ShelterConfig(String platform, String shelterId, String shelterName, String baseUrl) {
            this(platform, shelterId, shelterName, baseUrl, null);
        }
    }

    public static boolean matchesLocation(String query, ShelterConfig shelter) {
        if (shelter == null) {
            return false;
        }

        if (query == null || query.isBlank()) {
            return true;
        }

        if (shelter.locationText() == null || shelter.locationText().isBlank()) {
            return true;
        }

        String normalizedQuery = normalizeLocation(query);
        String normalizedLocation = normalizeLocation(shelter.locationText());

        if (normalizedQuery.isBlank() || normalizedLocation.isBlank()) {
            return true;
        }

        return normalizedLocation.contains(normalizedQuery)
                || normalizedQuery.contains(normalizedLocation);
    }

    private static String normalizeLocation(String value) {
        return value.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", " ")
                .trim();
    }
}