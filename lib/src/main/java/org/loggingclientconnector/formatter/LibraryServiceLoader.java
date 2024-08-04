package org.loggingclientconnector.formatter;

import org.loggingclientconnector.processor.XmlProcessor;

import java.util.ServiceLoader;

import static org.loggingclientconnector.formatter.Function.getOrDefault;

class LibraryServiceLoader {

	static XmlProcessor getXmlProcessor() {
		return getOrDefault(() -> {
			var loader = ServiceLoader.load(XmlProcessor.class);
			return loader.findFirst().get();
		}, XmlProcessor.NoopXmlProcessor.getInstance());
	}
}
