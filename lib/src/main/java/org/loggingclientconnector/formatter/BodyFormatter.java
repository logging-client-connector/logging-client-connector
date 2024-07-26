package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blacklist;

import static java.util.Objects.isNull;


interface BodyFormatter {

	String EMPTY_BODY = "";

	static String format(String content, Blacklist blacklist) {
		if (isNull(content) || EMPTY_BODY.equals(content)) {
			return "  [Empty Body]";
		}
		var formatter = getFormatter(content);
		return formatter.formatBody(content, blacklist);
	}

	private static BodyFormatter getFormatter(String content) {
		if (JsonBodyFormatter.isJson(content)) {
			return JsonBodyFormatter.getInstance();
		}
		if (FormBodyFormatter.isFormData(content)) {
			return FormBodyFormatter.getInstance();
		}
		return NoopBodyFormatter.getInstance();
	}

	String formatBody(String content, Blacklist blacklist);
}
