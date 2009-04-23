package speakit.ftrs.index;

import java.util.ArrayList;

public class Index {

	protected ArrayList<IndexRecord> records = new ArrayList<IndexRecord>();

	public boolean exists(String word) {
		return false;
	}

	public InversedList getInversedList(String word) {
		return null;
	}

	public void updateAppearances(ArrayList<IndexRecord> records) {
		this.records.addAll(records);
	}
}
