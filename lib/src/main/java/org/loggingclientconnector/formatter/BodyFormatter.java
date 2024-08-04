package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blocklist;

import static java.util.Objects.isNull;


interface BodyFormatter {

	String EMPTY_BODY = "";

	static String format(String content, BodyType bodyType, Blocklist blocklist) {
		if (isNull(content) || EMPTY_BODY.equals(content)) {
			return "  [Empty Body]";
		}

		if (bodyType == BodyType.UNKNOWN) {
			return format(content, blocklist);
		}

		return getFormatter(bodyType)
				.formatBody(content, blocklist);
	}

	static String format(String content, Blocklist blocklist) {
		if (isNull(content) || EMPTY_BODY.equals(content)) {
			return "  [Empty Body]";
		}

		var formatter = detectFormatter(content);
		return formatter.formatBody(content, blocklist);
	}

	private static BodyFormatter getFormatter(BodyType bodyType) {
		return switch (bodyType) {
			case JSON -> JsonBodyFormatter.getInstance();
			case XML -> XmlBodyFormatter.getInstance();
			case FORM_DATA -> FormBodyFormatter.getInstance();
			case MULTIPART_FORM_DATA -> MultipartFormBodyFormatter.getInstance();
			case UNSUPPORTED -> UnsupportedBodyFormatter.getInstance();
			case PLAIN_TEXT -> NoopBodyFormatter.getInstance();
			default -> NoopBodyFormatter.getInstance();
		};
	}

	private static BodyFormatter detectFormatter(String content) {
		if (JsonBodyFormatter.isJson(content)) {
			return JsonBodyFormatter.getInstance();
		}
		if (XmlBodyFormatter.isXml(content)) {
			return XmlBodyFormatter.getInstance();
		}
		if (FormBodyFormatter.isFormData(content)) {
			return FormBodyFormatter.getInstance();
		}
		if (MultipartFormBodyFormatter.isMultipartFormData(content)) {
			return MultipartFormBodyFormatter.getInstance();
		}

		return NoopBodyFormatter.getInstance();
	}

	String formatBody(String content, Blocklist blocklist);
}
