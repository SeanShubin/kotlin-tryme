package com.seanshubin.kotlin.tryme.domain.paircompare

import com.seanshubin.kotlin.tryme.domain.phonetic.Phonetic
import kotlin.random.Random

object PairComparePrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val seed = 12345L
        val candidateCount = 5
        val candidates = Phonetic.nato.take(candidateCount)
        var ranker = Ranker.create(candidates)
        displayRanker(ranker)
        val random = Random(seed)
        while(!ranker.fullySpecified()){
            val (candidate1, candidate2) = ranker.nextPair(random)
            ranker = ranker.addPreferenceWithTransitiveImplications(candidate1, candidate2)
            displayRanker(ranker)
        }
    }

    private fun displayRanker(ranker:Ranker){
        println("-".repeat(20))
        ranker.toLines().forEach(::println)
    }
}