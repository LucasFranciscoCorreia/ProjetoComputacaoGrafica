package br.ufrpe.computacaografica.beans

import br.ufrpe.computacaografica.exceptions.InvalidMatrixMultiplicationException

class Matrix {
    var matrix: Array<Array<Double>>
    var transpose: Array<Array<Double>> = arrayOf(Array(0) { 0.0 })
        get() {
            field = Array(matrix[0].size) { Array(matrix.size) { 0.0 } }
            for (i in matrix.indices) {
                for (j in matrix[i].indices) {
                    field[j][i] = matrix[i][j]
                }
            }
            return field
        }
        private set

    constructor(m: Array<Array<Double>>) {
        this.matrix = m
    }

    constructor(m: Array<Double>) {
        this.matrix = Array(1) { m }
    }

    private val columns: Int
        get() = matrix[0].size

    private val row: Int
        get() = matrix.size

    private fun multiplyRowColumn(row: Array<Double>, column: Array<Double>): Double = (arrayOf(row[0]*column[0]+row[1]*column[1]).sum().takeIf { row.size == 2 }) ?: arrayOf(row[0]*column[0]+row[1]*column[1]+row[2]*column[2]).sum()

    fun multiply(m: Matrix): Matrix {
        if (this.columns != m.row) throw InvalidMatrixMultiplicationException(this.row, m.columns)
        val res = Array(this.row) {Array(m.columns) {0.0}}

        for (i in 0 until this.row) {
            for (j in 0 until m.columns) {
                res[i][j] = this.multiplyRowColumn(this.matrix[i], m.transpose[j])
            }
        }

        return Matrix(res)
    }
    companion object {
        fun inverse(m: Array<Array<Double>>, determinant: Double): Array<Array<Double>> {
            val inversa = arrayOf(arrayOf(0.0, 0.0), arrayOf(0.0, 0.0))
            val a = m[0][0]
            val b = m[0][1]
            val c = m[1][0]
            val d = m[1][1]
            inversa[0][0] = d / determinant
            inversa[0][1] = -b / determinant
            inversa[1][0] = -c / determinant
            inversa[1][1] = a / determinant
            return inversa
        }
    }
}