package org.loggingclientconnector.customizer

import spock.lang.Specification

class BlocklistSpec extends Specification {

	def "should be able to create Blocklist"() {
		given:
		Blocklist blocklist = Blocklist.of("property1", "property2")

		expect:
		blocklist.isBlocklisted(input) == expected

		where:
		input       | expected
		"property1" | true
		"property2" | true
		"property3" | false
	}

	def 'should be able to create Blocklist with empty values'() {
		given:
		Blocklist blocklist = Blocklist.of()

		expect:
		!blocklist.isBlocklisted(input)

		where:
		input | _
		"any" | _
	}

	def "should be able to get properties"() {
		given:
		Blocklist blocklist = Blocklist.of("property1", "property2")

		expect:
		blocklist.properties().toSet() == ["property1", "property2"] as Set
	}
}
