package br.ufrpe.computacaografica

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class ScreenManager private constructor() {
    private var scene: Scene? = null
    private lateinit var stage: Stage

    init {
        val pane =
            FXMLLoader.load<BorderPane>(javaClass.classLoader.getResource("br/ufrpe/computacaografica/fxml/window.fxml"))
        scene = Scene(pane)
    }

    fun setStage(stage: Stage) {
        this.stage = stage
        stage.isResizable = false
    }

    fun openWindow() {
        stage.scene = scene
        stage.show()
    }

    companion object {
        val instance = ScreenManager()
    }
}