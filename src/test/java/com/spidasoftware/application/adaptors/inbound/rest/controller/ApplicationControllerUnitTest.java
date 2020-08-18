package com.spidasoftware.application.adaptors.inbound.rest.controller;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.spidasoftware.application.outbound.services.acl.ExternalApplicationClientService;
import com.spidasoftware.application.outbound.services.acl.dto.Application;

import reactor.core.publisher.Mono;

public class ApplicationControllerUnitTest {
	// Controller under isolation test
	private ApplicationController controller;

	@Mock
	private ExternalApplicationClientService externalApplicationClientService;

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

		controller = new ApplicationController(externalApplicationClientService);
	}

	// Successful create test
	@Test
	public void createTest() {
		// Create a valid application to create
		Application application = initApplication("1", "Captain Andersmith", "Something cool",
				"https://github.com/andersmitch/application.git", Collections.emptyList());

		String response = "Some Response";
		String result;

		// Mock response
		Mockito.when(externalApplicationClientService.create(application)).thenReturn(Mono.just(response));

		result = controller.create(application).block();

		Assertions.assertEquals(response, result);
	}

	// create test throwing an Exception
	@Test
	public void createExceptionTest() {
		// Create a valid application to create
		Application application = initApplication("1", "Captain Andersmith", "Something cool",
				"https://github.com/andersmitch/application.git", Collections.emptyList());

		// Mock response
		Mockito.when(externalApplicationClientService.create(application)).thenReturn(Mono.error(new Exception()));

		Assertions.assertTrue(controller.create(application).flatMap(result -> Mono.just(false))
				.onErrorReturn(Exception.class, true).block());
	}

	// Successful get test
	@Test
	public void getTest() {
		// Create a valid application to create
		Application response = initApplication("1", "Captain Andersmith", "Something cool",
				"https://github.com/andersmitch/application.git", Collections.emptyList());
		Application result;

		// Mock response
		Mockito.when(externalApplicationClientService.get("1")).thenReturn(Mono.just(response));

		result = controller.get("1").block();

		assertApplicationsEqual(response, result);
	}

	// get test throwing an exception
	@Test
	public void getExceptionTest() {
		// Mock response
		Mockito.when(externalApplicationClientService.get("1")).thenReturn(Mono.error(new Exception()));

		Assertions.assertTrue(
				controller.get("1").flatMap(result -> Mono.just(false)).onErrorReturn(Exception.class, true).block());
	}
}
