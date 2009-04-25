package speakit.ftrs;

import java.util.ArrayList;

public interface AppearanceStorage {
	
	public void addAppeareance(Appearance app);
	
	public ArrayList<Appearance> getSortedAppearanceList();
}
