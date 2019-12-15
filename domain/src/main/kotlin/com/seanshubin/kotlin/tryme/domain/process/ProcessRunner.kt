package com.seanshubin.kotlin.tryme.domain.process

interface ProcessRunner {
    fun run(input: ProcessInput): ProcessOutput
}
