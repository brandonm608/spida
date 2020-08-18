package com.spidasoftware.application.adaptors.inbound.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spidasoftware.application.outbound.services.acl.ExternalJobClientService;
import com.spidasoftware.application.outbound.services.acl.dto.Job;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v0/jobs")
public class JobController {
	private ExternalJobClientService externalJobClientService;

	public JobController(ExternalJobClientService externalJobClientService) {
		this.externalJobClientService = externalJobClientService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Job> list() {
		return externalJobClientService.list();
	}
}
