package br.ufrpe.computacaografica

import javafx.application.Application
import javafx.stage.Stage

/**
 * Main class that extends the JavaFX Application class.
 * This class is the entry point for the JavaFX application.
 */
class Main : Application() {
    /**
     * Starts the JavaFX application by setting the primary stage and opening the main window.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     */
    override fun start(primaryStage: Stage) {
        ScreenManager.instance.setStage(primaryStage)
        ScreenManager.instance.openWindow()
    }
}
/**
 * The main entry point of the application.
 * This function launches the JavaFX application by calling `Application.launch` with the `Main` class.
 */
fun main() {
    Application.launch(Main::class.java)
}
