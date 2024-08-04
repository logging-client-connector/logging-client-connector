package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blocklist;
import org.loggingclientconnector.processor.XmlProcessor;

import static org.loggingclientconnector.formatter.LibraryServiceLoader.getXmlProcessor;


class XmlBodyFormatter implements BodyFormatter {

	private static final XmlBodyFormatter INSTANCE = new XmlBodyFormatter();
	private static final XmlProcessor xmlProcessor = getXmlProcessor();

	private XmlBodyFormatter() {
	}

	static XmlBodyFormatter getInstance() {
		return INSTANCE;
	}

	static boolean isXml(String content) {
		return xmlProcessor.isXml(content);
	}


	@Override
	public String formatBody(String content, Blocklist blocklist) {
		return xmlProcessor.formatBody(content, blocklist);
	}
}
