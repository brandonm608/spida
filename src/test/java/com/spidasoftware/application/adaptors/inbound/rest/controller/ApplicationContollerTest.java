package com.spidasoftware.application.adaptors.inbound.rest.controller;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.spidasoftware.application.outbound.services.acl.ExternalApplicationClientService;
import com.spidasoftware.application.outbound.services.acl.dto.Application;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationContollerTest {
	@Autowired
	private WebTestClient request;

	@MockBean
	private ExternalApplicationClientService externalApplicationClientService;

	// Helper method to iniantiate and initialize an Application object
	private static Application initApplication(String jobId, String name, String justification, String code,
			List<String> additionalLinks) {
		Application app = new Application();

		app.setJobId(jobId);
		app.setName(name);
		app.setJustification(justification);
		app.setCode(code);
		app.setAdditionalLinks(additionalLinks);

		return app;
	}

	// Successful create test
	@Test
	public void createTest() {
		Application application = initApplication("1", "Captain Andersmith", "Something cool",
				"https://github.com/andersmitch/application.git", Collections.emptyList());

		Mockito.when(externalApplicationClientService.create(Mockito.any(Application.class)))
				.thenReturn(Mono.just("Some Response"));

		request.post().uri("/api/v0/applications").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).bodyValue(application).exchange().expectStatus().isCreated()
				.expectBody(String.class).isEqualTo("Some Response");
	}

	// Successful create test with null additionalLinks
	@Test
	public void createNullAdditionalLinksTest() {
		Application application = initApplication("1", "Captain Andersmith", "Something cool",
				"https://github.com/andersmitch/application.git", null);

		Mockito.when(externalApplicationClientService.create(Mockito.any(Application.class)))
				.thenReturn(Mono.just("Some Response"));

		request.post().uri("/api/v0/applications").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).bodyValue(application).exchange().expectStatus().isCreated()
				.expectBody(String.class).isEqualTo("Some Response");
	}

	// Invalid create test with additionalLink over 255 characters
	@Test
	public void createAdditionalLinkOver255Test() {
		Application application = initApplication("1", "Captain Andersmith", "Something cool",
				"https://github.com/andersmitch/application.git",
				Collections
						.singletonList("Very long test data is hard to come by. Very long test data is hard to come by."
								+ " Very long test data is hard to come by. Very long test data is hard to come by."
								+ " Very long test data is hard to come by. Very long test data is hard to come by. "
								+ "Very long test..."));

		Mockito.when(externalApplicationClientService.create(Mockito.any(Application.class)))
				.thenReturn(Mono.just("Some Response"));

		request.post().uri("/api/v0/applications").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).bodyValue(application).exchange().expectStatus().isBadRequest();
	}

	// Null Application value create test
	@Test
	public void createNullTest() {
		request.post().uri("/api/v0/applications").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isBadRequest();
	}

	// Invalid Application value create test
	@Test
	public void createInvalidTest() {
		request.post().uri("/api/v0/applications").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).bodyValue("{\"position: \"Junior Developer\"}").exchange()
				.expectStatus().isBadRequest();
	}

	// Successful get application by id test
	@Test
	public void getTest() {
		Application application = initApplication("1", "Captain Andersmith", "Something cool",
				"https://github.com/andersmitch/application.git", Collections.emptyList());

		Mockito.when(externalApplicationClientService.get("1")).thenReturn(Mono.just(application));

		request.get().uri("/api/v0/applications/1").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
				.expectBody().jsonPath("jobId").value(IsEqual.equalTo("1")).jsonPath("jobId")
				.value(IsEqual.equalTo("1")).jsonPath("name").value(IsEqual.equalTo("Captain Andersmith"))
				.jsonPath("justification").value(IsEqual.equalTo("Something cool")).jsonPath("additionalLinks")
				.isArray();
	}

	// get test throwing 404 Not Found
	// Successful get application by id test
	@Test
	public void getNotFoundWebClientExceptionTest() {
		Mockito.when(externalApplicationClientService.get("1"))
				.thenReturn(Mono.error(new WebClientResponseException(HttpStatus.NOT_FOUND.value(), "404 Not Found",
						new HttpHeaders(), null, Charset.defaultCharset())));

		request.get().uri("/api/v0/applications/1").accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
				.isNotFound();

	}

	// get test throwing Internal Server Error
	@Test
	public void getInternalServerErrorWebClientExceptionTest() {
		Mockito.when(externalApplicationClientService.get("1"))
				.thenReturn(Mono.error(new WebClientResponseException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"500 Internal Server Error", new HttpHeaders(), null, Charset.defaultCharset())));

		request.get().uri("/api/v0/applications/1").accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
				.is5xxServerError();

	}
}
