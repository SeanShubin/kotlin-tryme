package com.seanshubin.kotlin.tryme.domain.cursor

data class RowCol(val row: Int, val col: Int) {
    override fun toString(): String = "[${row + 1}:${col + 1}]"
}
