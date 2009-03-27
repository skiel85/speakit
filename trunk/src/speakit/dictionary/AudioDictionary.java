package speakit.dictionary;

import java.io.IOException;

public interface AudioDictionary {
	public void addEntry(String word, byte[] audio) throws IOException;

	public boolean contains(String word) throws IOException;

	public byte[] getAudio(String word) throws IOException;
}
