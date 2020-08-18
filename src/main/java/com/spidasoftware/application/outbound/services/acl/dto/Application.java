package com.spidasoftware.application.outbound.services.acl.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Application {
	@NotNull
	@Size(max = 255)
	private String Name;

	@NotNull
	@Size(max = 255)
	private String jobId;

	@NotNull
	@Size(max = 255)
	private String justification;

	@NotNull
	@Size(max = 255)
	private String code;

	@Size(max = 10)
	private List<@Valid @Size(max = 255) String> additionalLinks;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getAdditionalLinks() {
		return additionalLinks;
	}

	public void setAdditionalLinks(List<String> additionalLinks) {
		this.additionalLinks = additionalLinks;
	}
}
