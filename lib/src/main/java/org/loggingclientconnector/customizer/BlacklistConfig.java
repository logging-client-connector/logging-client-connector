package org.loggingclientconnector.customizer;

import java.util.StringJoiner;

public final class BlacklistConfig {

	private Blacklist requestBlacklist = Blacklist.newBlacklist();
	private Blacklist responseBlacklist = Blacklist.newBlacklist();
	private Blacklist requestHeaderBlacklist = Blacklist.newBlacklist();

	BlacklistConfig() {
	}

	public static BlacklistConfig newBlacklistConfig() {
		return new BlacklistConfig();
	}

	public BlacklistConfig requestBlacklist(Blacklist requestBlacklist) {
		this.requestBlacklist = requestBlacklist;
		return this;
	}

	public BlacklistConfig responseBlacklist(Blacklist responseBlacklist) {
		this.responseBlacklist = responseBlacklist;
		return this;
	}

	public BlacklistConfig requestHeaderBlacklist(Blacklist requestHeaderBlacklist) {
		this.requestHeaderBlacklist = requestHeaderBlacklist;
		return this;
	}

	public Blacklist getRequestBlacklist() {
		return requestBlacklist;
	}

	public Blacklist getResponseBlacklist() {
		return responseBlacklist;
	}

	public Blacklist getRequestHeaderBlacklist() {
		return requestHeaderBlacklist;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BlacklistConfig that)) return false;

		return requestBlacklist.equals(that.requestBlacklist) && responseBlacklist.equals(that.responseBlacklist) && requestHeaderBlacklist.equals(that.requestHeaderBlacklist);
	}

	@Override
	public int hashCode() {
		int result = requestBlacklist.hashCode();
		result = 31 * result + responseBlacklist.hashCode();
		result = 31 * result + requestHeaderBlacklist.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", BlacklistConfig.class.getSimpleName() + "[", "]")
				.add("requestBlacklist=" + requestBlacklist)
				.add("responseBlacklist=" + responseBlacklist)
				.add("requestHeaderBlacklist=" + requestHeaderBlacklist)
				.toString();
	}
}
