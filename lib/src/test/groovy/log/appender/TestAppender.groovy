package log.appender

import ch.qos.logback.classic.spi.LoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.Context
import ch.qos.logback.core.encoder.Encoder
import ch.qos.logback.core.spi.FilterReply

import java.util.concurrent.ConcurrentHashMap

class TestAppender<E> extends ConsoleAppender<E> {

	private ConsoleAppender<E> delegate
	private static Map<String, LogCapture> captures = new ConcurrentHashMap<>()

	TestAppender() {
		this.delegate = new ConsoleAppender<>()
	}

	@Override
	void start() {
		delegate.setContext(getContext())
		delegate.setName(getName())
		delegate.setEncoder(getEncoder())
		delegate.setTarget(getTarget())
		delegate.start()
		super.start()
	}

	@Override
	void stop() {
		delegate.stop()
		super.stop()
	}

	@Override
	protected void append(E eventObject) {
		delegate.doAppend(eventObject)
		if (captures.size() && eventObject instanceof LoggingEvent) {
			def event = eventObject as LoggingEvent
			def name = event.getLoggerName()
			def capture = captures.get(name)
			capture?.addMessage(event.toString())
		}
	}

	@Override
	void setContext(Context context) {
		super.setContext(context)
		delegate.setContext(context)
	}

	@Override
	void setName(String name) {
		super.setName(name)
		delegate.setName(name)
	}

	@Override
	void setEncoder(Encoder<E> encoder) {
		super.setEncoder(encoder)
		delegate.setEncoder(encoder)
	}

	@Override
	void setTarget(String value) {
		super.setTarget(value)
		delegate.setTarget(value)
	}

	@Override
	FilterReply getFilterChainDecision(E event) {
		return delegate.getFilterChainDecision(event)
	}

	static LogCapture createLogCapture(String... loggerName) {
		def logCapture = new LogCapture()
		logCapture.startCapturingLogs()
		loggerName.each { name ->
			captures.put(name, logCapture)
		}
		return logCapture
	}

	static boolean removeCapture(String loggerName) {
		captures.remove(loggerName)
	}
}