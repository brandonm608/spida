package com.spidasoftware.application.outbound.services.acl;

import java.nio.charset.Charset;
import java.util.ArrayList;
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
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.spidasoftware.application.outbound.services.acl.dto.Job;
import com.spidasoftware.application.outbound.services.acl.rest.RestExternalJobClientService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ExternalJobClientServiceTest {
	private RestExternalJobClientService restExternalJobClientService;

	private ExternalJobClientService externalJobClientService;

	@Mock
	private WebClient.Builder builder;

	private String baseUri = "http://localhost:8080/";

	@Mock
	private WebClient client;

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

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);

		Mockito.when(builder.baseUrl(baseUri + "jobs")).thenReturn(builder);
		Mockito.when(builder.build()).thenReturn(client);

		restExternalJobClientService = new RestExternalJobClientService(builder, baseUri);

		externalJobClientService = restExternalJobClientService;
	}

	// successful list test with one object
	@Test
	public void listTest() {
		// Mock response objects
		RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(RequestHeadersUriSpec.class);
		RequestHeadersSpec requestHeadersSpec = Mockito.mock(RequestHeadersSpec.class);
		ResponseSpec responseSpec = Mockito.mock(ResponseSpec.class);

		// Response object
		List<Job> jobs = new ArrayList<>();

		List<Job> result;
		Job tmp = initJob("id", "position", "description", Collections.emptyList());

		jobs.add(tmp);

		// Mock responses
		Mockito.when(client.get()).thenReturn(requestHeadersUriSpec);
		Mockito.when(requestHeadersUriSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.bodyToFlux(Job.class)).thenReturn(Flux.fromIterable(jobs));

		result = externalJobClientService.list().collectList().block();
		Assertions.assertNotNull(result);
		assertJobListsEqual(jobs, result);
	}

	// successful empty list test
	@Test
	public void emptyListTest() {
		// Mock response objects
		RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(RequestHeadersUriSpec.class);
		RequestHeadersSpec requestHeadersSpec = Mockito.mock(RequestHeadersSpec.class);
		ResponseSpec responseSpec = Mockito.mock(ResponseSpec.class);

		// Response object
		List<Job> jobs = Collections.emptyList();

		List<Job> result;

		// Mock responses
		Mockito.when(client.get()).thenReturn(requestHeadersUriSpec);
		Mockito.when(requestHeadersUriSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.bodyToFlux(Job.class)).thenReturn(Flux.fromIterable(jobs));

		result = externalJobClientService.list().collectList().block();
		Assertions.assertNotNull(result);
		assertJobListsEqual(jobs, result);
	}

	// Exception test with a 404 WebClientResponseException
	@Test
	public void clientResponseExceptionTest() {
		// Mock response objects
		RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(RequestHeadersUriSpec.class);
		RequestHeadersSpec requestHeadersSpec = Mockito.mock(RequestHeadersSpec.class);
		ResponseSpec responseSpec = Mockito.mock(ResponseSpec.class);

		// Mock responses
		Mockito.when(client.get()).thenReturn(requestHeadersUriSpec);
		Mockito.when(requestHeadersUriSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.bodyToFlux(Job.class))
				.thenReturn(Flux.error(new WebClientResponseException(HttpStatus.NOT_FOUND.value(), "404 Not Found",
						new HttpHeaders(), null, Charset.defaultCharset())));

		Assertions.assertTrue(externalJobClientService.list().collectList().flatMap(list -> Mono.just(false))
				.onErrorReturn(WebClientResponseException.class, true).block());
	}
}
