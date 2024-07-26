package org.loggingclientconnector

import org.loggingclientconnector.customizer.Blacklist
import org.loggingclientconnector.formatter.Formatter
import spock.lang.Specification


class LoggingClientConnectorSpec extends Specification {

	def "should be able to configure LoggingClientConnector"() {
		given:
		def clientConnector = LoggingClientConnector.create()

		when:
		clientConnector.configure { configuration ->
			configuration
					.blacklistConfig { blacklistConfig ->
						blacklistConfig.requestBlacklist(Blacklist.of("password"))
								.responseBlacklist(Blacklist.of("secret"))
								.requestHeaderBlacklist(Blacklist.of("Authorization"))
					}
					.formatter { formatterConfig -> Formatter.newFormatter() }
					.loggerConfig { loggerConfig ->
						loggerConfig.logLevel(LogLevel.INFO)
					}
		}

		then:
		def configuration = clientConnector.getConfiguration()

		configuration.blacklistConfig.requestBlacklist == Blacklist.of("password")
		configuration.blacklistConfig.responseBlacklist == Blacklist.of("secret")
		configuration.blacklistConfig.requestHeaderBlacklist == Blacklist.of("Authorization")
		configuration.formatter == Formatter.newFormatter()
		configuration.loggerConfig.logLevel == LogLevel.INFO
		configuration.loggerConfig.requestLoggerName == "Request Logger"
		configuration.loggerConfig.responseLoggerName == "Response Logger"
	}
}
