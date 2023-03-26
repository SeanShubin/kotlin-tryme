package com.seanshubin.kotlin.tryme.domain.beam

object AssemblerRepository {
    val sizeAssembler = UnsignedIntAssembler("size")
    val beamFileAssembler = BeamFileAssembler("beam-file")
    val beamInterchangeFileFormat = StringAssembler("beam-interchange-file-format")
    val beamSection = StringAssembler("beam-section")
    val map = listOf(
        sizeAssembler,
        beamFileAssembler,
        beamInterchangeFileFormat,
        beamSection
    ).associateBy {
        it.name
    }

}