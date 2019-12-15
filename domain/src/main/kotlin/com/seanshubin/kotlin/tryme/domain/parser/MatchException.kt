package com.seanshubin.kotlin.tryme.domain.parser

class MatchException(result: Failure<*>) : RuntimeException("${result.errorAtPosition.summary} ${result.message}")
