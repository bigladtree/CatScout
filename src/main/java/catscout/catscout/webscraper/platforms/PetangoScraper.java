package catscout.catscout.webscraper.platforms;

import catscout.catscout.webscraper.CatListing;
import catscout.catscout.webscraper.ShelterScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PetangoScraper implements ShelterScraper {

    private static final String LISTING_URL = "https://ws.petango.com/webservices/adoptablesearch/wsAdoptableAnimals2.aspx";
    private static final String DETAIL_URL = "https://ws.petango.com/webservices/adoptablesearch/wsAdoptableAnimalDetails2.aspx";
    private static final String CSS = "https://www.petango.com/WebServices/adoptablesearch/css/styles.css";

    @Override
    public List<CatListing> scrape(String authKey, String baseUrl) {
        List<CatListing> results = new ArrayList<>();

        // Step 1: get all animal IDs and basic info from listing page
        List<CatListing> basicListings = scrapeListingPage(authKey);
        System.out.println("Found " + basicListings.size() + " cats for authkey " + authKey);

        // Step 2: enrich each with detail page data
        for (CatListing basic : basicListings) {
            try {
                Thread.sleep(300);
                enrichFromDetailPage(basic, authKey);
                results.add(basic);
                System.out.println("Scraped: " + basic.getName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return results;
    }

    private List<CatListing> scrapeListingPage(String authKey) {
        List<CatListing> listings = new ArrayList<>();
        try {
            String url = buildUrl(LISTING_URL, Map.of(
                    "species", "Cat",
                    "sex", "A",
                    "agegroup", "All",
                    "location", "",
                    "site", "",
                    "onhold", "A",
                    "orderby", "ID",
                    "colnum", "4",
                    "css", CSS,
                    "authkey", authKey,
                    "recAmount", "",
                    "detailsInPopup", "Yes",
                    "featuredPet", "Include",
                    "stageID", ""));

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();

            // each animal card has class list-animal-info-block
            for (Element card : doc.select("div.list-animal-info-block")) {
                CatListing pet = new CatListing();
                pet.setPlatform("petango");

                // these fields are all right here in the listing page
                pet.setName(card.select("div.list-animal-name").text());
                pet.setBreed(card.select("div.list-animal-breed").text());
                pet.setAge(card.select("div.list-animal-age").text());

                // sex comes as "Female/Spayed" — just take the first part
                String sexRaw = card.select("div.list-animal-sexSN").text();
                pet.setSex(sexRaw.contains("/") ? sexRaw.split("/")[0] : sexRaw);

                // extract animal ID from the popup link
                Element link = card.selectFirst("a[href*='id=']");
                if (link != null) {
                    String href = link.attr("href");
                    String animalId = href.replaceAll(".*id=(\\d+).*", "$1");
                    String detailUrl = buildUrl(DETAIL_URL, Map.of(
                            "id", animalId,
                            "css", CSS,
                            "authkey", authKey,
                            "PopUp", "true"));
                    pet.setSourceUrl(detailUrl);
                }

                listings.add(pet);
            }
        } catch (IOException e) {
            System.err.println("Failed to scrape Petango listing: " + e.getMessage());
        }
        return listings;
    }

    private void enrichFromDetailPage(CatListing pet, String authKey) {
        if (pet.getSourceUrl() == null)
            return;
        try {
            Document doc = Jsoup.connect(pet.getSourceUrl())
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();

            // photo — the main animal photo
            Element photo = doc.selectFirst("img[src*='petango.com/photos']");
            if (photo == null)
                photo = doc.selectFirst("img[src*='g.petango.com']");
            pet.setPhotoUrl(photo != null ? photo.attr("src") : "");

            // all fields are in a table — label is in <strong>, value is next td
            for (Element row : doc.select("table tr")) {
                Element labelEl = row.selectFirst("td strong");
                Element valueEl = row.select("td").size() > 1 ? row.select("td").get(1) : null;
                if (labelEl == null || valueEl == null)
                    continue;

                String label = labelEl.text().trim().toLowerCase();
                String value = valueEl.text().trim();

                switch (label) {
                    case "color" -> pet.setColor(value);
                    case "size" -> pet.setSize(value);
                    case "gender" -> pet.setSex(value); // detail page uses "gender" not "sex"
                }
            }

            // description — look for any paragraph text after the table
            Element descEl = doc.selectFirst("div.animal-description");
            if (descEl == null)
                descEl = doc.selectFirst("td.notes");
            if (descEl == null)
                descEl = doc.selectFirst("span.animalNotes");
            pet.setDescription(descEl != null ? descEl.text().trim() : "");

            // adoption fee — petango doesn't always show fee on this page
            // the "Interested?" link goes to petpoint application, fee may not be listed
            pet.setAdoptionFee("Contact shelter");

        } catch (IOException e) {
            System.err.println("Failed to scrape cat profile " + pet.getSourceUrl() + ": " + e.getMessage());
        }
    }

    private String buildUrl(String baseUrl, Map<String, String> params) {
        StringBuilder query = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (!first) {
                query.append('&');
            }
            query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            query.append('=');
            query.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            first = false;
        }

        try {
            return new URI(baseUrl).resolve("?" + query).toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to build Petango URL", e);
        }
    }

    @Override
    public String getPlatformName() {
        return "petango";
    }
}