package speakit.ftrs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class MockAppearanceStorageImpl implements AppearanceStorage {

	protected ArrayList<Appearance> appearances;

	public MockAppearanceStorageImpl() {
		appearances = new ArrayList<Appearance>();
	}

	@Override
	public void addAppeareance(Appearance app) {
		appearances.add(app);
	}

	@Override
	public ArrayList<Appearance> getSortedAppearanceList() {
		Collections.sort(appearances);
		return appearances;
	}

	@Override
	public ArrayList<Appearance> getApearanceListFor(int termId) {
		ArrayList<Appearance> result = new ArrayList<Appearance>();
		for (Iterator<Appearance> iterator = appearances.iterator(); iterator.hasNext();) {
			Appearance appearance = iterator.next();
			if(appearance.getTermId() == termId)
				result.add(appearance);
		}
		return result;
	}

}
