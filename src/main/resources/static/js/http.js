// Helper class for creating rest calls
class Http {
	static baseUrl = "http://localhost:8080/api/v0/";

	static getRequest(path) {
		return fetch(Http.baseUrl + path, {headers: {"Accept": "application/json"}});
	}

	static postRequest(path, body) {
		return fetch(Http.baseUrl + path, {
			headers: {"Accept": "application/json", "Content-Type": "application/json"},
			method: "POST",
			body: JSON.stringify(body)
		});
	}
}

