package com.seanshubin.kotlin.tryme.domain.json.editor

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import com.seanshubin.kotlin.tryme.domain.json.editor.Converter.Companion.IntConverter
import com.seanshubin.kotlin.tryme.domain.json.editor.Converter.Companion.StringConverter
import com.seanshubin.kotlin.tryme.domain.json.util.JsonUtil.normalizeJson
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JsonFileKeyValueStoreTest {
    @Test
    fun setStringFromMissing() {
        // given
        val files = createFiles()
        val file = Paths.get("generated", "json-file-key-value-store.json")
        initializeFile(files, file, "")
        val expected = """{"a":{"b":"c"}}"""
        val editor = JsonFileKeyValueStore(files, file)

        // when
        editor.setValue("c", "a", "b")
        val actual = editor.text

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun setStringFromEmpty() {
        // given
        val files = createFiles()
        val file = Paths.get("generated", "json-file-key-value-store.json")
        initializeFile(files, file, "{}")
        val expected = """{"a":{"b":"c"}}"""
        val editor = JsonFileKeyValueStore(files, file)

        // when
        editor.setValue("c", "a", "b")
        val actual = editor.text

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun stringExists() {
        // given
        val files = createFiles()
        val file = Paths.get("generated", "json-file-key-value-store.json")
        initializeFile(files, file, """{"a":{"b":"c"}}""")
        val editor = JsonFileKeyValueStore(files, file)

        // when
        val actual = editor.valueExists("a", "b")

        // then
        assertTrue(actual)
    }

    @Test
    fun stringDoesNotExist() {
        // given
        val files = createFiles()
        val file = Paths.get("generated", "json-file-key-value-store.json")
        initializeFile(files, file, """{"a":{"c":"d"}}""")
        val editor = JsonFileKeyValueStore(files, file)

        // when
        val actual = editor.valueExists("a", "b")

        // then
        assertFalse(actual)
    }

    @Test
    fun getString() {
        // given
        val files = createFiles()
        val file = Paths.get("generated", "json-file-key-value-store.json")
        initializeFile(files, file, """{"a":{"b":"c"}}""")
        val expected = "c"
        val editor = JsonFileKeyValueStore(files, file)

        // when
        val actual = editor.getValue(StringConverter, "a", "b")

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun setIntFromMissing() {
        // given
        val files = createFiles()
        val file = Paths.get("generated", "json-file-key-value-store.json")
        initializeFile(files, file, "")
        val expected = """{"a":{"b":123}}"""
        val editor = JsonFileKeyValueStore(files, file)

        // when
        editor.setValue(123, "a", "b")
        val actual = editor.text

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun setIntFromEmpty() {
        // given
        val files = createFiles()
        val file = Paths.get("generated", "json-file-key-value-store.json")
        initializeFile(files, file, "{}")
        val expected = """{"a":{"b":123}}"""
        val editor = JsonFileKeyValueStore(files, file)

        // when
        editor.setValue(123, "a", "b")
        val actual = editor.text

        // then
        assertJsonEquals(expected, actual)
    }

    @Test
    fun intExists() {
        // given
        val files = createFiles()
        val file = Paths.get("generated", "json-file-key-value-store.json")
        initializeFile(files, file, """{"a":{"b":123}}""")
        val editor = JsonFileKeyValueStore(files, file)

        // when
        val actual = editor.valueExists("a", "b")

        // then
        assertTrue(actual)
    }

    @Test
    fun intDoesNotExist() {
        // given
        val files = createFiles()
        val file = Paths.get("generated", "json-file-key-value-store.json")
        initializeFile(files, file, """{"a":{"c":123}}""")
        val editor = JsonFileKeyValueStore(files, file)

        // when
        val actual = editor.valueExists("a", "b")

        // then
        assertFalse(actual)
    }

    @Test
    fun getInt() {
        // given
        val files = createFiles()
        val file = Paths.get("generated", "json-file-key-value-store.json")
        initializeFile(files, file, """{"a":{"b":123}}""")
        val expected = 123
        val editor = JsonFileKeyValueStore(files, file)

        // when
        val actual = editor.getValue(IntConverter, "a", "b")

        // then
        assertEquals(expected, actual)
    }

    private fun assertJsonEquals(expected: String, actual: String) {
        assertEquals(expected.normalizeJson(), actual.normalizeJson())
    }

    private fun initializeFile(files:FilesContract, file: Path, text: String) {
        files.createDirectories(file.parent)
        files.writeString(file, text)
    }

    private fun createFiles(): FilesContract {
        return FilesFake()
    }
}
