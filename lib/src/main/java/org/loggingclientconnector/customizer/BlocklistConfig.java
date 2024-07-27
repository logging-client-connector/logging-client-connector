package org.loggingclientconnector.customizer;

import java.util.StringJoiner;

/**
 * Configuration class for blocklists used in the LoggingClientConnector.
 */
public final class BlocklistConfig {

	private Blocklist requestBlocklist = Blocklist.newBlocklist();
	private Blocklist responseBlocklist = Blocklist.newBlocklist();
	private Blocklist requestHeaderBlocklist = Blocklist.newBlocklist();

	private BlocklistConfig() {
	}

	/**
	 * Creates a new instance of BlocklistConfig.
	 *
	 * @return a new instance of {@link BlocklistConfig}
	 */
	public static BlocklistConfig newBlocklistConfig() {
		return new BlocklistConfig();
	}

	/**
	 * Sets the request blocklist.
	 *
	 * @param requestBlocklist the blocklist to set for requests
	 * @return the updated {@link BlocklistConfig} instance
	 */
	public BlocklistConfig requestBlocklist(Blocklist requestBlocklist) {
		this.requestBlocklist = requestBlocklist;
		return this;
	}

	/**
	 * Sets the response blocklist.
	 *
	 * @param responseBlocklist the blocklist to set for responses
	 * @return the updated {@link BlocklistConfig} instance
	 */
	public BlocklistConfig responseBlocklist(Blocklist responseBlocklist) {
		this.responseBlocklist = responseBlocklist;
		return this;
	}

	/**
	 * Sets the request header blocklist.
	 *
	 * @param requestHeaderBlocklist the blocklist to set for request headers
	 * @return the updated {@link BlocklistConfig} instance
	 */
	public BlocklistConfig requestHeaderBlocklist(Blocklist requestHeaderBlocklist) {
		this.requestHeaderBlocklist = requestHeaderBlocklist;
		return this;
	}

	/**
	 * Gets the request blocklist.
	 *
	 * @return the current request {@link Blocklist}
	 */
	public Blocklist getRequestBlocklist() {
		return requestBlocklist;
	}

	/**
	 * Gets the response blocklist.
	 *
	 * @return the current response {@link Blocklist}
	 */
	public Blocklist getResponseBlocklist() {
		return responseBlocklist;
	}

	/**
	 * Gets the request header blocklist.
	 *
	 * @return the current request header {@link Blocklist}
	 */
	public Blocklist getRequestHeaderBlocklist() {
		return requestHeaderBlocklist;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BlocklistConfig that)) return false;

		return requestBlocklist.equals(that.requestBlocklist) && responseBlocklist.equals(that.responseBlocklist) && requestHeaderBlocklist.equals(that.requestHeaderBlocklist);
	}

	@Override
	public int hashCode() {
		int result = requestBlocklist.hashCode();
		result = 31 * result + responseBlocklist.hashCode();
		result = 31 * result + requestHeaderBlocklist.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", BlocklistConfig.class.getSimpleName() + "[", "]")
				.add("requestBlocklist=" + requestBlocklist)
				.add("responseBlocklist=" + responseBlocklist)
				.add("requestHeaderBlocklist=" + requestHeaderBlocklist)
				.toString();
	}
}
