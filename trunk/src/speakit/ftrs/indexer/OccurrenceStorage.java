package speakit.ftrs.indexer;

import java.util.ArrayList;


public interface OccurrenceStorage {

	public void addOccurrence(Occurrence occ);

	public ArrayList<Occurrence> getSortedAppearanceList();

	/**
	 * Devuelve la lista de appariciones, ordenadas, dado el nro de orden de
	 * aparicion de un termino
	 * 
	 * @param termId
	 * @return ArrayList<Appearance>
	 */
	public ArrayList<Occurrence> getApearanceListFor(int termId);
}
