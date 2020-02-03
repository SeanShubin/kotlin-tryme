package com.seanshubin.kotlin.tryme.domain.json

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract

class JsonConfigurer(val files: FilesContract) {
    fun <T> fromCommandLineArgument(args:Array<String>, index:Int, specification:T):T {
        TODO()
    }
}
