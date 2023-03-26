package com.seanshubin.kotlin.tryme.domain.beam

import java.nio.ByteBuffer

object Conversions {
    fun List<Byte>.toInt():Int =ByteBuffer.wrap(this.toByteArray()).int
    fun BeamFile.toBeamFileSummary():BeamFileSummary{
        return BeamFileSummary(
            beamFileInterchangeFormat,
            size,
            beamSection,
            sections.map{it.toSectionSummary()})
    }
    fun Section.toSectionSummary():SectionSummary{
        val bytesSize = bytes.size
        val bytesSummary = "$bytesSize bytes"
        return SectionSummary(name, size, bytesSummary)
    }
}
