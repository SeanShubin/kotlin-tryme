package com.seanshubin.kotlin.tryme.domain.jdbc

import java.sql.ResultSet

class ResultSetIterator(
    private val resultSet: ResultSet,
    private val columnNames: List<String>
) : Iterator<List<Any>> {
    private var resultSetNext = resultSet.next()
    override fun hasNext(): Boolean = resultSetNext

    override fun next(): List<Any> {
        val rowCells = columnNames.map { resultSet.getString(it) }
        resultSetNext = resultSet.next()
        return rowCells
    }
}
