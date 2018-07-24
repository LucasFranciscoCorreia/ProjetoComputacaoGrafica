package br.ufrpe.computacaografica;

import javafx.application.Application;
import javafx.stage.Stage;

public class ProjetoP3 extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		ScreenManager.getInstance().salvarStage(primaryStage);
		ScreenManager.getInstance().abrirJanelaDois();
	}

}
