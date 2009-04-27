package speakit.ftrs.index;

import java.util.ArrayList;

//TODO implementar
public class InvertedList {
 
	private ArrayList<InvertedListItem> items;


	public InvertedList() {
		items = new ArrayList<InvertedListItem>();
	}
	
	public InvertedList(ArrayList<InvertedListItem> items) {
		this.items = items;
	}
	


	public void sortByWeight() {
		// TODO Auto-generated method stub

	}

	public void truncateByFrecuency(int minTermFrecuency) {
		// TODO Auto-generated method stub

	}

	public ArrayList<Long> getDocuments() {
		ArrayList<Long> ids = new ArrayList<Long>();
		for (InvertedListItem item : items) {
			ids.add(item.getDocumentId());
		}
		return ids;
	}
	
	public int size() {
		return items.size();
	}
	
	public InvertedList add(InvertedListItem item){
		this.items.add(item);
		return this;
	}
}
