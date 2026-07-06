package catscout.catscout.webscraper;

import catscout.catscout.webscraper.platforms.ShelterluvScraper;
import catscout.catscout.webscraper.platforms.PetangoScraper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScraperManager {

    private final Map<String, ShelterScraper> scrapers = new HashMap<>();

    public ScraperManager() {
        // register one scraper per platform
        scrapers.put("shelterluv", new ShelterluvScraper());
        scrapers.put("petango", new PetangoScraper());
        // scrapers.put("petpoint", new PetpointScraper()); // uncomment once
        // implemented

    }

    public List<CatListing> scrapeAll() {
        List<CatListing> allCats = new ArrayList<>();

        for (ShelterRegistry.ShelterConfig shelter : ShelterRegistry.SHELTERS) {
            ShelterScraper scraper = scrapers.get(shelter.platform());
            if (scraper == null) {
                System.err.println("No scraper registered for platform: " + shelter.platform());
                continue;
            }

            System.out.println("Scraping: " + shelter.shelterName() + " (" + shelter.platform() + ")");
            try {
                List<CatListing> cats = scraper.scrape(shelter.shelterId());

                // stamp each cat with the shelter name since scraper only knows the ID
                cats.forEach(cat -> cat.setSourceShelter(shelter.shelterName()));

                allCats.addAll(cats);
                System.out.println("  Got " + cats.size() + " cats from " + shelter.shelterName());
            } catch (Exception e) {
                // one bad shelter shouldn't stop the whole run
                System.err.println("  Failed scraping " + shelter.shelterName() + ": " + e.getMessage());
            }
        }

        System.out.println("Total across all shelters: " + allCats.size());
        return allCats;
    }
}
