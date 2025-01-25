package br.ufrpe.computacaografica.control

import br.ufrpe.computacaografica.beans.*
import br.ufrpe.computacaografica.beans.Vector
import io.github.palexdev.materialfx.controls.MFXSlider
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.*
import java.net.URL
import java.util.*
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class Window : Initializable {
    @FXML
    private lateinit var canvas: Canvas

    @FXML
    private lateinit var sliderCameraX: MFXSlider

    @FXML
    private lateinit var sliderCameraY: MFXSlider

    @FXML
    private lateinit var sliderCameraZ: MFXSlider

    @FXML
    private lateinit var sliderD: MFXSlider

    @FXML
    private lateinit var sliderHX: MFXSlider

    @FXML
    private lateinit var sliderHY: MFXSlider

    @FXML
    private lateinit var sliderNX: MFXSlider

    @FXML
    private lateinit var sliderNY: MFXSlider

    @FXML
    private lateinit var sliderNZ: MFXSlider

    @FXML
    private lateinit var sliderVX: MFXSlider

    @FXML
    private lateinit var sliderVY: MFXSlider

    @FXML
    private lateinit var sliderVZ: MFXSlider

    @FXML
    private lateinit var sliderIambR: MFXSlider

    @FXML
    private lateinit var sliderIambG: MFXSlider

    @FXML
    private lateinit var sliderIambB: MFXSlider

    @FXML
    private lateinit var sliderIlB: MFXSlider

    @FXML
    private lateinit var sliderIlR: MFXSlider

    @FXML
    private lateinit var sliderIlG: MFXSlider

    @FXML
    private lateinit var sliderPlX: MFXSlider

    @FXML
    private lateinit var sliderPlY: MFXSlider

    @FXML
    private lateinit var sliderPlZ: MFXSlider

    @FXML
    private lateinit var sliderKa: MFXSlider

    @FXML
    private lateinit var sliderKdR: MFXSlider

    @FXML
    private lateinit var sliderKdG: MFXSlider

    @FXML
    private lateinit var sliderKdB: MFXSlider

    @FXML
    private lateinit var sliderEta: MFXSlider

    @FXML
    private lateinit var sliderOdR: MFXSlider

    @FXML
    private lateinit var sliderOdG: MFXSlider

    @FXML
    private lateinit var sliderOdB: MFXSlider

    @FXML
    private lateinit var sliderKs: MFXSlider


    private lateinit var points: Array<Point>
    private lateinit var triangles: Array<Triangle>
    private lateinit var screen: Array<Array<Pixel>>
    private lateinit var U: Vector
    private lateinit var V: Vector
    private lateinit var N: Vector
    private var d = 0.0
    private var hx = 0.0
    private var hy = 0.0
    private lateinit var C: Point
    private var KaValue = 0.0
    private var KsValue = 0.0
    private var etaValue = 0.0
    private lateinit var IambValue: Vector
    private lateinit var IlValue: Vector
    private lateinit var PlValue: Vector
    private lateinit var KdValue: Vector
    private lateinit var OdValue: Vector

    private lateinit var gc: GraphicsContext
    private lateinit var writableImage: WritableImage

    private var job: Job? = null

    private val height = 800
    private val width = 800

    private  fun readCameraAndLight() {
        var reader = BufferedReader(FileReader("camera.txt"))

        var line = reader.readLine()
        var qnt = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var a = qnt[0].toDouble()
        var b = qnt[1].toDouble()
        var c = qnt[2].toDouble()

        sliderNX.value = a
        sliderNY.value = b
        sliderNZ.value = c

        line = reader.readLine()
        qnt = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        a = qnt[0].toDouble()
        b = qnt[1].toDouble()
        c = qnt[2].toDouble()
        this.sliderVX.value = a
        this.sliderVY.value = b
        this.sliderVZ.value = c

        sliderD.value =reader.readLine().toDouble()

        sliderHX.value = reader.readLine().toDouble()

        sliderHY.value = reader.readLine().toDouble()


        line = reader.readLine()
        qnt = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        a = qnt[0].toDouble()
        b = qnt[1].toDouble()
        c = qnt[2].toDouble()
        this.sliderCameraX.value = a
        this.sliderCameraY.value = b
        this.sliderCameraZ.value = c

        reader.close()
        reader = BufferedReader(FileReader("iluminacao.txt"))
        line = reader.readLine()
        qnt = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        a = qnt[0].toDouble()
        b = qnt[1].toDouble()
        c = qnt[2].toDouble()
        this.sliderIambR.value = a
        this.sliderIambG.value = b
        this.sliderIambB.value = c


        sliderKa.value = reader.readLine().toDouble()

        line = reader.readLine()
        qnt = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        a = qnt[0].toDouble()
        b = qnt[1].toDouble()
        c = qnt[2].toDouble()
        this.sliderIlR.value = a
        this.sliderIlG.value = b
        this.sliderIlB.value = c

        line = reader.readLine()
        qnt = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        a = qnt[0].toDouble()
        b = qnt[1].toDouble()
        c = qnt[2].toDouble()
        this.sliderPlX.value = a
        this.sliderPlZ.value = b
        this.sliderPlY.value = c

        line = reader.readLine()
        qnt = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        a = qnt[0].toDouble()
        b = qnt[1].toDouble()
        c = qnt[2].toDouble()
        this.sliderKdR.value = a
        this.sliderKdG.value = b
        this.sliderKdB.value = c

        line = reader.readLine()
        qnt = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        a = qnt[0].toDouble()
        b = qnt[1].toDouble()
        c = qnt[2].toDouble()
        this.sliderOdR.value = a
        this.sliderOdG.value = b
        this.sliderOdB.value = c

        sliderKs.value = reader.readLine().toDouble()
        sliderEta.value = reader.readLine().toDouble()
        reader.close()
    }


    private fun readData() {
        this.N = Vector(arrayOf(sliderNX.value, sliderNY.value, sliderNZ.value))
        this.C = Point(arrayOf(sliderCameraX.value, sliderCameraY.value, sliderCameraZ.value))

        this.d = sliderD.value

        this.hx = sliderHX.value
        this.hy = sliderHY.value

        this.V = Vector(arrayOf(sliderVX.value, sliderVY.value, sliderVZ.value))

        this.etaValue = sliderEta.value
        this.KaValue = sliderKa.value
        this.KsValue = sliderKs.value

        this.IambValue = Vector(arrayOf(sliderIambR.value, sliderIambG.value, sliderIambB.value))


        this.KdValue = Vector(arrayOf(sliderKdR.value, sliderKdG.value, sliderKdB.value))

        this.OdValue = Vector(arrayOf(sliderOdR.value, sliderOdG.value, sliderOdB.value))

        this.PlValue = Vector(arrayOf(sliderPlX.value, sliderPlZ.value, sliderPlY.value))

        this.IlValue = Vector(arrayOf(sliderIlR.value, sliderIlG.value, sliderIlB.value))
    }

    private fun startScreen() {
        this.screen = Array(height) { Array(width) { Pixel(Color.BLACK) }}
        this.gc.fill = Color.BLACK
        this.gc.fillRect(0.0, 0.0, width.toDouble(), height.toDouble())
    }

    override fun initialize(arg0: URL?, arg1: ResourceBundle?) {
        fun updateFrame() {
            if (job?.isActive == true) job!!.cancel(CancellationException())
            runBlocking {
                job = launch {
                    this@Window.changePerspective()

                }
            }
        }
        try {
            writableImage = WritableImage(width, height)
            this.readCameraAndLight()
            this.gc = canvas.graphicsContext2D
            this.startScreen()
            this.getPointsInArchive()
            this.changePerspective()
            this.sliderCameraX.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderCameraY.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderCameraZ.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderD.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderHX.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderHY.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderNX.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderNY.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderNZ.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderVX.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderVY.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderVZ.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderIambR.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderIambG.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderIambB.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderKa.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderIlR.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderIlG.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderIlB.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderPlX.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderPlZ.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderPlY.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderKdR.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderKdG.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderKdB.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderEta.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderOdR.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderOdG.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderOdB.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
            this.sliderKs.valueProperty().addListener { _, _, newValue ->
                updateFrame()
            }
        } catch (var4: IOException) {
            var4.printStackTrace()
        }
    }

    @FXML
    @Throws(IOException::class)
     fun changePerspective() {
        this.readData()
        this.orthogonizeV()
        this.getPointsInArchive()
        this.setTrianglesOriginalReferences()
        this.updateScreenCoord()
        this.normalTriangles()
        this.normalPoints()
        this.changeScreen()
    }
    private fun orthogonizeV() {
        val ortho = V.scalarMultiplication(N) / N.scalarMultiplication(N)
        val n = N.v
        val vaux = arrayOf(ortho * n[0], ortho * n[1], ortho * n[2])
        V.v[0] -= vaux[0]
        V.v[1] -= vaux[1]
        V.v[2] -= vaux[2]
        this.N = N.normalizeVector()
        this.V = V.normalizeVector()
        this.U = N.crossProduct(V)
    }

    private fun changeScreen() {
        val pixelWriter = writableImage.pixelWriter
        triangles.forEach { triangle ->
            val pointA = this.projectPointOnScreen(triangle.points[0])
            val pointB = this.projectPointOnScreen(triangle.points[1])
            val pointC = this.projectPointOnScreen(triangle.points[2])
            triangle.screen = arrayOf(Point(pointA), Point(pointB), Point(pointC))
        }

        startScreen()

        triangles.forEach { triangle ->
            scanLine(triangle)
        }

        for (i in screen.indices){
            for (j in screen[i].indices){
                    pixelWriter.setColor(i, j, screen[i][j].color)
            }
        }

        gc.drawImage(writableImage, 0.0, 0.0)
    }

    private  fun scanLine(t: Triangle) {
        var ymin: Double = t.screen[0].points[1].coerceAtMost(t.screen[1].points[1])
        ymin = ymin.coerceAtMost(t.screen[2].points[1])
        lateinit var start: Point
        lateinit var middle: Point
        lateinit var finish: Point
        if (ymin == t.screen[0].points[1]) {
            start = t.screen[0]
            if (t.screen[1].points[1] <= t.screen[2].points[1]) {
                middle = t.screen[1]
                finish = t.screen[2]
            } else {
                middle = t.screen[2]
                finish = t.screen[1]
            }
        } else if (ymin == t.screen[1].points[1]) {
            start = t.screen[1]
            if (t.screen[0].points[1] <= t.screen[2].points[1]) {
                middle = t.screen[0]
                finish = t.screen[2]
            } else {
                middle = t.screen[2]
                finish = t.screen[0]
            }
        } else {
            start = t.screen[2]
            if (t.screen[1].points[1] <= t.screen[0].points[1]) {
                middle = t.screen[1]
                finish = t.screen[0]
            } else {
                middle = t.screen[0]
                finish = t.screen[1]
            }
        }
        this.paintTopDown(start, middle, finish, t)
        this.paintDownTop(start, middle, finish, t)
    }

    private  fun calculateAlphaBeta(start: Point, middle: Point, finis: Point): Pair<Double, Double> {
        val alpha: Double
        val beta: Double
        if (middle.points[0] < start.points[0]) {
            alpha = (middle.points[0] - finis.points[0]) / (middle.points[1] - finis.points[1])
            beta = (start.points[0] - finis.points[0]) / (start.points[1] - finis.points[1])
        } else {
            alpha = (start.points[0] - finis.points[0]) / (start.points[1] - finis.points[1])
            beta = (middle.points[0] - finis.points[0]) / (middle.points[1] - finis.points[1])
        }
        return Pair(alpha, beta)
    }

    private  fun paintDownTop(start: Point, middle: Point, finish: Point, triangle: Triangle) {
         fun paint(alpha: Double, beta: Double){
            var xmax: Double = finish.points[0]
            var xmin: Double = finish.points[0]

            val min = finish.points[1].toInt()
            val max = middle.points[1].toInt()
            for (y in min downTo max) {
                val ini = Math.round(xmin).toInt()
                val end = Math.round(xmax).toInt()
                runBlocking { repeat(end-ini+1){ i->
                    launch {
                        val x = ini+i
                        calculateColor(x.toDouble(), y.toDouble(), triangle)
                    }
                } }
                xmin -= alpha
                xmax -= beta
            }
        }
        val (alpha: Double, beta: Double) = calculateAlphaBeta(start, middle, finish)
        paint(alpha, beta)
        paint(beta, alpha)
    }

    private fun paintTopDown(start: Point, middle: Point, finish: Point, triangle: Triangle) {
         fun paint (alpha: Double, beta: Double){
            var xmax: Double = start.points[0]
            var xmin: Double = start.points[0]

             val min = start.points[1].toInt()
             val max = middle.points[1].toInt()

            for (y in min..max) {
                val ini = xmin.toInt()
                val end = xmax.toInt()
                runBlocking { repeat(end-ini+1){ i->
                    launch {
                        val x = ini+i
                        calculateColor(x.toDouble(), y.toDouble(), triangle)
                    }
                } }
                for(x in ini..end){
                    this.calculateColor(x.toDouble(), y.toDouble(), triangle)
                }

                xmin += alpha
                xmax += beta
            }
        }
        val (alpha,beta) = calculateAlphaBeta(finish, middle, start)
        paint(alpha, beta)
        paint(beta, alpha)
    }

    private fun calculateColor(x: Double, y: Double, t: Triangle) {
        t.points[0].normal = t.original[0].normal
        t.points[1].normal = t.original[1].normal
        t.points[2].normal = t.original[2].normal
        val q: Array<Double> = Point.barycentricCoordinate(t.screen[0], t.screen[1], t.screen[2], Point(arrayOf(x, y)))
        val p: Point = Point.calculatePointBarycentricCoordinate(t.points[0], t.points[1], t.points[2], q[0], q[1], 1.0 - q[0] - q[1])
        val origem = Point(arrayOf(0.0, 0.0, 0.0))
        val n1 = Vector(t.points[0].normal).multiplyByScalar(q[0])
        val n2 = Vector(t.points[1].normal).multiplyByScalar(q[1])
        val n3 = Vector(t.points[2].normal).multiplyByScalar(1.0 - q[0] - q[1])
        p.normal = n1.add(n2).add(n3).normalizeVector().v
        val N = Vector(p.normal)
        val V = Vector(origem.subtract(p).normalizeVector().v)
        val L = Vector(PlValue.subtract(Vector(p.points)).normalizeVector().v)
        val R = Vector(calculateR(N, L).normalizeVector().v)
        lateinit var Id: Array<Double>
        lateinit var Is: Array<Double>
        val Ia = Vector(IambValue.v).multiplyByScalar(this.KaValue)

        if (N.scalarMultiplication(L) < 0.0) {
            if (N.scalarMultiplication(V) < 0.0) {
                N.v = arrayOf(-N.v[0], -N.v[1],-N.v[2])
                Is = IlValue.multiplyByScalar(this.KsValue * (R).scalarMultiplication(V).pow(this.etaValue)).v
                Id = KdValue.elementWiseMultiplication(IlValue.elementWiseMultiplication(OdValue.multiplyByScalar(N.scalarMultiplication(L)))).v
            } else {
                Is = origem.points
                Id = origem.points
            }
        }else{
            Is = IlValue.multiplyByScalar(this.KsValue * (R).scalarMultiplication(V).pow(this.etaValue)).v
            Id = KdValue.elementWiseMultiplication(IlValue.elementWiseMultiplication(OdValue.multiplyByScalar(N.scalarMultiplication(L)))).v
        }

        if (R.scalarMultiplication(V) < 0.0) {
            Is = origem.points
        }

        val I = Ia.add(Vector(Id)).add(Vector(Is))

        I.v = arrayOf(
            min(max(I.v[0], 0.0), 255.0),
            min(max(I.v[1], 0.0), 255.0),
            min(max(I.v[2], 0.0), 255.0)
        )

        p.points[2] = -p.points[2]
        this.organizeZ(x.toInt(), y.toInt(), p.points[2], Color.rgb(I.v[0].toInt(), I.v[1].toInt(), I.v[2].toInt()))
    }

    private fun organizeZ(i: Int, j: Int, actualDepth: Double, color: Color) {

        if (i in 0 until width && j in 0 until height && screen[i][j].depth < actualDepth) {
            screen[i][j].depth = actualDepth
            screen[i][j].color = color
            //pixelWriter.setColor(i, j, cor)
        }
    }

    private fun calculateR(N: Vector, L: Vector): Vector = Vector(N.multiplyByScalar(2.0 * N.scalarMultiplication(L)).v.mapIndexed { i, value -> value - L.v[i] }.toTypedArray())

    private fun projectPointOnScreen(a: Point): Array<Double> {
        val pointA = arrayOf(
            this.d * a.points[0] / (a.points[2] * this.hx),
            this.d * a.points[1] / (a.points[2] * this.hy)
        )
        pointA[0] = floor((pointA[0] + 1.0) / 2.0 * height.toDouble() + 0.5)
        pointA[1] = floor(width.toDouble() - (pointA[1] + 1.0) / 2.0 * width.toDouble() + 0.5)
        return pointA
    }

    private fun normalPoints() {
        for (point in this.points) {
            val norm = pointsNormal(point).let { normalVertice ->
                Vector(normalVertice).normalizeVector().v
            }
            point.normal = Matrix(norm).matrix[0]
        }
    }

    private fun normalTriangles() {
        for (triangle in this.triangles) {
            val normal = this.normalOfATriangle(triangle)
            triangle.normal = Matrix(normal).matrix[0]
        }
    }

    private fun normalOfATriangle(triangle: Triangle): Array<Double> =
        triangle.points[0].let { p1 ->
            triangle.points[1].let { p2 ->
                triangle.points[2].let { p3 ->
                    p2.subtract(p1).crossProduct(p3.subtract(p1)).normalizeVector().v
                }
            }
        }

    private fun updateScreenCoord() {
        for (i in triangles.indices) {
            this.calculateScreenCoord(triangles[i], this.C)
        }
    }

    private fun calculateScreenCoord(triangle: Triangle, c: Point) {
        triangle.points = arrayOf(this.screenCord(c, triangle.points[0]), this.screenCord(c, triangle.points[1]), this.screenCord(c, triangle.points[2]))
    }

    private fun screenCord(c: Point, p: Point): Point =
        Matrix(this.orthogonalBasis()).let { ortonormal ->
            Matrix(p.subtract(c).v).let { subtract ->
                Matrix(subtract.transpose).let { transpose ->
                    Point(ortonormal.multiply(transpose).transpose[0])
                }
            }
        }

    private fun orthogonalBasis(): Array<Array<Double>> {
        val u = U.v
        val v = V.v
        val n = N.v
        return arrayOf(arrayOf(u[0], u[1], u[2]), arrayOf(v[0],v[1], v[2]), arrayOf(n[0], n[1], n[2]))
    }

    private fun setTrianglesOriginalReferences() {
        for (i in triangles.indices) {
            triangles[i].setOriginals()
        }
    }

    private fun getPointsInArchive(){
        val reader = BufferedReader(FileReader("triangulos.txt"))
        var line = reader.readLine()
        var qnt = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.points = Array(qnt[0].toInt()) { Point(0) }
        this.triangles = Array(qnt[1].toInt()) { Triangle(Array(0){Point(0)}) }
        points.forEachIndexed { i, point ->
            line = reader.readLine()
            qnt = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            points[i] = Point(arrayOf(qnt[0].toDouble(), qnt[1].toDouble(), qnt[2].toDouble()))
        }

        triangles.forEachIndexed { i, triangle ->
            line = reader.readLine()
            qnt = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val a = qnt[0].toInt()-1
            val b = qnt[1].toInt()-1
            val c = qnt[2].toInt()-1
            triangles[i] = Triangle(points[a], points[b], points[c])
            points[a].triangles.add(triangles[i])
            points[b].triangles.add(triangles[i])
            points[c].triangles.add(triangles[i])
        }

        reader.close()
    }

    companion object {
        private fun pointsNormal(point: Point): Array<Double> {
            var normal = Vector(arrayOf(0.0, 0.0, 0.0))

            for (triangle in point.triangles) {
                normal = normal.add(Vector(triangle.normal))
            }

            return normal.v
        }

    }
}