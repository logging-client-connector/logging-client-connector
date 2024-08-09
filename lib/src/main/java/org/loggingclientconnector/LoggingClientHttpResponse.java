package org.loggingclientconnector;

import org.loggingclientconnector.customizer.Configuration;
import org.loggingclientconnector.formatter.Formatter;
import org.slf4j.spi.LoggingEventBuilder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.client.reactive.ClientHttpResponseDecorator;
import reactor.core.publisher.Flux;

import static java.nio.charset.Charset.defaultCharset;
import static org.loggingclientconnector.Fn.ifNonNull;


class LoggingClientHttpResponse extends ClientHttpResponseDecorator {

	public static final String EMPTY_BODY = "";
	private LoggingEventBuilder logger = LoggerFactory.getLogger("Response Logger", LogLevel.INFO);
	private Formatter formatter = Formatter.newFormatter();

	public LoggingClientHttpResponse(ClientHttpResponse delegate) {
		super(delegate);
	}

	static LoggingClientHttpResponse decorate(ClientHttpResponse clientHttpResponse) {
		return new LoggingClientHttpResponse(clientHttpResponse);
	}

	public LoggingClientHttpResponse withConfiguration(Configuration configuration) {
		this.formatter = configuration.getFormatter()
				.addResponseBlocklist(configuration.getBlocklistConfig().getResponseBlocklist());

		this.logger = LoggerFactory.getLogger(configuration.getLoggerConfig().getResponseLoggerName(), configuration.getLoggerConfig().getLogLevel());
		return this;
	}

	@Override
	public Flux<DataBuffer> getBody() {
		return getDelegate().getBody()
				.doOnEach(signal -> {
					var databuffer = signal.get();
					ifNonNull(() -> databuffer, () -> {
						var body = databuffer.toString(defaultCharset());
						var payload = toResponsePayload(body);
						logger.log(formatter.formatResponse(payload));
					}).orElse(() -> {
						var payload = toResponsePayload(EMPTY_BODY);
						logger.log(formatter.formatResponse(payload));
					});
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
