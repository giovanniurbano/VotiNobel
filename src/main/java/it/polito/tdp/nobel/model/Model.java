package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set<Esame> parziale = new HashSet<Esame>();
		soluzioneMigliore = new HashSet<Esame>();
		mediaSoluzioneMigliore = 0;
		
		//cerca1(parziale, 0, numeroCrediti);
		cerca2(parziale, 0, numeroCrediti);
		
		return soluzioneMigliore;	
	}

	private void cerca2(Set<Esame> parziale, int L, int m) {
		//casi terminali
		
		//PARZIALE.sommaCrediti() >= m
		int crediti = this.sommaCrediti(parziale);
		if(crediti > m) {
			return;
		}
		if(crediti == m) {
			double media = this.calcolaMedia(parziale);
			if(media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new HashSet<>(parziale);
				mediaSoluzioneMigliore = media;
			}
			return;
		}
		
		//sicuramente crediti < m
		//L = N -> non ci sono più esami da aggiungere
		if(L == partenza.size()) {
			return;
		}
		
		//generare sottoproblemi
		//partenza[L] è da aggiungere o no? Provo entrambe le cose
		parziale.add(partenza.get(L));
		cerca2(parziale, L+1, m);
		
		parziale.remove(partenza.get(L));
		cerca2(parziale, L+1, m);
	}

	// COMPLESSITA' N! 
	private void cerca1(Set<Esame> parziale, int L, int m) {
		//casi terminali
		
		//PARZIALE.sommaCrediti() >= m
		int crediti = this.sommaCrediti(parziale);
		if(crediti > m) {
			return;
		}
		if(crediti == m) {
			double media = this.calcolaMedia(parziale);
			if(media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new HashSet<>(parziale);
				mediaSoluzioneMigliore = media;
			}
			return;
		}
		
		//sicuramente crediti < m
		//L = N -> non ci sono più esami da aggiungere
		if(L == partenza.size()) {
			return;
		}
		
		//generare sottoproblemi
		for(Esame e : partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca1(parziale, L+1, m);
				parziale.remove(e);
			}
		}
	}

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
