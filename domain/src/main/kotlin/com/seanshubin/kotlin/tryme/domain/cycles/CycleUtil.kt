package com.seanshubin.kotlin.tryme.domain.cycles

object CycleUtil {
    fun <T:Comparable<T>> findCycles(edges: Set<Pair<T, T>>): List<List<T>> {
        val vertices: List<T> = edges.flatMap { listOf(it.first, it.second) }.sorted().distinct()
        val adjacencyMatrix: MutableList<MutableList<Boolean>> = vertices.map { row ->
            vertices.map { column ->
                edges.contains(row to column)
            }.toMutableList()
        }.toMutableList()
        vertices.indices.forEach { k ->
            vertices.indices.forEach { i ->
                vertices.indices.forEach { j ->
                    if (adjacencyMatrix[i][k] && adjacencyMatrix[k][j]) {
                        adjacencyMatrix[i][j] = true
                    }
                }
            }
        }
        val inCycle = vertices.indices.filter{adjacencyMatrix[it][it]}
        val cycles = inCycle.map{ target ->
            val sameRow:List<Int> = vertices.indices.filter { adjacencyMatrix[target][it] }
            val sameColumn:List<Int> = vertices.indices.filter {adjacencyMatrix[it][target]}
            val cycleIndices = sameRow intersect sameColumn
            val cycle = cycleIndices.map{vertices[it]}
            cycle
        }
        return cycles.map{it.sorted()}.distinct()
    }
}
