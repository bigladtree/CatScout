package catscout.catscout;

import catscout.catscout.webscraper.CatListing;
import catscout.catscout.webscraper.ScraperManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CatListingControllerTest {

    @Test
    void shouldDelegateToScraperManagerWhenFetchingListings() {
        CatListingController controller = new CatListingController(new ScraperManager() {
            @Override
            public List<CatListing> scrapeAll(String orgId, String location) {
                CatListing cat = new CatListing();
                cat.setName("Test Cat");
                cat.setSourceShelter("Test Shelter");
                return List.of(cat);
            }
        });

        List<CatListing> cats = controller.getCats(null);

        assertEquals(1, cats.size());
        assertEquals("Test Cat", cats.get(0).getName());
        assertEquals("Test Shelter", cats.get(0).getSourceShelter());
    }
}
