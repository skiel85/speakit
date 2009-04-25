package speakit.ftrs;

import java.util.ArrayList;
import java.util.Collections;

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
		// TODO Auto-generated method stub
		Collections.sort(appearances);
		return appearances;
	}

}
