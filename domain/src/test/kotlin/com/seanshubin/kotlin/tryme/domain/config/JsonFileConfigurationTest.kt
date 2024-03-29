package com.seanshubin.kotlin.tryme.domain.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.contract.FilesContractUnsupportedOperation
import com.seanshubin.kotlin.tryme.domain.format.DurationFormat
import kotlin.test.Test
import java.nio.file.LinkOption
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JsonFileConfigurationTest {
    @Test
    fun loadDefaultInt() {
        // given
        val files: FilesContract = FakeFiles()
        val configFilePath = Paths.get("test-config.json")
        val jsonFileConfiguration = JsonFileConfiguration(files, configFilePath)
        val loadB = jsonFileConfiguration.intAt(123, listOf("a", "b")).load
        val expectedValue = 123
        val expectedJson =
            """{
              |  "a": {
              |    "b": 123
              |  }
              |}""".trimMargin()

        // when
        val actualValue = loadB()

        // then
        val actualJson = files.readString(configFilePath)
        assertEquals(expectedValue, actualValue)
        assertJsonEquals(expectedJson, actualJson)
    }

    @Test
    fun loadString() {
        // given
        val files = FakeFiles()
        val existingFileContent =
            """{
              |  "a": {
              |    "b": "actual string value"
              |  }
              |}""".trimMargin()
        val configFilePath = Paths.get("test-config.json")
        files.contentMap[configFilePath] = existingFileContent
        val jsonFileConfiguration = JsonFileConfiguration(files, configFilePath)
        val loadB = jsonFileConfiguration.stringAt("default string value", listOf("a", "b")).load
        val expectedValue = "actual string value"

        // when
        val actualValue = loadB()

        // then
        val actualJson = files.readString(configFilePath)
        assertEquals(expectedValue, actualValue)
        assertJsonEquals(existingFileContent, actualJson)
    }

    @Test
    fun defaultCanBeDifferentType() {
        // given
        val files: FilesContract = FakeFiles()
        val configFilePath = Paths.get("test-config.json")
        val jsonFileConfiguration = JsonFileConfiguration(files, configFilePath)
        val loadB = jsonFileConfiguration.intAt("string value", listOf("a", "b")).load
        val expectedMessage = "At path a.b, expected type int, got String for: string value"
        val expectedJson =
            """{
              |  "a": {
              |    "b": "string value"
              |  }
              |}""".trimMargin()

        // when
        val exception = assertFailsWith<RuntimeException> {
            loadB()
        }

        // then
        val actualJson = files.readString(configFilePath)
        assertEquals(expectedMessage, exception.message)
        assertJsonEquals(expectedJson, actualJson)
    }

    @Test
    fun loadPath() {
        // given
        val files = FakeFiles()
        val existingFileContent =
            """{
              |  "a": {
              |    "b": "my/path"
              |  }
              |}""".trimMargin()
        val configFilePath = Paths.get("test-config.json")
        files.contentMap[configFilePath] = existingFileContent
        val jsonFileConfiguration = JsonFileConfiguration(files, configFilePath)
        val loadB = jsonFileConfiguration.pathAt("default path value", listOf("a", "b")).load
        val expectedValue = Paths.get("my/path")

        // when
        val actualValue = loadB()

        // then
        val actualJson = files.readString(configFilePath)
        assertEquals(expectedValue, actualValue)
        assertJsonEquals(existingFileContent, actualJson)
    }

    @Test
    fun existingPath(){
        // given
        val text = """
            {
              "existing" : "my/path"
            }
        """.trimIndent()
        val expected = Paths.get("my/path")
        val path = Paths.get("test-config.json")
        val files = FakeFiles()
        files.contentMap[path] = text
        val configuration = JsonFileConfiguration(files, path)
        val loadPath = configuration.pathAt("dummy", listOf("existing")).load

        // when
        val actual = loadPath()

        // then
        assertEquals(expected, actual)
        assertEquals(text, files.readString(path))
    }

    @Test
    fun defaultPath(){
        // given
        val text = """
            {
              "created" : "my/path"
            }
        """.trimIndent()
        val expected = Paths.get("my/path")
        val path = Paths.get("test-config.json")
        val files = FakeFiles()
        val configuration = JsonFileConfiguration(files, path)
        val loadPath = configuration.pathAt(expected, listOf("created")).load

        // when
        val actual = loadPath()

        // then
        assertEquals(expected, actual)
        assertEquals(text, files.readString(path))
    }

    @Test
    fun existingInstant(){
        // given
        val text = """
            {
              "existing" : "2022-08-06T03:34:20.436432Z"
            }
        """.trimIndent()
        val expected = Instant.parse("2022-08-06T03:34:20.436432Z")
        val path = Paths.get("test-config.json")
        val files = FakeFiles()
        files.contentMap[path] = text
        val configuration = JsonFileConfiguration(files, path)
        val loadValue = configuration.instantAt("dummy", listOf("existing")).load

        // when
        val actual = loadValue()

        // then
        assertEquals(expected, actual)
        assertEquals(text, files.readString(path))
    }

    @Test
    fun defaultInstant(){
        // given
        val text = """
            {
              "created" : "2022-08-06T03:34:20.436432Z"
            }
        """.trimIndent()
        val expected = Instant.parse("2022-08-06T03:34:20.436432Z")
        val path = Paths.get("test-config.json")
        val files = FakeFiles()
        val configuration = JsonFileConfiguration(files, path)
        val loadValue = configuration.instantAt(expected, listOf("created")).load

        // when
        val actual = loadValue()

        // then
        assertEquals(expected, actual)
        assertEquals(text, files.readString(path))
    }

    @Test
    fun existingFormattedSeconds(){
        // given
        val text = """
            {
              "existing" : "1 minute 2 seconds"
            }
        """.trimIndent()
        val expected = DurationFormat.seconds.parse("1 minute 2 seconds")
        val path = Paths.get("test-config.json")
        val files = FakeFiles()
        files.contentMap[path] = text
        val configuration = JsonFileConfiguration(files, path)
        val loadValue = configuration.formattedSecondsAt("dummy", listOf("existing")).load

        // when
        val actual = loadValue()

        // then
        assertEquals(expected, actual)
        assertEquals(text, files.readString(path))
    }

    @Test
    fun defaultFormattedSeconds(){
        // given
        val text = """
            {
              "created" : "1 minute 2 seconds"
            }
        """.trimIndent()
        val expected = DurationFormat.seconds.parse("1 minute 2 seconds")
        val path = Paths.get("test-config.json")
        val files = FakeFiles()
        val configuration = JsonFileConfiguration(files, path)
        val loadValue = configuration.formattedSecondsAt("1 minute 2 seconds", listOf("created")).load

        // when
        val actual = loadValue()

        // then
        assertEquals(expected, actual)
        assertEquals(text, files.readString(path))
    }

    @Test
    fun increment(){
        // given
        val expected = 1
        val path = Paths.get("test-config.json")
        val files = FakeFiles()
        val configuration = JsonFileConfiguration(files, path)
        val counter = configuration.intAt(0, listOf("counter"))

        // when
        val oldValue = counter.load()
        val newValue = oldValue+1
        counter.store(newValue)
        val actual = counter.load()

        // then
        assertEquals(expected, actual)
    }

    class FakeFiles:FilesContractUnsupportedOperation{
        val contentMap:MutableMap<Path, String> = mutableMapOf()
        override fun exists(path: Path, vararg options: LinkOption): Boolean {
            return contentMap.containsKey(path)
        }

        override fun writeString(path: Path, csq: CharSequence, vararg options: OpenOption): Path {
            contentMap[path] = csq.toString()
            return path
        }

        override fun readString(path: Path): String {
            return contentMap.getValue(path)
        }
    }

    fun assertJsonEquals(expected:String, actual:String){
        assertEquals(expected.prettyJson(), actual.prettyJson())
    }

    val pretty: ObjectMapper = ObjectMapper().registerKotlinModule().enable(SerializationFeature.INDENT_OUTPUT)

    fun String.prettyJson():String {
        val asObject = pretty.readValue<Any>(this)
        val asNormalized = pretty.writeValueAsString(asObject)
        return asNormalized

    }
}