package com.seanshubin.kotlin.tryme.domain.svg.free

interface Entity {
  fun toSvg(): String
  fun toScreen(): Entity
}
