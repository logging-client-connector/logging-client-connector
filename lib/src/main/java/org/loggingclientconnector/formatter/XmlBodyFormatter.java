package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blocklist;
import org.loggingclientconnector.processor.XmlProcessor;

import static org.loggingclientconnector.formatter.LibraryServiceLoader.getXmlProcessor;


class XmlBodyFormatter implements BodyFormatter {

	private static XmlBodyFormatter INSTANCE;
	private static XmlProcessor xmlProcessor;

	private XmlBodyFormatter() {
		xmlProcessor = getXmlProcessor();
	}

	static synchronized XmlBodyFormatter getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new XmlBodyFormatter();
		}
		return INSTANCE;
	}

	static boolean isXml(String content) {
		if (xmlProcessor == null) {
			xmlProcessor = getXmlProcessor();
		}
		return xmlProcessor.isXml(content);
	}

	@Override
	public String formatBody(String content, Blocklist blocklist) {
		return xmlProcessor.formatBody(content, blocklist);
	}
}
