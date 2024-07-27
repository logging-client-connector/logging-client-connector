package org.loggingclientconnector;


import org.loggingclientconnector.customizer.Configuration;
import org.loggingclientconnector.customizer.Customizer;
import org.springframework.http.client.reactive.ClientHttpConnector;

/**
 * Interface for a logging client connector.
 */
public interface LoggingClientConnector extends ClientHttpConnector {

	/**
	 * Creates a new instance of LoggingClientHttpConnector.
	 *
	 * @return a new instance of {@link LoggingClientHttpConnector}
	 */
	static LoggingClientConnector create() {
		return new LoggingClientHttpConnector();
	}

	/**
	 * Creates a new instance of LoggingClientHttpConnector with a delegate.
	 *
	 * @param delegate the {@link org.springframework.http.client.reactive.ClientHttpConnector} to delegate to
	 * @return a new instance of {@link LoggingClientHttpConnector} with the specified delegate
	 */
	static LoggingClientConnector create(ClientHttpConnector delegate) {
		return new LoggingClientHttpConnector(delegate);
	}

	/**
	 * Configures the LoggingClientConnector with the specified customizer.
	 * <p>
	 * Example usage:
	 * <pre>{@code
	 * LoggingClientConnector connector = LoggingClientConnector.create().configure(config -> {
	 *     config.loggerConfig(loggerConfig -> {
	 *         loggerConfig
	 *             .requestLoggerName("REQUEST_LOGGER")
	 *             .responseLoggerName("RESPONSE_LOGGER")
	 *             .logLevel(LogLevel.INFO);
	 *     });
	 * });
	 * }</pre>
	 *
	 * @param configuration the customizer to configure the {@link LoggingClientConnector}
	 * @return the configured {@link LoggingClientConnector}
	 */
	LoggingClientConnector configure(Customizer<Configuration> configuration);

	/**
	 * Gets the current configuration of the LoggingClientConnector.
	 *
	 * @return the current {@link Configuration}
	 */
	Configuration getConfiguration();
}
