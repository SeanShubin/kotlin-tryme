package com.seanshubin.kotlin.tryme.domain.json.editor

import com.seanshubin.kotlin.tryme.domain.json.editor.Converter.Companion.StringConverter
import com.seanshubin.kotlin.tryme.domain.json.editor.Converter.Companion.IntConverter
import com.seanshubin.kotlin.tryme.domain.json.util.JsonUtil.normalizeJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JsonTextEditorTest {
    @Test
    fun setStringFromMissing() {
        // given
        val input = ""
        val expected = """{"a":{"b":"c"}}"""
        val editor = JsonTextEditor()

        // when
        val actual = editor.setValue(input, "c", "a", "b")

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun setStringFromEmpty() {
        // given
        val input = "{}"
        val expected = """{"a":{"b":"c"}}"""
        val editor = JsonTextEditor()

        // when
        val actual = editor.setValue(input, "c", "a", "b")

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun stringExists() {
        // given
        val input = """{"a":{"b":"c"}}"""
        val editor = JsonTextEditor()

        // when
        val actual = editor.valueExists(input, "a", "b")

        // then
        assertTrue(actual)
    }

    @Test
    fun stringDoesNotExist() {
        // given
        val input = """{"a":{"c":"d"}}"""
        val editor = JsonTextEditor()

        // when
        val actual = editor.valueExists(input, "a", "b")

        // then
        assertFalse(actual)
    }

    @Test
    fun getString() {
        // given
        val input = """{"a":{"b":"c"}}"""
        val expected = "c"
        val editor = JsonTextEditor()

        // when
        val actual = editor.getValue(StringConverter, input, "a", "b")

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun setIntFromMissing() {
        // given
        val input = ""
        val expected = """{"a":{"b":123}}"""
        val editor = JsonTextEditor()

        // when
        val actual = editor.setValue(input, 123, "a", "b")

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun setIntFromEmpty() {
        // given
        val input = "{}"
        val expected = """{"a":{"b":123}}"""
        val editor = JsonTextEditor()

        // when
        val actual = editor.setValue(input, 123, "a", "b")

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun intExists() {
        // given
        val input = """{"a":{"b":123}}"""
        val editor = JsonTextEditor()

        // when
        val actual = editor.valueExists(input, "a", "b")

        // then
        assertTrue(actual)
    }

    @Test
    fun intDoesNotExist() {
        // given
        val input = """{"a":{"c":123}}"""
        val editor = JsonTextEditor()

        // when
        val actual = editor.valueExists(input, "a", "b")

        // then
        assertFalse(actual)
    }

    @Test
    fun getInt() {
        // given
        val input = """{"a":{"b":123}}"""
        val expected = 123
        val editor = JsonTextEditor()

        // when
        val actual = editor.getValue(IntConverter, input, "a", "b")

        // then
        assertEquals(expected, actual)
    }

    private fun assertJsonEquals(expected: String, actual: String) {
        assertEquals(expected.normalizeJson(), actual.normalizeJson())
    }
}
