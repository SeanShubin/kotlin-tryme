package com.seanshubin.kotlin.tryme.domain.jvmclassformat.samples

import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import java.time.Clock

class Dependencies {
    val clock = Clock.systemUTC()
    val files = FilesDelegate
    val emit:(String)->Unit = ::println
    val sample = Sample2(clock, files, emit)
}
