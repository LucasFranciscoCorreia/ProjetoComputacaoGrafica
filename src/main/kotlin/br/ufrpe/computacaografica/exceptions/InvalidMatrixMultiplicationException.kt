package br.ufrpe.computacaografica.exceptions

class InvalidMatrixMultiplicationException(row: Int, column: Int) :
    Exception(String.format("Invalid matrix multiplication with %d rows and %d columns", row, column)) {
}