package speakit.ftrs.indexer;

public class Occurrence implements Comparable<Occurrence> {
	private int termId;
	private long document;

	public Occurrence(int termId, long document) {
		this.termId = termId;
		this.document = document;
	}

	/**
	 * @return the termId
	 */
	public int getTermId() {
		return termId;
	}

	/**
	 * @return the document
	 */
	public long getDocument() {
		return document;
	}

	@Override
	public int compareTo(Occurrence o) {
		if (o == null)
			return -1;
		if (this.termId == o.termId) {
			if (this.document == o.getDocument())
				return 0;
			return this.document < o.document ? -1 : 1;
		} else {
			return this.termId < o.termId ? -1 : 1;
		}
	}
	@Override
	public String toString() {
		return "[Termino:" + this.termId + " Documento:" + this.document + "]";
	}
}
