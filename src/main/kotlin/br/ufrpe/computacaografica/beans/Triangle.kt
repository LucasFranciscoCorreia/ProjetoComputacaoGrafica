package br.ufrpe.computacaografica.beans

import br.ufrpe.computacaografica.exceptions.DifferentPointsSizeException

class Triangle {
    var points: Array<Point>
    var normal: Array<Double> = Array(0){0.0}
        set(normal) {
            if(normal.size != 3) throw DifferentPointsSizeException(normal.size, 3)
            field = normal
        }
    var original: Array<Point>
    var screen: Array<Point>

    constructor(p: Array<Point>) {
        this.points = p
        this.original = p.clone()
        this.screen = arrayOf(Point(0), Point(0), Point(0))
    }

    constructor(p1: Point, p2: Point, p3: Point) {
        this.points = arrayOf(p1, p2, p3)
        this.original = points.clone()
        this.screen = arrayOf(Point(0), Point(0), Point(0))
    }

    fun contains(ponto: Point): Boolean {
        return points.contains(ponto)
    }

    fun setOriginals() {
        this.original = points.clone()
    }
}