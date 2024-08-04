package org.loggingclientconnector.processor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.loggingclientconnector.customizer.Blocklist;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JacksonXmlProcessor implements XmlProcessor {

	private final ObjectMapper xmlMapper = XmlMapper.xmlBuilder()
			.enable(SerializationFeature.INDENT_OUTPUT)
			.enable(ToXmlGenerator.Feature.UNWRAP_ROOT_OBJECT_NODE)
			.defaultUseWrapper(false)
			.addModule(getFixedDeserializer())
			.build();

	public JacksonXmlProcessor() {
	}

	private static SimpleModule getFixedDeserializer() {
		return new SimpleModule().addDeserializer(JsonNode.class, new JsonNodeDeserializer() {
			@Override
			public JsonNode deserialize(JsonParser p, DeserializationContext context) throws IOException {
				final var rootNode = super.deserialize(p, context);
				final var rootName = ((FromXmlParser) p).getStaxReader().getLocalName();
				return context.getNodeFactory().objectNode().set(rootName, rootNode);
			}
		});
	}

	@Override
	public boolean isXml(String content) {
		try {
			xmlMapper.readTree(content);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public String formatBody(String content, Blocklist blocklist) {
		try {
			JsonNode xmlNode = xmlMapper.readTree(content);

			removeBlocklisted(xmlNode, blocklist);

			String formattedXml = xmlMapper
					.writer()
					.withDefaultPrettyPrinter()
					.writeValueAsString(xmlNode);

			return formatWithIndentation(formattedXml);
		} catch (IOException e) {
			return content;
		}
	}

	private void removeBlocklisted(JsonNode xmlNode, Blocklist blocklist) {
		blocklist.properties().forEach((field) -> replaceField(xmlNode, field));
	}

	private void replaceField(JsonNode xmlNode, String fieldName) {
		if (xmlNode.isObject()) {
			Iterator<Map.Entry<String, JsonNode>> fields = xmlNode.fields();

			while (fields.hasNext()) {
				Map.Entry<String, JsonNode> field = fields.next();
				if (field.getKey().equals(fieldName)) {
					((ObjectNode) xmlNode).put(field.getKey(), Blocklist.REPLACEMENT_VALUE);
				} else {
					replaceField(field.getValue(), fieldName);
				}
			}
		} else if (xmlNode.isArray()) {
			xmlNode.forEach((arrayItem) -> replaceField(arrayItem, fieldName));
		}
	}

	private String formatWithIndentation(String xml) {
		return "  [\n" + xml.replaceAll("(?m)^", "    ") + "\n  ]";
	}
}
