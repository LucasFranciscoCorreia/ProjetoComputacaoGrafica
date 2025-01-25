/**
 * Represents a point in a multi-dimensional space.
 *
 * @property points An array of Double values representing the coordinates of the point.
 * @property normal An array representing the normal vector at this point. This property is initialized later and should be assigned before use.
 * @property triangles A list of triangles associated with this point.
 * @constructor Creates a Point object with a specified number of points, each initialized to 0.0.
 * @constructor Creates a Point object with the given array of points.
 *
 * @throws DifferentVectorSizeException if the points have different sizes when performing operations.
 */
package br.ufrpe.computacaografica.beans

import br.ufrpe.computacaografica.exceptions.DifferentVectorSizeException

class Point {
    /**
     * An array of Double values representing points.
     */
    val points: Array<Double>

    /**
     * An array representing the normal vector at this point.
     * This property is initialized later and should be assigned before use.
     */
    lateinit var normal: Array<Double>

    /**
     * A list of triangles associated with this point.
     */
    var triangles: ArrayList<Triangle>

    /**
     * Constructs a Point object with a specified number of points.
     *
     * @param i The number of points to initialize. Each point is initialized to 0.0.
     */
    constructor(i: Int) {
        this.points = Array(i){0.0}
        triangles = ArrayList()
    }

    /**
     * Constructs a Point object with the given array of points.
     *
     * @param points An array of Double values representing the coordinates of the point.
     */
    constructor(points: Array<Double>) {
        this.points = points.clone()
        triangles = ArrayList()
    }

    /**
     * Subtracts the given point from the current point and returns the resulting vector.
     *
     * @param p2 The point to subtract from the current point.
     * @return A new vector representing the difference between the two points.
     * @throws DifferentVectorSizeException if the points have different sizes.
     */
    fun subtract(p2: Point): Vector = Array(points.size) { i-> points[i]-p2.points[i]}.let { Vector(it) }.takeIf { points.size == p2.points.size } ?: throw DifferentVectorSizeException(this.points.size, p2.points.size)

    /**
     * Checks if this Point is equal to another object.
     *
     * @param other the object to compare with this Point.
     * @return true if the other object is a Point and has the same points as this Point, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(other !is Point) return false

        return this.points.contentEquals(other.points)
    }

    companion object {
        /**
         * Calculates the barycentric coordinates of a point with respect to a triangle.
         *
         * @param p1 The first vertex of the triangle.
         * @param p2 The second vertex of the triangle.
         * @param p3 The third vertex of the triangle.
         * @param p The point for which the barycentric coordinates are to be calculated.
         * @return An array of doubles representing the barycentric coordinates of the point.
         */
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

        /**
         * Calculates the barycentric coordinate of a point given three points and their respective barycentric coordinates.
         *
         * @param p1 The first point.
         * @param p2 The second point.
         * @param p3 The third point.
         * @param alfa The barycentric coordinate corresponding to the first point.
         * @param beta The barycentric coordinate corresponding to the second point.
         * @param gama The barycentric coordinate corresponding to the third point.
         * @return A new Point object representing the calculated barycentric coordinate.
         */
        fun calculatePointBarycentricCoordinate(p1: Point, p2: Point, p3: Point, alfa: Double, beta: Double, gama: Double): Point = 
        Point(arrayOf(
            alfa * p1.points[0] + beta * p2.points[0] + gama * p3.points[0],
            alfa * p1.points[1] + beta * p2.points[1] + gama * p3.points[1],
            alfa * p1.points[2] + beta * p2.points[2] + gama * p3.points[2])
        )
    }
}