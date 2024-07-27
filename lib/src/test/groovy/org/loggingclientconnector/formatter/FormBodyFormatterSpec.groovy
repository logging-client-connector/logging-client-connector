package org.loggingclientconnector.formatter

import org.loggingclientconnector.customizer.Blocklist
import spock.lang.Specification

class FormBodyFormatterSpec extends Specification {

	def "FormBodyFormatter test isFormData"() {
		expect:
		FormBodyFormatter.isFormData(input) == expected

		where:
		input       | expected
		"key=value" | true
		""          | false
		null        | false
		"some&data" | true
	}

	def "test formatBody"() {
		given:
		var formatter = FormBodyFormatter.getInstance()
		var blocklist = Blocklist.of("key2")
		var content = "key1=value1&key2=value2&key3=value3"

		when:
		var result = formatter.formatBody(content, blocklist)

		then:
		result.contains("key1: value1")
		result.contains("key2: [PROTECTED]")
		result.contains("key3: value3")
	}
}
