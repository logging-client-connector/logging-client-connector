package org.loggingclientconnector;

import org.loggingclientconnector.customizer.Configuration;
import org.loggingclientconnector.formatter.Formatter;
import org.slf4j.spi.LoggingEventBuilder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.client.reactive.ClientHttpResponseDecorator;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;

import static java.nio.charset.Charset.defaultCharset;
import static org.loggingclientconnector.Fn.doWhen;


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
		var responseLogged = new AtomicReference<>(false);
		return getDelegate().getBody()
				.doOnNext(databuffer -> doWhen(
						!responseLogged.get(),
						() -> {
							var payload = toResponsePayload(databuffer.toString(defaultCharset()));
							logger.log(formatter.formatResponse(payload));
							responseLogged.set(true);
						}
				)).doOnComplete(() -> doWhen(
						!responseLogged.get(),
						() -> {
							var payload = toResponsePayload(EMPTY_BODY);
							logger.log(formatter.formatResponse(payload));
							responseLogged.set(true);
						}
				));
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
