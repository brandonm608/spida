package com.spidasoftware.application.adaptors.inbound.rest.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.spidasoftware.application.outbound.services.acl.ExternalApplicationClientService;
import com.spidasoftware.application.outbound.services.acl.dto.Application;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v0/applications")
public class ApplicationController {
	private ExternalApplicationClientService externalApplicationClientService;

	public ApplicationController(ExternalApplicationClientService externalApplicationClientService) {
		this.externalApplicationClientService = externalApplicationClientService;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<String> create(@Valid @NotNull @RequestBody Application application) {
		return externalApplicationClientService.create(application);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Application> get(@PathVariable("id") String id) {
		return externalApplicationClientService.get(id);
	}
}
