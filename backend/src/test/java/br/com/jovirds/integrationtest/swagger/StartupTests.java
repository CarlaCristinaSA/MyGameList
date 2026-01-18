package br.com.jovirds.integrationtest.swagger;

import br.com.jovirds.configs.TestConfigs;
import br.com.jovirds.integrationtest.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StartupTests extends AbstractIntegrationTest {

	@LocalServerPort
	private int port;

	@Test
	void shouldDisplaySwaggerUIPage() {
		var content  = given()
				.basePath("/swagger-ui/index.html")
					.port(port)
				.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();

		assertTrue(content.contains("Swagger UI"));
	}

}
