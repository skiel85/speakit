package speakit.dictionary;

import java.io.IOException;

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

}
