package com.seanshubin.kotlin.tryme.domain.matrix

data class Matrix<T>(val rows: List<List<T>>, val size: Size) {
    constructor() : this(emptyList())
    constructor(size: Size, value: T) : this(inferRowsFromSize(size, value))
    constructor(size: Size, computeValue: (Int, Int) -> T) : this(inferRowsFromSize(size, computeValue))
    constructor(rows: List<List<T>>) : this(rows, inferSizeFromRows(rows))

    operator fun get(i: Int, j: Int): T = rows[i][j]

    fun <U> unaryOp(op: (T) -> U): Matrix<U> =
        Matrix((0 until size.row).map { i ->
            (0 until size.col).map { j ->
                op(this[i, j])
            }
        })

    fun <U> binaryOp(that: Matrix<T>, op: (T, T) -> U): Matrix<U> =
        Matrix((0 until size.row).map { i ->
            (0 until size.col).map { j ->
                op(this[i, j], that[i, j])
            }
        })

    fun <U> rowColOp(that: Matrix<T>, op: (List<T>, List<T>) -> U): Matrix<U> {
        val transposedThat = that.transpose()
        val newRows = (0 until size.row).map { i ->
            (0 until transposedThat.size.row).map { j ->
                op(this.rowVector(i), transposedThat.rowVector(j))
            }
        }
        return Matrix(newRows)
    }

    fun transpose(): Matrix<T> =
        Matrix((0 until size.col).map { i ->
            (0 until size.row).map { j ->
                this[j, i]
            }
        })

    fun rowVector(i: Int): List<T> = rows[i]

    fun colVector(i: Int): List<T> = transpose().rowVector(i)

    fun mutableCopy(): MutableList<MutableList<T>> =
        rows.map { row ->
            row.toMutableList()
        }.toMutableList()

    companion object {
        fun <T> inferRowsFromSize(size: Size, value: T): List<List<T>> =
            inferRowsFromSize(size) { _, _ -> value }

        fun <T> inferRowsFromSize(size: Size, computeValue: (Int, Int) -> T): List<List<T>> =
            (0 until size.row).map { i ->
                (0 until size.col).map { j ->
                    computeValue(i, j)
                }
            }

        fun <T> inferSizeFromRows(rows: List<List<T>>): Size {
            val rowCount = rows.size
            val colCount =
                if (rowCount == 0) 0
                else rows[0].size
            return Size(rowCount, colCount)
        }

        fun <T> row(vararg cells: T): Matrix<T> = Matrix(listOf(cells.toList()))

        fun <T> col(vararg cells: T): Matrix<T> = row(*cells).transpose()
    }
}


operator fun Matrix<Int>.plus(that: Matrix<Int>): Matrix<Int> = binaryOp(that) { a, b -> a + b }
operator fun Matrix<Int>.times(that: Int): Matrix<Int> = unaryOp { a -> a * that }
fun dotProduct(a: List<Int>, b: List<Int>): Int = a.zip(b).map { (c, d) -> c * d }.sum()
operator fun Matrix<Int>.times(that: Matrix<Int>): Matrix<Int> = rowColOp(that) { a, b -> dotProduct(a, b) }
