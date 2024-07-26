package org.loggingclientconnector.customizer;

import org.loggingclientconnector.formatter.Formatter;

public class Configuration {

	private BlacklistConfig blacklistConfig = BlacklistConfig.newBlacklistConfig();
	private LoggerConfig loggerConfig = LoggerConfig.newLoggerConfig();
	private Formatter formatter = Formatter.newFormatter();

	Configuration() {
	}

	public static Configuration newConfiguration() {
		return new Configuration();
	}

	public Configuration blacklistConfig(Customizer<BlacklistConfig> blacklistConfig) {
		BlacklistConfig newBlacklistConfig = BlacklistConfig.newBlacklistConfig();
		blacklistConfig.apply(newBlacklistConfig);
		this.blacklistConfig = newBlacklistConfig;
		return this;
	}

	public Configuration formatter(Customizer<Formatter> formatter) {
		Formatter newFormatter = Formatter.newFormatter();
		formatter.apply(newFormatter);
		this.formatter = newFormatter;
		return this;
	}

	public Configuration loggerConfig(Customizer<LoggerConfig> loggerConfig) {
		LoggerConfig newLoggerConfig = LoggerConfig.newLoggerConfig();
		loggerConfig.apply(newLoggerConfig);
		this.loggerConfig = newLoggerConfig;
		return this;
	}

	public BlacklistConfig getBlacklistConfig() {
		return blacklistConfig;
	}

	public Formatter getFormatter() {
		return formatter;
	}

	public LoggerConfig getLoggerConfig() {
		return loggerConfig;
	}
}
