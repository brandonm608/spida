// Enumeration corresponding to the main UI selector
mainSelection = {
		jobs: 0,
		applications: 1
}

// Enumeration corresponding to the application UI selector
applicationSelection = {
		apply: 0,
		status: 1
}

// Update main UI selector
function mainSelect(selectionEnum) {
    	document.querySelectorAll("#mainSelector li a").forEach(a => a.className = "");
    	document.querySelectorAll(".mainContainer").forEach(div => div.className = "mainContainer container");

    	if(selectionEnum == mainSelection.jobs) {
    		document.querySelector("#jobsSelector").className = "selected";
    		document.querySelector("#jobs").className = "mainContainer container selected";
    	} else if(selectionEnum == mainSelection.applications) {
    		document.querySelector("#applicationsSelector").className = "selected";
    		document.querySelector("#applications").className = "mainContainer container selected";
    	}
}

// Update the application UI selector
function applicationSelect(selectionEnum) {
	document.querySelectorAll("#applicationEndpoint li a").forEach(a => a.className = "");
	document.querySelectorAll(".applicationContainer").forEach(div => div.className = "applicationContainer container");

	switch(selectionEnum) {
	case applicationSelection.apply:
		document.querySelector("#applicationApplySelector").className = "selected";
		document.querySelector("#applicationApplyContainer").className = "applicationContainer selected container";
		break;
	case applicationSelection.status:
		document.querySelector("#applicationStatusSelector").className = "selected";
		document.querySelector("#applicationStatusContainer").className = "applicationContainer selected container";
		break;
	}
}

// Add event listeners
window.addEventListener("load", function() {
    document.querySelector("#jobsSelector").addEventListener("click", () => mainSelect(mainSelection.jobs));
    document.querySelector("#applicationsSelector").addEventListener("click", () => mainSelect(mainSelection.applications));

    document.querySelector("#applicationApplySelector").addEventListener("click", () => applicationSelect(applicationSelection.apply));
    document.querySelector("#applicationStatusSelector").addEventListener("click", () => applicationSelect(applicationSelection.status));
});
