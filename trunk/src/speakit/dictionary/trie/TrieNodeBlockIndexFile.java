package speakit.dictionary.trie;

import java.io.File;
import java.io.IOException;


import speakit.io.record.IntegerField;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;

import speakit.io.recordfile.DirectRecordFile;


public class TrieNodeBlockIndexFile implements RecordFactory{
	
	DirectRecordFile<TrieNodeBlockIndex,IntegerField> nodeBlockIndexFile;
	
	public DirectRecordFile<TrieNodeBlockIndex, IntegerField> getNodeBlockIndexFile() {
		return nodeBlockIndexFile;
	}

	public void setNodeBlockIndexFile(
			DirectRecordFile<TrieNodeBlockIndex, IntegerField> nodeBlockIndexFile) {
		this.nodeBlockIndexFile = nodeBlockIndexFile;
	}

	public TrieNodeBlockIndexFile(File file) throws IOException {
		this.nodeBlockIndexFile = new DirectRecordFile<TrieNodeBlockIndex, IntegerField>(file, this);
	}
	
	@Override
	public Record<IntegerField> createRecord() {
		return new TrieNodeBlockIndex();
	}
	
	
	
	

}
