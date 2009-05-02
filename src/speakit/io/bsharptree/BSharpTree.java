package speakit.io.bsharptree;

import java.io.File;
import java.io.IOException;

import speakit.io.blockfile.BasicBlockFile;
import speakit.io.blockfile.BasicBlockFileImpl;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.RecordFile;

public class BSharpTree<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> implements RecordFile<RECTYPE, KEYTYPE> {
	private BSharpTreeNode<RECTYPE, KEYTYPE> root;
	private BasicBlockFile blockFile;

	public BSharpTree(File file) {
		this.blockFile = new BasicBlockFileImpl(file);
	}

	public void create(int nodeSize) throws IOException {
		this.blockFile.create(nodeSize);
		this.root = new BSharpTreeLeafNode<RECTYPE, KEYTYPE>(this.blockFile);
	}

	public void load() throws IOException {
		this.blockFile.load();
		// TODO Cargar la raiz
	}

	@Override
	public boolean contains(KEYTYPE key) throws IOException, RecordSerializationException {
		return this.root.contains(key);
	}

	@Override
	public RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException {
		return this.root.getRecord(key);
	}

	@Override
	public void insertRecord(RECTYPE record) throws IOException, RecordSerializationException {
		this.root.insertRecord(record);
		if (this.root.isInOverflow()) {
			if (this.root.getLevel() == 0) {
//				BSharpTreeIndexNode<RECTYPE, KEYTYPE> newRoot = new BSharpTreeIndexNode<RECTYPE, KEYTYPE>(this.blockFile);
//				BSharpTreeLeafNode<RECTYPE, KEYTYPE>[] leafs;
//				leafs[0] = new BSharpTreeLeafNode<RECTYPE, KEYTYPE>(this.blockFile);
//				leafs[1] = new BSharpTreeLeafNode<RECTYPE, KEYTYPE>(this.blockFile);
//				leafs[2] = new BSharpTreeLeafNode<RECTYPE, KEYTYPE>(this.blockFile);
//
//				// leaf1.copyAllRecordsFrom(this.root);
//				this.root.balance(leafs);
//
//				newRoot.indexChild(leafs[0]);
//				newRoot.indexChild(leafs[1]);
//				newRoot.indexChild(leafs[2]);
//				this.root = newRoot;

			}
		}
	}

}
