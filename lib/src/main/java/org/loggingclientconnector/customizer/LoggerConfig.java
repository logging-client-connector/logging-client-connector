package org.loggingclientconnector.customizer;

import org.loggingclientconnector.LogLevel;

import java.util.StringJoiner;

/**
 * Configuration class for loggers used in the LoggingClientConnector.
 */
public final class LoggerConfig {

	private LogLevel logLevel = LogLevel.DEBUG;
	private String requestLoggerName = "Request Logger";
	private String responseLoggerName = "Response Logger";

	private LoggerConfig() {
	}

	/**
	 * Creates a new instance of LoggerConfig.
	 *
	 * @return a new instance of {@link LoggerConfig}
	 */
	public static LoggerConfig newLoggerConfig() {
		return new LoggerConfig();
	}

	/**
	 * Sets the log level.
	 *
	 * @param logLevel the log level to set
	 * @return the updated {@link LoggerConfig} instance
	 */
	public LoggerConfig logLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
		return this;
	}

	/**
	 * Sets the request logger name.
	 *
	 * @param loggerName the name to set for the request logger
	 * @return the updated {@link LoggerConfig} instance
	 */
	public LoggerConfig requestLoggerName(String loggerName) {
		this.requestLoggerName = loggerName;
		return this;
	}

	/**
	 * Sets the response logger name.
	 *
	 * @param loggerName the name to set for the response logger
	 * @return the updated {@link LoggerConfig} instance
	 */
	public LoggerConfig responseLoggerName(String loggerName) {
		this.responseLoggerName = loggerName;
		return this;
	}

	/**
	 * Gets the log level.
	 *
	 * @return the current {@link LogLevel}
	 */
	public LogLevel getLogLevel() {
		return logLevel;
	}

	/**
	 * Gets the request logger name.
	 *
	 * @return the current request logger name
	 */
	public String getRequestLoggerName() {
		return requestLoggerName;
	}

	/**
	 * Gets the response logger name.
	 *
	 * @return the current response logger name
	 */
	public String getResponseLoggerName() {
		return responseLoggerName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LoggerConfig that)) return false;

		return logLevel == that.logLevel
				&& requestLoggerName.equals(that.requestLoggerName)
				&& responseLoggerName.equals(that.responseLoggerName);
	}

	@Override
	public int hashCode() {
		int result = logLevel.hashCode();
		result = 31 * result + requestLoggerName.hashCode();
		result = 31 * result + responseLoggerName.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", LoggerConfig.class.getSimpleName() + "[", "]")
				.add("logLevel=" + logLevel)
				.add("requestLoggerName='" + requestLoggerName + "'")
				.add("responseLoggerName='" + responseLoggerName + "'")
				.toString();
	}
}
