package com.seanshubin.kotlin.tryme.domain.shell

interface Shell {
    fun execute(shellRequest: ShellRequest): ShellResponse
}
