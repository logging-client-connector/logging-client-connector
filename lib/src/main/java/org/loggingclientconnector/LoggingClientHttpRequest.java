package org.loggingclientconnector;

import org.loggingclientconnector.customizer.Configuration;
import org.loggingclientconnector.formatter.Formatter;
import org.loggingclientconnector.formatter.Formatter.RequestPayload;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.spi.LoggingEventBuilder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

import static org.loggingclientconnector.LogLevel.INFO;


class LoggingClientHttpRequest extends ClientHttpRequestDecorator {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(LoggingClientHttpRequest.class);
	private LoggingEventBuilder logger = LoggerFactory.getLogger("Request Logger", INFO);
	private Formatter formatter = Formatter.newFormatter();

	private LoggingClientHttpRequest(ClientHttpRequest delegate) {
		super(delegate);
		log.trace("Creating LoggingClientHttpRequest with delegate: {}", delegate);
	}

	public static LoggingClientHttpRequest decorate(ClientHttpRequest delegate) {
		return new LoggingClientHttpRequest(delegate);
	}

	private static RequestPayload toRequestPayload(String body, ClientHttpRequest request) {
		return new RequestPayload(
				request.getURI().toString(),
				request.getMethod().name(),
				body,
				request.getHeaders().toSingleValueMap(),
				request.getCookies().toSingleValueMap()
		);
	}

	public LoggingClientHttpRequest withConfiguration(Configuration configuration) {
		this.formatter = configuration.getFormatter()
				.addRequestBlocklist(configuration.getBlocklistConfig().getRequestBlocklist())
				.addRequestHeaderBlocklist(configuration.getBlocklistConfig().getRequestHeaderBlocklist());
		this.logger = LoggerFactory.getLogger(configuration.getLoggerConfig().getRequestLoggerName(), configuration.getLoggerConfig().getLogLevel());
		return this;
	}

	@Override
	public Mono<Void> setComplete() {
		ClientHttpRequest request = getDelegate();
		logRequest("", request);
		return super.setComplete();
	}

	@Override
	public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
		final var bodyReference = new AtomicReference<>(new StringBuilder());
		final var requestReference = new AtomicReference<ClientHttpRequest>();
		BaseSubscriber<DataBuffer> bodySubscriber = new BaseSubscriber<>() {

			@Override
			protected void hookOnNext(DataBuffer dataBuffer) {
				String bodyString = dataBuffer.toString(Charset.defaultCharset());
				ClientHttpRequest request = getDelegate();
				requestReference.set(request);
				bodyReference.get().append(bodyString);
			}

			@Override
			protected void hookOnComplete() {
				logRequest(bodyReference.get().toString(), requestReference.get());
			}
		};
		body.subscribe(bodySubscriber);
		return super.writeWith(body);
	}

	@Override
	public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
		final var bodyReference = new AtomicReference<>(new StringBuilder());
		final var requestReference = new AtomicReference<ClientHttpRequest>();
		BaseSubscriber<Publisher<? extends DataBuffer>> bodySubscriber = new BaseSubscriber<>() {

			@Override
			protected void hookOnNext(Publisher<? extends DataBuffer> next) {
				ClientHttpRequest request = getDelegate();
				requestReference.set(request);
				bodyReference.get().append(next);
			}

			@Override
			protected void hookOnComplete() {
				logRequest(bodyReference.get().toString(), requestReference.get());
			}
		};
		body.subscribe(bodySubscriber);
		bodySubscriber.request(Long.MAX_VALUE);

		return super.writeAndFlushWith(body);
	}

	private void logRequest(String body, ClientHttpRequest request) {
		var requestPayload = toRequestPayload(body, request);
		var message = formatter.formatRequest(requestPayload);
		logger.log(message);
	}
}
