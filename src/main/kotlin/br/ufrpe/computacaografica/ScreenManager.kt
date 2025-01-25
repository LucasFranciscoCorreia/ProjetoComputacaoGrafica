package br.ufrpe.computacaografica

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

/**
 * Manages the application's main screen and handles the loading and displaying of scenes.
 *
 * The ScreenManager class is responsible for managing the primary stage and scene of the application.
 * It follows the Singleton design pattern to ensure only one instance of the ScreenManager exists.
 *
 * @constructor Creates an instance of ScreenManager and initializes the scene with the FXML layout.
 */
class ScreenManager private constructor() {
    /**
     * The current scene being managed by the ScreenManager.
     * This variable holds a reference to a Scene object, which represents
     * the graphical content to be displayed and interacted with.
     * It is initially set to null and can be updated to point to different scenes
     * as needed.
     */
    private var scene: Scene? = null

    /**
     * A late-initialized property for the primary stage of the application.
     * This property will be initialized at a later point in the application lifecycle.
     */
    private lateinit var stage: Stage

    /**
     * Initializes the ScreenManager by loading the FXML layout from the specified resource path
     * and setting it as the root of the scene.
     *
     * The FXML file is expected to be located at "br/ufrpe/computacaografica/fxml/window.fxml".
     * The loaded layout is set to a BorderPane and assigned to the scene.
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    init {
        val pane =
            FXMLLoader.load<BorderPane>(javaClass.classLoader.getResource("br/ufrpe/computacaografica/fxml/window.fxml"))
        scene = Scene(pane)
    }

    /**
     * Sets the stage for the ScreenManager and configures its properties.
     *
     * @param stage The Stage object to be set.
     */
    fun setStage(stage: Stage) {
        this.stage = stage
        this.stage.title = "RenderCraft"
        this.stage.centerOnScreen();
        this.stage.icons.add(Image(javaClass.classLoader.getResourceAsStream("br/ufrpe/computacaografica/assets/logo.png")))
        stage.isResizable = false
    }

    /**
     * Opens a new window by setting the scene to the stage and displaying the stage.
     */
    fun openWindow() {
        stage.scene = scene
        stage.show()
    }

    /**
     * Singleton instance of ScreenManager.
     */
    companion object {
        val instance = ScreenManager()
    }
}