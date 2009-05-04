package speakit.ftrs.index;

import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;
import speakit.io.record.StringField;

public class InvertedIndexRecord extends Record<StringField>{
	private StringField term=new StringField();
	private IntegerField documentsQty=new IntegerField();
	private IntegerField maxLocalFrecuency=new IntegerField();
	private InvertedList documents=new InvertedList();

	public InvertedIndexRecord(String term, int documentsQty, int maxLocalFrecuency, InvertedList documents) {
		this.setTerm( term);
		this.setDocumentsQty(documentsQty);
		this.setMaxLocalFrecuency(maxLocalFrecuency);
		this.documents = documents;
	}

	public InvertedIndexRecord(String term, InvertedList documents) {
		this.setTerm( term);
		this.documents = documents;
		calculateTermsStatistics();
	}
	
	public InvertedIndexRecord() {
	}

	public InvertedList getInvertedList() {
		return documents;
	}

	public void setInvertedList(InvertedList list) {
		this.documents = list;
		this.calculateTermsStatistics();
	}

	public void calculateTermsStatistics() {
		if (this.documents == null) {
			this.documents = new InvertedList();
		}
		this.setDocumentsQty(this.documents.size());
		this.setMaxLocalFrecuency(this.documents.getMaxLocalFrecuency());
	}

	public double getTotalWeight(int totalDocuments) {
		return Math.log10(totalDocuments / this.getDocumentsQty());
	}

	// TODO: REVISAR PESO GLOBAL
	/**
	 * El termino mas importante es el mas raro, es decir el que aparezca en
	 * menos documentos. Por lo tanto el mas raro de los dos devuelve -1.
	 * 
	 * @param other
	 * @return
	 */
	public int compareByTermImportance(InvertedIndexRecord other) {
		if (this.getDocumentsQty() == other.getDocumentsQty()) {
			return 0;
		} else {
			if (this.getDocumentsQty() < other.getDocumentsQty()) {
				return -1;
			} else {
				return 1;
			}
		}
	}
	public void setTerm(String term) {
		this.term.setString( term);
	}

	public String getTerm() {
		return term.getString();
	}

	public void setMaxLocalFrecuency(int maxLocalFrecuency) {
		this.maxLocalFrecuency.setInteger( maxLocalFrecuency);
	}

	public int getMaxLocalFrecuency() {
		return maxLocalFrecuency.getInteger();
	}

	public void setDocumentsQty(int documentsQty) {
		this.documentsQty.setInteger( documentsQty);
	}

	public int getDocumentsQty() {
		return documentsQty.getInteger();
	}

	@Override
	protected Field[] getFields() {
		return new Field[]{this.term,this.documents,this.documentsQty,this.maxLocalFrecuency}; 
	}

	@Override
	public StringField getKey() {
		return this.term;
	}
}
