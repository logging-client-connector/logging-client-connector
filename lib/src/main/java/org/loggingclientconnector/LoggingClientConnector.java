package org.loggingclientconnector;


import org.loggingclientconnector.customizer.Configuration;
import org.loggingclientconnector.customizer.Customizer;
import org.springframework.http.client.reactive.ClientHttpConnector;

public interface LoggingClientConnector extends ClientHttpConnector {

	static LoggingClientConnector create() {
		return new LoggingClientHttpConnector();
	}

	static LoggingClientConnector create(ClientHttpConnector delegate) {
		return new LoggingClientHttpConnector(delegate);
	}

	LoggingClientConnector configure(Customizer<Configuration> configuration);

	Configuration getConfiguration();
}
