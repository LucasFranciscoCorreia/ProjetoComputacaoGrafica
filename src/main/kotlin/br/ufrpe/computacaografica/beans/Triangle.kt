/**
 * Represents a triangle in a 2D or 3D space.
 *
 * @property points An array of points representing the vertices of the triangle.
 * @property normal An array of doubles representing the normal vector. It must have exactly 3 elements.
 * @property original An array of Point objects representing the original vertices of the triangle.
 * @property screen An array of Point objects representing the screen coordinates of the triangle.
 *
 * @constructor Creates a Triangle object with the given array of points.
 * @constructor Creates a Triangle object with three given points.
 *
 * @throws DifferentPointsSizeException if the size of the normal array is not 3.
 */
package br.ufrpe.computacaografica.beans

import br.ufrpe.computacaografica.exceptions.DifferentPointsSizeException

class Triangle {
    /**
     * An array of points representing the vertices of the triangle.
     */
    var points: Array<Point>

    /**
     * Represents the normal vector of a triangle.
     * 
     * @property normal An array of doubles representing the normal vector. 
     *                  It must have exactly 3 elements.
     * @throws DifferentPointsSizeException if the size of the array is not 3.
     */
    var normal: Array<Double> = Array(0){0.0}
        set(normal) {
            if(normal.size != 3) throw DifferentPointsSizeException(normal.size, 3)
            field = normal
        }

    /**
     * An array of Point objects representing the original vertices of the triangle.
     */
    var original: Array<Point>

    /**
     * Represents the screen as an array of Point objects.
     */
    var screen: Array<Point>

    /**
     * Constructs a Triangle object with the given array of points.
     *
     * @param p An array of Point objects representing the vertices of the triangle.
     */
    constructor(p: Array<Point>) {
        this.points = p
        this.original = p.clone()
        this.screen = arrayOf(Point(0), Point(0), Point(0))
    }

    /**
     * Constructs a Triangle with three given points.
     *
     * @param p1 The first point of the triangle.
     * @param p2 The second point of the triangle.
     * @param p3 The third point of the triangle.
     */
    constructor(p1: Point, p2: Point, p3: Point) {
        this.points = arrayOf(p1, p2, p3)
        this.original = points.clone()
        this.screen = arrayOf(Point(0), Point(0), Point(0))
    }

    /**
     * Checks if the given point is contained within the triangle.
     *
     * @param ponto The point to check.
     * @return `true` if the point is contained within the triangle, `false` otherwise.
     */
    fun contains(ponto: Point): Boolean {
        return points.contains(ponto)
    }

    /**
     * Sets the original points of the triangle by cloning the current points.
     * This method is used to store the initial state of the points.
     */
    fun setOriginals() {
        this.original = points.clone()
    }
}