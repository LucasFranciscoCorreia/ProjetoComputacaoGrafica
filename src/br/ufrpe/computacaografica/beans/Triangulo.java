package br.ufrpe.computacaografica.beans;

public class Triangulo {
	private Ponto p[];
	private double normal[];
	private Ponto original[];
	private Ponto tela[];
	
	
	public void setTela(int i, Ponto p) {
		tela[i] = p;
	}
	public Triangulo(Ponto p[]) {
		this.p = p;
		this.original = p.clone();
		tela = new Ponto[3];
		//ordenarPontos();
	}
	
	public Ponto getBaricentro() {
		double res[] = {(this.getP(0).getP(0) + this.getP(0).getP(1) + this.getP(0).getP(2))/3,
				(this.getP(1).getP(0) + this.getP(1).getP(1) + this.getP(1).getP(2))/3,
				(this.getP(2).getP(0) + this.getP(2).getP(1) + this.getP(2).getP(2))/3};
		return new Ponto(res);
	}
	
	public Ponto getTela(int i) {
		return tela[i];
	}
	public Triangulo(Ponto p1, Ponto p2, Ponto p3) {
		p = new Ponto[3];
		p[0] = p1;
		p[1] = p2;
		p[2] = p3;

		original = p.clone();
		tela = new Ponto[3];
		//ordenarPontos();
	}
	
	
	public Ponto[] getP() {
		return p;
	}
	

	public void setP(Ponto[] p) {
		this.p = p;
	}
	
	public void setP(int i, Ponto p) {
		this.p[i] = p;
	}

	public double[] getNormal() {
		return normal;
	}

	public void setNormal(double[] normal) {
		if(normal.length == 3) {
			this.normal = normal;			
		}else {
			System.out.println("erro");
		}
	}

	public Ponto[] getOriginal() {
		return original;
	}
	public Ponto getOriginal(int i) {
		return original[i];
	}

	public void setOriginal(Ponto[] original) {
		this.original = original;
	}
	
	public Ponto[] getPontos() {
		return this.p;
	}
	public void ordenarPontos() {
		for(int i = 0; i < 3;i++) {
			for(int j = i+1; j < 3;j++) {
				if(p[i].getP(1) < p[j].getP(1)) {
					Ponto save = p[i];
					p[i] = p[j];
					p[j] = save;
				}
			}
		}
	}
	
	public Ponto getP(int i) {
		return p[i];
	}

	public boolean contains(Ponto ponto) {
		if(this.p[0].equals(ponto) || this.p[1].equals(ponto) || this.p[2].equals(ponto)) {
			return true;
		}
		return false;
	}

	public void setOriginais() {
		original = p.clone();
	}
}
