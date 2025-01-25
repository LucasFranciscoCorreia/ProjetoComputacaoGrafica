/**
 * Exception thrown when an attempt is made to invert a matrix that does not have a 2x2 size.
 *
 * @constructor Creates an InvalidMatrixInverseException with a default error message.
 */
package br.ufrpe.computacaografica.exceptions

class InvalidMatrixInverseException: Exception("Matrix doesn't have a 2x2 size") {
}