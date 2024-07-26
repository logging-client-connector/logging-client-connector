package org.loggingclientconnector.formatter;


import org.loggingclientconnector.customizer.Blacklist;

class NoopBodyFormatter implements BodyFormatter {

	private static final NoopBodyFormatter INSTANCE = new NoopBodyFormatter();

	static BodyFormatter getInstance() {
		return INSTANCE;
	}

	@Override
	public String formatBody(String content, Blacklist blacklist) {
		return content;
	}
}
