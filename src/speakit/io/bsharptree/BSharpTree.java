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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BSharpTree<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> implements RecordFile<RECTYPE, KEYTYPE> {
	private BSharpTreeNode root;
	private BasicBlockFile blockFile;

	public BSharpTree(File file) {
		this.blockFile = new BasicBlockFileImpl(file);
	}

	public void create(int nodeSize) throws IOException {
		this.blockFile.create(nodeSize);
		this.root = new BSharpTreeLeafNode(this);
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
		return (RECTYPE) this.root.getRecord(key);
	}

	@Override
	public void insertRecord(RECTYPE record) throws IOException, RecordSerializationException {
		this.root.insertRecord(record);
		
		if (this.root.isInOverflow()) {
			if (this.root.getLevel() == 0) {
				BSharpTreeLeafNode oldRoot = (BSharpTreeLeafNode) this.root;
				BSharpTreeIndexNode newRoot = new BSharpTreeIndexNode(this);
				ArrayList<BSharpTreeNode> leafs = new ArrayList<BSharpTreeNode>();
				leafs.add(new BSharpTreeLeafNode(this));
				leafs.add(new BSharpTreeLeafNode(this));
				leafs.add(new BSharpTreeLeafNode(this));

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

	public BSharpTreeNode getNode(int nodeNumberWhereToInsert) {
		throw new NotImplementedException();
	}
}
