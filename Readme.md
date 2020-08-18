To Use this program either compile with application.properties and run the fatJar.
Or you can compile without application.properties and export the appropriate environment variable and run the fatJar.

The property to add is: application.spidasoftware.service.baseUri

The environment is: APPLICATION_SPIDASOFTWARE_SERVICE_BASEURI

Note: The baseUri must end in a / (e.g. url/apply/)

The main webpage is located at localhost:8080/

Errata: I am unsure what the post response to applications looks like. I assume I get a unique id. The jobs list responded with an _id. I don't want to assume the response is the same and I don't want to miss it. So I left the applications post response as an ambiguous string rather than making a dto for it. This also means the UI only displays a raw unformatted String.