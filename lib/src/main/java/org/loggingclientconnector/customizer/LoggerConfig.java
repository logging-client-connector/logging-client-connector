package org.loggingclientconnector.customizer;

import org.loggingclientconnector.LogLevel;

import java.util.StringJoiner;

public final class LoggerConfig {

	public static final LoggerConfig DEFAULT = new LoggerConfig();

	private LogLevel logLevel = LogLevel.DEBUG;
	private String requestLoggerName = "Request Logger";
	private String responseLoggerName = "Response Logger";

	LoggerConfig() {
	}

	public static LoggerConfig newLoggerConfig() {
		return new LoggerConfig();
	}

	public LoggerConfig logLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
		return this;
	}

	public LoggerConfig requestLoggerName(String loggerName) {
		this.requestLoggerName = loggerName;
		return this;
	}

	public LoggerConfig responseLoggerName(String loggerName) {
		this.responseLoggerName = loggerName;
		return this;
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}

	public String getRequestLoggerName() {
		return requestLoggerName;
	}

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
