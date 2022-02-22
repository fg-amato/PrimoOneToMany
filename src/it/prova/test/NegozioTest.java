package it.prova.test;

import java.util.List;

import it.prova.dao.ArticoloDAO;
import it.prova.dao.NegozioDAO;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class NegozioTest {

	public static void main(String[] args) {
		NegozioDAO negozioDAOInstance = new NegozioDAO();
		ArticoloDAO articoloDAOInstance = new ArticoloDAO();

		// ora con i dao posso fare tutte le invocazioni che mi servono
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testInserimentoNegozio(negozioDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		testFindByIdNegozio(negozioDAOInstance);

		testInsertArticolo(negozioDAOInstance, articoloDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testFindByIdArticolo(articoloDAOInstance);
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testDeleteNegozio(negozioDAOInstance);

		testUpdateNegozio(negozioDAOInstance);

		testfindAllByIniziali(negozioDAOInstance);

		testPopulateArticoli(negozioDAOInstance, articoloDAOInstance);

		testUpdateArticolo(negozioDAOInstance, articoloDAOInstance);

		testDeleteArticolo(articoloDAOInstance);

		// ESERCIZIO: COMPLETARE DAO E TEST RELATIVI

		// ESERCIZIO SUCCESSIVO
		/*
		 * se io voglio caricare un negozio e contestualmente anche i suoi articoli
		 * dovrò sfruttare il populateArticoli presente dentro negoziodao. Per esempio
		 * Negozio negozioCaricatoDalDb = negozioDAOInstance.selectById...
		 * 
		 * negozioDAOInstance.populateArticoli(negozioCaricatoDalDb);
		 * 
		 * e da qui in poi il negozioCaricatoDalDb.getArticoli() non deve essere più a
		 * size=0 (se ha articoli ovviamente) LAZY FETCHING (poi ve lo spiego)
		 */

	}

	private static void testInserimentoNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testInserimentoNegozio inizio.............");
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("Negozio1", "via dei mille 14"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");

		System.out.println(".......testInserimentoNegozio fine: PASSED.............");
	}

	private static void testFindByIdNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testFindByIdNegozio inizio.............");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testFindByIdNegozio : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		Negozio negozioCheRicercoColDAO = negozioDAOInstance.selectById(primoNegozioDellaLista.getId());
		if (negozioCheRicercoColDAO == null
				|| !negozioCheRicercoColDAO.getNome().equals(primoNegozioDellaLista.getNome()))
			throw new RuntimeException("testFindByIdNegozio : FAILED, i nomi non corrispondono");

		if (!negozioCheRicercoColDAO.getIndirizzo().equals(primoNegozioDellaLista.getIndirizzo())) {
			throw new RuntimeException("testFindByIdNegozio : FAILED, gli indirizzi non corrispondono");
		}

		System.out.println(".......testFindByIdNegozio fine: PASSED.............");
	}

	private static void testInsertArticolo(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testInsertArticolo inizio.............");
		// mi serve un negozio esistente
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testInsertArticolo : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		int quantiArticoliInseriti = articoloDAOInstance
				.insert(new Articolo("articolo1", "matricola1", primoNegozioDellaLista));
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("testInsertArticolo : FAILED");

		System.out.println(".......testInsertArticolo fine: PASSED.............");
	}

	private static void testFindByIdArticolo(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testFindByIdArticolo inizio.............");
		List<Articolo> elencoArticoliPresenti = articoloDAOInstance.list();
		if (elencoArticoliPresenti.size() < 1)
			throw new RuntimeException("testFindByIdArticolo : FAILED, non ci sono articoli sul DB");

		Articolo primoArticoloDellaLista = elencoArticoliPresenti.get(0);

		Articolo articoloCheRicercoColDAO = articoloDAOInstance.selectById(primoArticoloDellaLista.getId());
		if (articoloCheRicercoColDAO == null
				|| !articoloCheRicercoColDAO.getNome().equals(primoArticoloDellaLista.getNome()))
			throw new RuntimeException("testFindByIdArticolo : FAILED, i nomi non corrispondono");

		System.out.println(".......testFindByIdArticolo fine: PASSED.............");
	}

	private static void testDeleteNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testDeleteNegozio inizio.............");
		Negozio negozioDaRimuovere = new Negozio("DaRimuovere", "via rimozione 15");
		if (negozioDAOInstance.insert(negozioDaRimuovere) != 1) {
			throw new RuntimeException("testDeleteNegozio: FAILED, Negozio non aggiunto");
		}
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		int numeroElementiPrimaDellaRimozione = elencoNegoziPresenti.size();
		int elementiRimossi = negozioDAOInstance
				.delete(elencoNegoziPresenti.get(numeroElementiPrimaDellaRimozione - 1));
		int numeroElementiDopoLaRimozione = negozioDAOInstance.list().size();
		if (elementiRimossi < 0 || elementiRimossi > 1) {
			throw new RuntimeException("testDeleteNegozio: FAILED, Rimozione non avvenuta correttamente");
		}
		if (numeroElementiDopoLaRimozione + 1 != numeroElementiPrimaDellaRimozione) {
			throw new RuntimeException("testDeleteNegozio: FAILED, Rimozione non avvenuta correttamente");
		}
		System.out.println(".......testDeleteNegozio fine: PASSED.............");
	}

	private static void testUpdateNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println("..........testUpdateNegozio inizio............");

		Negozio negozioDaAggiornare = new Negozio("DaAggiornare", "via aggiornamento 75");

		if (negozioDAOInstance.insert(negozioDaAggiornare) != 1) {
			throw new RuntimeException("testUpdateNegozio: FAILED, Negozio non aggiunto");
		}
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		int numeroElementiPrimaDellAggiornamento = elencoNegoziPresenti.size();
		int elementiAggiornati = negozioDAOInstance
				.update(new Negozio(elencoNegoziPresenti.get(numeroElementiPrimaDellAggiornamento - 1).getId(),
						"Aggiornato", "via aggiornato 90"));
		if (elementiAggiornati != 1) {
			throw new RuntimeException("testUpdateNegozio: FAILED, Aggiornamento non avvenuto correttamente");
		}

		System.out.println("..........testUpdateNegozio fine: PASSED............");
	}

	private static void testfindAllByIniziali(NegozioDAO negozioDAOInstance) {
		System.out.println("..........testfindAllByIniziali inizio............");

		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testfindAllByIniziali : FAILED, non ci sono negozi sul DB");

		negozioDAOInstance.findAllByIniziali("n");

		System.out.println("..........testfindAllByIniziali fine: PASSED............");

	}

	private static void testPopulateArticoli(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {

		System.out.println(".......testPopulateArticoli inizio.............");

		Negozio negozioConArticoli = new Negozio("Euronics", "via tiburtina 15");
		if (negozioDAOInstance.insert(negozioConArticoli) != 1) {
			throw new RuntimeException("testPopulateArticoli: FAILED, Negozio non aggiunto");
		}

		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		Negozio ultimoNegozioInElenco = elencoNegoziPresenti.get(elencoNegoziPresenti.size() - 1);
		int numeroArticoliPrimaDiAggiungere = ultimoNegozioInElenco.getArticoli().size();

		Articolo smartphoneSamsung = new Articolo("Samsung S21", "111", ultimoNegozioInElenco);
		Articolo tvSony = new Articolo("Sony TV", "232", ultimoNegozioInElenco);
		Articolo laptopMac = new Articolo("MacBook Pro", "343", ultimoNegozioInElenco);

		if (articoloDAOInstance.insert(tvSony) != 1) {
			throw new RuntimeException("testPopulateArticoli: FAILED, Articolo non aggiunto");
		}

		if (articoloDAOInstance.insert(smartphoneSamsung) != 1) {
			throw new RuntimeException("testPopulateArticoli: FAILED, Articolo non aggiunto");
		}

		if (articoloDAOInstance.insert(laptopMac) != 1) {
			throw new RuntimeException("testPopulateArticoli: FAILED, Articolo non aggiunto");
		}

		negozioDAOInstance.populateArticoli(ultimoNegozioInElenco);

		int articoliInNegozio = ultimoNegozioInElenco.getArticoli().size();

		if (articoliInNegozio != numeroArticoliPrimaDiAggiungere + 3) {
			throw new RuntimeException("testPopulateArticoli: FAILED, Numero di articoli non corretto");
		}

		System.out.println("..........testPopulateArticoli fine: PASSED............");

	}

	private static void testUpdateArticolo(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testUpdateArticolo inizio.............");
		// mi serve un negozio esistente
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testInsertArticolo : FAILED, non ci sono negozi sul DB");

		Articolo articoloDaModificare = articoloDAOInstance.list().get(0);

		articoloDaModificare.setNome("TELEFONOMODIFICATO");
		int elementiAggiornati = articoloDAOInstance.update(articoloDaModificare);
		if (elementiAggiornati != 1) {
			throw new RuntimeException("testUpdateArticolo: Articolo non aggiornato correttamente");
		}
		System.out.println(".......testUpdateArticolo fine: PASSED.............");
	}

	private static void testDeleteArticolo(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testDeleteArticolo inizio.............");

		List<Articolo> elencoArticoliPresenti = articoloDAOInstance.list();
		if (elencoArticoliPresenti.size() < 1)
			throw new RuntimeException("testDeleteArticolo : FAILED, non ci sono articoli sul DB");

		Articolo daRimuovere = elencoArticoliPresenti.get(0);

		if (articoloDAOInstance.delete(daRimuovere) != 1) {
			throw new RuntimeException("testDeleteArticolo : FAILED, rimozione non avvenuta correttamente");
		}

		if (articoloDAOInstance.list().size() != elencoArticoliPresenti.size() - 1) {
			throw new RuntimeException("testDeleteArticolo : FAILED, rimozione non avvenuta correttamente");
		}
		System.out.println(".......testDeleteArticolo fine: PASSED.............");
	}

	// private static void
}
