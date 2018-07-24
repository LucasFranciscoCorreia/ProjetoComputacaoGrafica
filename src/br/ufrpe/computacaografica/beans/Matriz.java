package br.ufrpe.computacaografica.beans;

public class Matriz {
	private double m[][];
	private double transposta[][];

	public Matriz(int linhas, int colunas) {
		this.m = new double[linhas][colunas];
		this.transposta = new double[colunas][linhas];
	}

	public Matriz(double m[][]) {
		this.m = m;
		transposta = new double[m[0].length][m.length];
		calcularTransposta();
	}
	public Matriz(double m[]) {
		this.m = new double[1][m.length];
		this.transposta = new double[m.length][1];
		this.m[0] = m;
		calcularTransposta();
	}
	public double[][] getMatriz() {
		return m;
	}

	public double getMatriz(int i, int j) {
		return m[i][j];
	}

	public void setMatriz(double[][] m) {
		this.m = m;
		transposta = new double[m[0].length][m.length];
		calcularTransposta();
	}

	public void setMatriz(int i, int j, double v) {
		this.m[i][j] = v;
	}

	public int getColunas() {
		return m[0].length;
	}

	public int getLinhas() {
		return m.length;
	}

	public double[][] getTransposta() {
		return transposta;
	}

	private void calcularTransposta(){
		for(int i = 0; i < this.m.length;i++) {
			for(int j = 0; j < this.m[i].length;j++) {
				transposta[j][i] = this.m[i][j];
			}
		}
	}

	private double[] getColuna(int i) {
		calcularTransposta();
		return transposta[i];
	}

	private double[] getLinha(int i) {
		return m[i];
	}

	private double multLinhaColuna(double linha[], double coluna[]) {
		double res = 0;
		for(int i = 0; i < linha.length;i++) {
			res+= linha[i]*coluna[i];
		}
		return res;	
	}

	public static double[] determinante(double m[][]) {
		double res[] = new double[3];
		res[0] = m[1][1]*m[2][2];
		res[0] -= m[1][2]*m[2][1];
		res[1] = m[1][2]*m[2][0];
		res[1] -= m[1][0]*m[2][2];
		res[2] = m[1][0]*m[2][1];
		res[2] -= m[1][1]*m[2][0];
		return res;
	}
	
	public Matriz mult(Matriz m) {
		if(this.getColunas() == m.getLinhas()) {
			double res[][] = new double[this.getLinhas()][m.getColunas()];
			for(int i =0; i < this.getLinhas();i++) {
				for(int j = 0; j < m.getColunas();j++) {
					res[i][j] = multLinhaColuna(this.getLinha(i), m.getColuna(j));
				}
			}
			return new Matriz(res);
		}else {
			return null;
		}
	}

	public static double[][] inversa(double m[][], double determinante) {
		double inversa[][] = new double[m.length][m[0].length];

		double a = m[0][0];
		double b = m[0][1];
		double c = m[1][0];
		double d = m[1][1];

		inversa[0][0] = d/determinante;
		inversa[0][1] = (-b)/determinante;
		inversa[1][0] = (-c)/determinante;
		inversa[1][1] = a/determinante;

		return inversa;
	}
}
