package org.loggingclientconnector.customizer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This package contains classes and interfaces for the LoggingClientConnector.
 */
public interface Blocklist {

	/**
	 * The value to replace blocklisted properties with.
	 */
	String REPLACEMENT_VALUE = "[PROTECTED]";

	/**
	 * Represents a blocklist used to protect sensitive properties.
	 * <p>
	 * Example usage:
	 * <pre>{@code
	 * Blocklist blocklist = Blocklist.of("password", "secretKey");
	 * boolean isBlocklisted = blocklist.isBlocklisted("password");
	 * }</pre>
	 * @return a new instance of {@link Blocklist}
	 */
	static Blocklist newBlocklist() {
		return new DefaultBlocklist(List.of());
	}

	/**
	 * Creates a new instance of {@link Blocklist} with the specified properties.
	 *
	 * @param properties the properties to include in the blocklist
	 * @return a new instance of {@link Blocklist} with the specified properties
	 */
	static Blocklist of(String... properties) {
		return new DefaultBlocklist(Set.of(properties));
	}

	/**
	 * Creates a new instance of {@link Blocklist} with the specified properties.
	 *
	 * @param properties the properties to include in the blocklist
	 * @return a new instance of {@link Blocklist} with the specified properties
	 */
	static Blocklist of(Collection<String> properties) {
		return new DefaultBlocklist(new HashSet<>(properties));
	}

	/**
	 * Checks if the specified property is blocklisted.
	 *
	 * @param property the property to check
	 * @return true if the property is blocklisted, false otherwise
	 */
	boolean isBlocklisted(String property);

	/**
	 * Gets the properties in the blocklist.
	 *
	 * @return the properties in the blocklist
	 */
	Collection<String> properties();

	/**
	 * This package contains classes and interfaces for the LoggingClientConnector.
	 */
	record DefaultBlocklist(Collection<String> properties) implements Blocklist {

		@Override
		public boolean isBlocklisted(String property) {
			return properties.contains(property);
		}
	}
}
