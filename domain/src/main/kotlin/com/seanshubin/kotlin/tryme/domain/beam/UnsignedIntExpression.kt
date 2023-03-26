package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class UnsignedIntExpression(override val name:String, private val byteName:String):Expression {
    override fun parse(start: Cursor<Byte>): Result {
        var current = start
        val list = mutableListOf<Tree<Byte>>()
        while(list.size < 4){
            if(current.isEnd) return Result.Failure("$name - $byteName: expected 4 bytes, got end")
            list.add(Tree.Leaf(byteName, current.value))
            current = current.next()
        }
        return Result.Success(current, Tree.Branch(name, list))
    }
}
