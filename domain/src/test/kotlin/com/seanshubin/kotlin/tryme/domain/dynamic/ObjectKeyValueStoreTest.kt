package com.seanshubin.kotlin.tryme.domain.dynamic

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ObjectKeyValueStoreTest {
    @Test
    fun primitiveOperations(){
        val initial = null
        val path = listOf("a", "b", "c")
        val actual = ObjectKeyValueStore.setValue(initial, path, 123)
        assertFalse(ObjectKeyValueStore.valueExists(initial,path))
        assertTrue(ObjectKeyValueStore.valueExists(actual,path))
        assertEquals(123, ObjectKeyValueStore.getValue(actual, path))
    }
}
