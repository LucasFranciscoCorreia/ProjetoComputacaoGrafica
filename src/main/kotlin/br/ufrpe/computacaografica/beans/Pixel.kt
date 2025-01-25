/**
 * Represents a pixel with a color and depth.
 *
 * @property color The color of the pixel.
 * @property depth The depth of the pixel, initialized to negative infinity.
 */
package br.ufrpe.computacaografica.beans

import javafx.scene.paint.Color

class Pixel (var color: Color) {
    var depth: Double = Double.NEGATIVE_INFINITY
}