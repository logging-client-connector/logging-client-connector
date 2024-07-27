package org.loggingclientconnector

import org.loggingclientconnector.customizer.Blocklist
import org.loggingclientconnector.formatter.Formatter
import spock.lang.Specification


class LoggingClientConnectorSpec extends Specification {

	def "should be able to configure LoggingClientConnector"() {
		given:
		def clientConnector = LoggingClientConnector.create()

		when:
		clientConnector.configure { configuration ->
			configuration
					.blocklistConfig { blocklistConfig ->
						blocklistConfig.requestBlocklist(Blocklist.of("password"))
								.responseBlocklist(Blocklist.of("secret"))
								.requestHeaderBlocklist(Blocklist.of("Authorization"))
					}
					.formatter { formatterConfig -> Formatter.newFormatter() }
					.loggerConfig { loggerConfig ->
						loggerConfig.logLevel(LogLevel.INFO)
					}
		}

		then:
		def configuration = clientConnector.getConfiguration()

		configuration.blocklistConfig.requestBlocklist == Blocklist.of("password")
		configuration.blocklistConfig.responseBlocklist == Blocklist.of("secret")
		configuration.blocklistConfig.requestHeaderBlocklist == Blocklist.of("Authorization")
		configuration.formatter == Formatter.newFormatter()
		configuration.loggerConfig.logLevel == LogLevel.INFO
		configuration.loggerConfig.requestLoggerName == "Request Logger"
		configuration.loggerConfig.responseLoggerName == "Response Logger"
	}
}
