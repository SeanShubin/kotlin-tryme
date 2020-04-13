package com.seanshubin.kotlin.tryme.domain.shell

data class ShellResult(val exitCode: Int,
                       val outputLines: List<String>,
                       val errorLines: List<String>)