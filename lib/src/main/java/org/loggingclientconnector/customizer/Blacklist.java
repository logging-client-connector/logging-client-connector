package org.loggingclientconnector.customizer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface Blacklist {

	String REPLACEMENT_VALUE = "[PROTECTED]";

	static Blacklist newBlacklist() {
		return new DefaultBlacklist(List.of());
	}

	static Blacklist of(String... properties) {
		return new DefaultBlacklist(Set.of(properties));
	}

	static Blacklist of(Collection<String> properties) {
		return new DefaultBlacklist(new HashSet<>(properties));
	}

	boolean isBlacklisted(String property);

	Collection<String> properties();

	record DefaultBlacklist(Collection<String> properties) implements Blacklist {

		@Override
		public boolean isBlacklisted(String property) {
			return properties.contains(property);
		}
	}
}
