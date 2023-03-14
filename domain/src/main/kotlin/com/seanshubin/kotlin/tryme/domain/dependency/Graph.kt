package com.seanshubin.kotlin.tryme.domain.dependency

import com.seanshubin.kotlin.tryme.domain.cycles.CycleUtil

data class Graph(val moduleList: List<Module>, val reversedModuleList:List<Module>, val cycleList: List<Cycle>) {
    val entryPoints: List<Module> = reversedModuleList.filter { it.dependsOn.isEmpty() }

    val moduleMap: Map<ModulePath, Module> = moduleList.associateBy { it.path }

    val cycleMap: Map<ModulePath, Cycle> = cycleList.flatMap { cycle ->
        cycle.parts.map { path ->
            path to cycle
        }
    }.toMap()


    fun toLines():List<String> =
        moduleList.flatMap { module ->
            module.dependsOn.map {
                "${module.simpleName} -> ${it.simpleName}"
            }
        }

    fun cycleDependsOn(cycle: Cycle): List<ModulePath> {
        val result = cycle.parts.flatMap { path ->
            val single = moduleMap.getValue(path)
            single.dependsOn
        }.filter { !cycle.parts.contains(it) }.distinct()
        return result
    }

    fun depth(path: ModulePath): Int {
        val cycle = cycleMap[path]
        val depthList = if (cycle == null) {
            moduleMap.getValue(path).dependsOn.map { depth(it) }
        } else {
            cycleDependsOn(cycle).map { depth(it)+cycle.parts.size-1 }
        }
        val biggest = depthList.maxOrNull() ?: -1
        return biggest + 1
    }

    fun transitive(path: ModulePath): Int = dependsOnTransitive(path).size

    fun dependsOnTransitive(path:ModulePath):List<ModulePath> {
        val cycle = cycleMap[path]
        val module = moduleMap.getValue(path)
        return if(cycle == null){
            val shallow = module.dependsOn
            val deep = module.dependsOn.flatMap(::dependsOnTransitive)
            val result = (shallow + deep).distinct()
            result
        } else {
            val fromCycle = cycle.parts.filter { it != path }
            val fromCycleDependsOn = cycleDependsOn(cycle)
            val deep = fromCycleDependsOn.flatMap(::dependsOnTransitive)
            val result = (fromCycle + fromCycleDependsOn + deep).distinct()
            result
        }
    }

    fun breadth(path: ModulePath): Int =
            moduleMap.getValue(path).dependsOn.size

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
            val reversedModuleList = createModuleList(list.map{it.swapPair()})
            val cycleList: List<Cycle> = CycleUtil.findCycles(list.toSet()).map { Cycle(it) }
            return Graph(moduleList,reversedModuleList,cycleList)
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
