package com.spidasoftware.application.outbound.services.acl.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.spidasoftware.application.outbound.services.acl.ExternalApplicationClientService;
import com.spidasoftware.application.outbound.services.acl.dto.Application;

import reactor.core.publisher.Mono;

@Service
public class RestExternalApplicationClientService implements ExternalApplicationClientService {
	private WebClient applicationClient;

	public RestExternalApplicationClientService(WebClient.Builder builder,
			@Value("${application.spidasoftware.service.baseUri}") String baseUri) {
		applicationClient = builder.baseUrl(baseUri + "applications").build();
	}

	@Override
	public Mono<String> create(Application application) {
		return applicationClient.post().contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.bodyValue(application).retrieve().bodyToMono(String.class);
	}

	@Override
	public Mono<Application> get(String id) {
		return applicationClient.get().uri("/" + id).accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToMono(Application.class);
	}
}
