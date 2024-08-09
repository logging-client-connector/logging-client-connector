package org.loggingclientconnector;

import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public interface Fn {

	static <T> OrElse ifNonNull(Supplier<T> fn, Runnable runnable) {
		T result = fn.get();
		if (nonNull(result)) {
			runnable.run();
		}

		return (orElse) -> {
			if (isNull(result))
				orElse.run();
		};
	}

	static <T> T provideOrFail(ThrowableSupplier<T> supplier) {
		try {
			return supplier.get();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	static <T> Optional<T> provide(ThrowableSupplier<T> provider) {
		try {
			return Optional.of(provider.get());
		} catch (Throwable ignored) {
		}
		return Optional.empty();
	}

	interface OrElse {
		void orElse(Runnable orElse);
	}

	interface ThrowableSupplier<T> {
		T get() throws Throwable;
	}
}
