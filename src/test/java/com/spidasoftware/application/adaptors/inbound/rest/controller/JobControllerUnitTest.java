package com.spidasoftware.application.adaptors.inbound.rest.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.spidasoftware.application.outbound.services.acl.ExternalJobClientService;
import com.spidasoftware.application.outbound.services.acl.dto.Job;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class JobControllerUnitTest {
	// controller under isolation test
	private JobController controller;

	@Mock
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

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);

		controller = new JobController(externalJobClientService);
	}

	// Successful list test
	@Test
	public void listTest() {
		List<Job> result;

		List<Job> response = new ArrayList<>();
		Job tmp = initJob("id", "position", "description", Collections.emptyList());
		response.add(tmp);

		// Mock response
		Mockito.when(externalJobClientService.list()).thenReturn(Flux.fromIterable(response));

		result = controller.list().collectList().block();

		assertJobListsEqual(response, result);
	}

	// list test with an exception thrown
	@Test
	public void listExceptionTest() {
		// Mock response
		Mockito.when(externalJobClientService.list()).thenReturn(Flux.error(new Exception()));

		Assertions.assertTrue(controller.list().collectList().flatMap(result -> Mono.just(false))
				.onErrorReturn(Exception.class, true).block());
	}
}
