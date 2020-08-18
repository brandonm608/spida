class JobService {
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

	// Create a list of jobs in the output div in the jobs ui
	static list() {
		Http.getRequest("jobs").then(response =>
	    	response.json().then(
	    			json => {
	    				let output = document.querySelector("#jobsListOutput");

	    				json.forEach(job => {
	    					let table = document.createElement("table");
	    					let tbody = document.createElement("tbody");

	    					// display id
	    					tbody.appendChild(JobService.createRow("ID:", job._id));

	    					// display position
	    					tbody.appendChild(JobService.createRow("Position:", job.position));

	    					// display description
	    					tbody.appendChild(JobService.createRow("Description:", job.description));

	    					// display requirements
	    					job.requirements.forEach(requirement => tbody.appendChild(JobService.createRow("Requirement:", requirement)));

	    					// display table
	    					table.appendChild(tbody);
	    					output.appendChild(table);
	    				});
	    			}
	    	));
    }
}
