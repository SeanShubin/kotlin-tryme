package com.seanshubin.kotlin.tryme.domain.jvmclassformat.samples

import java.nio.file.Files
import java.nio.file.Paths

class SampleLower {
    fun foo(){
        Files.list(Paths.get("."))
    }
}
