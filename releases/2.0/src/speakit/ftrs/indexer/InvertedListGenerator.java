package speakit.ftrs.indexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import speakit.TextDocument;
import speakit.ftrs.index.InvertedList;
import speakit.ftrs.index.TermOcurrence;

public class InvertedListGenerator {

	protected OccurrenceStorage storage;

	/**
	 * Procesa la lista de documentos, extrayendo las apariciones de cada
	 * termino basandose en el lexico existente
	 * 
	 * @param documents
	 *            Docs aaprocesar
	 * @param lexicon
	 *            Lexico
	 * @throws IOException 
	 */
	public void processTextDocuments(ArrayList<TextDocument> documents, Lexicon lexicon) throws IOException {
		for (TextDocument doc : documents) {
			for (String word : doc) {
				int termId = lexicon.getAppearanceOrder(word);
				Occurrence app = new Occurrence(termId, doc.getId());
				getStorage().addOccurrence(app);
			}
		}
	}

	/**
	 * Dado las apariciones registradas anteriormente, y el nro de termino
	 * definido en el lexico, deuelve el listado de los documentos q la
	 * referencian
	 * 
	 * @param termId
	 * @return
	 * @throws IOException 
	 */
	public InvertedList generate(int termId) throws IOException {
		ArrayList<TermOcurrence> invListItems = new ArrayList<TermOcurrence>();
		ArrayList<Occurrence> appearanceList = getStorage().getApearanceListFor(termId);
		if (appearanceList.size() == 0) {
			// La lista de apariencias es 0, devuelvo una lista vacia? o lanzo
			// excepcion?
			return new InvertedList();
		}
		int frecuency = 0;
		long currentDoc = appearanceList.get(0).getDocument();
		Occurrence app = null;
		for (Iterator<Occurrence> appIterator = appearanceList.iterator(); appIterator.hasNext();) {
			app = appIterator.next();
			if (currentDoc == app.getDocument()) {
				frecuency++;
			} else {
				TermOcurrence item = new TermOcurrence(currentDoc, frecuency == 0 ? 1 : frecuency);
				invListItems.add(item);
				frecuency = 0;
				currentDoc = app.getDocument();
			}
		}

		TermOcurrence item = new TermOcurrence(currentDoc, frecuency == 0 ? 1 : frecuency);
		invListItems.add(item);
		InvertedList resultList = new InvertedList(invListItems);
		return resultList.sortByFrecuency();
	}

	private OccurrenceStorage getStorage() throws IOException {
		if (storage == null)
			storage = new OccurrenceStorageImpl();
		return storage;
	}
}
