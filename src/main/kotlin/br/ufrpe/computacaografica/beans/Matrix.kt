package br.ufrpe.computacaografica.beans

import br.ufrpe.computacaografica.exceptions.InvalidMatrixInverseException
import br.ufrpe.computacaografica.exceptions.InvalidMatrixMultiplicationException

class Matrix {
    var matriz: Array<Array<Double>>
    var transposta: Array<Array<Double>> = arrayOf(Array(0) { 0.0 })
        get() {
            field = Array(matriz[0].size) { Array(matriz.size) { 0.0 } }
            for (i in matriz.indices) {
                for (j in matriz[i].indices) {
                    field[j][i] = matriz[i][j]
                }
            }
            return field
        }
        private set

    constructor(linhas: Int, colunas: Int) {
        this.matriz = Array(linhas) { Array(colunas) {0.0} }
        this.transposta = Array(colunas) { Array(linhas) {0.0} }
    }

    constructor(m: Array<Array<Double>>) {
        this.matriz = m
    }

    constructor(m: Array<Double>) {
        this.matriz = Array(1) { Array(m.size) {0.0} }
        this.matriz[0] = m
    }

    private val colunas: Int
        get() = matriz[0].size

    private val linhas: Int
        get() = matriz.size

    private fun multLinhaColuna(linha: Array<Double>, coluna: Array<Double>): Double = (arrayOf(linha[0]*coluna[0]+linha[1]*coluna[1]).sum().takeIf { linha.size == 2 }) ?: arrayOf(linha[0]*coluna[0]+linha[1]*coluna[1]+linha[2]*coluna[2]).sum()

    fun mult(m: Matrix): Matrix {
        if (this.colunas != m.linhas) throw InvalidMatrixMultiplicationException(this.linhas, m.colunas)
        val res = Array(this.linhas) {Array(m.colunas) {0.0}}

        for (i in 0 until this.linhas) {
            for (j in 0 until m.colunas) {
                res[i][j] = this.multLinhaColuna(this.matriz[i], m.transposta[j])
            }
        }

        return Matrix(res)
    }
    companion object {
        fun inversa(m: Array<Array<Double>>, determinante: Double): Array<Array<Double>> {
            val inversa = arrayOf(arrayOf(0.0, 0.0), arrayOf(0.0, 0.0))
            val a = m[0][0]
            val b = m[0][1]
            val c = m[1][0]
            val d = m[1][1]
            inversa[0][0] = d / determinante
            inversa[0][1] = -b / determinante
            inversa[1][0] = -c / determinante
            inversa[1][1] = a / determinante
            return inversa
        }
    }
}