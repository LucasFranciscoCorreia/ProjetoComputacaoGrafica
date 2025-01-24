package br.ufrpe.computacaografica

import javafx.application.Application
import javafx.stage.Stage

class Main : Application() {
    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        ScreenManager.instance.setStage(primaryStage)
        ScreenManager.instance.openWindow()
    }
}
fun main() {
    Application.launch(Main::class.java)
}
