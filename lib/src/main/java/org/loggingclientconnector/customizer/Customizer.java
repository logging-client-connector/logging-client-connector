package org.loggingclientconnector.customizer;

/**
 * A functional interface for customizing configurations.
 *
 * @param <T> the type of the object to customize
 */
public interface Customizer<T> {

	/**
	 * Applies customizations to the given object.
	 *
	 * @param t the object to customize
	 */
	void apply(T t);
}
