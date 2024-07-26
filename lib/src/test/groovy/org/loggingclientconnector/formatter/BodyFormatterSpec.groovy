package org.loggingclientconnector.formatter

import extension.JsonCompare
import org.loggingclientconnector.customizer.Blacklist
import spock.lang.Specification
import spock.util.mop.Use


@Use(JsonCompare)
class BodyFormatterSpec extends Specification {

	def "should be able to format body"() {
		when:
		def formatted = BodyFormatter.format(""" { "test": "1" } """, Blacklist.of())

		then:
		formatted.equals """
        {
			"test" : "1"
		}
		"""
	}

	def "should not throw exception when input is null"() {
		when:
		def formatted = BodyFormatter.format(null, Blacklist.of())

		then:
		formatted.contains("[Empty Body]")
	}

	def "should get the same text when try to format input which is not json or form data"() {
		when:
		def formatted = BodyFormatter.format("samle text", Blacklist.of())

		then:
		formatted == "samle text"
	}
}
