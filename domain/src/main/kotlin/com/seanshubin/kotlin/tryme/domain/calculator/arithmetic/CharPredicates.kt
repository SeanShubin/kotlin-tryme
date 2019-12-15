package com.seanshubin.kotlin.tryme.domain.calculator.arithmetic

object CharPredicates {
    val isWordChar =
        charMatchFunction("[a-zA-Z]")
    val isNumberChar = charMatchFunction("\\d")
    private fun charMatchFunction(pattern: String): (Char) -> Boolean =
        { c: Char -> Regex(pattern).matches("" + c) }
}
