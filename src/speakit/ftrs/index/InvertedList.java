package speakit.ftrs.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

//TODO implementar
public class InvertedList implements Iterable<InvertedListItem> {

	private ArrayList<InvertedListItem>	items;

	public InvertedList() {
		items = new ArrayList<InvertedListItem>();
	}

	public InvertedList(ArrayList<InvertedListItem> items) {
		this.items = items;
	}

	public InvertedList sortByFrecuency() {
		InvertedListItem[] sortedByFrecuency = sortItemsByFrecuency();
		return new InvertedList(new ArrayList<InvertedListItem>(Arrays.asList(sortedByFrecuency)));
	}

	private InvertedListItem[] sortItemsByFrecuency() {
		InvertedListItem[] sortedByFrecuency = (InvertedListItem[]) items.toArray(new InvertedListItem[items.size()]);
		Comparator<? super InvertedListItem> c = new Comparator<InvertedListItem>() {

			@Override
			public int compare(InvertedListItem o1, InvertedListItem o2) {
				return o1.compareByRelevance(o2);
			}

		};
		Arrays.sort(sortedByFrecuency, c);
		return sortedByFrecuency;
	}

	/**
	 * elimina las apariciones de términos que aparezcan menos de una cierta
	 * cantidad de veces y devuelve una lista nueva
	 */
	public InvertedList truncateByFrecuency(int minTermFrecuency) {
		InvertedList higherFrecuencyDocuments = new InvertedList();
		for (InvertedListItem item : items) {
			if (item.getLocalFrecuency() >= minTermFrecuency) {
				higherFrecuencyDocuments.add(item);
			}
		}
		return higherFrecuencyDocuments;
	}

	public List<Long> getDocuments() {
		List<Long> ids = new ArrayList<Long>();
		for (InvertedListItem item : items) {
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
	public InvertedList add(InvertedListItem item) {
		this.items.add(item);
		InvertedListItem[] sortedByFrecuency = sortItemsByFrecuency();
		this.items.clear();
		this.items.addAll(Arrays.asList(sortedByFrecuency));
		return this;
	}
	public int size() {
		return this.items.size();
	}

	@Override
	public Iterator<InvertedListItem> iterator() {
		return this.items.iterator();
	}

	public int getMaxLocalFrecuency() {
		int maxFrecuency = -1;
		for (InvertedListItem item : this.items) {
			if (item.getLocalFrecuency() > maxFrecuency) {
				maxFrecuency = item.getLocalFrecuency();
			}
		}
		return maxFrecuency;
	}

	public boolean containsDocument(int docId) {
		for (InvertedListItem item : this.items) {
			if (item.getDocumentId() == docId) {
				return true;
			}
		}
		return false;
	}

	public boolean equals(InvertedList other) {
		if (this.items.size() != other.items.size()) {
			return false;
		}
		for (int i = 0; i < this.items.size(); i++) {
			InvertedListItem thisItem = this.items.get(i);
			InvertedListItem otherItem = other.items.get(i);

			if (!thisItem.equals(otherItem)) {
				return false;
			}
		}
		return true;
	}

	public InvertedListItem get(int index) {
		return this.items.get(index);
	}

	public InvertedList clone() {
		InvertedListItem[] allItems = (InvertedListItem[]) items.toArray(new InvertedListItem[items.size()]);
		InvertedList copy = new InvertedList();
		copy.items.addAll(Arrays.asList(allItems));
		return copy;
	}
}
