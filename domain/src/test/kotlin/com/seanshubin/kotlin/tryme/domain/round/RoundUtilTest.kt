package com.seanshubin.kotlin.tryme.domain.round

import com.seanshubin.kotlin.tryme.domain.round.RoundUtil.roundBytes
import kotlin.test.Test
import kotlin.test.assertEquals

class RoundUtilTest {
  @Test
  fun testRoundBytes() {
    assertEquals("0b", roundBytes(0))
    assertEquals("1000b", roundBytes(1000))
    assertEquals("1023b", roundBytes(1023))
    assertEquals("1k", roundBytes(1024))
    assertEquals("1.00k", roundBytes(1025))
    assertEquals("1.10k", roundBytes(1127))
    assertEquals("1.11k", roundBytes(1137))
    assertEquals("1.23k", roundBytes(1264))
    assertEquals("1.24k", roundBytes(1265))
    assertEquals("1000k", roundBytes(1024000))
    assertEquals("1023k", roundBytes(1047552))
    assertEquals("1023k", roundBytes(1047553))
    assertEquals("1000M", roundBytes(1048576000))
    assertEquals("2.34M", roundBytes(2458910))
    assertEquals("2.35M", roundBytes(2458911))
    assertEquals("3.45G", roundBytes(3709778001L))
    assertEquals("3.46G", roundBytes(3709778002L))
    assertEquals("4.56T", roundBytes(5019270580797))
    assertEquals("4.57T", roundBytes(5019270580798))
    assertEquals("5.67P", roundBytes(6389481971331891))
    assertEquals("5.68P", roundBytes(6389481971331892))
    assertEquals("6.78E", roundBytes(7822572408757456732))
    assertEquals("6.79E", roundBytes(7822572408757456733))
  }
}
