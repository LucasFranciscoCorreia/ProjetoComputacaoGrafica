package br.ufrpe.computacaografica.exceptions

class DifferentPointsSizeException(size1: Int, size2: Int) : Exception(String.format("The points have different sizes: %d vs %d", size1, size2)){}