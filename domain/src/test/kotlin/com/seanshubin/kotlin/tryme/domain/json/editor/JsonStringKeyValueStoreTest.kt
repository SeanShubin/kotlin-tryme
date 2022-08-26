package com.seanshubin.kotlin.tryme.domain.json.editor

import com.seanshubin.kotlin.tryme.domain.json.editor.Converter.Companion.StringConverter
import com.seanshubin.kotlin.tryme.domain.json.editor.Converter.Companion.IntConverter
import com.seanshubin.kotlin.tryme.domain.json.util.JsonUtil.normalizeJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JsonStringKeyValueStoreTest {
    @Test
    fun setStringFromMissing() {
        // given
        val input = ""
        val expected = """{"a":{"b":"c"}}"""
        val editor = JsonStringKeyValueStore(input)

        // when
        editor.setValue( "c", "a", "b")
        val actual = editor.text

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun setStringFromEmpty() {
        // given
        val input = "{}"
        val expected = """{"a":{"b":"c"}}"""
        val editor = JsonStringKeyValueStore(input)

        // when
        editor.setValue( "c", "a", "b")
        val actual = editor.text

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun stringExists() {
        // given
        val input = """{"a":{"b":"c"}}"""
        val editor = JsonStringKeyValueStore(input)

        // when
        val actual = editor.valueExists( "a", "b")

        // then
        assertTrue(actual)
    }

    @Test
    fun stringDoesNotExist() {
        // given
        val input = """{"a":{"c":"d"}}"""
        val editor = JsonStringKeyValueStore(input)

        // when
        val actual = editor.valueExists( "a", "b")

        // then
        assertFalse(actual)
    }

    @Test
    fun getString() {
        // given
        val input = """{"a":{"b":"c"}}"""
        val expected = "c"
        val editor = JsonStringKeyValueStore(input)

        // when
        val actual = editor.getValue(StringConverter,  "a", "b")

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun setIntFromMissing() {
        // given
        val input = ""
        val expected = """{"a":{"b":123}}"""
        val editor = JsonStringKeyValueStore(input)

        // when
        editor.setValue(123, "a", "b")
        val actual = editor.text

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun setIntFromEmpty() {
        // given
        val input = "{}"
        val expected = """{"a":{"b":123}}"""
        val editor = JsonStringKeyValueStore(input)

        // when
        editor.setValue( 123, "a", "b")
        val actual = editor.text

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun intExists() {
        // given
        val input = """{"a":{"b":123}}"""
        val editor = JsonStringKeyValueStore(input)

        // when
        val actual = editor.valueExists( "a", "b")

        // then
        assertTrue(actual)
    }

    @Test
    fun intDoesNotExist() {
        // given
        val input = """{"a":{"c":123}}"""
        val editor = JsonStringKeyValueStore(input)

        // when
        val actual = editor.valueExists( "a", "b")

        // then
        assertFalse(actual)
    }

    @Test
    fun getInt() {
        // given
        val input = """{"a":{"b":123}}"""
        val expected = 123
        val editor = JsonStringKeyValueStore(input)

        // when
        val actual = editor.getValue(IntConverter,  "a", "b")

        // then
        assertEquals(expected, actual)
    }

    private fun assertJsonEquals(expected: String, actual: String) {
        assertEquals(expected.normalizeJson(), actual.normalizeJson())
    }
}
