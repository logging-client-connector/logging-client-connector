package org.loggingclientconnector.formatter;

import static java.util.Arrays.stream;

enum BodyType {
	JSON,
	XML,
	HTML,
	FORM_DATA,
	MULTIPART_FORM_DATA,
	PLAIN_TEXT,
	YAML,
	CSV,
	NDJSON,
	UNSUPPORTED,
	UNKNOWN;

	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_XML = "application/xml";
	public static final String TEXT_XML = "text/xml";
	public static final String TEXT_HTML = "text/html";
	public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String MULTIPART_FORM = "multipart/form-data";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String APPLICATION_X_YAML = "application/x-yaml";
	public static final String TEXT_CSV = "text/csv";
	public static final String APPLICATION_X_NDJSON = "application/x-ndjson";

	// unsupported
	public static final String APPLICATION_BSON = "application/bson";
	public static final String APPLICATION_X_PROTOBUF = "application/x-protobuf";
	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	public static final String SEPARATOR = ";";

	static BodyType detect(String content) {
		if (content == null || content.isBlank()) {
			return UNKNOWN;
		}
		return stream(content.split(SEPARATOR))
				.map(String::trim)
				.map(split -> switch (split) {
					case APPLICATION_JSON -> BodyType.JSON;
					case APPLICATION_XML, TEXT_XML -> BodyType.XML;
					case TEXT_HTML -> BodyType.HTML;
					case APPLICATION_X_WWW_FORM_URLENCODED -> BodyType.FORM_DATA;
					case MULTIPART_FORM -> BodyType.MULTIPART_FORM_DATA;
					case TEXT_PLAIN -> BodyType.PLAIN_TEXT;
					case APPLICATION_X_YAML -> BodyType.YAML;
					case TEXT_CSV -> BodyType.CSV;
					case APPLICATION_X_NDJSON -> BodyType.NDJSON;
					case APPLICATION_BSON, APPLICATION_X_PROTOBUF, APPLICATION_OCTET_STREAM -> BodyType.UNSUPPORTED;
					default -> BodyType.UNKNOWN;
				})
				.findFirst()
				.orElse(UNKNOWN);
	}
}
