// Helper class for creating rest calls
class Http {
	static baseUrl = "http://localhost:8080/api/v0/";

	static getRequest(path) {
		if(path == "jobs")
			return new Promise((resolution) => resolution({json: function() {
				return new Promise((res) => res([{
					_id: "1",
					position: "Junior Developer",
					description: "Assistan web developer",
					requirements: ["Be cool", "Humor"]
				}]));
			}}));

		if(path == "applications/1")
			return new Promise((resolution) => resolution({json: function() {
				return new Promise((res) => res({
					name: "Captain Andersmith",
					jobId: "1",
					justification: "Oh no not Captain Andersmith",
					code: "https://github.com/andersmith/application.git",
					additionalLinks: ["www.example.com", "www.example.com/awesomeness"]
				}));
			}}));
	}

	static postRequest(path, body) {
		if(path == "applications")
			return new Promise((resolution) => resolution({text: function() {
				return new Promise((res) => res("Response text with id?"));
			}}));
	}
}

