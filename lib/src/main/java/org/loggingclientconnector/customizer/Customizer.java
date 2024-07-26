package org.loggingclientconnector.customizer;

public interface Customizer<T> {

	void apply(T t);
}
