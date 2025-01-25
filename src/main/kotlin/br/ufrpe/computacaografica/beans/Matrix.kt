/**
 * A class representing a matrix of Double values.
 *
 * @property matrix A 2D array representing a matrix of Double values.
 * @property transpose A property that returns the transpose of the matrix.
 * 
 * @constructor Constructs a Matrix object with the given 2D array of Doubles.
 * @constructor Constructs a Matrix object with a single row from the given array of doubles.
 * 
 * @property columns Gets the number of columns in the matrix.
 * @property row Gets the number of rows in the matrix.
 * 
 * @throws InvalidMatrixMultiplicationException If the number of columns in the current matrix
 *         does not match the number of rows in the matrix to be multiplied.
 * 
 * @function multiplyRowColumn Multiplies a row vector by a column vector and returns the resulting scalar value.
 * @function multiply Multiplies the current matrix with another matrix.
 * 
 * @companion object
 * @function inverse Calculates the inverse of a 2x2 matrix.
 */
package br.ufrpe.computacaografica.beans

import br.ufrpe.computacaografica.exceptions.InvalidMatrixMultiplicationException

class Matrix {
    /**
     * Sets the value of the matrix at the specified index and recalculates the transpose.
     *
     * @receiver The 2D array representing the matrix.
     * @param i The index at which the value should be set.
     * @param value The new value to be set at the specified index, wrapped in an IndexedValue.
     */
    private operator fun Array<Array<Double>>.set(i: Int, value: IndexedValue<Array<Double>>) {
        this[i] = value.value
        calculateTranspose()
    }

    /**
     * Sets the value at the specified position in the 2D array and recalculates the transpose of the matrix.
     *
     * @receiver The 2D array of Double values.
     * @param i The row index where the value will be set.
     * @param j The column index where the value will be set.
     * @param value The new value to be set at the specified position.
     */
    private operator fun Array<Array<Double>>.set(i: Int, j: Int, value: Double) {
        this[i][j] = value
        calculateTranspose()
    }

    /**
     * Multiplies two arrays element-wise and returns the sum of the products.
     *
     * @param other The array to be multiplied with the current array.
     * @return The sum of the element-wise products of the two arrays.
     * @throws IllegalArgumentException If the arrays do not have the same length.
     */
    private operator fun Array<Double>.times(other: Array<Double>): Double {
        if (this.size != other.size) {
            throw IllegalArgumentException("Arrays must have the same length")
        }
        return (Array(this.size) { this[it] * other[it] }).sum()
    }

    /**
     * A 2D array representing a matrix of Double values.
     */
    var matrix: Array<Array<Double>>

    /**
     * A property representing the transpose of a matrix.
     * 
     * This property is an array of arrays of doubles, initialized to an empty 2D array.
     * The getter returns the current value of the transpose.
     * The setter is private, meaning it can only be modified within the class.
     */
    var transpose: Array<Array<Double>> = arrayOf(Array(0) { 0.0 })
        get() = field
        private set


    /**
     * Calculates the transpose of the current matrix and assigns it to the `transpose` property.
     * The transpose of a matrix is obtained by swapping its rows with its columns.
     */
    private fun calculateTranspose() {
        this.transpose = Array(matrix[0].size) { j ->
            Array(matrix.size) { i ->
                matrix[i][j]
            }
        }
    }
    /**
     * Constructs a Matrix object with the given 2D array of Doubles.
     *
     * @param m A 2D array of Doubles representing the matrix.
     */
    constructor(m: Array<Array<Double>>) {
        this.matrix = m
        calculateTranspose()
    }

    /**
     * Constructs a Matrix object with a single row from the given array of doubles.
     *
     * @param m An array of doubles representing the elements of the matrix row.
     */
    constructor(m: Array<Double>) {
        this.matrix = Array(1) { m }
        calculateTranspose()
    }

    /**
     * Gets the number of columns in the matrix.
     * This property retrieves the size of the first row in the matrix.
     *
     * @return the number of columns in the matrix.
     */
    private val columns: Int
        get() = matrix[0].size

    /**
     * Gets the number of rows in the matrix.
     * 
     * @return The number of rows in the matrix.
     */
    private val row: Int
        get() = matrix.size

    /**
     * Multiplies the current matrix with another matrix.
     *
     * @param m2 The matrix to be multiplied with the current matrix.
     * @return A new matrix which is the result of the multiplication.
     * @throws InvalidMatrixMultiplicationException If the number of columns in the current matrix
     *         does not match the number of rows in the matrix to be multiplied.
     */
    operator fun times(m2: Matrix): Matrix {
        if (this.columns != m2.row) throw InvalidMatrixMultiplicationException(this.row, m2.columns)
        return Matrix(Array(this.row) {i ->
            Array(m2.columns) {j ->
                this.matrix[i] * m2.transpose[j]
            }
        });
    }
    
    companion object {
        /**
         * Calculates the inverse of a 2x2 matrix.
         *
         * @param m The 2x2 matrix to be inverted, represented as a 2D array of Doubles.
         * @param determinant The determinant of the matrix `m`.
         * @return The inverse of the matrix `m`, represented as a 2D array of Doubles.
         * @throws IllegalArgumentException if the determinant is zero, as the matrix would not be invertible.
         */
        fun inverse(m: Array<Array<Double>>, determinant: Double): Array<Array<Double>> {
            val inverse = arrayOf(arrayOf(0.0, 0.0), arrayOf(0.0, 0.0))
            val a = m[0][0]
            val b = m[0][1]
            val c = m[1][0]
            val d = m[1][1]
            inverse[0][0] = d / determinant
            inverse[0][1] = -b / determinant
            inverse[1][0] = -c / determinant
            inverse[1][1] = a / determinant
            return inverse
        }
    }
}