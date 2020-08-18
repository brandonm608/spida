package com.spidasoftware.application.outbound.services.acl;

import com.spidasoftware.application.outbound.services.acl.dto.Application;

import reactor.core.publisher.Mono;

public interface ExternalApplicationClientService {
	public Mono<String> create(Application application);

	public Mono<Application> get(String id);
}
