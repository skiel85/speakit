package speakit.ftrs;

import java.util.ArrayList;

public interface AppearanceStorage {

	public void addAppeareance(Appearance app);

	public ArrayList<Appearance> getSortedAppearanceList();
	
	/**
	 * Devuelve la lista de appariciones, ordenadas, dado el nro de orden de aparicion
	 * de un termino
	 * @param termId
	 * @return ArrayList<Appearance>
	 */
	public ArrayList<Appearance> getApearanceListFor(int termId);
}
