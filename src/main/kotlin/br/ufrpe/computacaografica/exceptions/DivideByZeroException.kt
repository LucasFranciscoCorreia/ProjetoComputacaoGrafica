/**
 * Exception thrown when an attempt is made to divide by zero.
 *
 * @constructor Creates a new instance of DivideByZeroException with a default error message.
 */
package br.ufrpe.computacaografica.exceptions

class DivideByZeroException : Exception("Tried to divide something by zero") {
}