package com.seanshubin.kotlin.tryme.domain.beam

data class BeamFile(
    val beamFileInterchangeFormat: String,
    val size: Int,
    val beamSection:String,
    val sections:List<Section>
)
