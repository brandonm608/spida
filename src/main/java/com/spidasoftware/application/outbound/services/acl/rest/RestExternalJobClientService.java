package com.spidasoftware.application.outbound.services.acl.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.spidasoftware.application.outbound.services.acl.ExternalJobClientService;
import com.spidasoftware.application.outbound.services.acl.dto.Job;

import reactor.core.publisher.Flux;

@Service
public class RestExternalJobClientService implements ExternalJobClientService {
	private final WebClient jobClient;

	public RestExternalJobClientService(WebClient.Builder builder,
			@Value("application.spidasoftware.service.baseUri") String baseUri) {
		jobClient = builder.baseUrl(baseUri + "jobs").build();
	}

	@Override
	public Flux<Job> list() {
		return jobClient.get().accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Job.class).limitRequest(25);
	}
}
