package catscout.catscout;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import catscout.catscout.webscraper.CatListing;
import catscout.catscout.webscraper.ScraperManager;

@RestController
public class CatListingController {

    private final ScraperManager scraperManager;

    public CatListingController() {
        this(new ScraperManager());
    }

    CatListingController(ScraperManager scraperManager) {
        this.scraperManager = scraperManager;
    }

    @GetMapping("/api/cats")
    public List<CatListing> getCats(@RequestParam(required = false) String orgId,
            @RequestParam(required = false) String location) {
        return scraperManager.scrapeAll(orgId, location);
    }

    public List<CatListing> getCats(String orgId) {
        return getCats(orgId, null);
    }
}
