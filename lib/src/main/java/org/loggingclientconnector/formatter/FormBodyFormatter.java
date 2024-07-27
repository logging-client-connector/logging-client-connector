package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blocklist;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLDecoder;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;


class FormBodyFormatter implements BodyFormatter {

	private static final FormBodyFormatter INSTANCE = new FormBodyFormatter();

	private static final String NEW_LINE = System.lineSeparator();

	FormBodyFormatter() {
	}

	static BodyFormatter getInstance() {
		return INSTANCE;
	}

	static boolean isFormData(String input) {
		if (isNull(input) || input.isEmpty()) {
			return false;
		}
		if (!(input.contains("=") || input.contains("&"))) {
			return false;
		}
		try {
			var params = toMap(input);
			return !params.isEmpty();
		} catch (Exception e) {
			return false;
		}
	}

	private static Map<String, String> toMap(String input) {
		return UriComponentsBuilder
				.fromUriString("?" + input)
				.build()
				.getQueryParams()
				.toSingleValueMap();
	}

	private static String getValue(Blocklist blocklist, String key, String value) {
		if (isNull(value)) {
			return "";
		}
		return blocklist.isBlocklisted(key) ? Blocklist.REPLACEMENT_VALUE : URLDecoder.decode(value, UTF_8);
	}

	@Override
	public String formatBody(String content, Blocklist blocklist) {
		var params = toMap(content);
		var stringBuilder = new StringBuilder();
		stringBuilder.append("  [");
		params.forEach((key, value) -> stringBuilder
				.append(NEW_LINE)
				.append("      ")
				.append(key)
				.append(": ")
				.append(getValue(blocklist, key, value))
		);
		stringBuilder.append(NEW_LINE).append("  ]");
		return stringBuilder.toString();
	}
}