package speakit.dictionary.trie;

import java.util.ArrayList;
import java.util.List;

import speakit.io.record.ArrayField;
import speakit.io.record.LongField;
import speakit.io.record.Record;

public class TrieNode extends Record<LongField> {

	// private ArrayList<WordOffsetField> WordOffsetRecordList;
	// private long nodeNumber;

	private LongField					nodeNumber=new LongField();
	private ArrayField<WordOffsetField>	wordOffsetList=new ArrayField<WordOffsetField>();

	public TrieNode(ArrayList<WordOffsetField> wordOffsetList, long nodeNumber) {
		this(nodeNumber);
		for (WordOffsetField item : wordOffsetList) {
			this.wordOffsetList.addItem(item);
		}
	}
	
	public TrieNode() {
		this.setKey(this.nodeNumber);
		this.addField(this.nodeNumber);
		this.addField(this.wordOffsetList);
	}
	
	public TrieNode(long nodeNumber) {
		this();
		this.nodeNumber.setLong(nodeNumber);
	}
	
	public List<WordOffsetField> getWordOffsetList() {
		return wordOffsetList.getArray();
	}

	public void setWordOffsetRecordList(List<WordOffsetField> wordOffsetList) {
		this.wordOffsetList.setArray(wordOffsetList);
	}

	public long getNodeNumber() {
		return this.nodeNumber.getLong();
	}

	public void setNodeNumber(long nodeNumber) {
		this.nodeNumber.setLong(nodeNumber);
	}

}