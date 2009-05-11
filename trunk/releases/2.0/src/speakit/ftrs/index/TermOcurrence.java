package speakit.ftrs.index;

import speakit.io.record.CompositeField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.LongField;

public class TermOcurrence extends CompositeField {
	private LongField		documentId		= new LongField();
	private IntegerField	localFrecuency	= new IntegerField();

	public TermOcurrence(long documentId, int localFrecuency) {
		this.setDocumentId(documentId);
		this.setLocalFrecuency(localFrecuency);
	}

	/**
	 * @return the document
	 */
	public long getDocumentId() {
		return documentId.getLong();
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public void setDocumentId(long documentId) {
		this.documentId.setLong(documentId);
	}

	/**
	 * @return the frecuency
	 */
	public int getLocalFrecuency() {
		return localFrecuency.getInteger();
	}

	/**
	 * @param frecuency
	 *            the frecuency to set
	 */
	public void setLocalFrecuency(int frecuency) {
		this.localFrecuency.setInteger(frecuency);
	}

	/**
	 * Compara un TermOcurrence con otro segun relevancia
	 * 
	 * @param other
	 * @return
	 */
	public int compareByRelevance(TermOcurrence other) {
		if (this.localFrecuency.compareTo(other.localFrecuency)==0) {
			return this.documentId.compareTo(other.documentId);
		} else {
			return this.localFrecuency.compareTo(other.localFrecuency) * -1;
		}
	}
	
	@Override
	protected int compareToSameClass(Field o) {
		return compareByRelevance((TermOcurrence) o);
	}

	public boolean equals(TermOcurrence other) {
		return this.compareByRelevance(other) == 0;
	}

	@Override
	protected Field[] getFields() {
		return new Field[]{this.documentId,this.localFrecuency};
	}
	
	@Override
	protected String getStringRepresentation() {
		return "{doc:"+this.documentId.toString()+",frec:"+ this.localFrecuency.toString() +"}";
	}

}
