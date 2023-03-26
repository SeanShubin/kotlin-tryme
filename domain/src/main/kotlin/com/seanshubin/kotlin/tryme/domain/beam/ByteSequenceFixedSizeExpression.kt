package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class ByteSequenceFixedSizeExpression(override val name:String, private val size:Int):Expression {
    override fun parse(start: Cursor<Byte>): Result {
        var current = start
        val list = mutableListOf<Tree<Byte>>()
        while(list.size < size){
            if(current.isEnd) return Result.Failure("$name expected $size bytes, got end")
            val actual = current.value
            list.add(Tree.Leaf("$name-byte", actual))
            current = current.next()
        }
        return Result.Success(current, Tree.Branch(name, list))
    }
}
