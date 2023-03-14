package com.seanshubin.kotlin.tryme.domain.dependency

data class ModuleDetail(
    val module:Module,
    override val depth: Int,
    override val breadth: Int,
    override val transitive: Int,
    val cycle:Cycle?,
    val dependsOnInCycle:Map<ModulePath, Cycle>
):Detail {
    override val paths: List<ModulePath> = listOf(module.path)
    override fun toLines(): List<String> {
        val thisLine = "$depth ${module.simpleName}"
        val dependencyLines =  module.dependsOn.filterNot{
            cycle != null && dependsOnInCycle[it] == cycle
        }.map {
            "$depth ${module.simpleName} -> ${it.simpleName}"
        }
        return listOf(thisLine) + dependencyLines
    }
}
