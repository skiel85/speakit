package speakit.io.bsharptree.test;

import java.io.File;

import speakit.io.blockfile.BasicBlockFile;
import speakit.io.bsharptree.BSharpTree;
import speakit.io.record.Record;
import speakit.io.record.StringField;

public class TestBSharpTree extends BSharpTree<TestIndexRecord, StringField> {
	TestBSharpTree(File file) {
		super(file);
	}
	public BasicBlockFile getFile() {
		return this.blockFile;
	}

	public int getRootNoteBlocksQty() {
		return this.ROOT_NODE_BLOCKS_QTY;
	}

	@Override
	public Record createRecord() {
		return new TestIndexRecord("", 0);
	}
}

