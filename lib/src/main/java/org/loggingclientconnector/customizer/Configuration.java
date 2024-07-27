package org.loggingclientconnector.customizer;

import org.loggingclientconnector.formatter.Formatter;

/**
 * Configuration class for the LoggingClientConnector.
 */
public final class Configuration {

	private BlocklistConfig blocklistConfig = BlocklistConfig.newBlocklistConfig();
	private LoggerConfig loggerConfig = LoggerConfig.newLoggerConfig();
	private Formatter formatter = Formatter.newFormatter();

	private Configuration() {
	}

	/**
	 * Creates a new instance of Configuration.
	 *
	 * @return a new instance of {@link Configuration}
	 */
	public static Configuration newConfiguration() {
		return new Configuration();
	}

	/**
	 * Sets the blocklist configuration using the provided customizer.
	 * <p>
	 * Example usage:
	 * <pre>{@code
	 * LoggingClientConnector connector = LoggingClientConnector.create().configure(config -> {
	 *     config.blocklistConfig(blocklistConfig -> {
	 *         blocklistConfig
	 *             .requestBlocklist(Blocklist.of("password", "secretKey"))
	 *             .responseBlocklist(Blocklist.of("token", "sessionId"))
	 *             .requestHeaderBlocklist(Blocklist.of("Authorization", "Cookie"));
	 *     });
	 * });
	 * }</pre>
	 *
	 * @param blocklistConfig the customizer to configure the blocklist
	 * @return the updated {@link Configuration} instance
	 */
	public Configuration blocklistConfig(Customizer<BlocklistConfig> blocklistConfig) {
		BlocklistConfig newBlocklistConfig = BlocklistConfig.newBlocklistConfig();
		blocklistConfig.apply(newBlocklistConfig);
		this.blocklistConfig = newBlocklistConfig;
		return this;
	}

	/**
	 * Sets the formatter configuration using the provided customizer.
	 * @param formatterConfig the customizer to configure the formatter
	 * @return the updated {@link Configuration} instance
	 */
	public Configuration formatter(Customizer<Formatter> formatterConfig) {
		Formatter newFormatter = Formatter.newFormatter();
		formatterConfig.apply(newFormatter);
		this.formatter = newFormatter;
		return this;
	}

	/**
	 * Sets the logger configuration using the provided customizer.
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
	 * @param loggerConfig the customizer to configure the logger
	 * @return the updated {@link Configuration} instance
	 */
	public Configuration loggerConfig(Customizer<LoggerConfig> loggerConfig) {
		LoggerConfig newLoggerConfig = LoggerConfig.newLoggerConfig();
		loggerConfig.apply(newLoggerConfig);
		this.loggerConfig = newLoggerConfig;
		return this;
	}

	/**
	 * Gets the current blocklist configuration.
	 *
	 * @return the current {@link BlocklistConfig}
	 */
	public BlocklistConfig getBlocklistConfig() {
		return blocklistConfig;
	}

	/**
	 * Gets the current formatter configuration.
	 *
	 * @return the current {@link Formatter}
	 */
	public Formatter getFormatter() {
		return formatter;
	}

	/**
	 * Gets the current logger configuration.
	 *
	 * @return the current {@link LoggerConfig}
	 */
	public LoggerConfig getLoggerConfig() {
		return loggerConfig;
	}
}
