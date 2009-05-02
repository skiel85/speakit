package speakit.ftrs.index;

public class InvertedListItem {
	private long documentId;
	private int localFrecuency;

	public InvertedListItem(long document, int localFrecuency) {
		this.documentId = document;
		this.localFrecuency = localFrecuency;
	}

	/**
	 * @return the document
	 */
	public long getDocumentId() {
		return documentId;
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	/**
	 * @return the frecuency
	 */
	public int getLocalFrecuency() {
		return localFrecuency;
	}

	/**
	 * @param frecuency
	 *            the frecuency to set
	 */
	public void setLocalFrecuency(int frecuency) {
		this.localFrecuency = frecuency;
	}
	
	/**
	 * Compara un InvertedListItem con otro segun relevancia
	 * @param other
	 * @return
	 */
	public int compareByRelevance(InvertedListItem other) {
		if(this.localFrecuency==other.localFrecuency){
			return new Long(this.documentId).compareTo(other.documentId);
		}else{
			return new Integer(this.localFrecuency).compareTo(other.localFrecuency)*-1;
		}
	}
	
	public boolean equals(InvertedListItem other){
		return this.compareByRelevance(other)==0;
	}

}
