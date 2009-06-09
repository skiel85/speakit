package speakit.documentstorage;

import java.util.ArrayList;
import java.util.Iterator;

import speakit.TextDocument;

public class TextDocumentList implements Iterable<TextDocument> {
	protected ArrayList<TextDocument> documents = new ArrayList<TextDocument>();

	public void add(TextDocument document) {
		this.documents.add(document);
	}

	@Override
	public Iterator<TextDocument> iterator() {
		return documents.iterator();
	}

	public boolean isEmpty() {
		return documents.size() == 0;
	}

}
