package br.ufrpe.computacaografica.control

import br.ufrpe.computacaografica.beans.*
import br.ufrpe.computacaografica.beans.Vector
import io.github.palexdev.materialfx.controls.MFXSlider
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.WritableImage
import javafx.scene.layout.AnchorPane
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
    private lateinit var nValue: TextField

    @FXML
    private lateinit var vValue: TextField 

    @FXML
    private lateinit var dValue: TextField

    @FXML
    private lateinit var hxValue: TextField

    @FXML
    private lateinit var hyValue: TextField

    @FXML
    private lateinit var cValue: TextField

    @FXML
    private lateinit var Iamb: TextField

    @FXML
    private lateinit var Ka: TextField

    @FXML
    private lateinit var iL: TextField

    @FXML
    private lateinit var Pl: TextField

    @FXML
    private lateinit var Kd: TextField

    @FXML
    private lateinit var Od: TextField

    @FXML
    private lateinit var Ks: TextField

    @FXML
    private lateinit var eta: TextField

    @FXML
    private lateinit var calcular: Button

    @FXML
    private lateinit var alerta: Label

    @FXML
    private lateinit var janela: AnchorPane

    @FXML
    private lateinit var desenho: Canvas

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
    private lateinit var sliderIlB: MFXSlider

    @FXML
    private lateinit var sliderIlR: MFXSlider

    @FXML
    private lateinit var sliderIlG: MFXSlider

    @FXML
    private lateinit var sliderIambB: MFXSlider
    @FXML
    private lateinit var sliderKa: MFXSlider


    private lateinit var p: Array<Point>
    private lateinit var t: Array<Triangle>
    private lateinit var tela: Array<Array<Pixel>>
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

    private  fun readCameraEIluminacao() {
        var reader = BufferedReader(FileReader("camera.txt"))

        var linha = reader.readLine()
        var qnt = linha.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var a = qnt[0].toDouble()
        var b = qnt[1].toDouble()
        var c = qnt[2].toDouble()

        sliderNX.value = a
        sliderNY.value = b
        sliderNZ.value = c

        linha = reader.readLine()
        qnt = linha.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        a = qnt[0].toDouble()
        b = qnt[1].toDouble()
        c = qnt[2].toDouble()
        this.sliderVX.value = a
        this.sliderVY.value = b
        this.sliderVZ.value = c

        sliderD.value =reader.readLine().toDouble()

        sliderHX.value = reader.readLine().toDouble()

        sliderHY.value = reader.readLine().toDouble()


        linha = reader.readLine()
        qnt = linha.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        a = qnt[0].toDouble()
        b = qnt[1].toDouble()
        c = qnt[2].toDouble()
        this.sliderCameraX.value = a
        this.sliderCameraY.value = b
        this.sliderCameraZ.value = c

        reader.close()
        reader = BufferedReader(FileReader("iluminacao.txt"))
        linha = reader.readLine()
        qnt = linha.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        a = qnt[0].toDouble()
        b = qnt[1].toDouble()
        c = qnt[2].toDouble()
        this.sliderIambR.value = a
        this.sliderIambG.value = b
        this.sliderIambB.value = c

        sliderKa.value = reader.readLine().toDouble()

        linha = reader.readLine()
        qnt = linha.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        a = qnt[0].toDouble()
        b = qnt[1].toDouble()
        c = qnt[2].toDouble()
        this.sliderIlR.value = a
        this.sliderIlG.value = b
        this.sliderIlB.value = c

        Pl.text = reader.readLine()
        Kd.text = reader.readLine()
        Od.text = reader.readLine()
        Ks.text = reader.readLine()
        eta.text = reader.readLine()
        reader.close()
    }


    private fun readData() {
        this.N = Vector(arrayOf(sliderNX.value, sliderNY.value, sliderNZ.value))
        this.C = Point(arrayOf(sliderCameraX.value, sliderCameraY.value, sliderCameraZ.value))

        this.d = sliderD.value

        this.hx = sliderHX.value
        this.hy = sliderHY.value



        this.V = Vector(arrayOf(sliderVX.value, sliderVY.value, sliderVZ.value))



        this.etaValue = eta.text.toInt().toDouble()
        this.KaValue = sliderKa.value
        this.KsValue = Ks.text.toDouble()

        //var linha = Iamb.text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.IambValue = Vector(arrayOf(sliderIambR.value, sliderIambG.value, sliderIambB.value))


        var linha = Kd.text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.KdValue = Vector(arrayOf( linha[0].toDouble(),linha[1].toDouble(), linha[2].toDouble()))

        linha = Od.text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.OdValue = Vector(arrayOf( linha[0].toDouble(),linha[1].toDouble(), linha[2].toDouble()))

        linha = Pl.text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.PlValue = Vector(arrayOf( linha[0].toDouble(),linha[1].toDouble(), linha[2].toDouble()))

        this.IlValue = Vector(arrayOf(sliderIlR.value, sliderIlG.value, sliderIlB.value))
    }

    private fun iniciarTela() {
        this.tela = Array(height) { Array(width) { Pixel(Color.BLACK) }}
        this.gc.fill = Color.BLACK
        this.gc.fillRect(0.0, 0.0, width.toDouble(), height.toDouble())
    }

    override  fun initialize(arg0: URL?, arg1: ResourceBundle?) {
        fun updateFrame(){
            if(job?.isActive == true) job!!.cancel(CancellationException())
            runBlocking {
                job = launch {
                    this@Window.mudarPerspectiva(null)

                }
            }
            println("C.x ${sliderCameraX.value}")
        }
        try {
            writableImage = WritableImage(width, height)
            this.readCameraEIluminacao()
            this.gc = desenho.graphicsContext2D
            this.iniciarTela()
            this.getPontosArquivo()
            this.mudarPerspectiva(null)
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
        } catch (var4: IOException) {
            var4.printStackTrace()
        }
    }

    @FXML
    @Throws(IOException::class)
     fun mudarPerspectiva(event: ActionEvent?) {
        this.readData()
        this.ortogonizarV()
        this.getPontosArquivo()
        this.setReferenciasOriginaisDosTriangulos()
        this.atualizarCoordVista()
        this.normalTriangulo()
        this.normalVertice()
        this.pintarTela()
        //this.salvarPontosArquivo()
        //readCameraEIluminacao()
    }
    private  fun ortogonizarV() {
        val orto = V.multEscalar(N) / N.multEscalar(N)
        val n = N.v
        val vaux = arrayOf(orto * n[0], orto * n[1], orto * n[2])
        V.v[0] -= vaux[0]
        V.v[1] -= vaux[1]
        V.v[2] -= vaux[2]
        this.N = N.normalizarVetor()
        this.V = V.normalizarVetor()
        this.U = N.produtoVetorial(V)
    }

    @Throws(IOException::class)
    private fun salvarPontosArquivo() {
        var writer = BufferedWriter(FileWriter("camera.txt"))
        writer.write(nValue.text)
        writer.newLine()
        writer.write(vValue.text)
        writer.newLine()
        writer.write(dValue.text)
        writer.newLine()
        writer.write(hxValue.text)
        writer.newLine()
        writer.write(hyValue.text)
        writer.newLine()
        writer.write(cValue.text)
        writer.newLine()
        writer.flush()
        writer.close()
        writer = BufferedWriter(FileWriter("iluminacao.txt"))
        writer.write(Iamb.text)
        writer.newLine()
        writer.write(Ka.text)
        writer.newLine()
        writer.write(iL.text)
        writer.newLine()
        writer.write(Pl.text)
        writer.newLine()
        writer.write(Kd.text)
        writer.newLine()
        writer.write(Od.text)
        writer.newLine()
        writer.write(Ks.text)
        writer.newLine()
        writer.write(eta.text)
        writer.newLine()
        writer.flush()
        writer.close()
    }

    var el: Int = 0
    private  fun pintarTela() {
        var cont = 0
        writableImage = WritableImage(width, height)
        val pixelWriter = writableImage.pixelWriter
        t.forEach { triangle ->
            val pontoA = this.projetaPontoNaTela(triangle.p[0])
            val pontoB = this.projetaPontoNaTela(triangle.p[1])
            val pontoC = this.projetaPontoNaTela(triangle.p[2])
            triangle.tela = arrayOf(Point(pontoA), Point(pontoB), Point(pontoC))
            cont++
            println(cont)
        }
        cont = 0
        println("iniciando tela")
        iniciarTela()
        println("tela finaliazada")
        println("iniciando rasterização")
        t.forEach { triangle ->
            scanLine(triangle)
            cont++
            println(cont)
        }
        println("finalizando rasterizaçao")
        for (i in tela.indices){
            for (j in tela[i].indices){
                    pixelWriter.setColor(i, j, tela[i][j].color)
            }
        }

        gc.drawImage(writableImage, 0.0, 0.0)
        println("Canvas atualizado com writable image")
        el = el+50
    }

    private  fun scanLine(t: Triangle) {
        var ymin: Double = t.tela[0].p[1].coerceAtMost(t.tela[1].p[1])
        ymin = ymin.coerceAtMost(t.tela[2].p[1])
        lateinit var inicio: Point
        lateinit var meio: Point
        lateinit var fim: Point
        if (ymin == t.tela[0].p[1]) {
            inicio = t.tela[0]
            if (t.tela[1].p[1] <= t.tela[2].p[1]) {
                meio = t.tela[1]
                fim = t.tela[2]
            } else {
                meio = t.tela[2]
                fim = t.tela[1]
            }
        } else if (ymin == t.tela[1].p[1]) {
            inicio = t.tela[1]
            if (t.tela[0].p[1] <= t.tela[2].p[1]) {
                meio = t.tela[0]
                fim = t.tela[2]
            } else {
                meio = t.tela[2]
                fim = t.tela[0]
            }
        } else {
            inicio = t.tela[2]
            if (t.tela[1].p[1] <= t.tela[0].p[1]) {
                meio = t.tela[1]
                fim = t.tela[0]
            } else {
                meio = t.tela[0]
                fim = t.tela[1]
            }
        }
        this.pintarTopDown(inicio, meio, fim, t)
        this.pintarDownTop(inicio, meio, fim, t)
    }

    private  fun calcularAlphaBeta(inicio: Point, meio: Point, fim: Point): Pair<Double, Double> {
        val alpha: Double
        val beta: Double
        if (meio.p[0] < inicio.p[0]) {
            alpha = (meio.p[0] - fim.p[0]) / (meio.p[1] - fim.p[1])
            beta = (inicio.p[0] - fim.p[0]) / (inicio.p[1] - fim.p[1])
        } else {
            alpha = (inicio.p[0] - fim.p[0]) / (inicio.p[1] - fim.p[1])
            beta = (meio.p[0] - fim.p[0]) / (meio.p[1] - fim.p[1])
        }
        return Pair(alpha, beta)
    }

    private  fun pintarDownTop(inicio: Point, meio: Point, fim: Point, t: Triangle) {
         fun paint(alpha: Double, beta: Double){
            var xmax: Double = fim.p[0]
            var xmin: Double = fim.p[0]

            val min = fim.p[1].toInt()
            val max = meio.p[1].toInt()
            for (y in min downTo max) {
                val ini = Math.round(xmin).toInt()
                val end = Math.round(xmax).toInt()
                runBlocking { repeat(end-ini+1){ i->
                    launch {
                        val x = ini+i
                        calcularCor(x.toDouble(), y.toDouble(), t)
                    }
                } }
                xmin -= alpha
                xmax -= beta
            }
        }
        val (alpha: Double, beta: Double) = calcularAlphaBeta(inicio, meio, fim)
        paint(alpha, beta)
        paint(beta, alpha)
    }

    private fun pintarTopDown(inicio: Point, meio: Point, fim: Point, t: Triangle) {
         fun pintar (alpha: Double, beta: Double){
            var xmax: Double = inicio.p[0]
            var xmin: Double = inicio.p[0]

             val min = inicio.p[1].toInt()
             val max = meio.p[1].toInt()

            for (y in min..max) {
                val ini = xmin.toInt()
                val end = xmax.toInt()
                runBlocking { repeat(end-ini+1){ i->
                    launch {
                        val x = ini+i
                        calcularCor(x.toDouble(), y.toDouble(), t)
                    }
                } }
                for(x in ini..end){
                    this.calcularCor(x.toDouble(), y.toDouble(), t)
                }

                xmin += alpha
                xmax += beta
            }
        }
        val (alpha,beta) = calcularAlphaBeta(fim, meio, inicio)
        pintar(alpha, beta)
        pintar(beta, alpha)
    }

    private fun calcularCor(x: Double, y: Double, t: Triangle) {
        t.p[0].normal = t.original[0].normal
        t.p[1].normal = t.original[1].normal
        t.p[2].normal = t.original[2].normal
        val q: Array<Double> = Point.coordenadaBaricentrica(t.tela[0], t.tela[1], t.tela[2], Point(arrayOf(x, y)))
        val p: Point =Point.calcularPontoCoordenadaBaricentrica(t.p[0], t.p[1], t.p[2], q[0], q[1], 1.0 - q[0] - q[1])
        val origem = Point(arrayOf(0.0, 0.0, 0.0))
        val n1 = Vector(this.produtoPorEscalar(t.p[0].normal, q[0]))
        val n2 = Vector(this.produtoPorEscalar(t.p[1].normal, q[1]))
        val n3 = Vector(this.produtoPorEscalar(t.p[2].normal, 1.0 - q[0] - q[1]))
        p.normal = n1.mais(n2).mais(n3).normalizarVetor().v
        val N = Vector(p.normal)
        val V = Vector(origem.subtrai(p).normalizarVetor().v)
        val L = Vector(PlValue.menos(Vector(p.p)).normalizarVetor().v)
        val R = Vector(calcularR(N, L).normalizarVetor().v)
        lateinit var Id: Array<Double>
        lateinit var Is: Array<Double>
        val Ia = this.produtoPorEscalar(IambValue.v, this.KaValue)

        if (N.multEscalar(L) < 0.0) {
            if (N.multEscalar(V) < 0.0) {
                N.v = arrayOf(-N.v[0], -N.v[1],-N.v[2])
                Is = IlValue.multPorEscalar(this.KsValue * (R).multEscalar(V).pow(this.etaValue)).v
                Id = KdValue.multInterno(IlValue.multInterno(OdValue.multPorEscalar(N.multEscalar(L)))).v
            } else {
                Is = origem.p
                Id = origem.p
            }
        }else{
            Is = IlValue.multPorEscalar(this.KsValue * (R).multEscalar(V).pow(this.etaValue)).v
            Id = KdValue.multInterno(IlValue.multInterno(OdValue.multPorEscalar(N.multEscalar(L)))).v
        }

        if (R.multEscalar(V) < 0.0) {
            Is = origem.p
        }

        val I = (Vector(Ia)).mais(Vector(Id)).mais(Vector(Is))

        I.v = arrayOf(
            min(max(I.v[0], 0.0), 255.0),
            min(max(I.v[1], 0.0), 255.0),
            min(max(I.v[2], 0.0), 255.0)
        )

        p.p[2] = -p.p[2]
        this.organizarZ(x.toInt(), y.toInt(), p.p[2], Color.rgb(I.v[0].toInt(), I.v[1].toInt(), I.v[2].toInt()))
    }

    private fun organizarZ(i: Int, j: Int, profundidadeAtual: Double, cor: Color) {

        if (i in 0 until width && j in 0 until height && tela[i][j].profundidade < profundidadeAtual) {
            tela[i][j].profundidade = profundidadeAtual
            tela[i][j].color = cor
            //pixelWriter.setColor(i, j, cor)
        }
    }

    private fun calcularR(N: Vector, L: Vector): Vector = Vector(N.multPorEscalar(2.0 * N.multEscalar(L)).v.mapIndexed { i, value -> value - L.v[i] }.toTypedArray())

    private fun produtoPorEscalar(vet: Array<Double>, escalar: Double): Array<Double> = arrayOf(vet[0] * escalar, vet[1] * escalar, vet[2] * escalar)

    private fun projetaPontoNaTela(a: Point): Array<Double> {
        val pontoA = arrayOf(
            this.d * a.p[0] / (a.p[2] * this.hx),
            this.d * a.p[1] / (a.p[2] * this.hy)
        )
        pontoA[0] = floor((pontoA[0] + 1.0) / 2.0 * height.toDouble() + 0.5)
        pontoA[1] = floor(width.toDouble() - (pontoA[1] + 1.0) / 2.0 * width.toDouble() + 0.5)
        return pontoA
    }

    private fun normalVertice() {
        for (ponto in this.p) {
            val norm = normalDeUmVertice(ponto).let { normalVertice ->
                Vector(normalVertice).normalizarVetor().let { normalVetor ->
                    normalVetor.v
                }
            }
            ponto.normal = Matrix(norm).matriz[0]
        }
    }

    private fun normalTriangulo() {
        for (triangulo in this.t) {
            val normal = this.normalDoTriangulo(triangulo)
            triangulo.normal = Matrix(normal).matriz[0]
        }
    }

    private fun normalDoTriangulo(triangulo: Triangle): Array<Double> =
        triangulo.p[0].let { p1 ->
            triangulo.p[1].let { p2 ->
                triangulo.p[2].let { p3 ->
                    p2.subtrai(p1).produtoVetorial(p3.subtrai(p1)).normalizarVetor().v
                }
            }
        }

    private fun atualizarCoordVista() {
        for (i in t.indices) {
            this.calcularCoordenadaVista(t[i], this.C)
        }
    }

    private fun calcularCoordenadaVista(triangulo: Triangle, c: Point) {
        triangulo.p = arrayOf(this.coordenadaVista(c, triangulo.p[0]), this.coordenadaVista(c, triangulo.p[1]), this.coordenadaVista(c, triangulo.p[2]))
    }

    private fun coordenadaVista(c: Point, p: Point): Point =
        Matrix(this.BaseOrtonormal()).let { ortonormal ->
            Matrix(p.subtrai(c).v).let { subtracao ->
                Matrix(subtracao.transposta).let { transposta ->
                    Point(ortonormal.mult(transposta).transposta[0])
                }
            }
        }

    private fun BaseOrtonormal(): Array<Array<Double>> {
        val u = U.v
        val v = V.v
        val n = N.v
        return arrayOf(arrayOf(u[0], u[1], u[2]), arrayOf(v[0],v[1], v[2]), arrayOf(n[0], n[1], n[2]))
    }

    private fun setReferenciasOriginaisDosTriangulos() {
        for (i in t.indices) {
            t[i].setOriginais()
        }
    }

    private fun getPontosArquivo(){
        val reader = BufferedReader(FileReader("triangulos.txt"))
        var linha = reader.readLine()
        var qnt = linha.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.p = Array(qnt[0].toInt()) { Point(0) }
        this.t = Array(qnt[1].toInt()) { Triangle(Array(0){Point(0)}) }
        p.forEachIndexed { i, point ->
            linha = reader.readLine()
            qnt = linha.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            p[i] = Point(arrayOf(qnt[0].toDouble(), qnt[1].toDouble(), qnt[2].toDouble()))
        }

        t.forEachIndexed { i, triangle ->
            linha = reader.readLine()
            qnt = linha.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val a = qnt[0].toInt()-1
            val b = qnt[1].toInt()-1
            val c = qnt[2].toInt()-1
            t[i] = Triangle(p[a], p[b], p[c])
            p[a].triangulos.add(t[i])
            p[b].triangulos.add(t[i])
            p[c].triangulos.add(t[i])
        }

        reader.close()
    }

    @FXML
    private fun updateCamera(){
        val x = this.sliderCameraX.value
        val y = this.sliderCameraY.value
        val z = this.sliderCameraZ.value
        this.C = Point(arrayOf(x, y, z))

        //mudarPerspectiva(null)
    }

    companion object {
        private fun normalDeUmVertice(vertice: Point): Array<Double> {
            var normal = Vector(arrayOf(0.0, 0.0, 0.0))

            for (triangulo in vertice.triangulos) {
                normal = normal.mais(Vector(triangulo.normal))
            }

            return normal.v
        }

    }
}