// Source code is decompiled from a .class file using FernFlower decompiler.
package br.ufrpe.computacaografica.beans

import br.ufrpe.computacaografica.exceptions.DifferentVectorSizeException

class Point {
    val points: Array<Double>
    lateinit var normal: Array<Double>

    var triangles: ArrayList<Triangle>

    constructor(i: Int) {
        this.points = Array(i){0.0}
        triangles = ArrayList()
    }

    constructor(points: Array<Double>) {
        this.points = points.clone()
        triangles = ArrayList()
    }

    fun subtract(p2: Point): Vector = Array(points.size) { i-> points[i]-p2.points[i]}.let { Vector(it) }.takeIf { points.size == p2.points.size } ?: throw DifferentVectorSizeException(this.points.size, p2.points.size)

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(other !is Point) return false

        return this.points.contentEquals(other.points)
    }

    companion object {
        fun barycentricCoordinate(p1: Point, p2: Point, p3: Point, p: Point): Array<Double> {
            val m1 = Matrix(arrayOf(arrayOf(p1.points[0] - p3.points[0], p2.points[0] - p3.points[0]),arrayOf(p1.points[1] - p3.points[1], p2.points[1] - p3.points[1])))
            val m2 = Matrix(arrayOf(arrayOf(p.points[0] - p3.points[0]), arrayOf(p.points[1] - p3.points[1])))

            val a: Double = m1.matrix[0][0]
            val b: Double = m1.matrix[0][1]
            val c: Double = m1.matrix[1][0]
            val d: Double = m1.matrix[1][1]
            val determinant = a * d - b * c

            val inverse: Array<Array<Double>> = Matrix.inverse(m1.matrix, determinant)
            val mult: Matrix = (Matrix(inverse)).multiply(m2)
            return mult.transpose[0]
        }

        fun calculatePointBarycentricCoordinate(p1: Point, p2: Point, p3: Point, alfa: Double, beta: Double, gama: Double): Point
        = Point(arrayOf(
            alfa * p1.points[0] + beta * p2.points[0] + gama * p3.points[0],
            alfa * p1.points[1] + beta * p2.points[1] + gama * p3.points[1],
            alfa * p1.points[2] + beta * p2.points[2] + gama * p3.points[2])
        )
    }
}