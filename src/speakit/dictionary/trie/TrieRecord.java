package speakit.dictionary.trie;

public class TrieRecord {
	long recordNumber;
	String word;
	long nextRecord;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public long getNextRecord() {
		return nextRecord;
	}
	public void setNextRecord(long nextRecord) {
		this.nextRecord = nextRecord;
	}
	
	public TrieRecord(long recordNumber ,long nextRecord, String word) {
		
		this.recordNumber = recordNumber;
		this.nextRecord = nextRecord;
		this.word = word;
	}
}
