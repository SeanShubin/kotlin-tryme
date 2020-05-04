package com.seanshubin.kotlin.tryme.domain.format

object BytesFormat {

  val metricPrefixes = "kMGTPEYZ"
  fun formatBytes(bytes: Long): String {
    var remain = bytes
    var scale = 0
    while (remain >= 1024) {
      remain /= 1024
      scale++
    }
    val suffix = if (scale == 0) "" else metricPrefixes[scale - 1]
    return "%,d%s".format(remain, suffix)
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