package fixture

import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse

import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

class ResponseFixture {

	static HttpResponse someResponse() {
		response().withStatusCode(200)
				.withBody("""
					{
						"message": "Hello, World!"
					}
				""")
	}

	static HttpRequest someRequest() {
		request().withMethod("GET")
				.withPath("/hello")
	}

	static HttpResponse responseWithProtectedData() {
		response().withStatusCode(200)
				.withBody("""
					{
						"secret": "This is a secret",
						"notASecret": "This is not a secret"
					}
				""")
	}

	static HttpRequest requestWithProtectedData() {
		request()
				.withMethod("POST")
				.withPath("/protected")
	}
}
