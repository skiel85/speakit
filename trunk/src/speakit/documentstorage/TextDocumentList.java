package speakit.documentstorage;

import java.util.ArrayList;

import speakit.TextDocument;

public class TextDocumentList {
	protected ArrayList<TextDocument> documents = new ArrayList<TextDocument>();

	public void add(TextDocument document) {
		this.documents.add(document);
	}

}
