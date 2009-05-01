package speakit.dictionary.trie;

import java.util.ArrayList;

public class TrieNode {
	
	private ArrayList<WordOffsetRecord> WordOffsetRecordList;
	private long nodeNumber;
	
	public TrieNode(ArrayList<WordOffsetRecord> wordOffsetRecordList,
			long nodeNumber) {
		
		WordOffsetRecordList = wordOffsetRecordList;
		this.nodeNumber = nodeNumber;
	}
	
	public TrieNode(long nodeNumber) {
		
		WordOffsetRecordList = new ArrayList<WordOffsetRecord>();
		this.nodeNumber = nodeNumber;
	}

	public ArrayList<WordOffsetRecord> getWordOffsetRecordList() {
		return WordOffsetRecordList;
	}

	public void setWordOffsetRecordList(
			ArrayList<WordOffsetRecord> wordOffsetRecordList) {
		WordOffsetRecordList = wordOffsetRecordList;
	}

	public long getNodeNumber() {
		return nodeNumber;
	}

	public void setNodeNumber(long nodeNumber) {
		this.nodeNumber = nodeNumber;
	}
	

}