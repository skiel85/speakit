package speakit.ftrs.indexer;

import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.LongField;
import speakit.io.record.Record;

public class OccurrenceRecord extends Record<IntegerField> {

	private IntegerField term = new IntegerField();
	private LongField document = new LongField();
	
	public OccurrenceRecord() {
		//constructor vacio
	}
	public OccurrenceRecord(int term, long document) {
		this();
		this.term.setInteger(term);
		this.document.setLong(document);
	}
	
	/**
	 * @return the term
	 */
	public IntegerField getTerm() {
		return term;
	}
	/**
	 * @param term the term to set
	 */
	public void setTerm(IntegerField term) {
		this.term = term;
	}
	/**
	 * @return the document
	 */
	public LongField getDocument() {
		return document;
	}
	/**
	 * @param document the document to set
	 */
	public void setDocument(LongField document) {
		this.document = document;
	}
	@Override
	protected Field[] getFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IntegerField getKey() {
		return term;
	}

}
