package speakit;

import java.util.ArrayList;
import java.util.Iterator;

//TODO implementar funcionalidad para que guarde el texto completo del documento
//TODO implementar funcionalidad para utilizando un WordReader devuelva las palabras
public class TextDocument implements Iterable<String> {

	private ArrayList<String> words = new ArrayList<String>();

	@Override
	public Iterator<String> iterator() {
		return words.iterator();
	}

	public void add(String word) {
		words.add(word);
	}

	/*
	 * Para implementar este método es necesario tener como mínimo la lista de stop words. Esto se implementa en FTRS 
	public ArrayList<String> getRelevantTerms() {
		return words;
	}
	*/

	public ArrayList<String> getPreview() {
		ArrayList<String> preview = new ArrayList<String>(words.subList(0, 20));
		return preview;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
