package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

interface ConstantPoolInfo {
    val index: Int
    val tag: ConstantPoolTag
    val entriesTaken: Int get() = tag.entriesTaken
    fun line(): String
}
