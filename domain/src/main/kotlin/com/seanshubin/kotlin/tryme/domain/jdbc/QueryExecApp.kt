package com.seanshubin.kotlin.tryme.domain.jdbc

import com.seanshubin.kotlin.tryme.domain.format.RowStyleTableFormatter
import com.seanshubin.kotlin.tryme.domain.format.TableFormatter
import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import com.seanshubin.kotlin.tryme.domain.logger.LineEmittingAndFileLogger
import com.seanshubin.kotlin.tryme.domain.logger.LoggerFactory
import com.seanshubin.kotlin.tryme.domain.timer.Timer
import java.nio.file.Paths
import java.time.Clock

fun main(args: Array<String>) {
    val url = args[0]
    val user = args[1]
    val password = args[2]
    val query = args[3]
    val name = "command-line"
    val tableFormatter =
        RowStyleTableFormatter.boxDrawing.copy(cellToString = TableFormatter.escapeAndTruncateString(100))
    val path = Paths.get("out", "log")
    val clock = Clock.systemDefaultZone()
    val logger = LoggerFactory.instanceDefaultZone.createLogger(path, name)
    val logTable: (List<List<Any?>>) -> Unit = { data ->
        val table = tableFormatter.format(data)
        logger.log(table)
    }
    val timer = Timer(clock)
    val queryExec = QueryExec(url, user, password, timer, logger::log, logTable)
    queryExec.exec(query)
}