package com.seanshubin.kotlin.tryme.domain.shell

import java.nio.file.Path

data class ShellRequest(val command: List<String>,
                        val directory: Path? = null,
                        val environment: Map<String, String>? = null)
