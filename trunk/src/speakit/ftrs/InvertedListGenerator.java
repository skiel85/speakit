package speakit.ftrs;

import java.util.ArrayList;

import speakit.TextDocument;
import speakit.ftrs.index.InvertedList;

public class InvertedListGenerator {

	protected AppearanceStorage storage;

	/**
	 * Procesa la lista de documentos, extrayendo las apariciones de cada
	 * termino basandose en el lexico existente
	 * 
	 * @param documents
	 *            Docs aaprocesar
	 * @param lexicon
	 *            Lexico
	 */
	public void processTextDocuments(ArrayList<TextDocument> documents, Lexicon lexicon) {
		for (TextDocument doc : documents) {
			for (String word : doc) {
				int termId = lexicon.getAppearanceOrder(word);
				Appearance app = new Appearance(termId, doc.getId());
				getStorage().addAppeareance(app);
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
	 */
	public InvertedList generate(int termId) {
		// ordenar la lista de apariciones con el mecanismo de sort definido
		// para cada termino
		// recuperar la lista de apariciones y generar un
		// InvertedListItem(idDoc, frecuencia)
		// agregarlo a la lista invertida
		// devolver la nueva lista invertida
		return null;
	}

	private AppearanceStorage getStorage() {
		if (storage == null)
			storage = new MockAppearanceStorageImpl();
		return storage;
	}
}
