package extension

import static com.jayway.jsonpath.JsonPath.parse

class JsonCompare {
	static boolean equals(String actual, String expected) {
		return parse(actual).json() == parse(expected).json()
	}
}
