package com.seanshubin.kotlin.tryme.domain.dependency

import com.seanshubin.kotlin.tryme.domain.cycles.CycleUtil

data class Graph(
    val originPairs: List<Pair<ModulePath, ModulePath>>,
    val originSingles: List<ModulePath>,
    val moduleList: List<Module>,
    val reversedModuleList: List<Module>,
    val cycleList: List<Cycle>
) {
    fun allEmpty():Boolean =
        (originPairs.flatMap{it.toList()} + originSingles).distinct().all{
            it.pathParts.isEmpty()
        }
    fun perspective(context: List<String>): Graph {
        fun ModulePath.dropContext(): ModulePath? =
            if (pathParts.startsWith(context)) {
                copy(pathParts = pathParts.drop(context.size))
            } else {
                null
            }

        fun ModulePath.truncateAfterFirst(): ModulePath =
            copy(pathParts = pathParts.take(1))

        fun modifySingle(single: ModulePath): ModulePath? =
            single.dropContext()?.truncateAfterFirst()

        fun modifyPair(pair: Pair<ModulePath, ModulePath>): Pair<ModulePath, ModulePath>? {
            val newFirst = modifySingle(pair.first)
            val newSecond = modifySingle(pair.second)
            return if (newFirst == null || newSecond == null) {
                null
            } else if (newFirst == newSecond) {
                null
            } else {
                newFirst to newSecond
            }
        }

        fun modifyPairToSingles(pair: Pair<ModulePath, ModulePath>): List<ModulePath> {
            val newFirst = modifySingle(pair.first)
            val newSecond = modifySingle(pair.second)
            return listOfNotNull(newFirst, newSecond)
        }

        val newPairs = originPairs.mapNotNull(::modifyPair)
        val newSingles = originSingles.mapNotNull(::modifySingle) + originPairs.flatMap(::modifyPairToSingles)
        return createFromModulePaths(newPairs, newSingles)
    }

    fun List<String>.startsWith(list: List<String>): Boolean {
        if (list.size > size) return false
        return take(list.size) == list
    }

    val entryPoints: List<Module> = reversedModuleList.filter { it.dependsOn.isEmpty() }

    val moduleMap: Map<ModulePath, Module> = moduleList.associateBy { it.path }

    val reversedModuleMap: Map<ModulePath, Module> = reversedModuleList.associateBy { it.path }

    val cycleMap: Map<ModulePath, Cycle> = cycleList.flatMap { cycle ->
        cycle.parts.map { path ->
            path to cycle
        }
    }.toMap()

    val moduleDetails: List<ModuleDetail> = moduleList.map(::createModuleDetail)

    fun createModuleDetail(module: Module): ModuleDetail {
        val cycle = cycleMap[module.path]
        val depth = moduleDepth(module.path)
        val breadth = moduleBreadth(module.path)
        val transitive = moduleTransitive(module.path)
        val dependsOnInCycle = module.dependsOn.mapNotNull {
            val dependsOnCycle = cycleMap[it]
            if (dependsOnCycle == null) null
            else it to dependsOnCycle
        }.toMap()
        val dependedOn = reversedModuleMap.getValue(module.path).dependsOn.isNotEmpty()
        return ModuleDetail(module, depth, breadth, transitive, dependedOn, cycle, dependsOnInCycle)
    }

    val cycleDetails: List<CycleDetail> = cycleList.mapIndexed { index, cycle ->
        val depth = cycleDepth(cycle)
        val breadth = cycleBreadth(cycle)
        val transitive = cycleTransitive(cycle)
        val cycleDependencies = cycleDependencies(cycle)
        CycleDetail(cycle, index, depth, breadth, transitive, cycleDependencies)
    }

    val details: List<Detail> = (moduleDetails + cycleDetails).sorted()

    fun toLines(makeLink:(ModulePath)->String?): List<String> {
        val header = "digraph detangled {"
        val body = details.flatMap { it.toLines(makeLink) }.map { "  $it" }
        val footer = "}"
        return listOf(header) + body + listOf(footer)
    }

    fun cycleDependencies(cycle: Cycle): List<Pair<ModulePath, ModulePath>> {
        val allDependencies = cycle.parts.flatMap { path ->
            val module = moduleMap.getValue(path)
            module.dependsOn.map {
                path to it
            }
        }
        val dependenciesInCycle = allDependencies.filter { (first, second) ->
            cycle.parts.contains(first) && cycle.parts.contains(second)
        }
        return dependenciesInCycle
    }

    fun cycleDependsOn(cycle: Cycle): List<ModulePath> {
        val result = cycle.parts.flatMap { path ->
            val single = moduleMap.getValue(path)
            single.dependsOn
        }.filter { !cycle.parts.contains(it) }.distinct()
        return result
    }

    fun moduleDepth(path: ModulePath): Int {
        val cycle = cycleMap[path]
        return if (cycle == null) {
            val biggest = moduleMap.getValue(path).dependsOn.map { moduleDepth(it) }.maxOrNull() ?: -1
            biggest + 1
        } else {
            cycleDepth(cycle)
        }
    }

    fun cycleDepth(cycle: Cycle): Int {
        val biggest = cycleDependsOn(cycle).map { moduleDepth(it) }.maxOrNull() ?: -1
        val result = biggest + cycle.parts.size
        return result
    }

    fun moduleTransitive(path: ModulePath): Int = moduleDependsOnTransitive(path).size

    fun cycleTransitive(cycle: Cycle): Int = cycleDependsOnTransitive(cycle).size

    fun moduleDependsOnTransitive(path: ModulePath): List<ModulePath> {
        val cycle = cycleMap[path]
        val module = moduleMap.getValue(path)
        return if (cycle == null) {
            val shallow = module.dependsOn
            val deep = module.dependsOn.flatMap(::moduleDependsOnTransitive)
            val result = (shallow + deep).distinct()
            result
        } else {
            val shallow = cycle.parts.filter { it != path }
            val deep = cycleDependsOnTransitive(cycle)
            val result = (shallow + deep).distinct()
            result
        }
    }

    fun cycleDependsOnTransitive(cycle: Cycle): List<ModulePath> {
        val shallow = cycleDependsOn(cycle)
        val deep = shallow.flatMap(::moduleDependsOnTransitive)
        val result = (shallow + deep).distinct()
        return result
    }

    fun moduleBreadth(path: ModulePath): Int =
        moduleMap.getValue(path).dependsOn.size

    fun cycleBreadth(cycle: Cycle): Int = cycleDependsOn(cycle).size

    fun generatePerspectives(context: List<String>): List<Pair<List<String>, Graph>> {
        val current = perspective(context)
        val remaining =
            current.moduleList.filter { it.path.pathParts.isNotEmpty() }.map { it.path.pathParts[0] }.flatMap {
                val newContext = context + it
                generatePerspectives(newContext)
            }
        val result =
            if (current.allEmpty() && remaining.isEmpty()) emptyList()
            else listOf(context to current) + remaining
        return result
    }

    fun toObject(): Map<String, Any> = mapOf(
        "singleList" to moduleList.map { it.toObject() },
        "cycleList" to cycleList.map { it.toObject() }
    )

    companion object {
        fun create(pairs: List<Pair<List<String>, List<String>>>, singles: List<List<String>>): Graph {
            val singleList = singles.map { ModulePath(it) }
            val pairList = pairs.map { (first, second) ->
                ModulePath(first) to ModulePath(second)
            }
            return createFromModulePaths(pairList, singleList)
        }

        fun createFromModulePaths(
            nonDistinctPairs: List<Pair<ModulePath, ModulePath>>,
            unfilteredSingles: List<ModulePath>
        ): Graph {
            val pairs = nonDistinctPairs.distinct()
            val fromPairs = pairs.flatMap { it.toList() }.toSet()
            val singles = unfilteredSingles.filter { !fromPairs.contains(it) }
            val moduleList = createModuleList(pairs, singles)
            val reversedModuleList = createModuleList(pairs.map { it.swapPair() }, singles)
            val cycleList: List<Cycle> = CycleUtil.findCycles(pairs.toSet()).map { Cycle(it) }
            return Graph(pairs, singles, moduleList, reversedModuleList, cycleList)
        }

        private fun createModuleList(
            pairs: List<Pair<ModulePath, ModulePath>>,
            singles: List<ModulePath>
        ): List<Module> {
            val dependsOnMap = pairs.fold(emptyMap(), ::collapseToList)
            val all = (pairs.flatMap { it.toList() } + singles).distinct()
            val moduleList = all.map { path ->
                val dependsOnSingle: List<ModulePath> = dependsOnMap[path] ?: emptyList()
                Module(path, dependsOnSingle)
            }
            return moduleList
        }

        fun <KeyType, ValueType> Pair<KeyType, ValueType>.swapPair(): Pair<ValueType, KeyType> = second to first

        fun <KeyType, ValueType> collapseToList(
            accumulator: Map<KeyType, List<ValueType>>,
            current: Pair<KeyType, ValueType>
        ): Map<KeyType, List<ValueType>> {
            val key = current.first
            val existingValue = accumulator[key]
            val newValue = if (existingValue == null) {
                listOf(current.second)
            } else {
                existingValue + current.second
            }
            val newEntry = key to newValue
            return accumulator + newEntry
        }
    }
}
