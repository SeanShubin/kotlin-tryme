package com.seanshubin.kotlin.tryme.domain.beam

object AssemblerRepository {
    val sizeAssembler = UnsignedIntAssembler("size")
    val beamFileAssembler = BeamFileAssembler("beam-file")
    val beamInterchangeFileFormat = BeamInterchangeFileFormatAssembler("beam-interchange-file-format")
    val map = listOf(
        sizeAssembler,
        beamFileAssembler,
        beamInterchangeFileFormat
    ).associateBy {
        it.name
    }

}