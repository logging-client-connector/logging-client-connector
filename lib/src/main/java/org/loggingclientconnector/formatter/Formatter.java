package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blacklist;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;

import java.util.Map;

public interface Formatter {

	static Formatter newFormatter() {
		return new DefaultFormatter();
	}

	String formatRequest(RequestPayload requestPayload);

	String formatResponse(ResponsePayload responsePayload);

	Formatter addRequestBlacklist(Blacklist blacklist);

	Formatter addResponseBlacklist(Blacklist blacklist);

	Formatter addRequestHeaderBlacklist(Blacklist blacklist);

	record RequestPayload(
			String uri,
			String method,
			String body,
			Map<String, String> headers,
			Map<String, HttpCookie> cookies
	) {
	}

	record ResponsePayload(
			HttpStatusCode statusCode,
			String body,
			Map<String, String> headers,
			Map<String, ResponseCookie> cookies
	) {
	}
}
