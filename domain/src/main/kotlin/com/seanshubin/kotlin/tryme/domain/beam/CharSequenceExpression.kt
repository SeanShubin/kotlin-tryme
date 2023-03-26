package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.beam.Display.displayByte
import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class CharSequenceExpression(override val name:String, private val byteName:String, private val chars:String):Expression {
    override fun parse(start: Cursor<Byte>): Result {
        var index = 0
        var current = start
        val list = mutableListOf<Tree<Byte>>()
        while(index < chars.length){
            val expected = chars[index]
            if(current.isEnd) return Result.Failure("$name - $byteName expected ${expected.displayByte()}, got end")
            val actual = current.value
            if(expected.code != actual.toInt()) {
                return Result.Failure("$name - $byteName expected ${expected.displayByte()}, got ${actual.displayByte()}")
            }
            list.add(Tree.Leaf(byteName, actual))
            index++
            current = current.next()
        }
        return Result.Success(current, Tree.Branch(name, list))
    }
}
