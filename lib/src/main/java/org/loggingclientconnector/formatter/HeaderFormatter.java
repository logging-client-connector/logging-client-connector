package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blocklist;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

class HeaderFormatter {

	static Map<String, String> formatHeader(Formatter.RequestPayload payload, Blocklist requestHeaderBlocklist) {
		return payload.headers().entrySet()
				.stream()
				.peek(entry -> {
					if (requestHeaderBlocklist.isBlocklisted(entry.getKey())) {
						entry.setValue(Blocklist.REPLACEMENT_VALUE);
					}
				})
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
