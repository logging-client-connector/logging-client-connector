package extension

import static com.jayway.jsonpath.JsonPath.parse

class JsonCompare {
	static boolean equals(String expectedJson, String actualJson) {
		return parse(expectedJson).json() == parse(actualJson).json()
	}
}
