package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

sealed interface Result {
    val success: Boolean

    data class Success(val cursor: Cursor<Byte>, val tree: Tree<Byte>) : Result {
        override val success: Boolean = true
        fun wrap(name: String): Result = Success(cursor, Tree.Branch(name, listOf(tree)))
        fun merge(name:String, other:Success):Success {
                val cursor = other.cursor
                val tree = Tree.Branch(name, listOf(this.tree, other.tree))
                return Success(cursor, tree)
        }
    }

    data class Failure(val message:String) : Result {
        override val success: Boolean = false
    }

    companion object {
        fun wrap(name: String, result: Result): Result = when (result) {
            is Success -> Success(result.cursor, Tree.Branch(name, listOf(result.tree)))
            is Failure -> result
        }
    }
}
