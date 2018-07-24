package br.ufrpe.computacaografica.beans;

public class Vetor {
	private double v[];

	public Vetor(int i) {
		v = new double[i];
	}

	public Vetor(double v[]) {
		this.v = v;
	}

	public double[] getV() {
		return v;
	}

	public void setV(int i, double v) {
		this.v[i] = v;
	}

	public double multEscalar(Vetor v2) {
		double res = 0;
		if(v2.getV().length == this.v.length) {
			double rv2[] = v2.getV();
			for(int i = 0; i < v.length;i++)
				res+= v[i]*rv2[i];
		}
		return res;
	}

	public Vetor multVetorial(Vetor v2) {
		double rv2[] = v2.getV();
		if(this.v.length == v2.getV().length) {
			double res[] = new double[v.length]; 
			res[0] = (v[1])*(rv2[2]) - (v[2])*(rv2[1]);
			res[1] = (v[2])*(rv2[0]) - (v[0])*(rv2[2]);
			res[2] = (v[1])*(rv2[0]) - (v[0])*(rv2[1]);
			return new Vetor(res);
		}else {
			return null;
		}
	}
	
	public Vetor multPorEscalar(double mult) {
		double res[] = new double[this.v.length];
		for(int i = 0; i < this.v.length;i++) {
			res[i] = mult*v[i];
		}
		
		return new Vetor(res);
	}

	public double produtoInterno(Vetor v2) {
		double vetor[] = v2.getV();
		double res = 0;
		for(int i = 0; i < Math.max(this.v.length, v2.getV().length);i++) {
			res+= vetor[i]*v[i];
		}
		return res;
	}
	public double norma() {
		double res = 0;
		int i;
		for(i = 0; i < v.length;i++){
			res += v[i]*v[i];
		}
		
		return Math.sqrt(res);

	}
	public Vetor produtoVetorial(Vetor b) {
		
		double v[] = new double[3];
		double m[][] = new double[3][3];

		double aux[] = b.getV();
		for (int i = 0; i < 3; i++) {
			m[0][i] = 1;
			m[1][i] = this.v[i];
			m[2][i] = aux[i];
		}

		v[0] = m[0][0] * m[1][1] * m[2][2] - (m[0][0] * m[1][2] * m[2][1]);
		v[1] = m[0][1] * m[1][2] * m[2][0] - (m[0][1] * m[1][0] * m[2][2]);
		v[2] = m[0][2] * m[1][0] * m[2][1] - (m[0][2] * m[1][1] * m[2][0]);

		v[0] = -v[0];
		v[1] = -v[1];
		v[2] = -v[2];

		return new Vetor(v);
	}
	public Vetor normalizarVetor() {
		Vetor res = new Vetor(3);
	    double norma = this.norma();
	    res.setV(0, v[0]/norma);
	    res.setV(1, v[1]/norma);
	    res.setV(2, v[2]/norma);

	    return res;

	}
	
	public Vetor mais(Vetor v2) {
		if(this.v.length == v2.getV().length) {
			double rv2[] = v2.getV();
			double res[] = new double[v.length];
			for(int i = 0; i < res.length;i++) {
				res[i] = v[i]+rv2[i];
			}
			return new Vetor(res);
		}
		return null;
	}
	
	public Vetor multInterno(Vetor v2) {
		double v[] = v2.getV();
		double res[] = new double[3];
		for(int i = 0; i < v.length;i++) {
			res[i] = this.v[i]*v[i];
		}
		return new Vetor(res);
	}
	public Vetor menos(Vetor v2) {
		if(this.v.length == v2.getV().length) {
			double rv2[] = v2.getV();
			double res[] = new double[v.length];
			for(int i = 0; i < res.length;i++) {
				res[i] = v[i]-rv2[i];
			}
			return new Vetor(res);
		}
		return null;
	}

	public double getV(int i) {
		return v[i];
	}
}
