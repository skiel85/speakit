package speakit.ftrs.index;

import java.util.ArrayList;

public class Index {

	protected ArrayList<IndexRecord> records;

	public boolean exists(String word) {
		return false;
	}

	public InvertedList getInversedList(String word) {
		return null;
	}

	public void updateRecords(ArrayList<IndexRecord> records) {
		//aca deberia, en el modelo final, agregarse las entradas, 
		//en el modelo de prueba, por lo menos, mergearse las coleccioness
		this.records = records;
	}
}
