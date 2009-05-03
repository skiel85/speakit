package speakit.io.bsharptree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
		this.root = new BSharpTreeLeafNode<RECTYPE, KEYTYPE>(this);
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
				BSharpTreeLeafNode<RECTYPE, KEYTYPE> oldRoot = (BSharpTreeLeafNode<RECTYPE, KEYTYPE>) this.root;
				BSharpTreeIndexNode<RECTYPE, KEYTYPE> newRoot = new BSharpTreeIndexNode<RECTYPE, KEYTYPE>(this);
				ArrayList<BSharpTreeNode<RECTYPE, KEYTYPE>> leafs = new ArrayList<BSharpTreeNode<RECTYPE, KEYTYPE>>();
				leafs.add(new BSharpTreeLeafNode<RECTYPE, KEYTYPE>(this));
				leafs.add(new BSharpTreeLeafNode<RECTYPE, KEYTYPE>(this));
				leafs.add(new BSharpTreeLeafNode<RECTYPE, KEYTYPE>(this));

				leafs.get(0).insertElements((oldRoot.getElements()));
				this.root.balance(leafs);

				newRoot.indexChild(leafs.get(0));
				newRoot.indexChild(leafs.get(1));
				newRoot.indexChild(leafs.get(2));
				this.root = newRoot;
				
				if(leafs.get(0).isInOverflow() || leafs.get(1).isInOverflow() || leafs.get(2).isInOverflow()) {
					throw new RuntimeException("ERROR");
				}
			}
		}
	}

	public int getNodeSize() {
		return this.blockFile.getBlockSize();
	}
}
