package org.loggingclientconnector.customizer

import spock.lang.Specification

class BlacklistSpec extends Specification {

	def "should be able to create Blacklist"() {
		given:
		Blacklist blacklist = Blacklist.of("property1", "property2")

		expect:
		blacklist.isBlacklisted(input) == expected

		where:
		input       | expected
		"property1" | true
		"property2" | true
		"property3" | false
	}

	def "should be able to create Blacklist with empty values"() {
		given:
		Blacklist blacklist = Blacklist.of()

		expect:
		!blacklist.isBlacklisted(input)

		where:
		input | _
		"any" | _
	}

	def "should be able to get properties"() {
		given:
		Blacklist blacklist = Blacklist.of("property1", "property2")

		expect:
		blacklist.properties().toSet() == ["property1", "property2"] as Set
	}
}
