package speakit.dictionary;

import java.io.IOException;

import speakit.Configuration;
import speakit.FileManager;
import speakit.audio.Audio;

/**
 * Clase Dummy q implementa AudioDictionary
 * 
 * @author
 * 
 */
public class MockAudioDictionary implements AudioDictionary {

	@Override
	public void addEntry(String word, Audio audio) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(String word) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Audio getAudio(String word) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void load(FileManager fileManager, Configuration conf) throws IOException {
		// TODO Auto-generated method stub

	}

}
