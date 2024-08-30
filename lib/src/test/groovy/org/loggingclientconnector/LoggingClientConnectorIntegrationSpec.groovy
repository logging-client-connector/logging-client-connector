package org.loggingclientconnector

import log.appender.TestAppender
import org.loggingclientconnector.customizer.Blocklist
import org.mockserver.integration.ClientAndServer
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Execution
import spock.lang.Specification

import static fixture.HttpFixture.*
import static fixture.LoggingFixture.REQUEST_LOGGER_NAME
import static fixture.LoggingFixture.RESPONSE_LOGGER_NAME
import static org.loggingclientconnector.LogLevel.INFO
import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

@Execution(SAME_THREAD)
class LoggingClientConnectorIntegrationSpec extends Specification {

	def static mockServer = ClientAndServer.startClientAndServer(0)
	def static logCapture = TestAppender.createLogCapture(REQUEST_LOGGER_NAME, RESPONSE_LOGGER_NAME)

	def "should be able to log request and response"() {
		given: "setup WebClient with LoggingClientConnector"
		def loggingClientConnector = LoggingClientConnector.create()
				.configure(config -> config
						.loggerConfig(loggerConfig -> loggerConfig
								.requestLoggerName(REQUEST_LOGGER_NAME)
								.responseLoggerName(RESPONSE_LOGGER_NAME)
								.logLevel(INFO))
				)

		def client = WebClient.builder()
				.clientConnector(loggingClientConnector).build()

		and: "setup mock server"
		mockServer.when(someRequest())
				.respond(someResponse())

		when:
		client.get()
				.uri("http://localhost:${mockServer.localPort}/hello")
				.retrieve()
				.bodyToMono(String)
				.block()

		then: "verify request and response are logged"
		logCapture.contains(~/method: GET/)
		logCapture.contains(~/uri: http:\/\/localhost:${mockServer.localPort}\/hello/)
		logCapture.contains('"Hello, World!"')
	}

	def "should not log protected values"() {
		given: "setup blocklisted values"
		def loggingClientConnector = LoggingClientConnector.create()
				.configure(config -> config
						.loggerConfig(loggerConfig -> loggerConfig.logLevel(INFO))
						.blocklistConfig(blocklistConfig -> blocklistConfig
								.requestBlocklist(Blocklist.of("password"))
								.requestHeaderBlocklist(Blocklist.of("Authorization"))
								.responseBlocklist(Blocklist.of("secret"))
						)
				)

		def client = WebClient.builder()
				.clientConnector(loggingClientConnector).build()

		and: "setup mock server"
		mockServer.when(requestWithProtectedData())
				.respond(responseWithProtectedData())

		when:
		client.post()
				.uri("http://localhost:${mockServer.localPort}/protected")
				.bodyValue([
						'password': 'simple-password',
						'test'    : 'that-should-be-logged',
				])
				.headers { headers -> headers.setBearerAuth("token") }
				.retrieve()
				.bodyToMono(String)
				.block()

		then: "verify protected values are not logged"
		logCapture.contains(~/method: POST/)
		logCapture.contains(~/Authorization.*\[PROTECTED]/)
		logCapture.contains(~/password.*\[PROTECTED]/)
		logCapture.contains(~/test.*that-should-be-logged/)

		and: "verify response"
		logCapture.contains(~/secret.*\[PROTECTED]/)
		logCapture.contains(~/notASecret.*This is not a secret/)
	}

	def "should log multipart form data"() {
		given: "setup blocklisted values"
		def loggingClientConnector = LoggingClientConnector.create()
				.configure(config -> config
						.loggerConfig { loggerConfig -> loggerConfig.logLevel(INFO) })

		def requestBody = someMultipartFormDataBody()

		def client = WebClient.builder()
				.clientConnector(loggingClientConnector).build()

		and: "setup mock server"
		mockServer.when(requestWithMultipartFormData())
				.respond(responseWithMultipartFormData())

		when:
		client.post()
				.uri("http://localhost:${mockServer.localPort}/multipart")
				.body(BodyInserters.fromMultipartData(requestBody))
				.retrieve()
				.bodyToMono(String)
				.block()

		// TODO: check protected values
		then: "verify multipart form data is logged"
		logCapture.contains(~/form-data;.*key1/)
		logCapture.contains(~/form-data;.*key2/)
	}

	def "should log xml data"() {
		given: "setup blocklisted values"
		def loggingClientConnector = LoggingClientConnector.create()
				.configure(config -> config
						.loggerConfig(loggerConfig -> loggerConfig.logLevel(INFO))
						.blocklistConfig(blocklistConfig -> blocklistConfig
								.requestBlocklist(Blocklist.of("secret", "superSecret"))
						)
				)

		def requestBody = someXmlBody()

		def client = WebClient.builder()
				.clientConnector(loggingClientConnector).build()

		and: "setup mock server"
		mockServer.when(requestWithXmlData())
				.respond(someResponse())

		when:
		client.post()
				.uri("http://localhost:${mockServer.localPort}/xml")
		// content type is required to log xml data
				.headers { headers -> headers.add("Content-Type", "application/xml") }
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(String)
				.block()

		then: "verify xml data is logged"
		logCapture.contains("<note>")
		logCapture.contains(~/<secret>.*\[PROTECTED]/)
		logCapture.contains(~/<superSecret>.*\[PROTECTED]/)
	}

	def "should be able to rename loggers"() {
		given: "setup loggers with class name"
		def capture = TestAppender.createLogCapture(this.class.name)
		def loggingClientConnector = LoggingClientConnector.create()
				.configure(config -> config
						.loggerConfig(loggerConfig -> loggerConfig
								.requestLogger(this.class)
								.responseLogger(this.class)
								.logLevel(INFO)
						)
				)

		def client = WebClient.builder()
				.clientConnector(loggingClientConnector).build()

		and: "setup mock server"
		mockServer.when(someRequest())
				.respond(someResponse())

		when:
		client.get()
				.uri("http://localhost:${mockServer.localPort}/hello")
				.retrieve()
				.bodyToMono(String)
				.block()

		then: "verify that logger still works"
		capture.contains(~/method: GET/)
	}

	def "should log when there is no response body"() {
		given:
		def loggingClientConnector = LoggingClientConnector.create()
				.configure(config -> config
						.loggerConfig { loggerConfig -> loggerConfig.logLevel(INFO) })

		def client = WebClient.builder()
				.clientConnector(loggingClientConnector).build()

		and: "setup mock server"
		mockServer.when(requestWithWrongData())
				.respond(responseWithStatus500())

		when:
		client.post()
				.uri("http://localhost:${mockServer.localPort}/error")
				.bodyValue([wrongData: 'value1'])
				.retrieve()
				.onStatus(INTERNAL_SERVER_ERROR::equals, response -> Mono.error(new Ex("Error Message")))
				.bodyToMono(String)
				.blockOptional()

		then:
		def exception = thrown(Ex)
		exception.message == "Error Message"

		and:
		logCapture.contains(~/status code.*500/)
		logCapture.contains(~/Empty Body/)
	}

	static class Ex extends RuntimeException {
		Ex(String message) {
			super(message)
		}
	}
}
