package speakit.dictionary.trie;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.IntegerField;

public class TrieNodeBlockIndex extends Record<IntegerField>{
	
	public TrieNodeBlockIndex() {
	}

	public TrieNodeBlockIndex(IntegerField blockNumber, IntegerField nodeNumber) {
		this.blockNumber = blockNumber;
		this.nodeNumber = nodeNumber;
	}

	private IntegerField nodeNumber = new IntegerField();
	private IntegerField blockNumber = new IntegerField();
	
	public int getNodeNumber() {
		return nodeNumber.getInteger();
	}

	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber.setInteger(nodeNumber);
	}

	public int getBlockNumber() {
		return blockNumber.getInteger();
	}

	public void setBlockNumber(int blockNumber) {
		this.blockNumber.setInteger(blockNumber);
	}
	
	@Override
	protected Field[] getFields() {
		return new Field[] { this.nodeNumber, this.blockNumber };
		
	}

	@Override
	public IntegerField getKey() {
		return this.nodeNumber;
	}

	@Override
	protected String getStringRepresentation() {
		return "TrieNodeBlockIndex{nodeNumber:"+this.nodeNumber.toString()+",blockNumber:"+this.blockNumber.toString()+"}";
	}

}
