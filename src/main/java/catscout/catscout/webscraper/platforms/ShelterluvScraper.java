package catscout.catscout.webscraper.platforms;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;

import catscout.catscout.webscraper.CatListing;
import catscout.catscout.webscraper.ShelterScraper;

public class ShelterluvScraper implements ShelterScraper {

    private static final String BASE_URL = "https://www.shelterluv.com/embed";
    private static final int THREAD_COUNT = 5;

    @Override
    public List<CatListing> scrape(String orgId, String baseUrl) {
        List<CatListing> results = Collections.synchronizedList(new ArrayList<>());

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true));

            // Step 1: listing page is still single threaded (fast, only one page)
            BrowserContext listingContext = browser.newContext();
            Page listingPage = listingContext.newPage();
            List<String> animalUrls;
            try {
                animalUrls = scrapeListingPage(listingPage, orgId, baseUrl);
                System.out.println("Found " + animalUrls.size() + " animals for org " + orgId);
            } finally {
                listingPage.close();
                listingContext.close();
            }

            // Step 2: scrape profiles in parallel
            // each thread gets its own BrowserContext and Page — never share a Page across threads
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

            for (String url : animalUrls) {
                executor.submit(() -> {
                    BrowserContext context = browser.newContext();
                    Page page = context.newPage();
                    try {
                        CatListing cat = scrapeProfile(page, url, orgId);
                        if (cat != null) {
                            results.add(cat);
                            System.out.println("Scraped: " + cat.getName());
                        }
                    } finally {
                        page.close();
                        context.close();
                    }
                });
            }

            // wait for all threads to finish before closing the browser
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.MINUTES);
            browser.close();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Total scraped for org " + orgId + ": " + results.size());
        return results;
    }

    // ---- everything below this line is completely unchanged ----

    private CatListing scrapeProfile(Page page, String url, String orgId) {
        try {
            page.navigate(url);
            page.waitForSelector("h1.text-2xl",
                    new Page.WaitForSelectorOptions().setTimeout(15000));

            String html = page.content();
            Document doc = Jsoup.parse(html);

            Element h1 = doc.selectFirst("h1.text-2xl");
            if (h1 == null || h1.text().isBlank())
                return null;

            CatListing pet = new CatListing();
            pet.setPlatform("shelterluv");
            pet.setSourceUrl(url);
            pet.setSourceShelter(orgId);
            pet.setName(h1.text().trim());

            Element photo = doc.selectFirst("img[alt^='Photo 1']");
            if (photo == null)
                photo = doc.selectFirst("img[src*='profile-pictures']");
            pet.setPhotoUrl(photo != null ? photo.attr("src") : "");

            Element descEl = doc.selectFirst("div.mt-4.w-full.text-gray-600");
            pet.setDescription(descEl != null ? descEl.text().trim() : "");

            for (Element row : doc.select("div.flex.mb-6.items-end")) {
                Element labelEl = row.selectFirst("div.uppercase");
                Element valueEl = row.selectFirst("div.pl-2");
                if (labelEl == null || valueEl == null)
                    continue;

                String label = labelEl.text().trim().toLowerCase();
                String value = valueEl.text().trim();

                switch (label) {
                    case "breed" -> pet.setBreed(value);
                    case "sex" -> pet.setSex(value);
                    case "age" -> pet.setAge(value);
                    case "weight" -> pet.setSize(value);
                    case "color" -> pet.setColor(value);
                    case "adoption fee" -> pet.setAdoptionFee(value);
                }
            }

            return pet;

        } catch (Exception e) {
            System.err.println("Failed to scrape cat profile " + url + ": " + e.getMessage());
            return null;
        }
    }

    private List<String> scrapeListingPage(Page page, String orgId, String baseUrl) {
        List<String> urls = new ArrayList<>();

        try {
            String listingUrl = buildListingUrl(orgId, baseUrl);
            page.navigate(listingUrl);
            page.waitForSelector("a[href*='/embed/animal/']",
                    new Page.WaitForSelectorOptions().setTimeout(15000));

            String html = page.content();
            Document doc = Jsoup.parse(html);

            for (Element link : doc.select("a[href*='/embed/animal/']")) {
                String href = link.attr("abs:href");
                if (!href.isBlank()) {
                    urls.add(href);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to scrape listing: " + e.getMessage());
        }
        return urls;
    }

    String buildListingUrl(String orgId, String baseUrl) {
        String safeOrgId = sanitizeShelterId(orgId);
        URI normalizedBase = normalizeBaseUrl(baseUrl);
        String path = normalizedBase.getPath() == null || normalizedBase.getPath().isBlank()
                ? "/"
                : normalizedBase.getPath();
        String cleanedPath = path.endsWith("/") ? path : path + "/";
        String query = "species=Cat&embedded=1&columns=1";

        try {
            return new URI(
                    normalizedBase.getScheme(),
                    normalizedBase.getAuthority(),
                    cleanedPath + safeOrgId,
                    query,
                    null).toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to build Shelterluv listing URL", e);
        }
    }

    private String sanitizeShelterId(String orgId) {
        if (orgId == null || orgId.isBlank()) {
            throw new IllegalArgumentException("Shelter ID must not be blank");
        }

        String safeOrgId = orgId.trim();
        if (!safeOrgId.matches("[A-Za-z0-9._-]+")) {
            throw new IllegalArgumentException("Shelter ID contains unsupported characters");
        }
        return URLEncoder.encode(safeOrgId, StandardCharsets.UTF_8);
    }

    private URI normalizeBaseUrl(String baseUrl) {
        String effectiveBaseUrl = (baseUrl == null || baseUrl.isBlank()) ? BASE_URL : baseUrl.trim();
        URI uri;
        try {
            uri = URI.create(effectiveBaseUrl);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Base URL is not a valid URI", e);
        }

        if (!"https".equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException("Only HTTPS base URLs are allowed");
        }
        if (uri.getHost() == null || uri.getHost().isBlank()) {
            throw new IllegalArgumentException("Base URL must include a host");
        }
        return uri;
    }

    @Override
    public String getPlatformName() {
        return "shelterluv";
    }
}