package com.seanshubin.kotlin.tryme.domain.parser

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

data class Success<T>(val name: String, val value: Tree<T>, val positionAfterMatch: Cursor<T>) : Result<T>
