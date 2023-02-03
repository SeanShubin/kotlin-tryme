package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI
import java.net.URISyntaxException
import kotlin.test.Test
import kotlin.test.assertEquals

class LinkParserHtmlTest {
    @Test
    fun simpleFullyQualified() {
        // given
        val page = URI("https://site.org/aaa/bbb.html")
        val text = """
            <a href="https://site.org/aaa/bbb/ccc.html">click me</a>
        """.trimIndent()
        val expected = listOf("https://site.org/aaa/bbb/ccc.html").map { URI(it) }
        val linkParser = createLinkParserHtml()

        // when
        val actual = linkParser.parseLinks(page, text)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun noBody() {
        // given
        val page = URI("https://site.org/aaa/bbb.html")
        val text = """
            <a href="https://site.org/aaa/bbb/ccc.html"/a>
        """.trimIndent()
        val expected = listOf("https://site.org/aaa/bbb/ccc.html").map { URI(it) }
        val linkParser = createLinkParserHtml()

        // when
        val actual = linkParser.parseLinks(page, text)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun lettersBeforeHref() {
        // given
        val page = URI("https://site.org/aaa/bbb.html")
        val text = """
            <a xxxhref="https://site.org/aaa/bbb/ccc.html">click me</a>
        """.trimIndent()
        val expected = emptyList<URI>()
        val linkParser = createLinkParserHtml()

        // when
        val actual = linkParser.parseLinks(page, text)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun startsWithSlash() {
        // given
        val page = URI("https://site.org/aaa/bbb.html")
        val text = """
            <a href="/ddd/eee/fff.html">click me</a>
        """.trimIndent()
        val expected = listOf("https://site.org/ddd/eee/fff.html").map { URI(it) }
        val linkParser = createLinkParserHtml()

        // when
        val actual = linkParser.parseLinks(page, text)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun relative() {
        // given
        val page = URI("https://site.org/aaa/bbb.html")
        val text = """
            <a href="ccc/ddd.html">click me</a>
        """.trimIndent()
        val expected = listOf("https://site.org/aaa/ccc/ddd.html").map { URI(it) }
        val linkParser = createLinkParserHtml()

        // when
        val actual = linkParser.parseLinks(page, text)

        // then
        assertEquals(expected, actual)
    }

    fun createLinkParserHtml(): LinkParser = LinkParserHtml(::uriSyntaxException, ::uriPathIsNull)

    fun uriSyntaxException(uriString: String, ex: URISyntaxException) {
    }

    fun uriPathIsNull(uri:URI){

    }
}
