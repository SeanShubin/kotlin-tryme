package com.seanshubin.kotlin.tryme.domain.parser

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class AssemblingCursor<FromType, ToType>(
    val cursor: Cursor<Matched<FromType>>,
    val assemble: (String, Tree<FromType>) -> ToType
) : Cursor<ToType> {
    private var lazyValue: ToType? = null
    private var lazyNext: AssemblingCursor<FromType, ToType>? = null
    override val isEnd: Boolean
        get() = cursor.isEnd
    override val value: ToType
        get() {
            reifyLazy()
            return lazyValue!!
        }

    override fun next(): AssemblingCursor<FromType, ToType> {
        reifyLazy()
        return lazyNext!!
    }

    override val summary: String get() = cursor.summary

    private fun reifyLazy() {
        if (lazyValue == null) {
            lazyValue = assemble(cursor.value.name, cursor.value.tree)
            lazyNext = AssemblingCursor(cursor.next(), assemble)
        }
    }
}
