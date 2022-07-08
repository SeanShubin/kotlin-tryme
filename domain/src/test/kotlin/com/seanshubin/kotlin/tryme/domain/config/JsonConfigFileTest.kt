package com.seanshubin.kotlin.tryme.domain.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.contract.FilesContractNotImplemented
import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import org.junit.Test
import java.nio.file.LinkOption
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JsonConfigFileTest {
    @Test
    fun loadDefaultInt() {
        // given
        val files: FilesContract = FakeFiles()
        val configFilePath = Paths.get("test-config.json")
        val jsonConfigFile = JsonConfigFile(files, configFilePath)
        val loadB = jsonConfigFile.loadInt(123, "a", "b")
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
        files.contentMap["test-config.json"] = existingFileContent
        val configFilePath = Paths.get("test-config.json")
        val jsonConfigFile = JsonConfigFile(files, configFilePath)
        val loadB = jsonConfigFile.loadString("default string value", "a", "b")
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
        val jsonConfigFile = JsonConfigFile(files, configFilePath)
        val loadB = jsonConfigFile.loadInt("string value", "a", "b")
        val expectedMessage = "At path a.b, expected type Int, got String for: string value"
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

    class FakeFiles:FilesContractNotImplemented{
        val contentMap:MutableMap<String, String> = mutableMapOf()
        override fun exists(path: Path, vararg options: LinkOption): Boolean {
            return contentMap.containsKey(path.toString())
        }

        override fun writeString(path: Path, csq: CharSequence, vararg options: OpenOption): Path {
            contentMap[path.toString()] = csq.toString()
            return path
        }

        override fun readString(path: Path): String {
            return contentMap.getValue(path.toString())
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