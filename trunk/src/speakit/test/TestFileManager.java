package speakit.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import speakit.FileManager;

public class TestFileManager extends FileManager {

	private String	prefix;

	public TestFileManager(String prefix) {
		this.prefix = prefix;
		openedFiles = new HashMap<String, File>();
	}

	HashMap<String, File>	openedFiles;

	@Override
	public File openFile(String name) throws IOException {
		File result = null;
		// Hago esto porque cuando piden open file 2 veces en la misma prueba,
		// el openFile devuelve archivos distintos, pues createTempFile devuelve
		// files con nombres al azar
		if (openedFiles.containsKey(name)) {
			result = openedFiles.get(name);
		} else {
			result = File.createTempFile(name, prefix);
			super.setUp(result);
			openedFiles.put(name, result);
		}
		return result;
	}

	@Override
	public boolean exists(String name) {
		return this.openedFiles.containsKey(name);
	}
}
