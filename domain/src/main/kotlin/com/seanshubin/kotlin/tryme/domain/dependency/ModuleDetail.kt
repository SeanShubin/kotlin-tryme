package com.seanshubin.kotlin.tryme.domain.dependency

data class ModuleDetail(
    val module:Module,
    override val depth: Int,
    override val breadth: Int,
    override val transitive: Int,
    val dependedOn:Boolean,
    val cycle:Cycle?,
    val dependsOnInCycle:Map<ModulePath, Cycle>
):Detail {
    override val paths: List<ModulePath> = listOf(module.path)
    override fun toLines(makeLink:(ModulePath)->String?): List<String> {
        val thisLine = listOf(module.linkName(makeLink))

        val dependencyLines =  module.dependsOn.filterNot{
            cycle != null && dependsOnInCycle[it] == cycle
        }.map {
            "${module.simpleName} -> ${it.simpleName}"
        }
        return thisLine + dependencyLines
    }
}
