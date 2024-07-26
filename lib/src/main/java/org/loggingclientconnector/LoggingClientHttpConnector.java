package org.loggingclientconnector;

import org.loggingclientconnector.customizer.Configuration;
import org.loggingclientconnector.customizer.Customizer;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

import static org.loggingclientconnector.customizer.Configuration.newConfiguration;


class LoggingClientHttpConnector implements LoggingClientConnector {

	private final ClientHttpConnector delegate;
	private Configuration configuration;

	LoggingClientHttpConnector() {
		this.delegate = new ReactorClientHttpConnector();
	}

	LoggingClientHttpConnector(ClientHttpConnector delegate) {
		this.delegate = delegate;
	}

	@Override
	public Mono<ClientHttpResponse> connect(HttpMethod method, URI uri, Function<? super ClientHttpRequest, Mono<Void>> callback) {
		return this.delegate.connect(method, uri, getClientHttpRequest(callback))
				.map(getClientHttpResponse());
	}

	private Function<ClientHttpRequest, Mono<Void>> getClientHttpRequest(Function<? super ClientHttpRequest, Mono<Void>> callback) {
		return request -> callback.apply(LoggingClientHttpRequest.decorate(request)
				.withConfiguration(getConfiguration()));
	}

	private Function<ClientHttpResponse, ClientHttpResponse> getClientHttpResponse() {
		return clientHttpResponse -> LoggingClientHttpResponse.decorate(clientHttpResponse)
				.withConfiguration(getConfiguration());
	}

	@Override
	public LoggingClientConnector configure(Customizer<Configuration> configuration) {
		var newConfiguration = newConfiguration();
		configuration.apply(newConfiguration);
		this.configuration = newConfiguration;
		return this;
	}

	@Override
	public Configuration getConfiguration() {
		return Optional.ofNullable(this.configuration)
				.orElse(newConfiguration());
	}
}