class ApplicationService {
	// Helper to create a row with 2 columns of text
	static createRow(column1, column2) {
		let tr;
		let td;

		tr = document.createElement("tr");
		td = document.createElement("td");
		td.innerText = column1;
		tr.appendChild(td);
		td = document.createElement("td");
		td.innerText = column2;
		tr.appendChild(td);

		return tr;
	}

	static apply(form) {
		let additionalLinkInputs = document.querySelectorAll("input[name=additionalLink]");
		let additionalLinks = [];

		for(let i = 0; i < additionalLinkInputs.length; i++)
			additionalLinks.push(additionalLinkInputs[i].value);

		let application = {
				jobId: form.elements["jobId"].value,
				name: form.elements["name"].value,
				justification: form.elements["justification"].value,
				code: form.elements["code"].value,
				additionalLinks: additionalLinks
			};

		Http.postRequest("applications", application).then(response => response.text().then(
				body => document.querySelector("#applicationCreateOutput").innerText = body
		));
	}

	static status(form) {
		let id = form.elements["id"].value;

		Http.getRequest("applications/" + id).then(response => response.json().then(
				json => {
					let output = document.querySelector("#applicationStatusOutput");

					let table = document.createElement("table");
					let tbody = document.createElement("tbody");

					tbody.appendChild(ApplicationService.createRow("Name:", json.name));
					tbody.appendChild(ApplicationService.createRow("Job ID:", json.jobId));
					tbody.appendChild(ApplicationService.createRow("Why should we hire you?:", json.justification));
					tbody.appendChild(ApplicationService.createRow("Code Repository:", json.code));
					if(json.additionalLinks)
						json.additionalLinks.forEach(link => tbody.appendChild(ApplicationService.createRow("Additional Link:", link)));

					table.appendChild(tbody);
					output.appendChild(table);
				}));
	}
}