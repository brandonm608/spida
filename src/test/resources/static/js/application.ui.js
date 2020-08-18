function addLink() {
	let tr = document.querySelector("#additionalLinksButtonsRow");
	let tbody = tr.parentNode;
	let newRow = document.createElement("tr");

	newRow.className = "additionalLinkRow";
	newRow.innerHTML = "<td>Link:</td><td><input type='text' name='additionalLink' maxlength='255' /></td>"

	tbody.insertBefore(newRow, tr);
}

function removeLastLink() {
	let rows = document.querySelectorAll(".additionalLinkRow");

	if(rows.length > 0) {
		let row = rows[rows.length - 1];
		let tbody = row.parentNode;

		tbody.removeChild(row);
	}
}

function clearLinks() {
	document.querySelectorAll(".additionalLinkRow").forEach(row => row.parentNode.removeChild(row));
}

window.addEventListener("load", function() {
	document.querySelector("#addLinksButton").addEventListener("click", () => addLink());
	document.querySelector("#removeLinkButton").addEventListener("click", () => removeLastLink());
	document.querySelector("#resetApplicationButton").addEventListener("click", () => clearLinks());
});
