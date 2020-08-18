package com.spidasoftware.application.outbound.services.acl.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Job {
	@NotNull
	@Size(max = 255)
	@JsonProperty("_id")
	private String id;

	@NotNull
	@Size(max = 255)
	private String position;

	@NotNull
	@Size(max = 255)
	private String description;

	@Size(max = 25)
	private List<@Size(max = 255) String> requirements;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getRequirements() {
		return requirements;
	}

	public void setRequirements(List<String> requirements) {
		this.requirements = requirements;
	}
}
