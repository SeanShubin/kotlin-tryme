package com.seanshubin.kotlin.tryme.domain.format

object BytesFormat {

  val metricPrefixes = "kMGTPEYZ"
  fun formatBytes(bytes: Long): String {
    return DurationFormat.bytes.format(bytes)
  }

  @JvmStatic
  fun main(args: Array<String>) {
    println(formatBytes(0))
    println(formatBytes(1))
    println(formatBytes(2))
    println(formatBytes(1023))
    println(formatBytes(1024))
    println(formatBytes(1025))
    println(formatBytes(Long.MAX_VALUE))
  }
}