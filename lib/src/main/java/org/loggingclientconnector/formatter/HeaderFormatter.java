package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blacklist;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

class HeaderFormatter {

	static Map<String, String> formatHeader(Formatter.RequestPayload payload, Blacklist requestHeaderBlacklist) {
		return payload.headers().entrySet()
				.stream()
				.peek(entry -> {
					if (requestHeaderBlacklist.isBlacklisted(entry.getKey())) {
						entry.setValue(Blacklist.REPLACEMENT_VALUE);
					}
				})
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
