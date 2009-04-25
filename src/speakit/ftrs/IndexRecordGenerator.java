package speakit.ftrs;

import java.util.ArrayList;
import java.util.Iterator;

import speakit.TextDocument;
import speakit.ftrs.index.IndexRecord;
import speakit.ftrs.index.InvertedList;
import speakit.ftrs.index.Term;

public class IndexRecordGenerator {
	private ArrayList<TextDocument> docs;
	private InvertedListGenerator invertedListGenerator;
	private ArrayList<Term> lexicon;

	public IndexRecordGenerator() {
		docs = new ArrayList<TextDocument>();
		invertedListGenerator = new InvertedListGenerator();
		lexicon = new ArrayList<Term>();
	}

	public void addDocuments(ArrayList<TextDocument> documentList) {

	}

	public void addSingleDocument(TextDocument textDocument) {
		docs.add(textDocument);
	}

	public ArrayList<IndexRecord> generateNewRegisters() {
		ArrayList<IndexRecord> result = new ArrayList<IndexRecord>();
		getInvertedListGenerator().processTextDocuments(getDocuments(), getLexicon());
		for (Iterator<Term> iterator = getLexicon().iterator(); iterator.hasNext();) {
			Term term = iterator.next();
			InvertedList invList = getInvertedListGenerator().generate(term.getTermId());
			result.add(new IndexRecord(term.getValue(), invList));
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
	public ArrayList<Term> getLexicon() {
		return lexicon;
	}

}
