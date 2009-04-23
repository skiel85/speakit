package speakit;

import java.util.ArrayList;
import java.util.Iterator;

public class TextDocument implements Iterable<String> {

	private ArrayList<String> words = new ArrayList<String>();

	@Override
	public Iterator<String> iterator() {
		return words.iterator();
	}

	public void add(String word) {
		words.add(word);
	}

	public ArrayList<String> getRelevantTerms() {
		return words;
	}

	public ArrayList<String> getPreview() {
		ArrayList<String> preview = new ArrayList<String>(words.subList(0, 20));
		return preview;
	}
}
