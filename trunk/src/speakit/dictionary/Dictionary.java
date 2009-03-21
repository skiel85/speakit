package speakit.dictionary;

public interface Dictionary {
	public void addEntry(String word, byte[] audio);
	public boolean contains(String word);
	public byte[] getAudio(String word);
}
