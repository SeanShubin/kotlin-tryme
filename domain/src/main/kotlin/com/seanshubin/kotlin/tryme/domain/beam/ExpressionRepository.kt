package com.seanshubin.kotlin.tryme.domain.beam

object ExpressionRepository {
    val beamInterchangeFileFormat = CharSequenceExpression("beam-interchange-file-format","beam-interchange-file-format-byte", "FOR1")
    val beamFileSize = UnsignedIntExpression("size", "size-byte")
    val beamSectionLiteral = CharSequenceExpression("beam-section-literal","beam-section-literal-byte", "BEAM")
    val beamSectionName = ByteSequenceFixedSizeExpression("beam-section-name", 4)
    val beamSectionSizeAndContent = DynamicallySizedByteSequenceExpression("beam-section-size-and-content", "size", "content")
    val beamSection = SequenceExpression("beam-section", listOf(
        beamSectionName,
        beamSectionSizeAndContent
    ))
    val beamSectionList = ZeroOrMoreExpression("beam-section-list", beamSection)
    val beamFile = SequenceExpression("beam-file", listOf(
        beamInterchangeFileFormat,
        beamFileSize,
        beamSectionLiteral,
        beamSectionList
    ))

    val map = listOf(beamInterchangeFileFormat, beamFileSize, beamFile).associateBy {
        it.name
    }
}
