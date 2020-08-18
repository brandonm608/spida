package com.spidasoftware.application.adaptors.inbound.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@RestControllerAdvice(basePackageClasses = { JobController.class, ApplicationController.class })
public class WebClientResponseExceptionControllerAdvice {
	@ExceptionHandler(WebClientResponseException.class)
	public Mono<ResponseEntity> handle(WebClientResponseException e) {
		return Mono.just(ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString()));
	}
}
