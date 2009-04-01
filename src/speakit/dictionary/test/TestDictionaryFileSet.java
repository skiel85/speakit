package speakit.dictionary.test;

import java.io.File;
import java.io.IOException;

import speakit.dictionary.DictionaryFileSet;

public class TestDictionaryFileSet implements DictionaryFileSet{
	File	audioFile;
	File	audioIndexFile; 

	public TestDictionaryFileSet() throws IOException
	{ 
		this.audioFile = File.createTempFile(this.getClass().getName(), ".dat");
		this.audioIndexFile =  File.createTempFile(this.getClass().getName(), ".dat");

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
	
	public void close(){
		audioFile.delete();
		audioIndexFile.delete();
	}
	
	public void destroyFiles(){
		audioFile.delete();
		audioIndexFile.delete();
	}
}