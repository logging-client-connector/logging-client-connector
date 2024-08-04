package org.loggingclientconnector.formatter;

import java.util.function.Supplier;

import static java.util.Objects.nonNull;

class Function {

	static <T> T getOrDefault(Supplier<T> fn, T defaultValue) {
		T result = Try(fn);
		return nonNull(result) ? result : defaultValue;
	}

	static <T> T Try(Supplier<T> fn) {
		try {
			return fn.get();
		} catch (Exception ignored) {
		}
		return null;
	}
}
