package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blocklist;

class UnsupportedBodyFormatter implements BodyFormatter {

	private static final UnsupportedBodyFormatter INSTANCE = new UnsupportedBodyFormatter();

	static UnsupportedBodyFormatter getInstance() {
		return INSTANCE;
	}

	@Override
	public String formatBody(String content, Blocklist blocklist) {
		return "  [Unsupported Body]";
	}
}
