package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blocklist;


@SuppressWarnings("StringBufferReplaceableByString")
class DefaultFormatter implements Formatter {

	private static final String NEW_LINE = System.lineSeparator();

	private Blocklist requestBlocklist = Blocklist.newBlocklist();
	private Blocklist responseBlocklist = Blocklist.newBlocklist();
	private Blocklist requestHeaderBlocklist = Blocklist.newBlocklist();

	DefaultFormatter() {
	}

	@Override
	public String formatRequest(RequestPayload payload) {
		var body = BodyFormatter.format(payload.body(), requestBlocklist);
		var headers = HeaderFormatter.formatHeader(payload, requestHeaderBlocklist);

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
		var body = BodyFormatter.format(payload.body(), responseBlocklist);

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
	public Formatter addRequestBlocklist(Blocklist blocklist) {
		this.requestBlocklist = blocklist;
		return this;
	}

	@Override
	public Formatter addResponseBlocklist(Blocklist blocklist) {
		this.responseBlocklist = blocklist;
		return this;
	}

	@Override
	public Formatter addRequestHeaderBlocklist(Blocklist blocklist) {
		this.requestHeaderBlocklist = blocklist;
		return this;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DefaultFormatter that)) return false;

		return requestBlocklist.equals(that.requestBlocklist)
				&& responseBlocklist.equals(that.responseBlocklist)
				&& requestHeaderBlocklist.equals(that.requestHeaderBlocklist);
	}

	@Override
	public int hashCode() {
		int result = requestBlocklist.hashCode();
		result = 31 * result + responseBlocklist.hashCode();
		result = 31 * result + requestHeaderBlocklist.hashCode();
		return result;
	}
}