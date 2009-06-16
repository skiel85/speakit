package speakit.ftrs.indexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import speakit.TextDocument;
import speakit.documentstorage.TextDocumentList;
import speakit.ftrs.index.InvertedIndexRecord;
import speakit.ftrs.index.InvertedList;
import speakit.ftrs.index.Term;

public class InvertedIndexRecordGenerator {
	private ArrayList<TextDocument> docs;
	private InvertedListGenerator invertedListGenerator;
	private Lexicon lexicon;

	public InvertedIndexRecordGenerator() {
		docs = new ArrayList<TextDocument>();
		invertedListGenerator = new InvertedListGenerator();
		lexicon = new Lexicon();
	}

	public void addDocuments(TextDocumentList documentList) {
		
	}

	/**
	 * Recibe un documento conteniendo solo los terminos relevantes, con los
	 * filtros aplicados y lo pre procesa para generar los regstros del indice.
	 * 
	 * @param cleanTextDocument
	 */
	public void addSingleDocument(TextDocument cleanTextDocument) {
		docs.add(cleanTextDocument);
		Iterator<String> it = cleanTextDocument.iterator();
		while (it.hasNext()) {
			String word = (String) it.next();
			getLexicon().add(word);
		}
	}

	public ArrayList<InvertedIndexRecord> generateNewRegisters() throws IOException {
		ArrayList<InvertedIndexRecord> result = new ArrayList<InvertedIndexRecord>();
		getInvertedListGenerator().processTextDocuments(getDocuments(), getLexicon());
		for (Iterator<Term> iterator = getLexicon().iterator(); iterator.hasNext();) {
			Term term = iterator.next();
			InvertedList invList = getInvertedListGenerator().generate(term.getTermId());
			result.add(new InvertedIndexRecord(term.getValue(), invList));
		}
		return result;
	}

	/**
	 * @return the docs
	 */
	public ArrayList<TextDocument> getDocuments() {
		return docs;
	}

	/**
	 * @return the invertedListGenerator
	 */
	public InvertedListGenerator getInvertedListGenerator() {
		return invertedListGenerator;
	}

	/**
	 * @return the lexicon
	 */
	public Lexicon getLexicon() {
		return lexicon;
	}

}
