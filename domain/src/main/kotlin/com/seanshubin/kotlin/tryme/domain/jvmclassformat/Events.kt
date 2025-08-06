package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.nio.file.Path

interface Events {
    fun opCodeParsed(opCode: OpCodeEntry)
    fun parsingFile(path: Path)
    companion object {
        val nop: Events = object : Events {
            override fun opCodeParsed(opCode: OpCodeEntry) {
                // No operation
            }

            override fun parsingFile(path: Path) {
                // No operation
            }
        }
    }
}
