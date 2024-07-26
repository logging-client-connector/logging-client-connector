package org.loggingclientconnector

import log.appender.TestAppender
import org.loggingclientconnector.customizer.Blacklist
import org.mockserver.integration.ClientAndServer
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Execution
import spock.lang.Specification

import static fixture.LoggingFixture.REQUEST_LOGGER_NAME
import static fixture.LoggingFixture.RESPONSE_LOGGER_NAME
import static fixture.ResponseFixture.*
import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD


@Execution(SAME_THREAD)
class LoggingClientConnectorIntegrationSpec extends Specification {

	def static mockServer = ClientAndServer.startClientAndServer(0)
	def static logCapture = TestAppender.createLogCapture(REQUEST_LOGGER_NAME, RESPONSE_LOGGER_NAME)

	def "should be able to log request and response"() {
		given: "setup WebClient with LoggingClientConnector"
		def loggingClientConnector = LoggingClientConnector.create().configure { config ->
			config.loggerConfig { loggerConfig ->
				loggerConfig
						.requestLoggerName(REQUEST_LOGGER_NAME)
						.responseLoggerName(RESPONSE_LOGGER_NAME)
						.logLevel(LogLevel.INFO)
			}
		}

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
		given: "setup blacklisted values"
		def loggingClientConnector = LoggingClientConnector.create().configure { config ->
			config.loggerConfig { loggerConfig -> loggerConfig.logLevel(LogLevel.INFO) }
					.blacklistConfig { blacklistConfig ->
						blacklistConfig
								.requestBlacklist(Blacklist.of("password"))
								.requestHeaderBlacklist(Blacklist.of("Authorization"))
								.responseBlacklist(Blacklist.of("secret"))
					}
		}

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
}
