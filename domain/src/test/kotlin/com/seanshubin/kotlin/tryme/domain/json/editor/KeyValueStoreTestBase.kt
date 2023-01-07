package com.seanshubin.kotlin.tryme.domain.json.store

import com.seanshubin.kotlin.tryme.domain.json.JsonMappers.normalizeJson
import com.seanshubin.kotlin.tryme.domain.json.editor.KeyValueStore
import com.seanshubin.kotlin.tryme.domain.json.editor.Converter.Companion.IntConverter
import com.seanshubin.kotlin.tryme.domain.json.editor.Converter.Companion.StringConverter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class KeyValueStoreTestBase {
    abstract fun createStore(): KeyValueStore
    abstract fun createStore(initialValue:Map<*, *>): KeyValueStore

    @Test
    fun setStringFromMissing() {
        // given
        val expected = """{"a":{"b":"c"}}"""
        val store = createStore()

        // when
        store.setValue("c", "a", "b")
        val actual = store.text

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun setStringFromEmpty() {
        // given
        val initialValue = emptyMap<String, String>()
        val expected = """{"a":{"b":"c"}}"""
        val store = createStore(initialValue)

        // when
        store.setValue("c", "a", "b")
        val actual = store.text

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun stringExists() {
        // given
        val initialValue = mapOf("a" to mapOf("b" to "c"))
        val store = createStore(initialValue)

        // when
        val actual = store.valueExists("a", "b")

        // then
        assertTrue(actual)
    }

    @Test
    fun stringDoesNotExist() {
        // given
        val initialValue = mapOf("a" to mapOf("c" to "d"))
        val store = createStore(initialValue)

        // when
        val actual = store.valueExists("a", "b")

        // then
        assertFalse(actual)
    }

    @Test
    fun getString() {
        // given
        val initialValue = mapOf("a" to mapOf("b" to "c"))
        val expected = "c"
        val store = createStore(initialValue)

        // when
        val actual = store.getValue(StringConverter, "a", "b")

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun setIntFromMissing() {
        // given
        val expected = """{"a":{"b":123}}"""
        val store = createStore()

        // when
        store.setValue(123, "a", "b")
        val actual = store.text

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun setIntFromEmpty() {
        // given
        val expected = """{"a":{"b":123}}"""
        val store = createStore()

        // when
        store.setValue(123, "a", "b")
        val actual = store.text

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun intExists() {
        // given
        val initialValue = mapOf("a" to mapOf("b" to 123))
        val store = createStore(initialValue)

        // when
        val actual = store.valueExists("a", "b")

        // then
        assertTrue(actual)
    }

    @Test
    fun intDoesNotExist() {
        // given
        val initialValue = mapOf("a" to mapOf("c" to 123))
        val store = createStore(initialValue)

        // when
        val actual = store.valueExists("a", "b")

        // then
        assertFalse(actual)
    }

    @Test
    fun getInt() {
        // given
        val initialValue = mapOf("a" to mapOf("b" to 123))
        val expected = 123
        val store = createStore(initialValue)

        // when
        val actual = store.getValue(IntConverter, "a", "b")

        // then
        assertEquals(expected, actual)
    }

    private fun assertJsonEquals(expected: String, actual: String) {
        assertEquals(expected.normalizeJson(), actual.normalizeJson())
    }
}
