package com.seanshubin.kotlin.tryme.domain.paircompare

import kotlin.random.Random

data class Ranker(
    val candidates:List<String>,
    val explicitPreferences:List<Pair<String, String>>,
    val transitivePreferences:List<Pair<String, String>>,
    val possiblePreferences:List<Pair<String, String>>
    ) {
    fun nextPair(random: Random):Pair<String, String>{
        val index = random.nextInt(possiblePreferences.size)
        return possiblePreferences[index]
    }

    fun toLines(): List<String> {
        val lines = mutableListOf<String>()
        lines.add("Candidates(${candidates.size}):")
        candidates.forEach { lines.add("  $it") }
        lines.add("Explicit Preferences(${explicitPreferences.size}):")
        explicitPreferences.map(::preferenceToString).forEach { lines.add("  $it") }
        lines.add("Transitive Preferences(${transitivePreferences.size}):")
        transitivePreferences.map(::preferenceToString).forEach { lines.add("  $it") }
        lines.add("Possible Preferences(${possiblePreferences.size}):")
        possiblePreferences.map(::preferenceToString).forEach { lines.add("  $it") }
        return lines
    }

    fun addPreferenceWithTransitiveImplications(candidate1: String, candidate2: String): Ranker {
        val newPreference = candidate1 to candidate2
        val preferences = explicitPreferences + transitivePreferences
        val transitivesBefore =  preferences.filter{it.second == candidate1}.map{it.first to candidate2}.filterNot{preferences.contains(it)}
        val transitivesAfter = preferences.filter{it.first == candidate2}.map{candidate1 to it.second}.filterNot{preferences.contains(it)}
        val newTransitivePreferences = transitivesBefore + transitivesAfter
        val result = addPreferences(newPreference, newTransitivePreferences)
        return result
    }

    fun rankings():List<String>{
        var result = emptyList<String>()
        var current = this
        while(current.candidates.isNotEmpty()){
            val top = current.top()
            result = result + top
            current = current.remove(top)
        }
        return result
    }

    private fun top():String{
        val preferences = explicitPreferences + transitivePreferences
        val top = candidates.filter { potentialWinner ->
            preferences.none { it.second == potentialWinner }
        }
        if(top.size != 1){
            throw RuntimeException("Ambiguous top: $top")
        }
        return top[0]
    }

    private fun remove(target:String):Ranker{
        val newCandidates = candidates.filterNot{it == target}
        val newExplicitPreferences = explicitPreferences.filterNot { it.first == target || it.second == target }
        val newTransitivePreferences = transitivePreferences.filterNot { it.first == target || it.second == target }
        val newPossiblePreferences = possiblePreferences.filterNot{it.first == target || it.second == target}
        return Ranker(newCandidates, newExplicitPreferences, newTransitivePreferences, newPossiblePreferences)
    }

    private fun addExplicitPreference(explicitPreference:Pair<String, String>):Ranker {
        val newExplicitPreferences = explicitPreferences + explicitPreference
        val newPossiblePreferences = cleanupPossiblePreferences(explicitPreference)
        return copy(explicitPreferences = newExplicitPreferences, possiblePreferences = newPossiblePreferences)
    }

    private fun addTransitivePreference(transitivePreference:Pair<String, String>):Ranker {
        val newTransitivePreferences = transitivePreferences + transitivePreference
        val newPossiblePreferences = cleanupPossiblePreferences(transitivePreference)
        return copy(transitivePreferences = newTransitivePreferences, possiblePreferences = newPossiblePreferences)
    }

    private fun cleanupPossiblePreferences(preference:Pair<String, String>):List<Pair<String, String>>{
        val antiPreference = preference.second to preference.first
        return possiblePreferences.filterNot{it == preference}.filterNot{it == antiPreference}
    }

    private fun addPreferences(explicitPreference:Pair<String, String>, transitivePreferences:List<Pair<String, String>>):Ranker {
        var newRanker = this.addExplicitPreference(explicitPreference)
        transitivePreferences.forEach { preference ->
            newRanker = newRanker.addTransitivePreference(preference)
        }
        return newRanker
    }

    fun fullySpecified():Boolean = possiblePreferences.isEmpty()

    companion object {
        fun create(candidates:List<String>):Ranker {
            val preferences = emptyList<Pair<String, String>>()
            val explicitPreferences = emptyList<Pair<String, String>>()
            val possiblePreferences = candidates.flatMap { candidate1 ->
                candidates.filter { it != candidate1 }.map { candidate2 ->
                    Pair(candidate1, candidate2)
                }
            }
            return Ranker(candidates, explicitPreferences, preferences, possiblePreferences)
        }

        private fun preferenceToString(preference:Pair<String, String>):String = "${preference.first} > ${preference.second}"
    }
}