package br.ufrpe.computacaografica.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import br.ufrpe.computacaografica.beans.Matriz;
import br.ufrpe.computacaografica.beans.Pixel;
import br.ufrpe.computacaografica.beans.Ponto;
import br.ufrpe.computacaografica.beans.Triangulo;
import br.ufrpe.computacaografica.beans.Vetor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class JanelaParteDois implements Initializable{

	@FXML
	private TextField nValue;

	@FXML
	private TextField vValue;

	@FXML
	private TextField dValue;

	@FXML
	private TextField hxValue;

	@FXML
	private TextField hyValue;

	@FXML
	private TextField cValue;

	@FXML
	private TextField Iamb;

	@FXML
	private TextField Ka;

	@FXML
	private TextField iL;

	@FXML
	private TextField Pl;

	@FXML
	private TextField Kd;

	@FXML
	private TextField Od;

	@FXML
	private TextField Ks;

	@FXML
	private TextField eta;

	@FXML
	private Button calcular;

	@FXML
	private Label alerta;

	@FXML
	private AnchorPane janela;

	@FXML
	private Canvas desenho;

	Ponto p[];
	Triangulo t[];
	Pixel tela[][];

	private Vetor U, V, N;
	private double d, hx, hy;
	private Ponto C;

	private double KaValue, KsValue, etaValue;
	private Vetor IambValue, IlValue, PlValue, KdValue, OdValue;


	@FXML
	void mudarPerspectiva(ActionEvent event) throws IOException {
		double[] n = new double[3];
		String linha[] = this.cValue.getText().split(" ");
		n[0] = Double.parseDouble(linha[0]);
		n[1] = Double.parseDouble(linha[1]);
		n[2] = Double.parseDouble(linha[2]);
		this.C = new Ponto(n);
		n = new double[3];
		linha = this.nValue.getText().split(" ");
		n[0] = Double.parseDouble(linha[0]);
		n[1] = Double.parseDouble(linha[1]);
		n[2] = Double.parseDouble(linha[2]);
		this.N = new Vetor(n);
		n = new double[3];
		linha = this.vValue.getText().split(" ");
		n[0] = Double.parseDouble(linha[0]);
		n[1] = Double.parseDouble(linha[1]);
		n[2] = Double.parseDouble(linha[2]);
		this.V = new Vetor(n);
		n = new double[1];
		linha = this.dValue.getText().split(" ");
		n[0] = Double.parseDouble(linha[0]);
		this.d = n[0];
		n = new double[1];
		linha = this.hxValue.getText().split(" ");
		n[0] = Double.parseDouble(linha[0]);
		this.hx = n[0];
		n = new double[1];
		linha = this.hyValue.getText().split(" ");
		n[0] = Double.parseDouble(linha[0]);
		this.hy = n[0];
		this.etaValue = Integer.parseInt(eta.getText());
		this.KaValue = Double.parseDouble(Ka.getText());
		this.KsValue = Double.parseDouble(Ks.getText());
		n = new double[3];
		linha = this.Iamb.getText().split(" ");
		n[0] = Double.parseDouble(linha[0]);
		n[1] = Double.parseDouble(linha[1]);
		n[2] = Double.parseDouble(linha[2]);
		this.IambValue = new Vetor(n);
		n = new double[3];
		linha = this.Kd.getText().split(" ");
		n[0] = Double.parseDouble(linha[0]);
		n[1] = Double.parseDouble(linha[1]);
		n[2] = Double.parseDouble(linha[2]);
		this.KdValue = new Vetor(n);
		n = new double[3];
		linha = this.Od.getText().split(" ");
		n[0] = Double.parseDouble(linha[0]);
		n[1] = Double.parseDouble(linha[1]);
		n[2] = Double.parseDouble(linha[2]);
		this.OdValue = new Vetor(n);
		n = new double[3];
		linha = this.Pl.getText().split(" ");
		n[0] = Double.parseDouble(linha[0]);
		n[1] = Double.parseDouble(linha[1]);
		n[2] = Double.parseDouble(linha[2]);
		this.PlValue = new Vetor(n);
		n = new double[3];
		linha = this.iL.getText().split(" ");
		n[0] = Double.parseDouble(linha[0]);
		n[1] = Double.parseDouble(linha[1]);
		n[2] = Double.parseDouble(linha[2]);
		this.IlValue = new Vetor(n);
		ortogonizarV();
		getPontosArquivo();
		iniciarTela();
		setReferenciasOriginaisDosTriangulos();
		atualizarCoordVista();
		normalTriangulo();
		normalVertice();
		pintarTela();
		salvarPontosArquivo();
		BufferedReader reader = new BufferedReader(new FileReader("camera.txt"));
		this.nValue.setText(reader.readLine());
		this.vValue.setText(reader.readLine());
		this.dValue.setText(reader.readLine());
		this.hxValue.setText(reader.readLine());
		this.hyValue.setText(reader.readLine());
		this.cValue.setText(reader.readLine());
		reader.close();
		reader = new BufferedReader(new FileReader("iluminacao.txt"));
		this.Iamb.setText(reader.readLine());
		this.Ka.setText(reader.readLine());
		this.iL.setText(reader.readLine());
		this.Pl.setText(reader.readLine());
		this.Kd.setText(reader.readLine());
		this.Od.setText(reader.readLine());
		this.Ks.setText(reader.readLine());
		this.eta.setText(reader.readLine());
		reader.close();
	}

	private void ortogonizarV() {
		double aux = produtoEscalar(V.getV(), N.getV()) / produtoEscalar(N.getV(), N.getV());
		double vaux[]= new double[3];
		double n[] = N.getV();
		vaux[0] = aux * n[0];
		vaux[1] = aux * n[1];
		vaux[2] = aux * n[2];

		double v[] = V.getV();
		v[0] -= vaux[0];
		v[1] -= vaux[1];
		v[2] -= vaux[2];

		N = N.normalizarVetor();
		V = V.normalizarVetor();
		U = N.produtoVetorial(V);
	}

	private void salvarPontosArquivo() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("camera.txt"));
		writer.write(nValue.getText());
		writer.newLine();
		writer.write(vValue.getText());
		writer.newLine();
		writer.write(dValue.getText());
		writer.newLine();
		writer.write(hxValue.getText());
		writer.newLine();
		writer.write(hyValue.getText());
		writer.newLine();
		writer.write(cValue.getText());
		writer.newLine();
		writer.flush();
		writer.close();
		writer = new BufferedWriter(new FileWriter("iluminacao.txt"));
		writer.write(Iamb.getText());
		writer.newLine();
		writer.write(Ka.getText());
		writer.newLine();
		writer.write(iL.getText());
		writer.newLine();
		writer.write(Pl.getText());
		writer.newLine();
		writer.write(Kd.getText());
		writer.newLine();
		writer.write(Od.getText());
		writer.newLine();
		writer.write(Ks.getText());
		writer.newLine();
		writer.write(eta.getText());
		writer.newLine();
		writer.flush();
		writer.close();
	}

	private void pintarTela() {
		GraphicsContext gc = this.desenho.getGraphicsContext2D();
		gc.setFill(Color.BLACK); // Cor do fundo
		gc.fillRect(0, 0, 500, 500); // Pinta o fundo

		for (int i = 0; i < t.length; i++) {
			double pontoA[] = projetaPontoNaTela(t[i].getP(0));
			double pontoB[] = projetaPontoNaTela(t[i].getP(1));
			double pontoC[] = projetaPontoNaTela(t[i].getP(2));

			t[i].setTela(0, new Ponto(pontoA));
			t[i].setTela(1, new Ponto(pontoB));
			t[i].setTela(2, new Ponto(pontoC));

			scanLine(gc, t[i]);
		}
	}

	private void scanLine(GraphicsContext gc, Triangulo t) {
		double ymin = Math.min(t.getTela(0).getP(1), t.getTela(1).getP(1));
		ymin = Math.min(ymin, t.getTela(2).getP(1));
		Ponto inicio = null, meio = null, fim = null;
		if (ymin == t.getTela(0).getP(1)) {
			inicio = t.getTela(0);
			if (t.getTela(1).getP(1) <= t.getTela(2).getP(1)) {
				meio = t.getTela(1);
				fim = t.getTela(2);
			} else {
				meio = t.getTela(2);
				fim = t.getTela(1);
			}
		} else if (ymin == t.getTela(1).getP(1)) {
			inicio = t.getTela(1);
			if (t.getTela(0).getP(1) <= t.getTela(2).getP(1)) {
				meio = t.getTela(0);
				fim = t.getTela(2);
			} else {
				meio = t.getTela(2);
				fim = t.getTela(0);
			}
		} else if (ymin == t.getTela(2).getP(1)) {
			inicio = t.getTela(2);
			if (t.getTela(1).getP(1) <= t.getTela(0).getP(1)) {
				meio = t.getTela(1);
				fim = t.getTela(0);
			} else {
				meio = t.getTela(0);
				fim = t.getTela(1);
			}
		} else {
			System.err.println("ymin não corresponde a nada");
		}

		//		analisarOrdem(inicio, meio, fim);

		//        if (Double.valueOf(meio.y).equals(fim.y)) System.out.println("Superior");
		//        else if (Double.valueOf(inicio.y).equals(meio.y)) System.out.println("Inferior");
		//        else System.out.println("Normal");

		pintarTopDown(gc, inicio, meio, fim, t);
		pintarDownTop(gc, inicio, meio, fim, t);
	}

	private void pintarDownTop(GraphicsContext gc, Ponto inicio, Ponto meio, Ponto fim, Triangulo t) {
		double alpha, beta;
		if (meio.getP(0) < inicio.getP(0)) {
			alpha = (meio.getP(0) - fim.getP(0)) / (meio.getP(1) - fim.getP(1));
			beta = (inicio.getP(0) - fim.getP(0)) / (inicio.getP(1) - fim.getP(1));
		} else {
			alpha = (inicio.getP(0) - fim.getP(0)) / (inicio.getP(1) - fim.getP(1));
			beta = (meio.getP(0) - fim.getP(0)) / (meio.getP(1) - fim.getP(1));
		}

		double xmax = fim.getP(0);
		double xmin = fim.getP(0);
		for (int y = (int) fim.getP(1); y >= (int) meio.getP(1); y--) {
			int ini = (int) Math.round(xmin);
			int end = (int) Math.round(xmax);
			for (int x = ini; x <= end; x++) {
				calcularCor(x, y, t);
			}
			xmin -= alpha;
			xmax -= beta;
		}	
		double aux = beta;
		beta = alpha;
		alpha = aux;

		xmax = fim.getP(0);
		xmin = fim.getP(0);
		for (int y = (int) fim.getP(1); y >= (int) meio.getP(1); y--) {
			int ini = (int) Math.round(xmin);
			int end = (int) Math.round(xmax);
			for (int x = ini; x <= end; x++) {
				calcularCor(x, y, t);
			}
			xmin -= alpha;
			xmax -= beta;
		}
	}

	private void pintarTopDown(GraphicsContext gc, Ponto inicio, Ponto meio, Ponto fim, Triangulo t) {
		double alpha, beta;
		if (meio.getP(0) < fim.getP(0)) {
			alpha = (meio.getP(0) - inicio.getP(0)) / (meio.getP(1) - inicio.getP(1));
			beta = (fim.getP(0) - inicio.getP(0)) / (fim.getP(1) - inicio.getP(1));
		} else {
			alpha = (fim.getP(0) - inicio.getP(0)) / (fim.getP(1) - inicio.getP(1));
			beta = (meio.getP(0) - inicio.getP(0)) / (meio.getP(1) - inicio.getP(1));
		}

		double xmax = inicio.getP(0);
		double xmin = inicio.getP(0);


		for (int y = (int) Math.round(inicio.getP(1)); y <= (int) Math.round(meio.getP(1)); y++) {
			double aux = Math.max(inicio.getP(0), meio.getP(0));
			aux = Math.max(aux, fim.getP(0));
			int ini = (int) Math.round(xmin);
			int end = (int) Math.round(xmax);
			for (int x = ini; x <= end; x++) {
				calcularCor(x, y, t);
			}
			xmin += alpha;
			xmax += beta;
		}
		xmax = inicio.getP(0);
		xmin = inicio.getP(0);

		double aux2 = beta;
		beta = alpha;
		alpha = aux2;

		for (int y = (int) Math.round(inicio.getP(1)); y <= (int) Math.round(meio.getP(1)); y++) {
			double aux = Math.max(inicio.getP(0), meio.getP(0));
			aux = Math.max(aux, fim.getP(0));
			int ini = (int) Math.round(xmin);
			int end = (int) Math.round(xmax);
			for (int x = ini; x <= end; x++) {
				calcularCor(x, y, t);
			}
			xmin += alpha;
			xmax += beta;
		}
	}

	private void calcularCor(int x, int scanlineY, Triangulo t) {
		t.getP(0).setNormal(t.getOriginal(0).getNormal());
		t.getP(1).setNormal(t.getOriginal(1).getNormal());
		t.getP(2).setNormal(t.getOriginal(2).getNormal());
		double aux[] = {x, scanlineY};
		double[] q = Ponto.coordenadaBaricentrica(t.getTela(0), t.getTela(1), t.getTela(2), new Ponto(aux));
		Ponto p = Ponto.calcularPontoCoordenadaBaricentrica(t.getP(0), t.getP(1), t.getP(2), q[0], q[1], 1-q[0]-q[1]);
		double origem[] = {0,0,0};
		double[] n1 = produtoPorEscalar(t.getP(0).getNormal(), q[0]);
		double[] n2 = produtoPorEscalar(t.getP(1).getNormal(), q[1]);
		double[] n3 = produtoPorEscalar(t.getP(2).getNormal(), 1-q[0] - q[1]); 
		p.setNormal(new Vetor(n1).mais(new Vetor(n2)).mais(new Vetor(n3)).normalizarVetor().getV());
		double[] N = p.getNormal();
		double[] V = new Ponto(origem).subtrai(p).normalizarVetor().getV();
		double[] L = this.PlValue.menos(new Vetor(p.getP())).normalizarVetor().getV();
		double[] R = calcularR(N, L).normalizarVetor().getV();
		double[] Ia = null, Id = null, Is = null;
		Ia = produtoPorEscalar(this.IambValue.getV(), this.KaValue);
		if (new Vetor(N).multEscalar(new Vetor(L)) < 0) {
			if (new Vetor(N).multEscalar(new Vetor(V))  < 0) {
				N[0] = -N[0];
				N[1] = -N[1];
				N[2] = -N[2];
			} else {
				Is = origem;
				Id = origem;
			}
		}
		if (new Vetor(R).multEscalar(new Vetor(V)) < 0) {
			Is = origem;
		}
		if (Is == null) {
			Is = this.IlValue.multPorEscalar(this.KsValue * Math.pow(new Vetor(R).multEscalar(new Vetor(V)), this.etaValue)).getV();
		}
		if (Id == null) {
			Id = this.KdValue.multInterno(this.IlValue.multInterno(this.OdValue.multPorEscalar(produtoEscalar(N, L)))).getV();
		}
		Vetor I = new Vetor(Ia).mais(new Vetor(Id)).mais(new Vetor(Is));
		I.setV(0, Math.min((int) Math.round(Math.max((int) Math.round(I.getV(0)), 0)), 255));
		I.setV(1, Math.min((int) Math.round(Math.max((int) Math.round(I.getV(1)), 0)), 255));
		I.setV(2, Math.min((int) Math.round(Math.max((int) Math.round(I.getV(2)), 0)), 255));

		p.setP(2,-p.getP(2));
		organizarZ(Math.round(x), scanlineY, p.getP(2), Color.rgb((int) I.getV(0), (int) I.getV(1), (int) I.getV(2)));
	}

	private void organizarZ(int i, int j, double profundidadeAtual, Color cor) {
		if (i < 500 && i >= 0 && j < 500 && j >= 0 && tela[i][j].profundidade < profundidadeAtual) {
			tela[i][j].profundidade = profundidadeAtual;
			this.desenho.getGraphicsContext2D().setFill(cor);
			this.desenho.getGraphicsContext2D().fillRect(i, j, 1, 1);
		}
	}

	private Vetor calcularR(double[] N, double[] L) {
		double[] R;
		R = produtoPorEscalar(N, 2 * produtoEscalar(N, L));
		R[0] -= L[0];
		R[1] -= L[1];
		R[2] -= L[2];
		return new Vetor(R);
	}

	private double produtoEscalar(double[] A, double[] B) {
		double v = 0;
		for (int i = 0; i < 3; i++) {
			v += A[i] * B[i];
		}
		return v;
	}

	private double[] produtoPorEscalar(double[] vet, double escalar) {
		double retorno[] = new double[3];
		retorno[0] = vet[0] * escalar;
		retorno[1] = vet[1] * escalar;
		retorno[2] = vet[2] * escalar;
		return retorno;
	}

	private double[] projetaPontoNaTela(Ponto a) {
		double pontoA[] = new double[2];
		pontoA[0] = (this.d * a.getP(0)) / (a.getP(2) * this.hx);
		pontoA[1] = (this.d * a.getP(1)) / (a.getP(2) * this.hy);
		pontoA[0] = Math.floor(((pontoA[0] + 1) / 2) * 500 + 0.5);
		pontoA[1] = Math.floor(500 - ((pontoA[1] + 1) / 2) * 500 + 0.5);
		return pontoA;
	}

	private void normalVertice() {
		ArrayList<double[]> listaDeNormais = new ArrayList<>();
		double res[][];
		for (int i = 0; i < p.length; i++) {
			listaDeNormais.add(new Vetor(normalDeUmVertice(p[i], t)).normalizarVetor().getV());
			res = new Matriz(listaDeNormais.get(i)).getMatriz();
			p[i].setNormal(res[0]);
		}
	}

	private static double[] normalDeUmVertice(Ponto vertice, Triangulo t[]) {
		double[] normal = new double[3];
		normal[0] = 0;
		normal[1] = 0;
		normal[2] = 0;
		ArrayList<Triangulo> listaDeTriangulos = triangulosDeUmPonto(vertice, t);
		for (int i = 0; i < listaDeTriangulos.size(); i++) {
			normal = new Vetor(normal).mais(new Vetor(listaDeTriangulos.get(i).getNormal())).getV();
		}
		return normal;
	}

	private static ArrayList<Triangulo> triangulosDeUmPonto(Ponto ponto, Triangulo t[]) {
		ArrayList<Triangulo> lista = new ArrayList<Triangulo>();
		for (int i = 0; i < t.length; i++) {
			if (t[i].getOriginal(0).equals(ponto) || t[i].getOriginal(1).equals(ponto) || t[i].getOriginal(2).equals(ponto))
				lista.add(t[i]);
		}
		return lista;
	}

	private ArrayList<double[]> normalTriangulo() {
		ArrayList<double[]> listaDeNormais = new ArrayList<double[]>();
		for (int i = 0; i < t.length; i++) {
			listaDeNormais.add(normalDoTriangulo(t[i]));
			Matriz m = new Matriz(listaDeNormais.get(i));
			double res[][] = m.getMatriz();
			t[i].setNormal(res[0]);
		}
		return listaDeNormais;
	}

	private double[] normalDoTriangulo(Triangulo triangulo) {
		return triangulo.getP(1).subtrai(triangulo.getP(0)).produtoVetorial(triangulo.getP(2).subtrai(triangulo.getP(0))).normalizarVetor().getV();
	}

	private void atualizarCoordVista() {
		for (int i = 0; i < t.length; i++) {
			calcularCoordenadaVista(t[i], this.C);
		}
	}

	private void calcularCoordenadaVista(Triangulo triangulo, Ponto c) {
		triangulo.setP(0, coordenadaVista(c, triangulo.getP(0)));
		triangulo.setP(1, coordenadaVista(c, triangulo.getP(1)));
		triangulo.setP(2, coordenadaVista(c, triangulo.getP(2)));
	}

	private Ponto coordenadaVista(Ponto c, Ponto p) {
		double cv[][] = (new Matriz(BaseOrtonormal()).mult(new Matriz(new Matriz(p.subtrai(c).getV()).getTransposta()))).getTransposta();
		return new Ponto(cv[0]);
	}

	private double[][] BaseOrtonormal() {
		double r[][] = null;
		if (U.getV() != null) {
			double u[] = U.getV();
			double v[] = V.getV();
			double n[] = N.getV();
			double aux[][] = { { u[0],u[1],u[2] }, { v[0], v[1], v[2] },{ n[0], n[1], n[2] } };
			r = aux;
		}
		return r;
	}

	private void setReferenciasOriginaisDosTriangulos() {
		for (int i = 0; i < t.length; i++) {
			t[i].setOriginais();
		}
	}

	private void iniciarTela() {
		tela = new Pixel[500][500];
		for(int i = 0; i < 500;i++) {
			for(int j = 0; j < 500;j++) {
				tela[i][j] = new Pixel();
				tela[i][j].cor = Color.BLACK;
				tela[i][j].profundidade = Double.NEGATIVE_INFINITY;
			}
		}
	}

	private void getPontosArquivo() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("triangulos.txt"));
		String linha = reader.readLine();
		String qnt[] = linha.split(" ");
		p = new Ponto[Integer.parseInt(qnt[0])];
		t = new Triangulo[Integer.parseInt(qnt[1])];
		for (int i = 0; i < p.length; i++) {
			linha = reader.readLine();
			qnt = linha.split(" ");
			p[i] = new Ponto(3);
			p[i].setP(0, Double.parseDouble(qnt[0]));
			p[i].setP(1, Double.parseDouble(qnt[1]));
			p[i].setP(2, Double.parseDouble(qnt[2]));
		}
		for (int i = 0; i < t.length; i++) {
			linha = reader.readLine();
			qnt = linha.split(" ");
			int a, b, c;
			a = Integer.parseInt(qnt[0]);
			b = Integer.parseInt(qnt[1]);
			c = Integer.parseInt(qnt[2]);
			t[i] = new Triangulo(p[a - 1], p[b - 1], p[c - 1]);
		}
		reader.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("camera.txt"));
			this.nValue.setText(reader.readLine());
			this.vValue.setText(reader.readLine());
			this.dValue.setText(reader.readLine());
			this.hxValue.setText(reader.readLine());
			this.hyValue.setText(reader.readLine());
			this.cValue.setText(reader.readLine());
			reader.close();
			reader = new BufferedReader(new FileReader("iluminacao.txt"));
			this.Iamb.setText(reader.readLine());
			this.Ka.setText(reader.readLine());
			this.iL.setText(reader.readLine());
			this.Pl.setText(reader.readLine());
			this.Kd.setText(reader.readLine());
			this.Od.setText(reader.readLine());
			this.Ks.setText(reader.readLine());
			this.eta.setText(reader.readLine());
			reader.close();
			mudarPerspectiva(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


