/**
 * Exception thrown when two vectors have different sizes.
 *
 * @param size1 The size of the first vector.
 * @param size2 The size of the second vector.
 */
package br.ufrpe.computacaografica.exceptions

class DifferentVectorSizeException(size1: Int, size2: Int) : Exception(String.format("The points have different sizes: %d vs %d", size1, size2)){}
