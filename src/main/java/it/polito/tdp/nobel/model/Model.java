package it.polito.tdp.nobel.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	private List<Esame> partenza;
	private List<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza = dao.getTuttiEsami();
	}
	
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		List<Esame> parziale = new ArrayList<Esame>();
		soluzioneMigliore = new ArrayList<Esame>();
		mediaSoluzioneMigliore = 0;
		
		cerca1(parziale, 0, numeroCrediti);
		//cerca2(parziale, 0, numeroCrediti);
		
		return soluzioneMigliore;	
	}

	//COMPLESSITA' 2^N
	private void cerca2(List<Esame> parziale, int L, int m) {
		//casi terminali
		
		//PARZIALE.sommaCrediti() >= m
		int crediti = this.sommaCrediti(parziale);
		if(crediti > m) {
			return;
		}
		if(crediti == m) {
			double media = this.calcolaMedia(parziale);
			if(media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new ArrayList<>(parziale);
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
	private void cerca1(List<Esame> parziale, int L, int m) {
		//casi terminali
		
		//PARZIALE.sommaCrediti() >= m
		int crediti = this.sommaCrediti(parziale);
		if(crediti > m) {
			return;
		}
		if(crediti == m) {
			double media = this.calcolaMedia(parziale);
			if(media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new ArrayList<>(parziale);
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
		/*for(Esame e : partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca1(parziale, L+1, m);
				parziale.remove(e);
			}
		}*/
		/*for(int i=0; i<partenza.size(); i++) {
			if(!parziale.contains(partenza.get(i)) && i>=L) {
				parziale.add(partenza.get(i));
				cerca1(parziale, L+1, m);
				parziale.remove(partenza.get(i));
			}
		}*/
		int lastIndex = 0;
		if(parziale.size() > 0)
			lastIndex = partenza.indexOf(parziale.get(parziale.size()-1));
		
		for(int i=lastIndex; i<partenza.size(); i++) {
			if(!parziale.contains(partenza.get(i))) {
				parziale.add(partenza.get(i));
				cerca1(parziale, L+1, m);
				parziale.remove(partenza.get(i));
			}
		}
	}

	public double calcolaMedia(List<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(List<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
