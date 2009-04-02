package speakit.dictionary;

import java.io.IOException;

import speakit.audio.Audio;

public interface AudioDictionary {
	public void load() throws IOException;

	public void load(DictionaryFileSet fileSet) throws IOException;

	public void addEntry(String word, Audio audio) throws IOException;

	public boolean contains(String word) throws IOException;

	public Audio getAudio(String word) throws IOException;
}
