package org.loggingclientconnector;

import org.slf4j.event.Level;
import org.slf4j.spi.LoggingEventBuilder;

interface LoggerFactory {

	static LoggingEventBuilder getLogger(String name) {
		var log4jLevel = Level.valueOf(LogLevel.DEBUG.name());
		return org.slf4j.LoggerFactory.getLogger(name).atLevel(log4jLevel);
	}

	static LoggingEventBuilder getLogger(String name, LogLevel level) {
		var log4jLevel = Level.valueOf(level.name());
		return org.slf4j.LoggerFactory.getLogger(name).atLevel(log4jLevel);
	}
}



