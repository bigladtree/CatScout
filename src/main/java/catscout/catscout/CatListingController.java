package catscout.catscout;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import catscout.catscout.webscraper.CatListing;
import catscout.catscout.webscraper.platforms.ShelterluvScraper;

@RestController
public class CatListingController {

    @GetMapping("/api/cats")
    public List<CatListing> getCats(@RequestParam(defaultValue = "1863") String orgId) {
        ShelterluvScraper scraper = new ShelterluvScraper();
        return scraper.scrape(orgId);
    }
}
