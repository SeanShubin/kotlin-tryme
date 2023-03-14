package com.seanshubin.kotlin.tryme.domain.dependency

import com.seanshubin.kotlin.tryme.domain.cycles.CycleUtil

data class Graph(val moduleList: List<Module>, val reversedModuleList: List<Module>, val cycleList: List<Cycle>) {
    val entryPoints: List<Module> = reversedModuleList.filter { it.dependsOn.isEmpty() }

    val moduleMap: Map<ModulePath, Module> = moduleList.associateBy { it.path }

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
            if(dependsOnCycle == null) null
            else it to dependsOnCycle
        }.toMap()
        return ModuleDetail(module, depth, breadth, transitive, cycle, dependsOnInCycle)
    }

    val cycleDetails: List<CycleDetail> = cycleList.map { cycle ->
        val depth = cycleDepth(cycle)
        val breadth = cycleBreadth(cycle)
        val transitive = cycleTransitive(cycle)
        val cycleDependencies = cycleDependencies(cycle)
        CycleDetail(cycle, depth, breadth, transitive, cycleDependencies)
    }

    val details: List<Detail> = (moduleDetails + cycleDetails).sorted()

    fun toLines(): List<String> = details.flatMap { it.toLines() }

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

    fun toObject(): Map<String, Any> = mapOf(
        "singleList" to moduleList.map { it.toObject() },
        "cycleList" to cycleList.map { it.toObject() }
    )

    companion object {
        fun create(list: List<Pair<List<String>, List<String>>>): Graph {
            val pathList = list.map { (first, second) ->
                ModulePath(first) to ModulePath(second)
            }
            return createFromModulePaths(pathList)
        }

        fun createFromModulePaths(list: List<Pair<ModulePath, ModulePath>>): Graph {
            val moduleList = createModuleList(list)
            val reversedModuleList = createModuleList(list.map { it.swapPair() })
            val cycleList: List<Cycle> = CycleUtil.findCycles(list.toSet()).map { Cycle(it) }
            return Graph(moduleList, reversedModuleList, cycleList)
        }

        fun createModuleList(list: List<Pair<ModulePath, ModulePath>>): List<Module> {
            val dependsOnMap = list.fold(emptyMap(), ::collapseToList)
            val moduleList = list.flatMap { it.toList() }.distinct().map { path ->
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
