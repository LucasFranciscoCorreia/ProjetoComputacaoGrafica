/**
 * Exception thrown when two sets of points have different sizes.
 *
 * @param size1 The size of the first set of points.
 * @param size2 The size of the second set of points.
 */
package br.ufrpe.computacaografica.exceptions

class DifferentPointsSizeException(size1: Int, size2: Int) : Exception(String.format("The points have different sizes: %d vs %d", size1, size2)){}