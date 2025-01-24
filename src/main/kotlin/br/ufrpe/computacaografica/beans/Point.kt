// Source code is decompiled from a .class file using FernFlower decompiler.
package br.ufrpe.computacaografica.beans

import br.ufrpe.computacaografica.exceptions.DifferentVectorSizeException

class Point {
    val p: Array<Double>
    lateinit var normal: Array<Double>

    var triangulos: ArrayList<Triangle>

    constructor(i: Int) {
        this.p = Array(i){0.0}
        triangulos = ArrayList()
    }

    constructor(res: Array<Double>) {
        this.p = res.clone()
        triangulos = ArrayList()
    }

    fun subtrai(p2: Point): Vector = Array(p.size) { i-> p[i]-p2.p[i]}.let { Vector(it) }.takeIf { p.size == p2.p.size } ?: throw DifferentVectorSizeException(this.p.size, p2.p.size)

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(other !is Point) return false

        return this.p.contentEquals(other.p)
    }

    companion object {
        fun coordenadaBaricentrica(p1: Point, p2: Point, p3: Point, p: Point): Array<Double> {
            val m1 = Matrix(arrayOf(arrayOf(p1.p[0] - p3.p[0], p2.p[0] - p3.p[0]),arrayOf(p1.p[1] - p3.p[1], p2.p[1] - p3.p[1])))
            val m2 = Matrix(arrayOf(arrayOf(p.p[0] - p3.p[0]), arrayOf(p.p[1] - p3.p[1])))

            val a: Double = m1.matriz[0][0]
            val b: Double = m1.matriz[0][1]
            val c: Double = m1.matriz[1][0]
            val d: Double = m1.matriz[1][1]
            val determinante = a * d - b * c

            val inversa: Array<Array<Double>> = Matrix.inversa(m1.matriz, determinante)
            val mult: Matrix = (Matrix(inversa)).mult(m2)
            return mult.transposta[0]
        }

        fun calcularPontoCoordenadaBaricentrica(p1: Point,p2: Point,p3: Point,alfa: Double,beta: Double,gama: Double): Point
        = Point(arrayOf(
            alfa * p1.p[0] + beta * p2.p[0] + gama * p3.p[0],
            alfa * p1.p[1] + beta * p2.p[1] + gama * p3.p[1],
            alfa * p1.p[2] + beta * p2.p[2] + gama * p3.p[2])
        )
    }
}