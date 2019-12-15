package com.seanshubin.kotlin.tryme.domain.cursor

class IndexedCursor<ElementType> private constructor(
    private val cursor: Cursor<ElementType>,
    private val index: Int
) : Cursor<ElementType> {
    private var lazyValue: ElementType? = null
    private var lazyNext: IndexedCursor<ElementType>? = null
    override val summary: String get() = "[${index + 1}]"
    override val isEnd: Boolean get() = cursor.isEnd
    override val value: ElementType
        get() {
            reifyLazy()
            return lazyValue!!
        }

    override fun next(): IndexedCursor<ElementType> {
        reifyLazy()
        return lazyNext!!
    }

    private fun reifyLazy() {
        if (lazyValue == null) {
            lazyValue = cursor.value
            lazyNext = IndexedCursor(cursor.next(), index + 1)
        }
    }

    companion object {
        fun <ElementType> create(iterator: Iterator<ElementType>): IndexedCursor<ElementType> =
            IndexedCursor(IteratorCursor.create(iterator), 0)

        fun create(s: String): IndexedCursor<Char> = create(s.iterator())
    }
}
