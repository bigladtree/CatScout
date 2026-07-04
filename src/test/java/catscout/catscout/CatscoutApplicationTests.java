package catscout.catscout;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CatscoutApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void shouldRunSpringBootByDefault() {
		assertTrue(CatscoutApplication.shouldRunSpringBoot(new String[] { "--org-id", "1863" }));
	}

	@Test
	void shouldSkipSpringBootWhenScrapeOnlyIsRequested() {
		assertFalse(CatscoutApplication.shouldRunSpringBoot(new String[] { "--scrape-only", "--org-id", "1863" }));
	}

}
