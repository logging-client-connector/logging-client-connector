package org.loggingclientconnector.processor;

import org.loggingclientconnector.customizer.Blocklist;

public interface XmlProcessor {
	boolean isXml(String content);

	String formatBody(String content, Blocklist blocklist);

	class NoopXmlProcessor implements XmlProcessor {
		private static final NoopXmlProcessor INSTANCE = new NoopXmlProcessor();

		public static XmlProcessor getInstance() {
			return INSTANCE;
		}

		@Override
		public boolean isXml(String content) {
			return false;
		}

		@Override
		public String formatBody(String content, Blocklist blocklist) {
			return content;
		}
	}
}
