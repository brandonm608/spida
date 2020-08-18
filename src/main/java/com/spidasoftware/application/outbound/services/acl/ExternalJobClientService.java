package com.spidasoftware.application.outbound.services.acl;

import com.spidasoftware.application.outbound.services.acl.dto.Job;

import reactor.core.publisher.Flux;

public interface ExternalJobClientService {
	public Flux<Job> list();
}
