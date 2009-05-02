package speakit.ftrs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class MockAppearanceStorageImpl implements OccurrenceStorage {

	protected ArrayList<Occurrence>	appearances;

	public MockAppearanceStorageImpl() {
		appearances = new ArrayList<Occurrence>();
	}

	@Override
	public void addOccurrence(Occurrence app) {
		appearances.add(app);
	}

	@Override
	public ArrayList<Occurrence> getSortedAppearanceList() {
		Collections.sort(appearances);
		return appearances;
	}

	@Override
	public ArrayList<Occurrence> getApearanceListFor(int termId) {
		ArrayList<Occurrence> result = new ArrayList<Occurrence>();
		for (Iterator<Occurrence> iterator = appearances.iterator(); iterator.hasNext();) {
			Occurrence appearance = iterator.next();
			if (appearance.getTermId() == termId)
				result.add(appearance);
		}
		return result;
	}

}
