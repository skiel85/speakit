package speakit.dictionary;

import java.io.IOException;

/**
 * Clase Dummy q implementa AudioDictionary
 * 
 * @author
 * 
 */
public class MockAudioDictionary implements AudioDictionary {

	@Override
	public void addEntry(String word, byte[] audio) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(String word) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] getAudio(String word) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
