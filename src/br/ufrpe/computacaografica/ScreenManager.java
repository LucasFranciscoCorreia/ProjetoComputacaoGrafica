package br.ufrpe.computacaografica;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ScreenManager {
	private FXMLLoader projetoParteDois;
	private BorderPane janela;
	private Scene ppdois;
	private Stage mainStage;
	
	private static ScreenManager instance;
	
	private ScreenManager() {
		try {
			projetoParteDois = new FXMLLoader(this.getClass().getResource("janelaProjetoP2.fxml"));
			janela = projetoParteDois.load();
			ppdois = new Scene(janela);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ScreenManager getInstance() {
		if(instance == null) {
			instance = new ScreenManager();
		}
		return instance;
	}

	public void salvarStage(Stage stage) {
		this.mainStage = stage;
		stage.setResizable(false);
	}
	public void abrirJanelaDois() {
		this.mainStage.setScene(ppdois);
		this.mainStage.show();
	}
	
	
}
