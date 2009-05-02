package speakit.ftrs.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

//TODO implementar
public class InvertedList implements Iterable<InvertedListItem> {

	private ArrayList<InvertedListItem> items;

	public InvertedList() {
		items = new ArrayList<InvertedListItem>();
	}

	public InvertedList(ArrayList<InvertedListItem> items) {
		this.items = items;
	}

	public InvertedList sortByFrecuency() {
		InvertedListItem[] sortedByFrecuency = (InvertedListItem[]) items.toArray(new InvertedListItem[items.size()]);
		Comparator<? super InvertedListItem> c = new Comparator<InvertedListItem>() {

			@Override
			public int compare(InvertedListItem o1, InvertedListItem o2) {
				if (o1.getLocalFrecuency() == o2.getLocalFrecuency()) {
					return 0;
				} else {
					if (o1.getLocalFrecuency() > o2.getLocalFrecuency()) {
						return -1;
					} else {
						return 1;
					}
				}
			}

		};
		Arrays.sort(sortedByFrecuency, c);
		return new InvertedList(new ArrayList<InvertedListItem>(Arrays.asList(sortedByFrecuency)));
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
}
