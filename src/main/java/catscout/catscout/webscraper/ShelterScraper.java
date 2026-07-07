package catscout.catscout.webscraper;

import java.util.List;

public interface ShelterScraper {
    default List<CatListing> scrape(String shelterId) {
        return scrape(shelterId, null);
    }

    List<CatListing> scrape(String shelterId, String baseUrl);

    String getPlatformName();
}
