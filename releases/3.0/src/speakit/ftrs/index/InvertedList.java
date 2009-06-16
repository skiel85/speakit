package speakit.ftrs.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import speakit.io.record.ArrayField;

//TODO implementar
public class InvertedList extends ArrayField<TermOcurrence>   {
 

	public InvertedList() { 
	}

	public InvertedList(ArrayList<TermOcurrence> ocurrences) {
		this.setItems(ocurrences);
	}
	
	public void setItems(List<TermOcurrence> ocurrences){
		this.clear();
		for (TermOcurrence termOcurrence : ocurrences) {
			this.addItem(termOcurrence);
		}
	}

	public InvertedList sortByFrecuency() {
		List<TermOcurrence> list = this.getArray();
		Comparator<? super TermOcurrence> c = new Comparator<TermOcurrence>() {

			@Override
			public int compare(TermOcurrence o1, TermOcurrence o2) {
				return o1.compareByRelevance(o2);
			}

		}; 
		Collections.sort(list,c); 
		return new InvertedList(new ArrayList<TermOcurrence>(list));
	} 

	/**
	 * elimina las apariciones de términos que aparezcan menos de una cierta
	 * cantidad de veces y devuelve una lista nueva
	 */
	public InvertedList truncateByFrecuency(int minTermFrecuency) {
		InvertedList higherFrecuencyDocuments = new InvertedList();
		for (TermOcurrence item : this) {
			if (item.getLocalFrecuency() >= minTermFrecuency) {
				higherFrecuencyDocuments.add(item);
			}
		}
		return higherFrecuencyDocuments;
	}

	public List<Long> getDocuments() {
		List<Long> ids = new ArrayList<Long>();
		for (TermOcurrence item : this) {
			ids.add(item.getDocumentId());
		}
		return ids;
	}

	/**
	 * Agrega un elemento
	 * 
	 * @param item
	 * @return la misma lista, implementado por comodidad de uso.
	 */
	public InvertedList add(TermOcurrence item) {
		this.addItem(item);
		this.sort(); 
		return this;
	} 
	
	public int getMaxLocalFrecuency() {
		int maxFrecuency = -1;
		for (TermOcurrence item : this) {
			if (item.getLocalFrecuency() > maxFrecuency) {
				maxFrecuency = item.getLocalFrecuency();
			}
		}
		return maxFrecuency;
	}

	public boolean containsDocument(int docId) {
		for (TermOcurrence item : this) {
			if (item.getDocumentId() == docId) {
				return true;
			}
		}
		return false;
	}

	public boolean equals(InvertedList other) {
		if (this.size() != other.size()) {
			return false;
		}
		for (int i = 0; i < this.size(); i++) {
			TermOcurrence thisItem = this.get(i);
			TermOcurrence otherItem = other.get(i);

			if (!thisItem.equals(otherItem)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected TermOcurrence createField() {
		return new TermOcurrence(0,0);
	}
	
//	@Override
//	protected String getStringRepresentation() {
//		String result="InvertedList[" + this.size() + "]{";
//		for (Field field : this) {
//			result += "," + field.toString();	
//		}
//		result+="}";
//		return result;		
//	}
	
	

//	public InvertedList clone() {
//		TermOcurrence[] allItems = (TermOcurrence[]) items.toArray(new TermOcurrence[items.size()]);
//		InvertedList copy = new InvertedList();
//		copy.items.addAll(Arrays.asList(allItems));
//		return copy;
//	}
}
