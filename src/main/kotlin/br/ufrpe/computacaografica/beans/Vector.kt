package br.ufrpe.computacaografica.beans

import br.ufrpe.computacaografica.exceptions.DifferentVectorSizeException
import kotlin.math.sqrt

class Vector(var v: Array<Double>) {

    fun multEscalar(v2: Vector): Double = arrayOf(v[0] * v2.v[0], v[1] * v2.v[1], v[2] * v2.v[2]).sum()

    fun multPorEscalar(mult: Double): Vector = Vector(arrayOf(mult * v[0], mult * v[1], mult * v[2]))

    private val norma: Double = sqrt(arrayOf(v[0] * v[0], v[1] * v[1], v[2] * v[2]).sum())

    fun produtoVetorial(b: Vector): Vector {
        if(v.size != b.v.size) throw DifferentVectorSizeException(v.size, b.v.size)

        val m = Array(3) { Array(3){0.0} }

        for (i in 0..2) {
            m[0][i] = 1.0
            m[1][i] = this.v[i]
            m[2][i] = b.v[i]
        }

        return Vector(arrayOf(
            -(m[0][0] * m[1][1] * m[2][2] - m[0][0] * m[1][2] * m[2][1]),
            -(m[0][1] * m[1][2] * m[2][0] - m[0][1] * m[1][0] * m[2][2]),
            -(m[0][2] * m[1][0] * m[2][1] - m[0][2] * m[1][1] * m[2][0])
        ))
    }

    fun normalizarVetor(): Vector = norma.let {  Vector(arrayOf(v[0] / it, v[1] / it, v[2] / it)) }

    fun mais(v2: Vector): Vector = Vector(arrayOf(v[0]+v2.v[0], v[1]+v2.v[1], v[2]+v2.v[2]))

    fun multInterno(v2: Vector): Vector = Vector(arrayOf(v[0]*v2.v[0], v[1]*v2.v[1], v[2]*v2.v[2]))

    fun menos(v2: Vector): Vector = Vector(arrayOf(v[0]-v2.v[0], v[1]-v2.v[1], v[2]-v2.v[2]))
}