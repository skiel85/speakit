package speakit.io.bsharptree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import speakit.io.blockfile.BasicBlockFile;
import speakit.io.blockfile.BasicBlockFileImpl;
import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.WrongBlockNumberException;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.RecordFile;

@SuppressWarnings("unchecked")
public abstract class BSharpTree<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> implements RecordFile<RECTYPE, KEYTYPE>, RecordFactory {
	/**
	 * Indica la cantidad de bloques que ocupa la raiz
	 */
	protected static final int	ROOT_NODE_BLOCKS_QTY	= 2;

	protected BSharpTreeNode	root;
	protected BasicBlockFile	blockFile;
	protected RecordEncoder		encoder;
	public BSharpTree(File file, RecordEncoder encoder) {
		this.blockFile = new BasicBlockFileImpl(file);
		this.encoder = encoder;
	}

	public void create(int nodeSize) throws IOException {
		this.blockFile.create(nodeSize);
		this.blockFile.appendBlock();
		this.blockFile.appendBlock();
		this.root = new BSharpTreeLeafNode(this, ROOT_NODE_BLOCKS_QTY, encoder);
		this.saveNode(this.root);
		this.load();
	}

	public void saveNode(BSharpTreeNode node) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		Record nodeRecord = node.getNodeRecord();
		List<byte[]> serializationParts = nodeRecord.serializeInParts(this.getNodeSize());
		if (serializationParts.size() > node.getBlockQty()) {
			throw new BlockFileOverflowException(serializationParts.size() * this.getNodeSize(), node.getBlockQty() * this.getNodeSize());
		}
		for (int i = 0; i < serializationParts.size(); i++) {
			byte[] part = serializationParts.get(i);
			this.blockFile.write(i + node.getNodeNumber(), part);
			// TODO falta el offset que es el numero de bloque
		}
	}

	public void load() throws IOException {
		this.blockFile.load();
		this.root = this.getNode(0, null);
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
	public long insertRecord(RECTYPE record) throws IOException, RecordSerializationException {
		this.root.insertRecord(record);
		// TODO implementar balanceo y split para el caso de que quede en
		// overflow luego de insertar.
		if (false) {
			if (this.root.isInOverflow()) {
				if (this.root.getLevel() == 0) {
					BSharpTreeLeafNode oldRoot = (BSharpTreeLeafNode) this.root;
					BSharpTreeIndexNode newRoot = new BSharpTreeIndexNode(this, 1);
					ArrayList<BSharpTreeNode> leafs = new ArrayList<BSharpTreeNode>();
					leafs.add(new BSharpTreeLeafNode(this, 1, encoder));
					leafs.add(new BSharpTreeLeafNode(this, 1, encoder));
					leafs.add(new BSharpTreeLeafNode(this, 1, encoder));

					leafs.get(0).insertElements((oldRoot.getElements()));
					// this.root.balance(leafs);

					newRoot.indexChild(leafs.get(0));
					newRoot.indexChild(leafs.get(1));
					newRoot.indexChild(leafs.get(2));
					this.root = newRoot;

					if (leafs.get(0).isInOverflow() || leafs.get(1).isInOverflow() || leafs.get(2).isInOverflow()) {
						throw new RuntimeException("ERROR");
					}
				}
			}
		}
		this.saveNode(this.root);
		return 0;
	}

	public int getNodeSize() {
		return this.blockFile.getBlockSize();
	}

	/**
	 * 
	 * @param nodeNumber
	 * @param parent
	 * @return
	 * @throws IOException
	 */
	public BSharpTreeNode getNode(int nodeNumber, BSharpTreeNode parent) throws IOException {
		BSharpTreeNode newNode = createNode(nodeNumber, parent);
		return deserializeNode(nodeNumber, newNode);
	}

	/**
	 * Crea un nodo raiz o no raiz
	 * 
	 * @param nodeNumber
	 * @param parent
	 * @return
	 * @throws IOException
	 */
	private BSharpTreeNode createNode(int nodeNumber, BSharpTreeNode parent) throws IOException {
		if (nodeNumber == 0) {
			return this.createRootNode();
		} else {
			return this.createNonRootNode(parent.getLevel());
		}
	}

	/**
	 * Crea la raiz. Para saber de que tipo es se fija la cantidad de bloques
	 * del archivo: Si cant > cant nodos que ocupa la raiz --> La raiz es nodo
	 * indice Si cant = cant nodos que ocupa la raiz --> La raiz es nodo hoja
	 * 
	 * @return
	 * @throws IOException
	 */
	private BSharpTreeNode createRootNode() throws IOException {
		int blockCount = this.blockFile.getBlockCount();
		if (blockCount == ROOT_NODE_BLOCKS_QTY) {
			// Es un nodo hoja
			return new BSharpTreeLeafNode(this, ROOT_NODE_BLOCKS_QTY, encoder);
		} else {
			// nodo indice
			return new BSharpTreeIndexNode(this, ROOT_NODE_BLOCKS_QTY);
		}
	}

	/**
	 * Fabrica de nodos no hojas a partir del nivel
	 */
	private BSharpTreeNode createNonRootNode(int level) {
		if (level == 0) {
			return new BSharpTreeLeafNode(this, 1, encoder);
		} else if (level > 0) {
			return new BSharpTreeIndexNode(this, 1);
		}
		throw new IllegalArgumentException("No se puede construir un nodo con level: " + level);
	}

	private BSharpTreeNode deserializeNode(int nodeNumber, BSharpTreeNode newNode) throws IOException, RecordSerializationException {
		List<byte[]> parts = new ArrayList<byte[]>();
		for (int i = 0; i < newNode.getBlockQty(); i++) {
			parts.add(blockFile.read(nodeNumber + i));
		}
		newNode.getNodeRecord().deserializeFromParts(parts);
		return newNode;
	}

	public BSharpTreeNode getRoot() {
		return root;
	}

	protected void setRoot(BSharpTreeNode root) {
		this.root = root;
	}

	public int getRootNoteBlocksQty() {
		return ROOT_NODE_BLOCKS_QTY;
	}

	public BasicBlockFile getBlockFile() {
		return this.blockFile;
	}

}
