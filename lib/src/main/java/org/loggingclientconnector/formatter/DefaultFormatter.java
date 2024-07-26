package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blacklist;


@SuppressWarnings("StringBufferReplaceableByString")
class DefaultFormatter implements Formatter {

	private static final String NEW_LINE = System.lineSeparator();

	private Blacklist requestBlacklist = Blacklist.newBlacklist();
	private Blacklist responseBlacklist = Blacklist.newBlacklist();
	private Blacklist requestHeaderBlacklist = Blacklist.newBlacklist();

	DefaultFormatter() {
	}

	@Override
	public String formatRequest(RequestPayload payload) {
		var body = BodyFormatter.format(payload.body(), requestBlacklist);
		var headers = HeaderFormatter.formatHeader(payload, requestHeaderBlacklist);

		return new StringBuilder()
				.append(NEW_LINE)
				.append("Request: [").append(NEW_LINE)
				.append("  uri: ").append(payload.uri()).append(NEW_LINE)
				.append("  method: ").append(payload.method()).append(NEW_LINE)
				.append("  headers: ").append(headers.entrySet()).append(NEW_LINE)
				.append("  body: ").append(NEW_LINE).append(body).append(NEW_LINE)
				.append("  cookies: ").append(payload.cookies().entrySet()).append(NEW_LINE)
				.append("]")
				.toString();
	}

	@Override
	public String formatResponse(ResponsePayload payload) {
		var body = BodyFormatter.format(payload.body(), responseBlacklist);

		return new StringBuilder()
				.append(NEW_LINE)
				.append("Response: [").append(NEW_LINE)
				.append("  status code: ").append(payload.statusCode()).append(NEW_LINE)
				.append("  headers: ").append(payload.headers().entrySet()).append(NEW_LINE)
				.append("  body: ").append(NEW_LINE).append(body).append(NEW_LINE)
				.append("  cookies: ").append(payload.cookies().entrySet()).append(NEW_LINE)
				.append("]")
				.toString();
	}

	@Override
	public Formatter addRequestBlacklist(Blacklist blacklist) {
		this.requestBlacklist = blacklist;
		return this;
	}

	@Override
	public Formatter addResponseBlacklist(Blacklist blacklist) {
		this.responseBlacklist = blacklist;
		return this;
	}

	@Override
	public Formatter addRequestHeaderBlacklist(Blacklist blacklist) {
		this.requestHeaderBlacklist = blacklist;
		return this;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DefaultFormatter that)) return false;

		return requestBlacklist.equals(that.requestBlacklist)
				&& responseBlacklist.equals(that.responseBlacklist)
				&& requestHeaderBlacklist.equals(that.requestHeaderBlacklist);
	}

	@Override
	public int hashCode() {
		int result = requestBlacklist.hashCode();
		result = 31 * result + responseBlacklist.hashCode();
		result = 31 * result + requestHeaderBlacklist.hashCode();
		return result;
	}
}