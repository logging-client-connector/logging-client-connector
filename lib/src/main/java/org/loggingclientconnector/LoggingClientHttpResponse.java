package org.loggingclientconnector;

import org.loggingclientconnector.customizer.Configuration;
import org.loggingclientconnector.formatter.Formatter;
import org.slf4j.spi.LoggingEventBuilder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.client.reactive.ClientHttpResponseDecorator;
import reactor.core.publisher.Flux;

import static java.nio.charset.Charset.defaultCharset;


class LoggingClientHttpResponse extends ClientHttpResponseDecorator {

	public static final String DEFAULT_LOGGER_NAME = "Response Logger";

	private LoggingEventBuilder logger = LoggerFactory.getLogger("Response Logger", LogLevel.DEBUG);
	private Formatter formatter = Formatter.newFormatter();

	public LoggingClientHttpResponse(ClientHttpResponse delegate) {
		super(delegate);
	}

	static LoggingClientHttpResponse decorate(ClientHttpResponse clientHttpResponse) {
		return new LoggingClientHttpResponse(clientHttpResponse);
	}

	public LoggingClientHttpResponse withConfiguration(Configuration configuration) {
		this.formatter = configuration.getFormatter()
				.addResponseBlacklist(configuration.getBlacklistConfig().getResponseBlacklist());

		this.logger = LoggerFactory.getLogger(DEFAULT_LOGGER_NAME, configuration.getLoggerConfig().getLogLevel());
		return this;
	}

	@Override
	public Flux<DataBuffer> getBody() {
		return getDelegate().getBody()
				.map(dataBuffer -> {
					var body = dataBuffer.toString(defaultCharset());
					var payload = toResponsePayload(body);
					logger.log(formatter.formatResponse(payload));
					return dataBuffer;
				});
	}

	private Formatter.ResponsePayload toResponsePayload(String body) {
		return new Formatter.ResponsePayload(
				getDelegate().getStatusCode(),
				body,
				getDelegate().getHeaders().toSingleValueMap(),
				getDelegate().getCookies().toSingleValueMap()
		);
	}
}
