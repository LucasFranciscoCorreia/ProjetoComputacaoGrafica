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

/**
 * The `Window` class represents a graphical window in a computer graphics application.
 * It implements the `Initializable` interface to initialize the window and its components.
 *
 * The class contains various properties and methods to manage the graphical user interface (GUI),
 * including sliders for adjusting camera and lighting settings, canvas for drawing, and methods
 * for reading data from files, updating the screen, and changing the perspective.
 *
 * Properties:
 * - `canvas`: The canvas element in the FXML file, injected by the FXMLLoader.
 * - `sliderCameraX`, `sliderCameraY`, `sliderCameraZ`: Sliders for adjusting the camera's position.
 * - `sliderD`, `sliderHX`, `sliderHY`: Sliders for adjusting the camera's distance and horizontal coordinates.
 * - `sliderNX`, `sliderNY`, `sliderNZ`: Sliders for adjusting the normal vector components.
 * - `sliderVX`, `sliderVY`, `sliderVZ`: Sliders for adjusting the view vector components.
 * - `sliderIambR`, `sliderIambG`, `sliderIambB`: Sliders for adjusting the ambient light intensity components.
 * - `sliderIlR`, `sliderIlG`, `sliderIlB`: Sliders for adjusting the light intensity components.
 * - `sliderPlX`, `sliderPlY`, `sliderPlZ`: Sliders for adjusting the light position components.
 * - `sliderKa`, `sliderKdR`, `sliderKdG`, `sliderKdB`: Sliders for adjusting the reflection coefficients.
 * - `sliderEta`, `sliderOdR`, `sliderOdG`, `sliderOdB`, `sliderKs`: Sliders for adjusting various lighting parameters.
 * - `points`: An array of `Point` objects representing the points in the window.
 * - `triangles`: An array of `Triangle` objects representing the triangles to be rendered.
 * - `screen`: A 2D array of `Pixel` objects representing the screen.
 * - `U`, `V`, `N`: Vectors representing various properties or directions in the application.
 * - `d`, `hx`, `hy`: Double values representing various parameters.
 * - `C`: A point representing a coordinate in the window.
 * - `KaValue`, `KsValue`, `etaValue`: Double values representing various lighting parameters.
 * - `IambValue`, `IlValue`, `PlValue`, `KdValue`, `OdValue`: Vectors representing various lighting parameters.
 * - `gc`: A `GraphicsContext` instance used for drawing operations.
 * - `writableImage`: A `WritableImage` instance used for drawing or manipulating pixel data.
 * - `job`: A nullable `Job` instance representing a cancellable coroutine.
 * - `height`, `width`: Integers representing the height and width of the window in pixels.
 *
 * Methods:
 * - `readCameraAndLight()`: Reads camera and light settings from files and updates the corresponding sliders.
 * - `readData()`: Reads data from various sliders and initializes the corresponding properties.
 * - `startScreen()`: Initializes the screen by creating a 2D array of Pixels and fills the screen with black color.
 * - `initialize(arg0: URL?, arg1: ResourceBundle?)`: Initializes the window and sets up the necessary components and event listeners.
 * - `changePerspective()`: Changes the perspective of the graphical window by performing a series of operations.
 * - `orthogonizeV()`: Orthogonizes the vector V with respect to the vector N.
 * - `changeScreen()`: Changes the screen by projecting points of triangles onto the screen and updating the pixel colors.
 * - `scanLine(t: Triangle)`: Performs the scanline algorithm on a given triangle.
 * - `calculateAlphaBeta(start: Point, middle: Point, finis: Point)`: Calculates the alpha and beta values based on the given points.
 * - `paintDownTop(start: Point, middle: Point, finish: Point, triangle: Triangle)`: Paints a triangle from the bottom to the top.
 * - `paintTopDown(start: Point, middle: Point, finish: Point, triangle: Triangle)`: Paints the top-down portion of a triangle.
 * - `calculateColor(x: Double, y: Double, t: Triangle)`: Calculates the color at a given point within a triangle using barycentric coordinates and lighting models.
 * - `organizeZ(i: Int, j: Int, actualDepth: Double, color: Color)`: Updates the depth and color of a pixel on the screen if the given depth is greater than the current depth.
 * - `calculateR(N: Vector, L: Vector)`: Calculates the reflection vector R given the normal vector N and the light vector L.
 * - `projectPointOnScreen(a: Point)`: Projects a 3D point onto the 2D screen.
 * - `normalPoints()`: Normalizes the points by calculating their normal vectors.
 * - `normalTriangles()`: Calculates and assigns the normal vector for each triangle.
 * - `normalOfATriangle(triangle: Triangle)`: Calculates the normal vector of a given triangle.
 * - `updateScreenCoord()`: Updates the screen coordinates for all triangles.
 * - `calculateScreenCoord(triangle: Triangle, c: Point)`: Calculates the screen coordinates for the given triangle's points.
 * - `screenCord(c: Point, p: Point)`: Transforms a point from world coordinates to screen coordinates.
 * - `orthogonalBasis()`: Generates an orthogonal basis matrix from the vectors U, V, and N.
 * - `setTrianglesOriginalReferences()`: Sets the original references for each triangle.
 * - `getPointsInArchive()`: Reads points and triangles from a file and initializes the `points` and `triangles` arrays.
 *
 * Companion Object:
 * - `pointsNormal(point: Point)`: Calculates the normal vector for a given point by averaging the normals of the triangles that share this point.
 */

class Window : Initializable {
    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * The canvas element in the FXML file.
     */
    @FXML
    private lateinit var canvas: Canvas

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents a slider control for adjusting the camera's X-axis position.
     */
    @FXML
    private lateinit var sliderCameraX: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents a slider control for adjusting the camera's Y-axis position.
     */
    @FXML
    private lateinit var sliderCameraY: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents a slider control for adjusting the camera's Z-axis position.
     */
    @FXML
    private lateinit var sliderCameraZ: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents a slider control for adjusting the camera's Z-axis position.
     */
    @FXML
    private lateinit var sliderD: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the horizontal length of the screen
     */
    @FXML
    private lateinit var sliderHX: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the vertical length of the screen
     */
    @FXML
    private lateinit var sliderHY: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the normal vector component in the X-axis
     */
    @FXML
    private lateinit var sliderNX: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the normal vector component in the Y-axis
     */
    @FXML
    private lateinit var sliderNY: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the normal vector component in the Z-axis
     */
    @FXML
    private lateinit var sliderNZ: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the view vector component in the X-axis
     */
    @FXML
    private lateinit var sliderVX: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the view vector component in the Y-axis
     */
    @FXML
    private lateinit var sliderVY: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the view vector component in the Z-axis
     */
    @FXML
    private lateinit var sliderVZ: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the ambient light intensity component for the Red color.
     */
    @FXML
    private lateinit var sliderIambR: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the ambient light intensity component for the Green color.
     */
    @FXML
    private lateinit var sliderIambG: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the ambient light intensity component for the Blue color.
     */
    @FXML
    private lateinit var sliderIambB: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the light intensity component for the Red color.
     */
    @FXML
    private lateinit var sliderIlB: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the light intensity component for the Green color.
     */
    @FXML
    private lateinit var sliderIlR: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the light intensity component for the Blue color.
     */
    @FXML
    private lateinit var sliderIlG: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the light's X-axis position.
     */
    @FXML
    private lateinit var sliderPlX: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the light's Y-axis position.
     */
    @FXML
    private lateinit var sliderPlY: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the light's Z-axis position.
     */
    @FXML
    private lateinit var sliderPlZ: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the ambient reflection coefficient.
     */
    @FXML
    private lateinit var sliderKa: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the diffuse reflection coefficients for the Red color.
     */
    @FXML
    private lateinit var sliderKdR: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the diffuse reflection coefficients for the Green color.
     */
    @FXML
    private lateinit var sliderKdG: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the diffuse reflection coefficients for the Blue color.
     */
    @FXML
    private lateinit var sliderKdB: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the refraction index value.
     */
    @FXML
    private lateinit var sliderEta: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the object color coefficients for the Red color.
     */
    @FXML
    private lateinit var sliderOdR: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the object color coefficients for the Green color.
     */
    @FXML
    private lateinit var sliderOdG: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the object color coefficients for the Blue color.
     */
    @FXML
    private lateinit var sliderOdB: MFXSlider

    /**
     * FXML annotation indicates that this property will be injected by the FXML loader.
     * This lateinit property represents the specular reflection coefficient.
     */
    @FXML
    private lateinit var sliderKs: MFXSlider
    
    /**
     * This array off Points is used to store the points for graphical computations.
     */
    private lateinit var points: Array<Point>


    /**
     * This array off Triangles is used to store the triangles that will be rendered in the window.
     */
    private lateinit var triangles: Array<Triangle>


    /**
     * This two-dimensional array representing the screen, where each element is a Pixel
     */
    private lateinit var screen: Array<Array<Pixel>>
    
    /**
     * This property orthogonizes the Vector V to be orthogonal to the Vector N.
     */
    private lateinit var U: Vector

    /**
     * This Vector representing the view vector
     */
    private lateinit var V: Vector

    /**
     * This Vector representing the normal vector
     */
    private lateinit var N: Vector
    
    /**
     * This property representing the camera's distance from the origin
     */
    private var d = 0.0

    /**
     * This property represents the x-coordinate of a point or position in the screen.
     */
    private var hx = 0.0

    /**
     * This property represents the y-coordinate of a point or position in the screen.
     */
    private var hy = 0.0

    /**
     * A point representing the camera's view position.
     */
    private lateinit var C: Point

    /**
     * This property represents the ambient reflection coefficient value, used in 
     * the Phong reflection model for shading in computer graphics. It determines 
     * the amount of ambient light that the surface reflects.
     */
    private var KaValue = 0.0


    /**
     * This property representes the specular reflection coefficient (Ks) value in
     * the Phong reflection model.
     */
    private var KsValue = 0.0

    /**
     * This property represents the refraction index value (Eta) used in the Phong
     * reflection model.
     */
    private var etaValue = 0.0
    
    /**
     * This Vector representes the ambient light intensity value.
     */
    private lateinit var IambValue: Vector

    /**
     * This Vector represents the light intensity value.
     */
    private lateinit var IlValue: Vector

    /**
     * This Vector represents the light position value .
     */
    private lateinit var PlValue: Vector

    /**
     * This Vector represents the diffuse reflection coefficient (Kd) value.
     */
    private lateinit var KdValue: Vector

    /**
     * This Vector represents the object color value.
     */
    private lateinit var OdValue: Vector

    /**
     * GraphicsContext instance used for drawing operations.
     * This property is initialized later and should be used to perform
     * various graphics-related tasks such as rendering shapes, images, and text.
     */
    private lateinit var gc: GraphicsContext

    /**
     * A writable image that can be used to draw or manipulate pixel data.
     * This property is initialized later in the code.
     */
    private lateinit var writableImage: WritableImage

    /**
     * A nullable Job instance that represents a cancellable coroutine.
     * This variable is used to manage the lifecycle of a coroutine, allowing it to be cancelled if needed.
     */
    private var job: Job? = null

    /**
     * The height of the window in pixels.
     */
    private val height = 800

    /**
     * The width of the window in pixels.
     */
    private val width = 800

    /**
     * Reads camera and light settings from "camera.txt" and "iluminacao.txt" files and updates the corresponding sliders.
     *
     * The method performs the following steps:
     * 1. Reads the camera settings from "camera.txt" and updates the sliders:
     *    - Normal vector (NX, NY, NZ)
     *    - View vector (VX, VY, VZ)
     *    - Distance (D)
     *    - Screen size (HX, HY)
     *    - Camera position (CameraX, CameraY, CameraZ)
     * 
     * 2. Reads the light settings from "iluminacao.txt" and updates the sliders:
     *    - Ambient light intensity (IambR, IambG, IambB)
     *    - Ambient reflection coefficient (Ka)
     *    - Light intensity (IlR, IlG, IlB)
     *    - Light position (PlX, PlY, PlZ)
     *    - Diffuse reflection coefficient (KdR, KdG, KdB)
     *    - Object color (OdR, OdG, OdB)
     *    - Specular reflection coefficient (Ks)
     *    - Refraction index (Eta)
     *
     * The method assumes that the files "camera.txt" and "iluminacao.txt" exist and contain the necessary data in the expected format.
     */
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

    /**
     * Reads data from various sliders and initializes the corresponding properties.
     *
     * - N: The normal vector initialized with values from sliders sliderNX, sliderNY, and sliderNZ.
     * - C: The camera position anitialized with values from sliders sliderCameraX, sliderCameraY, and sliderCameraZ.
     * - d: The distance of the camera.
     * - hx: The horizontal size of the screen.
     * - hy: The vertical size of the screen.
     * - V: The view vector  initialized with values from sliders sliderVX, sliderVY, and sliderVZ.
     * - etaValue: The refraction index value from sliderEta.
     * - KaValue: The ambient reflection coefficient from sliderKa.
     * - KsValue: The specular reflection coefficient from sliderKs.
     * - IambValue: The ambient light intensity from sliders sliderIambR, sliderIambG, and sliderIambB.
     * - KdValue: The diffuse reflection coefficient from sliders sliderKdR, sliderKdG, and sliderKdB.
     * - OdValue: The object color from sliders sliderOdR, sliderOdG, and sliderOdB.
     * - PlValue: The light position from sliders sliderPlX, sliderPlZ, and sliderPlY.
     * - IlValue: The light intensity from sliders sliderIlR, sliderIlG, and sliderIlB.
     */
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

    /**
     * Initializes the screen by creating a 2D array of Pixels with the specified height and width,
     * and fills the entire screen with the color black.
     */
    private fun startScreen() {
        this.screen = Array(height) { Array(width) { Pixel(Color.BLACK) }}
        this.gc.fill = Color.BLACK
        this.gc.fillRect(0.0, 0.0, width.toDouble(), height.toDouble())
    }

    /**
     * Initializes the window and sets up the necessary components and event listeners.
     *
     * @param url The URL location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The ResourceBundle used to localize the root object, or null if the root object was not localized.
     *
     * This method performs the following tasks:
     * - Initializes a writable image with the specified width and height.
     * - Reads camera and light settings.
     * - Sets up the graphics context for the canvas.
     * - Starts the screen.
     * - Loads points from an archive.
     * - Changes the perspective of the view.
     * - Adds listeners to various sliders to update the frame when their values change.
     *
     * The `updateFrame` function is defined within this method to handle the frame update logic.
     * It cancels any existing job if active, and launches a new coroutine to change the perspective.
     *
     * If an IOException occurs during initialization, it is caught and its stack trace is printed.
     */
    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
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

    /**
     * Changes the perspective of the graphical window by performing a series of operations.
     * 
     * This function performs the following steps:
     * 1. Reads data required for perspective change.
     * 2. Orthogonizes the view.
     * 3. Retrieves points from an archive.
     * 4. Sets the original references for triangles.
     * 5. Updates the screen coordinates.
     * 6. Normalizes the triangles.
     * 7. Normalizes the points.
     * 8. Changes the screen to reflect the new perspective.
     * 
     * @throws IOException if an I/O error occurs during the perspective change process.
     */
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

    /**
     * Orthogonizes the vector V with respect to the vector N.
     * This method adjusts the vector V to be orthogonal to the vector N.
     * It first calculates the orthogonal projection of V onto N, then subtracts this projection from V.
     * Finally, it normalizes the vectors N and V, and updates the vector U as the cross product of N and V.
     */
    private fun orthogonizeV() {
        val ortho = V.scalarMultiplication(N) / N.scalarMultiplication(N)
        val n = N.v
        val vaux = Vector(arrayOf(ortho * n[0], ortho * n[1], ortho * n[2]))
        V -= vaux
        this.N = N.normalizeVector()
        this.V = V.normalizeVector()
        this.U = N*V
    }

    /**
     * Changes the screen by projecting points of triangles onto the screen, 
     * performing scanline rasterization, and updating the pixel colors.
     *
     * This function performs the following steps:
     * 1. Projects the points of each triangle onto the screen and updates the triangle's screen coordinates.
     * 2. Initializes the screen for drawing.
     * 3. Applies the scanline algorithm to each triangle to determine the pixels that need to be colored.
     * 4. Updates the pixel colors on the writable image based on the screen buffer.
     * 5. Draws the updated writable image onto the graphics context.
     */
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

    /**
     * This function performs the scanline algorithm on a given triangle.
     * It first determines the vertex with the minimum y-coordinate (ymin) and then
     * sorts the vertices of the triangle into start, middle, and finish points based on their y-coordinates.
     * After sorting, it calls two functions: `paintTopDown` and `paintDownTop` to fill the triangle.
     *
     * @param triangle The Triangle to be processed.
     */
    private fun scanLine(triangle: Triangle) {
        var ymin: Double = triangle.screen[0].points[1].coerceAtMost(triangle.screen[1].points[1])
        ymin = ymin.coerceAtMost(triangle.screen[2].points[1])
        lateinit var start: Point
        lateinit var middle: Point
        lateinit var finish: Point
        if (ymin == triangle.screen[0].points[1]) {
            start = triangle.screen[0]
            if (triangle.screen[1].points[1] <= triangle.screen[2].points[1]) {
                middle = triangle.screen[1]
                finish = triangle.screen[2]
            } else {
                middle = triangle.screen[2]
                finish = triangle.screen[1]
            }
        } else if (ymin == triangle.screen[1].points[1]) {
            start = triangle.screen[1]
            if (triangle.screen[0].points[1] <= triangle.screen[2].points[1]) {
                middle = triangle.screen[0]
                finish = triangle.screen[2]
            } else {
                middle = triangle.screen[2]
                finish = triangle.screen[0]
            }
        } else {
            start = triangle.screen[2]
            if (triangle.screen[1].points[1] <= triangle.screen[0].points[1]) {
                middle = triangle.screen[1]
                finish = triangle.screen[0]
            } else {
                middle = triangle.screen[0]
                finish = triangle.screen[1]
            }
        }
        this.paintTopDown(start, middle, finish, triangle)
        this.paintDownTop(start, middle, finish, triangle)
    }

    /**
     * Calculates the alpha and beta values based on the given points.
     *
     * @param start The starting point.
     * @param middle The middle point.
     * @param finish The ending point.
     * @return A pair containing the alpha and beta values.
     */
    private  fun calculateAlphaBeta(start: Point, middle: Point, finish: Point): Pair<Double, Double> {
        val alpha: Double
        val beta: Double
        if (middle.points[0] < start.points[0]) {
            alpha = (middle.points[0] - finish.points[0]) / (middle.points[1] - finish.points[1])
            beta = (start.points[0] - finish.points[0]) / (start.points[1] - finish.points[1])
        } else {
            alpha = (start.points[0] - finish.points[0]) / (start.points[1] - finish.points[1])
            beta = (middle.points[0] - finish.points[0]) / (middle.points[1] - finish.points[1])
        }
        return Pair(alpha, beta)
    }

    /**
     * Paints the bottom-up portion of a triangle.
     *
     * This function paints the bottom-up portion of a triangle by calculating the color of each pixel
     * within the specified range. It uses a nested function `paint` to handle the painting process
     * for a given range of y-coordinates.
     *
     * @param start The starting point of the triangle.
     * @param middle The middle point of the triangle.
     * @param finish The finishing point of the triangle.
     * @param triangle The triangle object containing the points and other relevant data.
     */
    private  fun paintDownTop(start: Point, middle: Point, finish: Point, triangle: Triangle) {
        /**
         * Paints a section of the window by calculating the color for each point within the specified range.
         *
         * @param alpha The decrement value for xmin in each iteration.
         * @param beta The decrement value for xmax in each iteration.
         *
         * The function initializes the xmax and xmin values from the first point of the finish object.
         * It then iterates from the minimum y value (from finish.points) down to the maximum y value (from middle.points).
         * For each y value, it calculates the range of x values (from ini to end) and launches a coroutine for each x value
         * to calculate its color using the calculateColor function.
         * After processing each row, it decrements xmin and xmax by alpha and beta respectively.
         */
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

    /**
     * Paints the top-down portion of a triangle.
     *
     * This function paints the top-down portion of a triangle by calculating the color of each pixel
     * within the specified range. It uses a nested function `paint` to handle the painting process
     * for a given range of y-coordinates.
     *
     * @param start The starting point of the triangle.
     * @param middle The middle point of the triangle.
     * @param finish The finishing point of the triangle.
     * @param triangle The triangle object containing the necessary data for painting.
     */
    private fun paintTopDown(start: Point, middle: Point, finish: Point, triangle: Triangle) {
        /**
         * Paints a section of the window by calculating the color for each point within the specified range.
         *
         * @param alpha The decrement value for xmin in each iteration.
         * @param beta The decrement value for xmax in each iteration.
         *
         * The function initializes the xmax and xmin values from the first point of the finish object.
         * It then iterates from the minimum y value (from start.points) down to the maximum y value (from middle.points).
         * For each y value, it calculates the range of x values (from ini to end) and launches a coroutine for each x value
         * to calculate its color using the calculateColor function.
         * After processing each row, it increments xmin and xmax by alpha and beta respectively.
         */
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

    /**
     * Calculates the color at a given point (x, y) within a triangle using barycentric coordinates and lighting models.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @param t The triangle containing the point.
     *
     * This function performs the following steps:
     * 1. Copies the original normals of the triangle's vertices to the current points.
     * 2. Computes the barycentric coordinates of the point (x, y) within the triangle.
     * 3. Calculates the interpolated point using the barycentric coordinates.
     * 4. Computes the normal vector at the interpolated point.
     * 5. Calculates the vectors for the normal (N), view direction (V), light direction (L), and reflection direction (R).
     * 6. Computes the ambient (Iamb), diffuse (Id), and specular (Is) lighting components based on the Phong reflection model.
     * 7. Combines the lighting components to get the final color intensity (I).
     * 8. Clamps the color intensity values to the range [0, 255].
     * 9. Updates the z-coordinate of the point and organizes the color information for rendering.
     */
    private fun calculateColor(x: Double, y: Double, t: Triangle) {
        t.points[0].normal = t.original[0].normal
        t.points[1].normal = t.original[1].normal
        t.points[2].normal = t.original[2].normal
        val q: Array<Double> = Point.barycentricCoordinate(t.screen[0], t.screen[1], t.screen[2], Point(arrayOf(x, y)))
        val p: Point = Point.calculatePointBarycentricCoordinate(t.points[0], t.points[1], t.points[2], q[0], q[1], 1.0 - q[0] - q[1])
        val origem = Point(arrayOf(0.0, 0.0, 0.0))
        val n1 = Vector(t.points[0].normal)*q[0]
        val n2 = Vector(t.points[1].normal)*q[1]
        val n3 = Vector(t.points[2].normal)*(1.0 - q[0] - q[1])
        p.normal = (n1+n2+n3).normalizeVector().v
        var N = Vector(p.normal)
        val V = Vector((origem - p).normalizeVector().v)
        val L = Vector((PlValue - Vector(p.points)).normalizeVector().v)
        val R = Vector(calculateR(N, L).normalizeVector().v)
        lateinit var Id: Array<Double>
        lateinit var Is: Array<Double>
        val Ia = Vector(IambValue.v)*this.KaValue

        if (N.scalarMultiplication(L) < 0.0) {
            if (N.scalarMultiplication(V) < 0.0) {
                N = -N
                Is = (IlValue*(this.KsValue * (R).scalarMultiplication(V).pow(this.etaValue))).v
                Id = KdValue.elementWiseMultiplication(IlValue.elementWiseMultiplication(OdValue*N.scalarMultiplication(L))).v
            } else {
                Is = origem.points
                Id = origem.points
            }
        }else{
            Is = (IlValue*(this.KsValue * (R).scalarMultiplication(V).pow(this.etaValue))).v
            Id = KdValue.elementWiseMultiplication(IlValue.elementWiseMultiplication(OdValue*N.scalarMultiplication(L))).v
        }

        if (R.scalarMultiplication(V) < 0.0) {
            Is = origem.points
        }

        val I = Ia+Vector(Id)+Vector(Is)

        I.v = arrayOf(
            min(max(I.v[0], 0.0), 255.0),
            min(max(I.v[1], 0.0), 255.0),
            min(max(I.v[2], 0.0), 255.0)
        )

        p.points[2] = -p.points[2]
        this.organizeZ(x.toInt(), y.toInt(), p.points[2], Color.rgb(I.v[0].toInt(), I.v[1].toInt(), I.v[2].toInt()))
    }

    /**
     * Updates the depth and color of a pixel on the screen if the given depth is greater than the current depth.
     *
     * @param i The x-coordinate of the pixel.
     * @param j The y-coordinate of the pixel.
     * @param actualDepth The depth value to compare and potentially set.
     * @param color The color to set if the depth is updated.
     */
    private fun organizeZ(i: Int, j: Int, actualDepth: Double, color: Color) {
        if (i in 0 until width && j in 0 until height && screen[i][j].depth < actualDepth) {
            screen[i][j].depth = actualDepth
            screen[i][j].color = color
        }
    }

    /**
     * Calculates the reflection vector R given the normal vector N and the light vector L.
     *
     * The reflection vector R is calculated using the formula:
     * R = 2 * (N ⋅ L) * N - L
     * where "⋅" denotes the dot product.
     *
     * @param N The normal vector.
     * @param L The light vector.
     * @return The reflection vector R.
     */
    private fun calculateR(N: Vector, L: Vector): Vector = Vector((N*(2.0 * N.scalarMultiplication(L))).v.mapIndexed { i, value -> value - L.v[i] }.toTypedArray())

    /**
     * Projects a 3D point onto the 2D screen.
     *
     * This function takes a 3D point and projects it onto a 2D screen using perspective projection.
     * The resulting 2D coordinates are adjusted to fit within the screen dimensions.
     *
     * @param a The 3D point to be projected, represented as a Point object.
     * @return An array of two Double values representing the projected 2D coordinates on the screen.
     */
    private fun projectPointOnScreen(a: Point): Array<Double> {
        val pointA = arrayOf(
            this.d * a.points[0] / (a.points[2] * this.hx),
            this.d * a.points[1] / (a.points[2] * this.hy)
        )
        pointA[0] = floor((pointA[0] + 1.0) / 2.0 * height.toDouble() + 0.5)
        pointA[1] = floor(width.toDouble() - (pointA[1] + 1.0) / 2.0 * width.toDouble() + 0.5)
        return pointA
    }

    /**
     * Normalizes the points in the `points` array by calculating their normal vectors.
     * For each point in the array, it computes the normal vector using the `pointsNormal` function,
     * normalizes the resulting vector, and assigns the normalized vector to the point's normal property.
     */
    private fun normalPoints() {
        for (point in this.points) {
            val norm = pointsNormal(point).let { normalVertice ->
                Vector(normalVertice).normalizeVector().v
            }
            point.normal = Matrix(norm).matrix[0]
        }
    }

    /**
     * Calculates and assigns the normal vector for each triangle in the list of triangles.
     * 
     * This function iterates through the array of triangles and computes the normal vector
     * for each triangle using the `normalOfATriangle` method. The computed normal vector
     * is then assigned to the `normal` property of the triangle.
     */
    private fun normalTriangles() {
        for (triangle in this.triangles) {
            val normal = this.normalOfATriangle(triangle)
            triangle.normal = Matrix(normal).matrix[0]
        }
    }

    /**
     * Calculates the normal vector of a given triangle.
     *
     * This function takes a triangle as input and computes its normal vector by performing the following steps:
     * 1. Subtracts the first point from the second point to get a vector.
     * 2. Subtracts the first point from the third point to get another vector.
     * 3. Computes the cross product of the two vectors obtained in steps 1 and 2.
     * 4. Normalizes the resulting vector from the cross product.
     * 5. Returns the normalized vector as an array of doubles.
     *
     * @param triangle The triangle for which the normal vector is to be calculated.
     * @return An array of doubles representing the normalized normal vector of the triangle.
     */
    private fun normalOfATriangle(triangle: Triangle): Array<Double> =
        triangle.points[0].let { p1 ->
            triangle.points[1].let { p2 ->
                triangle.points[2].let { p3 ->
                    ((p2 - p1)*(p3 - p1)).normalizeVector().v
                }
            }
        }

    /**
     * Updates the screen coordinates for all triangles in the list.
     * Iterates through each triangle and calculates its screen coordinates
     * using the provided transformation matrix.
     */
    private fun updateScreenCoord() {
        for (i in triangles.indices) {
            this.calculateScreenCoord(triangles[i])
        }
    }

    /**
     * Calculates the screen coordinates for the given triangle's points relative to the camera point.
     * Updates the triangle's points with their corresponding screen coordinates.
     *
     * @param triangle The triangle whose points' screen coordinates are to be calculated.
     */
    private fun calculateScreenCoord(triangle: Triangle) {
        triangle.points = arrayOf(this.screenCord(triangle.points[0]), this.screenCord(triangle.points[1]), this.screenCord(triangle.points[2]))
    }

    /**
     * Transforms a point from world coordinates to screen coordinates.
     *
     * @param p The point to be transformed in world coordinates.
     * @return The transformed point in screen coordinates.
     */
    private fun screenCord(p: Point): Point =
        Matrix(this.orthogonalBasis()).let { ortonormal ->
            Matrix((p- this.C).v).let { subtract ->
                Matrix(subtract.transpose).let { transpose ->
                    Point((ortonormal*transpose).transpose[0])
                }
            }
        }

    /**
     * Generates an orthogonal basis matrix from the vectors U, V, and N.
     *
     * @return A 2D array representing the orthogonal basis matrix, where each row is one of the vectors U, V, and N.
     */
    private fun orthogonalBasis(): Array<Array<Double>> {
        val u = U.v
        val v = V.v
        val n = N.v
        return arrayOf(arrayOf(u[0], u[1], u[2]), arrayOf(v[0],v[1], v[2]), arrayOf(n[0], n[1], n[2]))
    }

    /**
     * Sets the original references for each triangle in the list.
     * This method iterates through the `triangles` list and calls the `setOriginals` method
     * on each triangle to store their original state or references.
     */
    private fun setTrianglesOriginalReferences() {
        for (i in triangles.indices) {
            triangles[i].setOriginals()
        }
    }

    /**
     * Reads points and triangles from a file named "triangulos.txt" and initializes the `points` and `triangles` arrays.
     * The file is expected to have the following format:
     * - The first line contains two integers separated by a space: the number of points and the number of triangles.
     * - Each of the next lines (equal to the number of points) contains three double values separated by spaces, representing the coordinates of a point.
     * - Each of the remaining lines (equal to the number of triangles) contains three integers separated by spaces, representing the indices of the points that form a triangle.
     * 
     * This function performs the following steps:
     * 1. Reads the first line to determine the number of points and triangles.
     * 2. Initializes the `points` array with the specified number of points.
     * 3. Initializes the `triangles` array with the specified number of triangles.
     * 4. Reads the coordinates of each point and stores them in the `points` array.
     * 5. Reads the indices of the points that form each triangle and stores them in the `triangles` array.
     * 6. Adds each triangle to the list of triangles for each point that forms the triangle.
     * 7. Closes the file reader.
     */
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
        /**
         * Calculates the normal vector for a given point by averaging the normals of the triangles
         * that share this point.
         *
         * @param point The point for which the normal vector is to be calculated.
         * @return An array of Double representing the normal vector.
         */
        private fun pointsNormal(point: Point): Array<Double> {
            var normal = Vector(arrayOf(0.0, 0.0, 0.0))

            for (triangle in point.triangles) {
                normal += Vector(triangle.normal)
            }

            return normal.v
        }

    }
}