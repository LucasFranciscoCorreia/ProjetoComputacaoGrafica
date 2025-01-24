// Source code is decompiled from a .class file using FernFlower decompiler.
package br.ufrpe.computacaografica.beans

import br.ufrpe.computacaografica.exceptions.DifferentPointsSizeException

class Triangle {
    var p: Array<Point>
    var normal: Array<Double> = Array(0){0.0}
        set(normal) {
            if(normal.size != 3) throw DifferentPointsSizeException(normal.size, 3)
            field = normal
        }
    var original: Array<Point>
    var tela: Array<Point>

    constructor(p: Array<Point>) {
        this.p = p
        this.original = p.clone()
        this.tela = arrayOf(Point(0), Point(0), Point(0))
    }

    constructor(p1: Point, p2: Point, p3: Point) {
        this.p = arrayOf(p1, p2, p3)
        this.original = p.clone()
        this.tela = arrayOf(Point(0), Point(0), Point(0))
    }

    fun contains(ponto: Point): Boolean {
        return p.contains(ponto)
    }

    fun setOriginais() {
        this.original = p.clone()
    }
}