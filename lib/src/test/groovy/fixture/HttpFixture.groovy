package fixture

import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.util.LinkedMultiValueMap

import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

class HttpFixture {

	static LinkedMultiValueMap<String, Object> someMultipartFormDataRequest() {
		def request = new LinkedMultiValueMap<String, Object>()
		request.add("key1", "value1")
		request.add("key2", "value2")
		request
	}

	static String someXmlRequest() {
		"""
			<note>
				<heading>Reminder</heading>
				<body>Check todo list</body>
				<secret>This is a secret</secret>
				<superSecret>
					<secret>This is a super secret</secret>
					<secret>This is another super secret</secret>
				</superSecret>
			</note>
		"""
	}

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

	static HttpRequest requestWithMultipartFormData() {
		request()
				.withMethod("POST")
				.withPath("/multipart")
	}

	static HttpResponse responseWithMultipartFormData() {
		response().withStatusCode(200)
				.withBody("""
					{
						"message": "Multipart form data"
					}
				""")
	}

	static HttpRequest requestWithWrongData() {
		request()
				.withMethod("POST")
				.withPath("/error")
	}

	static HttpResponse responseWithStatus500() {
		response().withStatusCode(500)
	}
}
