package com.seanshubin.kotlin.tryme.domain.logger

import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

class FileLoggerTest {
    @Test
    fun logToFile() {
        // given
        val initialize = InitializeStub()
        val files = FilesStub()
        val logFile = Paths.get("file.log")
        val fileLogger = FileLogger(initialize, files, logFile)

        // when
        fileLogger.log("hello")

        // then
        assertEquals(1, initialize.invocationCount)
        assertEquals(1, files.whatHappened.size)
        assertEquals("write(path = file.log, lines = [hello], options = [APPEND, CREATE])", files.whatHappened[0])
    }

    @Test
    fun onlyInitializeOnce() {
        // given
        val initialize = InitializeStub()
        val files = FilesStub()
        val logFile = Paths.get("file.log")
        val fileLogger = FileLogger(initialize, files, logFile)

        // when
        fileLogger.log("hello")
        fileLogger.log("world")

        // then
        assertEquals(1, initialize.invocationCount)
    }

    class InitializeStub : () -> Unit {
        var invocationCount = 0
        override fun invoke() {
            invocationCount++
        }
    }
}
