package com.seanshubin.kotlin.tryme.domain.dependency

data class CycleDetail(
    val cycle: Cycle,
    override val depth: Int,
    override val breadth: Int,
    override val transitive: Int,
    val cycleDependencyList:List<Pair<ModulePath, ModulePath>>
) : Detail {
    override val paths: List<ModulePath> = cycle.parts
    override fun toLines(): List<String> =
        listOf("begin-cycle") + cycleDependencyList.map(::formatDependency) + listOf("end-cycle")
    private fun formatDependency(dependency:Pair<ModulePath, ModulePath>):String {
        val (first, second) = dependency
        val firstName = first.simpleName
        val secondName = second.simpleName
        return "  $firstName -> $secondName"
    }
}
