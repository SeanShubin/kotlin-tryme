package com.seanshubin.kotlin.tryme.domain.untyped

import kotlin.test.Test
import kotlin.test.assertEquals

class UntypedTest {
    @Test
    fun hasValueAtPath(){
        assertEquals(true, Untyped(null).hasValueAtPath())
        assertEquals(false, Untyped(null).hasValueAtPath("a"))
        assertEquals(true, Untyped(mapOf("a" to 1)).hasValueAtPath("a"))
        assertEquals(false, Untyped(mapOf("a" to 1)).hasValueAtPath("b"))
        assertEquals(true, Untyped(mapOf("a" to mapOf("b" to 1))).hasValueAtPath())
        assertEquals(true, Untyped(mapOf("a" to mapOf("b" to 1))).hasValueAtPath("a"))
        assertEquals(true, Untyped(mapOf("a" to mapOf("b" to 1))).hasValueAtPath("a", "b"))
        assertEquals(false, Untyped(mapOf("a" to mapOf("b" to 1))).hasValueAtPath("a", "c"))
    }

    @Test
    fun getValueAtPath(){
        assertEquals(123, Untyped(123).getValueAtPath())
        assertEquals(1, Untyped(mapOf("a" to 1, "b" to 2)).getValueAtPath("a"))
        assertEquals(2, Untyped(mapOf("a" to 1, "b" to 2)).getValueAtPath("b"))
        assertEquals(3, Untyped(mapOf("a" to mapOf("b" to 3))).getValueAtPath("a", "b"))
    }

    @Test
    fun setValueAtPathRootToPrimitive(){
        assertEquals(1, Untyped(mapOf("a" to mapOf("b" to 1))).setValueAtPath(1, ).value)
    }

    @Test
    fun setValueAtPathCreateStructure(){
        assertEquals(mapOf("a" to mapOf("b" to 1)), Untyped(null).setValueAtPath(1, "a", "b").value)
    }

    @Test
    fun setValueAtPathOverwriteExisting(){
        assertEquals(mapOf("a" to mapOf("b" to 2)), Untyped(mapOf("a" to mapOf("b" to 1))).setValueAtPath(2, "a", "b").value)
    }

    @Test
    fun updateValueAtPath(){
        val oldMap = Untyped(mapOf("a" to mapOf("b" to 1)))
        val newMap = oldMap.updateValueAtPath({old:Any? -> (old as Int)+1}, "a", "b")
        assertEquals(mapOf("a" to mapOf("b" to 2)), newMap.value)
    }
}