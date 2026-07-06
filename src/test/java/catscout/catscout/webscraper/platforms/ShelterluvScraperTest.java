package catscout.catscout.webscraper.platforms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ShelterluvScraperTest {

    @Test
    void shouldUseExplicitBaseUrlWhenProvided() {
        ShelterluvScraper scraper = new ShelterluvScraper();

        String url = scraper.buildListingUrl("13324", "https://new.shelterluv.com/embed/");

        assertEquals("https://new.shelterluv.com/embed/13324?species=Cat&embedded=1&columns=1", url);
    }

    @Test
    void shouldUseDefaultBaseUrlWhenNoneProvided() {
        ShelterluvScraper scraper = new ShelterluvScraper();

        String url = scraper.buildListingUrl("1863", null);

        assertEquals("https://www.shelterluv.com/embed/1863?species=Cat&embedded=1&columns=1", url);
    }
}
