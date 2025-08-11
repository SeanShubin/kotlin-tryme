package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.nio.file.Path

class Summary {
    var filesParsed = 0
    fun parsingFile(file: Path){
        filesParsed++
    }
    fun lines(): List<String> {
        return listOf(
            "Files parsed: $filesParsed",
        )
    }
}