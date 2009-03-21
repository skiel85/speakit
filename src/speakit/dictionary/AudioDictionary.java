package speakit.dictionary;

public interface AudioDictionary {
	public void addEntry(String word, byte[] audio);
	public boolean contains(String word);
	public byte[] getAudio(String word);
}
