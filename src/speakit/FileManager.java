package speakit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

	ArrayList<File>	openedFiles;

	public FileManager() {
		openedFiles = new ArrayList<File>();
	}

	public File openFile(String name) throws IOException {
		File file = new File(name);
		//setUp(file);
		return file;
	}

	public boolean exists(String name) {
		File file = new File(name);
		return file.exists();
	}

	protected void setUp(File file) throws IOException {
		file.setWritable(true);
		file.createNewFile();
		this.openedFiles.add(file);
	}

	public void destroyFiles() {
		@SuppressWarnings("unused")
		boolean delete = false;
		for (File file : openedFiles) {
			delete = file.delete();
		}
	}

}
