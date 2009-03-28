package speakit.test;

import java.io.File;
import java.io.IOException;

import speakit.SpeakitFileSet;

public class SpeakitTestFileSet implements SpeakitFileSet {

	File audioFile;
	File audioIndexFile;

	public SpeakitTestFileSet() throws IOException {
		this.audioFile = File.createTempFile(this.getClass().getName(), ".dat");
		this.audioIndexFile = File.createTempFile(this.getClass().getName(), ".dat");
		this.audioFile.setWritable(true);
		this.audioIndexFile.setWritable(true);
		this.audioFile.createNewFile();
		this.audioIndexFile.createNewFile();
	}

	@Override
	public File getAudioFile() {
		return this.audioFile;
	}

	@Override
	public File getAudioIndexFile() {
		return this.audioIndexFile;
	}

	public void destroyFiles() {
		this.audioFile.delete();
		this.audioFile.delete();
	}
}
