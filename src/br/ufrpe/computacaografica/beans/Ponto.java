package br.ufrpe.computacaografica.beans;

public class Ponto {
	private double p[];
	private double normal[];
	public Ponto(int i) {
		p = new double[i];
	}
	
	
	public Ponto(double[] res) {
		p = res.clone();
	}

	public double[] getP() {
		return p;
	}
	public double getP(int i) {
		return p[i];
	}
	public void setP(int i, double v) {
		this.p[i] = v;
	}
	
	public Vetor subtrai(Ponto p2) {
		Vetor res = null;;
		if(p.length == p2.getP().length) {
			double vres[] = new double[this.p.length];
			double vp2[] = p2.getP();
			for(int i = 0; i < p.length;i++) {
				vres[i] = p[i] - vp2[i];
			}
			
			res = new Vetor(vres);
		}
		return res;
	}

	public boolean equals(Ponto b) {
		double v[] = b.getP();
		for(int i = 0; i < v.length;i++) {
			if(v[i] != this.p[i]) {
				return false;
			}
		}
		return true;
	}
	public static double[] coordenadaBaricentrica(Ponto p1, Ponto p2, Ponto p3, Ponto p) {
		Matriz m1 = new Matriz(2,2);

	    m1.setMatriz(0,0, p1.getP(0) - p3.getP(0));
	    m1.setMatriz(0,1, p2.getP(0) - p3.getP(0));
	    m1.setMatriz(1,0, p1.getP(1) - p3.getP(1));
	    m1.setMatriz(1,1, p2.getP(1) - p3.getP(1));

	    Matriz m2 = new Matriz(2,1);
	    m2.setMatriz(0,0, p.getP(0) - p3.getP(0));
	    m2.setMatriz(1,0, p.getP(1) - p3.getP(1));
	    
	    double a = m1.getMatriz(0, 0);
	    double b = m1.getMatriz(0, 1);
	    double c = m1.getMatriz(1, 0);
	    double d = m1.getMatriz(1, 1);

	    double determinante = a*d - b*c;

	    double inversa[][] = Matriz.inversa(m1.getMatriz(), determinante);
	    a = inversa[0][0];
	    b = inversa[0][1];
	    c = inversa[1][0];
	    d = inversa[1][1];

	    Matriz res = new Matriz(inversa).mult(m2);
	    double[][] sol = res.getTransposta();
	    
	    return sol[0];
	}

	public static Ponto calcularPontoCoordenadaBaricentrica(Ponto p1, Ponto p2, Ponto p3, double alfa, double beta, double gama) {
		Ponto res = new Ponto(3);

	    res.setP(0, alfa*p1.getP(0) + beta*p2.getP(0) + gama*p3.getP(0));
	    res.setP(1, alfa*p1.getP(1) + beta*p2.getP(1) + gama*p3.getP(1));
	    res.setP(2, alfa*p1.getP(2) + beta*p2.getP(2) + gama*p3.getP(2));
	    return res;
	}


	public void setNormal(double[] ds) {
		normal = ds;
	}


	public double[] getNormal() {
		return normal;
	}
}
