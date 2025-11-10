package com.seanshubin.kotlin.tryme.domain.compare

import com.seanshubin.kotlin.tryme.domain.compare.ListUtil.splitByContiguousBlocks
import kotlin.test.Test
import kotlin.test.assertEquals

class ListUtilTest {
    @Test
    fun breakIntoGroups() {
        val sample = "abc-?123def456hij-kjl"
        val predicates: List<(Char) -> Boolean> = listOf(Character::isLetter, Character::isDigit)
        val actual = sample.toList().splitByContiguousBlocks(predicates).map { it.joinToString("") }
        val expected = listOf(
            "abc",
            "-?",
            "123",
            "def",
            "456",
            "hij",
            "-",
            "kjl"
        )
        assertEquals(expected, actual)
    }
}