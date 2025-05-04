package com.seanshubin.kotlin.tryme.domain.dynamic

import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonKeyValueStoreTest {
    @Test
    fun intValue(){
        withTemporaryFile { path ->
            val keyValueStore = JsonFileKeyValueStore(path, FilesDelegate)
            val documentationPrefix = listOf("documentation")
            val keyValueStoreWithDocumentation = KeyValueStoreWithDocumentationDelegate(keyValueStore, documentationPrefix)
            val key = listOf("a", "b", "c")
            val documentationKey = listOf("documentation") + key
            val documentation = listOf("this is a number")
            val value = 456
            val actualValue = keyValueStoreWithDocumentation.load(key, value, documentation)
            val actualDocumentation = keyValueStore.load(documentationKey)
            println(Files.readString(path))
            assertEquals(documentation, actualDocumentation)
            assertEquals(value, actualValue)
        }
    }

    private fun withTemporaryFile(f:(Path)->Unit){
        val path = Files.createTempFile("test", ".json")
        path.toFile().deleteOnExit()
        f(path)
        Files.delete(path)
    }
}
