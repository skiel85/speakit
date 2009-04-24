package speakit.ftrs;

import java.util.ArrayList;

import speakit.TextDocument;
import speakit.ftrs.index.InvertedList;
import speakit.ftrs.index.Term;

public class InvertedListGenerator {

	public void processTextDocuments(ArrayList<TextDocument> documents,
			ArrayList<Term> lexicon) {
		//para cada textDocument
			//almacenar el listado de apariciones secuencialmente (Term.id, doc.id)
	}

	public InvertedList generate(int termId) {
		//ordenar la lista de apariciones con el mecanismo de sort definido		
		//para cada termino
			//recuperar la lista de apariciones y generar un InvertedListItem(idDoc, frecuencia)
			//agregarlo a la lista invertida
		//devolver la nueva lista invertida 
		return null;
	}
}
