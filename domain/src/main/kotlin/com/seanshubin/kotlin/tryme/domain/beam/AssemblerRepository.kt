package com.seanshubin.kotlin.tryme.domain.beam

object AssemblerRepository {
    val sizeAssembler = UnsignedIntAssembler("size")
    val beamFileAssembler = BeamFileAssembler("beam-file")
    val beamInterchangeFileFormat = StringAssembler("beam-interchange-file-format")
    val beamSectionLiteral = StringAssembler("beam-section-literal")
    val beamSection = BeamSectionAssembler("beam-section")
    val beamSectionList = BeamSectionListAssembler("beam-section-list")
    val beamSectionName = StringAssembler("beam-section-name")
    val beamSectionSizeAndContent = SizeAndContentAssembler("beam-section-size-and-content")
    val beamSectionSizeAndContentSize = UnsignedIntAssembler("beam-section-size-and-content-size")
    val beamSectionSizeAndContentContent = ByteListAssembler("beam-section-size-and-content-content")
    val map = listOf(
        sizeAssembler,
        beamFileAssembler,
        beamInterchangeFileFormat,
        beamSectionLiteral,
        beamSection,
        beamSectionList,
        beamSectionName,
        beamSectionSizeAndContent,
        beamSectionSizeAndContentSize,
        beamSectionSizeAndContentContent
    ).associateBy {
        it.name
    }

}