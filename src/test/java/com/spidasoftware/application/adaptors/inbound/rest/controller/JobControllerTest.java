package com.spidasoftware.application.adaptors.inbound.rest.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
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

import com.spidasoftware.application.outbound.services.acl.ExternalJobClientService;
import com.spidasoftware.application.outbound.services.acl.dto.Job;

import reactor.core.publisher.Flux;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class JobControllerTest {
	// The spring helper to make requests against the test server
	@Autowired
	private WebTestClient request;

	// The mock for the controller under test
	@MockBean
	private ExternalJobClientService externalJobClientService;

	// Helper method to instantiate and fill in a new Job object
	private Job initJob(String id, String position, String description, List<String> requirements) {
		Job job = new Job();

		job.setId(id);
		job.setPosition(position);
		job.setDescription(description);
		job.setRequirements(requirements);

		return job;
	}

	// Helper method to assert job requirements match
	private void assertRequirementsEqual(List<String> expected, List<String> actual) {
		Assertions.assertEquals(expected.size(), actual.size());

		for (int i = 0; i < expected.size(); i++) {
			Assertions.assertEquals(expected.get(i), actual.get(i));
		}
	}

	// Helper method to assert Job objects match
	private void assertJobsEqual(Job expected, Job actual) {
		Assertions.assertEquals(expected.getId(), actual.getId());
		Assertions.assertEquals(expected.getPosition(), actual.getPosition());
		Assertions.assertEquals(expected.getDescription(), actual.getDescription());

		assertRequirementsEqual(expected.getRequirements(), actual.getRequirements());
	}

	// Helper method to assert Job lists match
	private void assertJobListsEqual(List<Job> expected, List<Job> actual) {
		Assertions.assertEquals(expected.size(), actual.size());

		for (int i = 0; i < expected.size(); i++) {
			assertJobsEqual(expected.get(i), actual.get(i));
		}
	}

	// successful list test
	@Test
	public void listTest() throws UnsupportedEncodingException, Exception {
		List<Job> response = new ArrayList<>();

		response.add(initJob("1", "Junior Developer", "Assistant Web Developer", Collections.emptyList()));

		Mockito.when(externalJobClientService.list()).thenReturn(Flux.fromIterable(response));

		request.get().uri("/api/v0/jobs").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
				.expectBody().jsonPath("[0]._id").value(IsEqual.equalTo(response.get(0).getId()))
				.jsonPath("[0].position").value(IsEqual.equalTo(response.get(0).getPosition()))
				.jsonPath("[0].description").value(IsEqual.equalTo(response.get(0).getDescription()))
				.jsonPath("[0].requirements").isArray();
	}

	// list test throwing an internal server error
	@Test
	public void listWebClientResponseExceptionTest() throws UnsupportedEncodingException, Exception {
		Mockito.when(externalJobClientService.list())
				.thenReturn(Flux.error(new WebClientResponseException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"500 Internal Server Error", new HttpHeaders(),
						"{\"error\": \"Internal Server Error\"}".getBytes(), Charset.defaultCharset())));

		System.out.println(request.get().uri("/api/v0/jobs").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().is5xxServerError().expectBody().jsonPath("error")
				.value(IsEqual.equalTo("Internal Server Error")));
	}
}
