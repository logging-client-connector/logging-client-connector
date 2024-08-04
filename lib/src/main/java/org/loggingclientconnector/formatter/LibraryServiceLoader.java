package org.loggingclientconnector.formatter;

import org.loggingclientconnector.processor.XmlProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.ServiceLoader;

class LibraryServiceLoader {

	private static final Logger logger = LoggerFactory.getLogger(LibraryServiceLoader.class);

	public static XmlProcessor getXmlProcessor() {
		var loader = ServiceLoader.load(XmlProcessor.class);
		var iterator = loader.iterator();
		XmlProcessor found = null;
		while (iterator.hasNext()) {
			try {
				found = iterator.next();
			} catch (Error ignored) {
			}
		}
		return Optional.ofNullable(found)
				.orElseGet(() -> {
					logger.warn("No jackson-dataformat-xml found on classpath. if you need to log xml, please add it to your dependencies.");
					return XmlProcessor.NoopXmlProcessor.getInstance();
				});
	}
}
