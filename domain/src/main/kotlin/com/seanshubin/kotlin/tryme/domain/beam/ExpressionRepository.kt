package com.seanshubin.kotlin.tryme.domain.beam

object ExpressionRepository {
    val beamInterchangeFileFormat = CharSequenceExpression("beam-interchange-file-format","beam-interchange-file-format-byte", "FOR1")
    val beamFileSize = UnsignedIntExpression("size", "size-byte")
    val beamSection = CharSequenceExpression("beam-section","beam-section-byte", "BEAM")
    val beamFile = SequenceExpression("beam-file", listOf(
        beamInterchangeFileFormat,
        beamFileSize,
        beamSection
    ))
    val map = listOf(beamInterchangeFileFormat, beamFileSize, beamFile).associateBy {
        it.name
    }
}
