package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

interface ConstantPoolInfo {
    val tag: ConstantPoolTag
    val entriesTaken: Int get() = tag.entriesTaken
}
