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
     * A 2D array representing a matrix of Double values.
     */
    var matrix: Array<Array<Double>>
    /**
     * A property that returns the transpose of the matrix.
     * 
     * The transpose of a matrix is obtained by swapping rows with columns.
     * This property is computed dynamically each time it is accessed.
     * 
     * @property transpose The transposed matrix as a 2D array of Doubles.
     */
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

    /**
     * Constructs a Matrix object with the given 2D array of Doubles.
     *
     * @param m A 2D array of Doubles representing the matrix.
     */
    constructor(m: Array<Array<Double>>) {
        this.matrix = m
    }

    /**
     * Constructs a Matrix object with a single row from the given array of doubles.
     *
     * @param m An array of doubles representing the elements of the matrix row.
     */
    constructor(m: Array<Double>) {
        this.matrix = Array(1) { m }
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
     * Multiplies a row vector by a column vector and returns the resulting scalar value.
     *
     * This function supports both 2D and 3D vectors. If the vectors have a size of 2, it calculates
     * the dot product for 2D vectors. Otherwise, it calculates the dot product for 3D vectors.
     *
     * @param row The row vector represented as an array of Doubles.
     * @param column The column vector represented as an array of Doubles.
     * @return The resulting scalar value from the dot product of the row and column vectors.
     */
    private fun multiplyRowColumn(row: Array<Double>, column: Array<Double>): Double = 
        (arrayOf(row[0]*column[0]+row[1]*column[1]).sum()
        .takeIf { row.size == 2 }) ?: arrayOf(row[0]*column[0]+row[1]*column[1]+row[2]*column[2]).sum()

    /**
     * Multiplies the current matrix with another matrix.
     *
     * @param m The matrix to be multiplied with the current matrix.
     * @return A new matrix which is the result of the multiplication.
     * @throws InvalidMatrixMultiplicationException If the number of columns in the current matrix
     *         does not match the number of rows in the matrix to be multiplied.
     */
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