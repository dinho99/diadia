package it.uniroma3.diadia.giocatore;

import it.uniroma3.diadia.attrezzi.Attrezzo;

public class Borsa {
	public final static int DEFAULT_PESO_MAX_BORSA = 10;
	private Attrezzo[] attrezzi;
	private int numeroAttrezzi;
	private int pesoMax;
	
	public Borsa() {
		this(DEFAULT_PESO_MAX_BORSA);
	}
	
	public Borsa(int pesoMax) {
		this.pesoMax = pesoMax;
		this.attrezzi = new Attrezzo[10]; // speriamo che bastino...
		this.numeroAttrezzi = 0;
	}
	
	public boolean addAttrezzo(Attrezzo attrezzo) {
		if (this.getPeso() + attrezzo.getPeso() > this.getPesoMax())
			return false;
		if (this.numeroAttrezzi==10)
			return false;
		this.attrezzi[this.numeroAttrezzi] = attrezzo;
		this.numeroAttrezzi++;
		return true;
	}
	
	public int getPesoMax() {
		return pesoMax;
	}
	
	public Attrezzo getAttrezzo(String nomeAttrezzo) {
		Attrezzo a = null;
		for (int i= 0; i<this.numeroAttrezzi; i++)
			if (attrezzi[i]!=null && this.attrezzi[i].getNome().equals(nomeAttrezzo))
				a = attrezzi[i];

		return a;
	}
	
	public int getPeso() {
		int peso = 0;
		for (int i= 0; i<this.numeroAttrezzi; i++) {
			if(this.attrezzi[i]!=null)
				peso += this.attrezzi[i].getPeso();
		}
		return peso;
	}
	
	public boolean isEmpty() {
		return this.numeroAttrezzi == 0;
	}
	
	/*Controlla se un attrezzo esiste nella borsa*/
	public boolean hasAttrezzo(String nomeAttrezzo) {
		boolean trovato = false;
		for(Attrezzo attrezzo : this.attrezzi) {
			if(attrezzo!=null && attrezzo.getNome().equals(nomeAttrezzo))
				trovato = true;
		}
		return trovato;
	}
	/* Metodo che rimuove l'attrezzo dato il nome
	 */
	public Attrezzo removeAttrezzo(String nomeAttrezzo) {
		Attrezzo a = null;
		int i = 0;
		while(a==null && i<attrezzi.length) {
			if(attrezzi[i]!=null) {
				if(attrezzi[i].getNome().equals(nomeAttrezzo)) {
					a = this.attrezzi[i];
					this.attrezzi[i] = null;
				}
			}
			i ++;
		}
		return a;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();

		if (!this.isEmpty()) {
			s.append("Contenuto borsa ("+this.getPeso()+"kg/"+this.getPesoMax()+"kg): ");
			for (int i= 0; i<this.numeroAttrezzi; i++)
				s.append(attrezzi[i].toString()+" ");
		}
		else
			s.append("Borsa vuota");
		return s.toString();
	}
}
