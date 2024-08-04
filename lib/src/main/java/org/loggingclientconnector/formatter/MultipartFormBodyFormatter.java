package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blocklist;

import java.util.regex.Pattern;

class MultipartFormBodyFormatter implements BodyFormatter {

	private static final MultipartFormBodyFormatter INSTANCE = new MultipartFormBodyFormatter();
	private static final Pattern FORM_DATA_PATTERN = Pattern.compile("(?m)^Content-Disposition: form-data", Pattern.MULTILINE);

	private MultipartFormBodyFormatter() {
	}

	static MultipartFormBodyFormatter getInstance() {
		return INSTANCE;
	}

	static boolean isMultipartFormData(String content) {
		return FORM_DATA_PATTERN.matcher(content).find();
	}

	@Override
	public String formatBody(String content, Blocklist blocklist) {
		var formattedContent = content.replaceAll("(?m)^", "    ");
		return "  [\n" + formattedContent + "\n  ]";
	}
}
