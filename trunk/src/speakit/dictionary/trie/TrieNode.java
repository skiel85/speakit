package speakit.dictionary.trie;

import java.util.ArrayList;
import java.util.List;

import speakit.io.record.ArrayField;
import speakit.io.record.Field;
import speakit.io.record.LongField;
import speakit.io.record.Record;

public class TrieNode extends Record<LongField> {

	// private ArrayList<WordOffsetField> WordOffsetRecordList;
	// private long nodeNumber;

	private LongField nodeNumber = new LongField();
	private ArrayField<WordOffsetField> wordOffsetList = new ArrayField<WordOffsetField>(){

		@Override
		protected WordOffsetField createField() {
			return new WordOffsetField();
		}
		
	};

	public TrieNode(ArrayList<WordOffsetField> wordOffsetList, long nodeNumber) {
		this(nodeNumber);
		for (WordOffsetField item : wordOffsetList) {
			this.wordOffsetList.addItem(item);
		}
	}

	public TrieNode() {
		// Dejado intencionalmente en blanco.
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

	@Override
	protected Field[] getFields() {
		return new Field[] { this.nodeNumber, this.wordOffsetList };
	}

	@Override
	public LongField getKey() {
		return this.nodeNumber;
	}

}