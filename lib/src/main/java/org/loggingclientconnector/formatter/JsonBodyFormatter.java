package org.loggingclientconnector.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.loggingclientconnector.customizer.Blocklist;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_TRAILING_TOKENS;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;


class JsonBodyFormatter implements BodyFormatter {

	private static final BodyFormatter INSTANCE = new JsonBodyFormatter();

	private static final ObjectMapper mapper = new ObjectMapper()
			.enable(FAIL_ON_TRAILING_TOKENS)
			.enable(INDENT_OUTPUT);

	private static final DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter()
			.withArrayIndenter(new DefaultIndenter("    ", DefaultIndenter.SYS_LF))
			.withObjectIndenter(new DefaultIndenter("    ", DefaultIndenter.SYS_LF));

	static BodyFormatter getInstance() {
		return INSTANCE;
	}

	static boolean isJson(String content) {
		try {
			mapper.readTree(content);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public String formatBody(String content, Blocklist blocklist) {
		try {
			JsonNode jsonNode = mapper.readTree(content);
			jsonNode = removeBlocklisted(jsonNode, blocklist);
			return mapper.writer(prettyPrinter).writeValueAsString(jsonNode)
					.replaceAll("(?m)^", "  ");
		} catch (JsonProcessingException e) {
			return content;
		}
	}

	public JsonNode removeBlocklisted(JsonNode jsonNode, Blocklist blocklist) {
		blocklist.properties().forEach((field) -> replaceField(jsonNode, field));
		return jsonNode;
	}

	private void replaceField(JsonNode jsonNode, String fieldName) {

		if (jsonNode.isObject()) {
			ObjectNode objectNode = (ObjectNode) jsonNode;
			Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();

			fields.forEachRemaining((field) -> {
				if (field.getKey().equals(fieldName)) {
					objectNode.put(field.getKey(), Blocklist.REPLACEMENT_VALUE);
				} else {
					replaceField(field.getValue(), fieldName);
				}
			});
		} else if (jsonNode.isArray()) {
			jsonNode.forEach((arrayItem) -> replaceField(arrayItem, fieldName));
		}
	}
}