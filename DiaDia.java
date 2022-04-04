package it.uniroma3.diadia;

import it.uniroma3.diadia.ambienti.Stanza;
import it.uniroma3.diadia.attrezzi.Attrezzo;
import it.uniroma3.diadia.giocatore.Borsa;
import it.uniroma3.diadia.IOConsole.IOConsole;

/**
 * Classe principale di diadia, un semplice gioco di ruolo ambientato al dia.
 * Per giocare crea un'istanza di questa classe e invoca il metodo gioca
 *
 * Questa e' la classe principale crea e istanzia tutte le altre
 *
 * @author  docente di POO 
 *         (da un'idea di Michael Kolling and David J. Barnes) 
 *          
 * @version base
 */

public class DiaDia {

	static final private String MESSAGGIO_BENVENUTO = ""+
			"Ti trovi nell'Universita', ma oggi e' diversa dal solito...\n" +
			"Meglio andare al piu' presto in biblioteca a studiare. Ma dov'e'?\n"+
			"I locali sono popolati da strani personaggi, " +
			"alcuni amici, altri... chissa!\n"+
			"Ci sono attrezzi che potrebbero servirti nell'impresa:\n"+
			"puoi raccoglierli, usarli, posarli quando ti sembrano inutili\n" +
			"o regalarli se pensi che possano ingraziarti qualcuno.\n\n"+
			"Per conoscere le istruzioni usa il comando 'aiuto'.";

	static final private String[] elencoComandi = {"vai", "aiuto", "prendi", "posa", "fine"};

	private Partita partita;
	private IOConsole io;

	public DiaDia(IOConsole io) {
		this.partita = new Partita();
		this.io = io;
	}

	public void gioca() {
		String istruzione; 

		this.io.mostraMessaggio(MESSAGGIO_BENVENUTO);
		do		
			istruzione = this.io.leggiRiga(); //legge una riga da tastiera
		while (!processaIstruzione(istruzione));
	}   


	/**
	 * Processa una istruzione 
	 *
	 * @return true se l'istruzione e' eseguita e il gioco continua, false altrimenti
	 */
	private boolean processaIstruzione(String istruzione) {
		Comando comandoDaEseguire = new Comando(istruzione);

		if(comandoDaEseguire.getNome() == null) { //se l'utente non ha scritto nulla
			this.io.mostraMessaggio("Devi inserire un comando!");
			return false;
		}
		if(this.partita.getCfu()==0) {
			this.io.mostraMessaggio("Game over!");
			return true;
		}
		
		if (comandoDaEseguire.getNome().equals("fine")) {
			this.fine(); 
			return true;
		} 
		else if (comandoDaEseguire.getNome().equals("vai"))
			this.vai(comandoDaEseguire.getParametro());
		else if (comandoDaEseguire.getNome().equals("aiuto"))
			this.aiuto();
		else if(comandoDaEseguire.getNome().equals("prendi"))
			this.prendi(comandoDaEseguire.getParametro());
		else if(comandoDaEseguire.getNome().equals("posa"))
			this.posa(comandoDaEseguire.getParametro());
		else
			this.io.mostraMessaggio("Comando sconosciuto");
		if (this.partita.vinta()) {
			this.io.mostraMessaggio("Hai vinto!");
			return true;
		} else
			return false;
	}   

	// implementazioni dei comandi dell'utente:

	/**
	 * Stampa informazioni di aiuto.
	 */
	private void aiuto() {
		for(int i=0; i< elencoComandi.length; i++) 
			this.io.mostraMessaggio(elencoComandi[i]+" ");
		this.io.mostraMessaggio("");
	}

	/**
	 * Cerca di andare in una direzione. Se c'e' una stanza ci entra 
	 * e ne stampa il nome, altrimenti stampa un messaggio di errore
	 */
	private void vai(String direzione) {
		if(direzione==null)
			this.io.mostraMessaggio("Dove vuoi andare ?");
		Stanza prossimaStanza = null;
		prossimaStanza = this.partita.getStanzaCorrente().getStanzaAdiacente(direzione);
		if (prossimaStanza == null)
			this.io.mostraMessaggio("Direzione inesistente");
		else {
			this.partita.setStanzaCorrente(prossimaStanza);
			int cfu = this.partita.getCfu();
			this.partita.setCfu(cfu--);
		}
		this.io.mostraMessaggio(partita.getStanzaCorrente().getDescrizione());
	}

	/* Gli attrezzi presi vengono rimossi dalla stanza e aggiunti alla borsa
	 * */
	private void prendi(String cosaPrendo) {
		if(cosaPrendo == null)
			this.io.mostraMessaggio("Cosa vuoi prendere ?");
		Stanza stanza = this.partita.getStanzaCorrente();
		if(!(stanza.hasAttrezzo(cosaPrendo))) //se non trovo attrezzi con quel nome
			this.io.mostraMessaggio("L'attrezzo non è presente nella stanza!");
		else {
			Attrezzo attrezzoDaPrendere = stanza.getAttrezzo(cosaPrendo);
			boolean preso = partita.getGiocatore().getBorsa().addAttrezzo(attrezzoDaPrendere);
			if(!preso)
				this.io.mostraMessaggio("Capienza massima della borsa, non posso prendere l'attrezzo");
			else {
				stanza.removeAttrezzo(attrezzoDaPrendere);
				this.io.mostraMessaggio("Attrezzo preso!");
			}
		}
	}
	
	/*Gli attrezzi posati vengono rimossi dalla borsa
	 * e aggiunti alla stanza */
	private void posa(String cosaPoso) {
		if(cosaPoso == null)
			this.io.mostraMessaggio("Cosa vuoi posare ?");
		Borsa borsa = partita.getGiocatore().getBorsa();
		if(!(borsa.hasAttrezzo(cosaPoso)))
			this.io.mostraMessaggio("L' attrezzo non è presente nella borsa!");
		else {
			Attrezzo attrezzoDaPosare = borsa.getAttrezzo(cosaPoso);
			boolean posato = partita.getStanzaCorrente().addAttrezzo(attrezzoDaPosare);
			if(!posato)
				this.io.mostraMessaggio("Impossibile posare l'attrezzo. Non c'è più spazio nella stanza!");
			else {
				borsa.removeAttrezzo(cosaPoso);
				this.io.mostraMessaggio("Attrezzo posato!");

			}
		}
	}
	
	/**
	 * Comando "Fine".
	 */
	private void fine() {
		this.io.mostraMessaggio("Grazie di aver giocato!");  // si desidera smettere
	}

	public static void main(String[] argc) {
		IOConsole ioConsole = new IOConsole();
		DiaDia gioco = new DiaDia(ioConsole);
		gioco.gioca();
	}
}
