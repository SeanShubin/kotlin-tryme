package com.seanshubin.kotlin.tryme.domain.format

import com.seanshubin.kotlin.tryme.domain.format.StringUtil.escape
import com.seanshubin.kotlin.tryme.domain.format.StringUtil.truncate
import java.io.Reader

interface TableFormatter {
    interface Justify {
        data class Left(val x: Any?) : Justify

        data class Right(val x: Any?) : Justify
    }

    fun format(originalRows: List<List<Any?>>): List<String>
    fun <T> parse(reader: Reader, mapToElement:(Map<String, String>) -> T):Iterable<T>

    companion object {
        val escapeString: (Any?) -> String = { cell ->
            when (cell) {
                null -> "null"
                else -> cell.toString().escape()
            }
        }

        fun escapeAndTruncateString(max: Int): (Any?) -> String = { cell ->
            escapeString(cell).truncate(max)
        }
    }
}
