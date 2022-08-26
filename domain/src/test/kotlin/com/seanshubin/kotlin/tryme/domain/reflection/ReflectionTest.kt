package com.seanshubin.kotlin.tryme.domain.reflection

import kotlin.test.Test
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ReflectionTest {
    @Test
    fun compareClasses(){
        data class Foo(val name:String)
        val foo = Foo("foo name")
        assertEquals(Foo::class,Foo::class)
        assertEquals(Foo::class,foo::class)
    }

    enum class Color {
        RED,
        ORANGE,
        YELLOW
    }

    enum class Fruit {
        APPLE,
        ORANGE,
        BANANA
    }

    @Test
    fun enumList(){
        val myEnums:List<KClass<out Enum<*>>> = listOf(Color::class, Fruit::class)
        myEnums.forEach{
            println(it.simpleName)
            println(it.members)
        }
    }
}