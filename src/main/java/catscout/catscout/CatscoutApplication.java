package catscout.catscout;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import catscout.catscout.webscraper.CatListing;
import catscout.catscout.webscraper.ScraperManager;

@SpringBootApplication
public class CatscoutApplication {

	public static void main(String[] args) throws Exception {
		String orgId = resolveOrgId(args);
		boolean scrapeOnly = hasFlag(args, "--scrape-only");

		if (!scrapeOnly) {
			System.out.println("Launching Spring Boot app...");
		}

		runScrapeDemo(orgId);

		if (!scrapeOnly) {
			SpringApplication.run(CatscoutApplication.class, args);
		}
	}

	static boolean shouldRunSpringBoot(String[] args) {
		return !hasFlag(args, "--scrape-only");
	}

	private static void runScrapeDemo(String orgId) {
		ScraperManager scraperManager = new ScraperManager();
		List<CatListing> cats = scraperManager.scrapeAll(orgId);

		System.out.println("===== RESULTS =====");
		System.out.println("Total cats found: " + cats.size());
		for (CatListing cat : cats) {
			System.out.println("---");
			System.out.println("Name:  " + cat.getName());
			System.out.println("Breed: " + cat.getBreed());
			System.out.println("Age:   " + cat.getAge());
			System.out.println("Sex:   " + cat.getSex());
			System.out.println("Fee:   " + cat.getAdoptionFee());
			System.out.println("URL:   " + cat.getSourceUrl());
		}
	}

	private static String resolveOrgId(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith("--org-id=")) {
				return arg.substring("--org-id=".length());
			}
			if ("--org-id".equals(arg) && i + 1 < args.length) {
				return args[i + 1];
			}
		}
		return "1863";
	}

	private static boolean hasFlag(String[] args, String flag) {
		return Arrays.stream(args).anyMatch(flag::equals);
	}

}
