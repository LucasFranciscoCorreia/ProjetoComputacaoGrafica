/**
 * Exception thrown when an invalid matrix multiplication is attempted.
 *
 * @param row The number of rows in the matrix.
 * @param column The number of columns in the matrix.
 */
package br.ufrpe.computacaografica.exceptions

class InvalidMatrixMultiplicationException(row: Int, column: Int) :
    Exception(String.format("Invalid matrix multiplication with %d rows and %d columns", row, column)) {
}