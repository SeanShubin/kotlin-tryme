package com.seanshubin.kotlin.tryme.domain.dice

import com.seanshubin.kotlin.tryme.domain.ratio.Ratio

object SphereseOfInfluenceDiceStatisticsApp {
    fun incrementDieRoll(values: List<Int>, faces: Int): List<Int> {
        val newValues = mutableListOf<Int>()
        var carry = true
        for (currentIndex in values.size - 1 downTo 0) {
            val value = values[currentIndex]
            if (carry) {
                if (value == faces) {
                    newValues.add(1)
                } else {
                    newValues.add(value + 1)
                    carry = false
                }
            } else {
                newValues.add(value)
            }
        }
        newValues.reverse()
        return newValues
    }

    fun allRolls(quantity: Int, faces: Int): List<List<Int>> {
        val startingValue = (1..quantity).map { 1 }
        var currentValue = startingValue
        val results = mutableListOf(currentValue)
        currentValue = incrementDieRoll(currentValue, faces)
        while (currentValue != startingValue) {
            results.add(currentValue)
            currentValue = incrementDieRoll(currentValue, faces)
        }
        return results
    }

    fun List<Int>.extractAtIndex(index: Int): Pair<Int, List<Int>> {
        val value = get(index)
        val before = subList(0, index)
        val after = subList(index + 1, size)
        val remain = before + after
        return Pair(value, remain)
    }

    fun List<Int>.permutations(): List<List<Int>> {
        if (size < 2) return listOf(this)
        val allPermutations = mutableListOf<List<Int>>()
        for (index in this.indices) {
            val (atIndex, remain) = extractAtIndex(index)
            remain.permutations().forEach {
                val permutation = listOf(atIndex) + it
                allPermutations.add(permutation)
            }
        }
        return allPermutations
    }

    fun List<Int>.hitsForRoll(): Int {
        var hits = 0
        var accumulator = 0
        for (index in indices) {
            val current = get(index)
            accumulator += current
            if (accumulator >= 6) {
                hits++
                accumulator = 0
            }
        }
        return hits
    }

    fun List<Int>.maxHitsForRoll(): Int {
        val scores: List<Int> = permutations().map { it.hitsForRoll() }
        return scores.max() ?: throw RuntimeException("unable to compute maximum of $scores")
    }

    fun List<List<Int>>.scoreHistogram(): Map<Int, Int> {
        val histogram = mutableMapOf<Int, Int>()
        forEach {
            val score = it.maxHitsForRoll()
            histogram[score] = (histogram[score] ?: 0) + 1
        }
        return histogram
    }

    fun List<Int>.scoreDisplay(): String {
        val score = maxHitsForRoll()
        return "$this -> $score"
    }

    fun Int.scoreHistogram(): Map<Int, Int> {
        val allRolls = allRolls(this, 6)
        return allRolls.scoreHistogram()
    }

    fun pluralize(quantity: Int, singular: String, plural: String): String {
        return if (quantity == 1) singular else plural
    }

    fun <T> List<List<T>>.flattenWithSpacer(spacer: T): List<T> =
            map { listOf(spacer) + it }.flatten().drop(1)

    data class HistogramForQuantity(val quantity: Int, val histogram: Map<Int, Int>) {
        fun summary(): List<String> {
            val total = histogram.values.sum()
            val keys = histogram.keys.sorted()
            val diceString = pluralize(quantity, "die", "dice")
            val caption = "$quantity $diceString"
            val summaries = keys.map {
                val ratio = Ratio(histogram.getValue(it), total)
                val percent = ratio.toDouble * 100
                val hitString = pluralize(it, "hit", "hits")
                val summary = "$ratio (%.2f%%) chance of $it $hitString".format(percent)
                summary
            }
            return listOf(caption) + summaries
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val header = listOf(
                "1 hit per grouping of dice that sums to 6 or greater",
                ""
        )
        val body = (1..5).map {
            HistogramForQuantity(it, it.scoreHistogram())
        }.map {
            it.summary()
        }.flattenWithSpacer("")
        val lines = header + body
        lines.forEach(::println)
    }
}
/*
1 hit per grouping of dice that sums to 6 or greater

1 die
5/6 (83.33%) chance of 0 hits
1/6 (16.67%) chance of 1 hit

2 dice
10/36 (27.78%) chance of 0 hits
25/36 (69.44%) chance of 1 hit
1/36 (2.78%) chance of 2 hits

3 dice
10/216 (4.63%) chance of 0 hits
145/216 (67.13%) chance of 1 hit
60/216 (27.78%) chance of 2 hits
1/216 (0.46%) chance of 3 hits

4 dice
5/1296 (0.39%) chance of 0 hits
333/1296 (25.69%) chance of 1 hit
847/1296 (65.35%) chance of 2 hits
110/1296 (8.49%) chance of 3 hits
1/1296 (0.08%) chance of 4 hits

5 dice
1/7776 (0.01%) chance of 0 hits
456/7776 (5.86%) chance of 1 hit
4258/7776 (54.76%) chance of 2 hits
2885/7776 (37.10%) chance of 3 hits
175/7776 (2.25%) chance of 4 hits
1/7776 (0.01%) chance of 5 hits
*/
