package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class ParserImpl:Parser {
    override fun parse(name: String, cursor: Cursor<Byte>): Result =
        ExpressionRepository.map.getValue(name).parse(cursor)
}
