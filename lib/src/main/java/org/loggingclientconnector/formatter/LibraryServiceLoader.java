package org.loggingclientconnector.formatter;

import org.loggingclientconnector.processor.XmlProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.ServiceLoader;

class LibraryServiceLoader {

	private static final Logger logger = LoggerFactory.getLogger(LibraryServiceLoader.class);

	public static XmlProcessor getXmlProcessor() {
		return ServiceLoader.load(XmlProcessor.class)
				.stream()
				.map(LibraryServiceLoader::provide)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.findFirst()
				.orElseGet(() -> {
					logger.warn("No jackson-dataformat-xml found on classpath. if you need to log xml, please add it to your dependencies.");
					return XmlProcessor.NoopXmlProcessor.getInstance();
				});
	}

	public static <T> Optional<T> provide(ServiceLoader.Provider<T> provider) {
		try {
			return Optional.of(provider.get());
		} catch (Throwable ignored) {
		}
		return Optional.empty();
	}
}
