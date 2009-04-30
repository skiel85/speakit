package speakit.ftrs.index;

public class IndexRecord {
	protected String term;
	protected int documentsQty;
	protected int maxLocalFrecuency;
	protected InvertedList documents;

	public IndexRecord(String term,int documentsQty, int maxLocalFrecuency, InvertedList documents) {
		this.term = term;
		this.documentsQty = documentsQty;
		this.maxLocalFrecuency = maxLocalFrecuency;
		this.documents = documents;
	}
	
	public IndexRecord(String term, InvertedList documents) {
		this.term = term; 
		this.documents = documents;
		calculateTermsStatistics();
	}

	public InvertedList getInvertedList() {
		return documents;
	}
	
	public void setInvertedList(InvertedList list){
		this.documents=list;
		this.calculateTermsStatistics();
	}
	
	public void calculateTermsStatistics(){
		if(this.documents==null){
			this.documents=new InvertedList();
		}
		this.documentsQty=this.documents.size();
		this.maxLocalFrecuency=this.documents.getMaxLocalFrecuency();
	}
	
	public double getTotalWeight(int totalDocuments){
		return Math.log10(totalDocuments/this.documentsQty);
	}
	
	//TODO: REVISAR PESO GLOBAL
	/**
	 * El documento mas importante es el mas raro, es decir el que aparezca en menos documentos. Por lo tanto el mas raro de los dos devuelve -1.
	 * @param other
	 * @return
	 */
	public int compareByTermImportance(IndexRecord other){
		if(this.documentsQty==other.documentsQty){
			return 0;
		}else{
			if(this.documentsQty<other.documentsQty){
				return -1;
			}else{
				return 1;
			}	
		}
	}
}
