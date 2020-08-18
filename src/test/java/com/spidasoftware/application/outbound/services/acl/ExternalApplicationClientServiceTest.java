package com.spidasoftware.application.outbound.services.acl;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.spidasoftware.application.outbound.services.acl.dto.Application;
import com.spidasoftware.application.outbound.services.acl.rest.RestExternalApplicationClientService;

import reactor.core.publisher.Mono;

public class ExternalApplicationClientServiceTest {
	private RestExternalApplicationClientService restExternalApplicationClientService;

	private ExternalApplicationClientService externalApplicationClientService;

	@Mock
	private WebClient.Builder builder;

	private String baseUri = "http://localhost:8080/";

	@Mock
	private WebClient client;

	// Helper method to assert Application objects match
	private static void assertApplicationsEqual(Application expected, Application actual) {
		Assertions.assertEquals(expected.getJobId(), actual.getJobId());
		Assertions.assertEquals(expected.getName(), actual.getName());
		Assertions.assertEquals(expected.getJustification(), actual.getJustification());
		Assertions.assertEquals(expected.getCode(), actual.getCode());
		assertAdditionalLinksEqual(expected.getAdditionalLinks(), actual.getAdditionalLinks());
	}

	// Helper method to assert additionalLinks match
	private static void assertAdditionalLinksEqual(List<String> expected, List<String> actual) {
		Assertions.assertEquals(expected.size(), actual.size());

		for (int i = 0; i < expected.size(); i++) {
			Assertions.assertEquals(expected.get(i), actual.get(i));
		}
	}

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

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);

		Mockito.when(builder.baseUrl(baseUri + "applications")).thenReturn(builder);
		Mockito.when(builder.build()).thenReturn(client);

		restExternalApplicationClientService = new RestExternalApplicationClientService(builder, baseUri);

		externalApplicationClientService = restExternalApplicationClientService;
	}

	// Successful create test
	@Test
	public void createTest() {
		// Mock response objects
		RequestBodyUriSpec requestBodyUriSpec = Mockito.mock(RequestBodyUriSpec.class);
		RequestHeadersSpec requestHeadersSpec = Mockito.mock(RequestHeadersSpec.class);
		ResponseSpec responseSpec = Mockito.mock(ResponseSpec.class);

		// Application to submit
		Application application = initApplication("1", "Captain Andersmith", "Something cool",
				"https://github.com/andersmith/application.git", Collections.emptyList());

		// Response object
		String response = "Some response json or link with id";

		String result;

		// Mock responses
		Mockito.when(client.post()).thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.bodyValue(application)).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(response));

		result = externalApplicationClientService.create(application).block();

		Assertions.assertEquals(response, result);
	}

	// Create test throwing an Internal Server Error
	@Test
	public void createWebClientResponseExceptionTestTest() {
		// Mock response objects
		RequestBodyUriSpec requestBodyUriSpec = Mockito.mock(RequestBodyUriSpec.class);
		RequestHeadersSpec requestHeadersSpec = Mockito.mock(RequestHeadersSpec.class);
		ResponseSpec responseSpec = Mockito.mock(ResponseSpec.class);

		// Application to submit
		Application application = initApplication("1", "Captain Andersmith", "Something cool",
				"https://github.com/andersmith/application.git", Collections.emptyList());

		// Response object
		String response = "Some response json or link with id";

		// Mock responses
		Mockito.when(client.post()).thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.bodyValue(application)).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.bodyToMono(String.class))
				.thenReturn(Mono.error(new WebClientResponseException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"500 Internal Server Error", new HttpHeaders(), null, Charset.defaultCharset())));

		Assertions.assertTrue(externalApplicationClientService.create(application).flatMap(result -> Mono.just(false))
				.onErrorReturn(WebClientResponseException.class, true).block());
	}

	// Successful get test
	@Test
	public void getTest() {
		// Mock response objects
		RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(RequestHeadersUriSpec.class);
		RequestHeadersSpec requestHeadersSpec = Mockito.mock(RequestHeadersSpec.class);
		ResponseSpec responseSpec = Mockito.mock(ResponseSpec.class);

		// Response object
		Application response = initApplication("1", "Captain Andersmith", "Something cool",
				"https://github.com/andersmith/application.git", Collections.emptyList());

		Application result;

		// Mock responses
		Mockito.when(client.get()).thenReturn(requestHeadersUriSpec);
		Mockito.when(requestHeadersUriSpec.uri("/1")).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.bodyToMono(Application.class)).thenReturn(Mono.just(response));

		result = externalApplicationClientService.get("1").block();

		assertApplicationsEqual(response, result);
	}

	// Get test throwing a Not Found Error
	@Test
	public void getWebClientResponseExceptionTest() {
		// Mock response objects
		RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(RequestHeadersUriSpec.class);
		RequestHeadersSpec requestHeadersSpec = Mockito.mock(RequestHeadersSpec.class);
		ResponseSpec responseSpec = Mockito.mock(ResponseSpec.class);

		// Mock responses
		Mockito.when(client.get()).thenReturn(requestHeadersUriSpec);
		Mockito.when(requestHeadersUriSpec.uri("/1")).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.bodyToMono(Application.class))
				.thenReturn(Mono.error(new WebClientResponseException(HttpStatus.NOT_FOUND.value(), "404 Not Found",
						new HttpHeaders(), null, Charset.defaultCharset())));

		Assertions.assertTrue(externalApplicationClientService.get("1").flatMap(result -> Mono.just(false))
				.onErrorReturn(WebClientResponseException.class, true).block());
	}
}
