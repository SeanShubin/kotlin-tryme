package com.seanshubin.kotlin.tryme.domain.round

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object RoundUtil {
  private val metricPrefixes = "bkMGTPEYZ"
  private val orderOfMagnitude = BigDecimal(1024)
  private val thousand = BigDecimal(1000)
  private val precision = 3
  private val roundingMode = RoundingMode.HALF_UP
  private val mathContext = MathContext(precision, roundingMode)

  fun roundBytes(x: Int): String = roundBytes(x.toLong())

  fun roundBytes(x: Long): String {
    var remain = BigDecimal(x)
    var scaleIndex = 0
    while (remain >= orderOfMagnitude) {
      remain = remain.divide(orderOfMagnitude)
      scaleIndex++
    }
    val suffix = metricPrefixes[scaleIndex]
    if (remain < thousand) {
      val rounded = remain.round(mathContext)
      return "$rounded$suffix"
    } else {
      val integerPart = remain.setScale(0, RoundingMode.FLOOR)
      return "$integerPart$suffix"
    }
  }
}
