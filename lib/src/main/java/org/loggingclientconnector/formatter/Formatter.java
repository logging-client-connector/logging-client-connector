package org.loggingclientconnector.formatter;

import org.loggingclientconnector.customizer.Blocklist;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;

import java.util.Map;

/**
 * Formatter interface for formatting request and response payloads.
 */
public interface Formatter {

	/**
	 * Creates a new instance of {@link Formatter}.
	 *
	 * @return a new instance of {@link Formatter}
	 */
	static Formatter newFormatter() {
		return new DefaultFormatter();
	}

	/**
	 * Formats the request payload.
	 *
	 * @param requestPayload the request payload to format
	 * @return the formatted request as a {@link String}
	 */
	String formatRequest(RequestPayload requestPayload);

	/**
	 * Formats the response payload.
	 *
	 * @param responsePayload the response payload to format
	 * @return the formatted response as a {@link String}
	 */
	String formatResponse(ResponsePayload responsePayload);

	/**
	 * Adds a blocklist for requests.
	 *
	 * @param blocklist the blocklist to add
	 * @return the updated {@link Formatter} instance
	 */
	Formatter addRequestBlocklist(Blocklist blocklist);

	/**
	 * Adds a blocklist for responses.
	 *
	 * @param blocklist the blocklist to add
	 * @return the updated {@link Formatter} instance
	 */
	Formatter addResponseBlocklist(Blocklist blocklist);

	/**
	 * Adds a blocklist for request headers.
	 *
	 * @param blocklist the blocklist to add
	 * @return the updated {@link Formatter} instance
	 */
	Formatter addRequestHeaderBlocklist(Blocklist blocklist);

	/**
	 * Represents the request payload.
	 */
	record RequestPayload(
			String uri,
			String method,
			String body,
			Map<String, String> headers,
			Map<String, HttpCookie> cookies
	) {
	}

	/**
	 * Represents the response payload.
	 */
	record ResponsePayload(
			HttpStatusCode statusCode,
			String body,
			Map<String, String> headers,
			Map<String, ResponseCookie> cookies
	) {
	}
}
