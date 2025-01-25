package br.ufrpe.computacaografica.beans

import br.ufrpe.computacaografica.exceptions.DifferentVectorSizeException
import kotlin.math.sqrt

class Vector(var v: Array<Double>) {

    /**
     * Performs scalar multiplication (dot product) between this vector and another vector.
     *
     * @param v2 The vector to be multiplied with this vector.
     * @return The result of the scalar multiplication as a Double.
     */
    fun scalarMultiplication(v2: Vector): Double = arrayOf(v[0] * v2.v[0], v[1] * v2.v[1], v[2] * v2.v[2]).sum()

    /**
     * Multiplies the vector by a scalar value.
     *
     * @param mult The scalar value to multiply the vector by.
     * @return A new vector that is the result of the multiplication.
     */
    operator fun times(mult: Double): Vector = Vector(arrayOf(mult * v[0], mult * v[1], mult * v[2]))

    /**
     * Computes the cross product of this vector with another vector.
     *
     * @param v2 The vector to compute the cross product with.
     * @return A new vector that is the cross product of this vector and the given vector.
     * @throws DifferentVectorSizeException if the vectors have different sizes.
     */
    operator fun times (v2: Vector): Vector {
        if(v.size != v2.v.size) throw DifferentVectorSizeException(v.size, v2.v.size)

        val m = Array(3) { Array(3){0.0} }

        for (i in 0..2) {
            m[0][i] = 1.0
            m[1][i] = this.v[i]
            m[2][i] = v2.v[i]
        }

        return Vector(arrayOf(
            -(m[0][0] * m[1][1] * m[2][2] - m[0][0] * m[1][2] * m[2][1]),
            -(m[0][1] * m[1][2] * m[2][0] - m[0][1] * m[1][0] * m[2][2]),
            -(m[0][2] * m[1][0] * m[2][1] - m[0][2] * m[1][1] * m[2][0])
        ))
    }

    /**
     * The norm of the vector, calculated as the square root of the sum of the squares of its components.
     */
    private val norm: Double = sqrt(arrayOf(v[0] * v[0], v[1] * v[1], v[2] * v[2]).sum())


    /**
     * Normalizes the vector.
     *
     * This function calculates the normalized version of the vector by dividing each component
     * of the vector by its norm (magnitude). The resulting vector will have a magnitude of 1.
     *
     * @return A new `Vector` instance representing the normalized vector.
     */
    fun normalizeVector(): Vector = norm.let {  Vector(arrayOf(v[0] / it, v[1] / it, v[2] / it)) }

    /**
     * Adds the given vector to this vector.
     *
     * @param v2 The vector to be added.
     * @return A new vector that is the result of the addition.
     */
    operator fun plus(v2: Vector): Vector = Vector(arrayOf(v[0]+v2.v[0], v[1]+v2.v[1], v[2]+v2.v[2]))
    /**
     * Performs element-wise multiplication of this vector with another vector.
     *
     * @param v2 The vector to multiply with.
     * @return A new vector resulting from the element-wise multiplication.
     */
    fun elementWiseMultiplication(v2: Vector): Vector = Vector(arrayOf(v[0]*v2.v[0], v[1]*v2.v[1], v[2]*v2.v[2]))

    /**
     * Subtracts the given vector from this vector.
     *
     * @param v2 The vector to subtract from this vector.
     * @return A new vector that is the result of the subtraction.
     */
    operator fun minus(v2: Vector): Vector = Vector(arrayOf(v[0]-v2.v[0], v[1]-v2.v[1], v[2]-v2.v[2]))

    /**
     * Returns a new Vector instance with all components negated.
     *
     * This operator function allows the use of the unary minus operator (-) on a Vector instance.
     * For example, if `v` is a Vector, `-v` will return a new Vector with each component negated.
     *
     * @return A new Vector with each component negated.
     */
    operator fun unaryMinus(): Vector = Vector(arrayOf(-v[0], -v[1], -v[2]))
}