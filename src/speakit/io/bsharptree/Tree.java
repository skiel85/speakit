package speakit.io.bsharptree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import speakit.io.blockfile.BasicBlockFile;
import speakit.io.blockfile.BasicBlockFileImpl;
import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.WrongBlockNumberException;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.io.recordfile.RecordFile;

@SuppressWarnings("unchecked")
public class Tree<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> implements RecordFile<RECTYPE, KEYTYPE>, RecordFactory {
	/**
	 * Indica la cantidad de bloques que ocupa la raiz
	 */
	protected static final int	ROOT_NODE_BLOCKS_QTY		= 2;
	protected static final int	NON_ROOT_NODE_BLOCKS_QTY	= 1;

	protected TreeNode			root;
	protected BasicBlockFile	blockFile;
	protected RecordEncoder		encoder;

	private RecordFactory		recordFactory;

	public Tree(File file, RecordFactory recordFactory) {
		this(file, recordFactory, new DefaultRecordEncoder(recordFactory));
	}

	public Tree(File file, RecordFactory recordFactory, RecordEncoder encoder) {
		this.recordFactory = recordFactory;
		this.blockFile = new BasicBlockFileImpl(file);
		this.encoder = encoder;
	}

	/**
	 * Reserva el espacio necesario para el nodo y le asigna un numero
	 * 
	 * @param node
	 * @throws IOException
	 * @returns el numero de nodo nuevo
	 */
	protected int allocateNodeSpace(int blockQty) throws IOException {
		int blockNumber = appendBlock();
		for (int i = 1; i < blockQty; i++) {
			this.appendBlock();
		}
		return blockNumber;
	}

	private int appendBlock() throws IOException {
		return this.blockFile.appendBlock();
	}

	@Override
	public boolean contains(KEYTYPE key) throws IOException, RecordSerializationException {
		return this.root.contains(key);
	}

	public void create(int nodeSize) throws IOException {
		this.blockFile.create(nodeSize);
		allocateNodeSpace(ROOT_NODE_BLOCKS_QTY);
		this.root = instantiateRootNode(0);
		this.updateNode(this.root);
		this.load();
	}

	@Override
	public Record createRecord() {
		return recordFactory.createRecord();
	}

	private TreeNode deserializeNode(int nodeNumber, TreeNode newNode) throws IOException, RecordSerializationException {
		List<byte[]> parts = new ArrayList<byte[]>();
		for (int i = 0; i < newNode.getBlockQty(); i++) {
			parts.add(blockFile.read(nodeNumber + i));
		}
		newNode.deserializeFromParts(parts, nodeNumber);
		return newNode;
	}

	public BasicBlockFile getBlockFile() {
		return this.blockFile;
	}

	public RecordEncoder getEncoder() {
		return this.encoder;
	}

	/**
	 * 
	 * @param nodeNumber
	 * @param parent
	 * @return
	 * @throws IOException
	 */
	public TreeNode getNode(int nodeNumber, TreeNode parent) throws IOException {
		if (nodeNumber == 0) {
			return this.root;
		} else {
			TreeNode node;
			if (parent.getLevel() == 1) {
				node = new TreeLeafNode(this, nodeNumber, 1);
			} else if (parent.getLevel() > 1) {
				node = new TreeIndexNode(this, nodeNumber, 1);
			} else {
				throw new IllegalArgumentException("No se puede construir un nodo con padre con level: " + parent.getLevel());
			}
			return deserializeNode(nodeNumber, node);
		}
	}

	public int getNodeSize() {
		return this.blockFile.getBlockSize();
	}

	@Override
	public RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException {
		return (RECTYPE) this.root.getRecord(key);
	}

	public TreeNode getRoot() {
		return root;
	}

	public int getRootNoteBlocksQty() {
		return ROOT_NODE_BLOCKS_QTY;
	}
 
	@Override
	public long insertRecord(RECTYPE record) throws IOException, RecordSerializationException {
		if(record.getKey().compareTo(new StringField("salud"))==0){
			//sarasa
			int a=0;
		}
		this.root.insertRecord(record);
		
		// TODO implementar balanceo y split para el caso de que quede en
		// overflow luego de insertar.

		if (this.root.isInOverflow()) {
			splitRootNode(record);
		}else{
			this.updateNode(this.root);
		}
		return 0;
	}

	private void splitRootNode(RECTYPE record) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		if (this.root.getLevel() == 0) {
			//Splitea la raiz cuando es un nodo hoja
			splitRootLeafNode(record);
		}else{
			//Splitea la raiz cuando es un nodo indice
			splitRootIndexNode(record);
		}
	}

	private void splitRootLeafNode(RECTYPE record) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		TreeNode oldRoot = (TreeNode) this.root;
		this.root = this.instantiateRootNode(oldRoot.getLevel() + 1);

		ArrayList<TreeNode> leafs = new ArrayList<TreeNode>();
		leafs.add(this.instantiateNewLeafNodeAndSave());
		leafs.add(this.instantiateNewLeafNodeAndSave());
		leafs.add(this.instantiateNewLeafNodeAndSave());

		leafs.get(0).insertElements((oldRoot.extractAllElements()));
		leafs.get(0).passMinimumCapacityExcedentToTheRight(leafs.get(1));
		leafs.get(1).passMinimumCapacityExcedentToTheRight(leafs.get(2));

		((TreeIndexNode) this.root).indexChild(leafs.get(0));
		((TreeIndexNode) this.root).indexChild(leafs.get(1));
		((TreeIndexNode) this.root).indexChild(leafs.get(2));

		if (leafs.get(0).isInOverflow() || leafs.get(1).isInOverflow() || leafs.get(2).isInOverflow()) {
			throw new RuntimeException("ERROR: No se pudo hacer el split al insertar el registro (" + record + "). Un nodo quedó en overflow. Pruebe agrandando el tamaño de bloques.");
		}

		this.updateNode(leafs.get(0));
		this.updateNode(leafs.get(1));
		this.updateNode(leafs.get(2));
		this.updateNode(this.root);
	}

	private void splitRootIndexNode(RECTYPE record) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		/**
		 * Descripción:
		 * -Extrae todos los hijos de la raiz y los deja en una lista temporal
		 * -crea 3 nodos nuevos que serán hijos de la raiz
		 * -para cada nodo{
		 * --indexa viejos hijos de la raiz en cada nuevo nodo y los va eliminando de la lista temporal
		 * --indexa el nuevo nodo en la raiz
		 * -}
		 * -guarda todos los nodos nuevos
		 * -guarda la raiz
		 * 
		 */
//		System.out.println("Before split. Record = ("+record.toString()+"): \n" + this.toString());
		
		ArrayList<TreeIndexNode> newRootChilds = new ArrayList<TreeIndexNode>();
		newRootChilds.add((TreeIndexNode) this.instantiateNewIndexNodeAndSave(this.root.getLevel()));
		newRootChilds.add((TreeIndexNode) this.instantiateNewIndexNodeAndSave(this.root.getLevel()));
		newRootChilds.add((TreeIndexNode) this.instantiateNewIndexNodeAndSave(this.root.getLevel()));
		
		List<TreeNode> oldRootChildsTempList = ((TreeIndexNode) this.root).getExtractChildNodes();
		
		Iterator<TreeNode> oldRootChildsTempListIterator = oldRootChildsTempList.iterator();
		//recorro los nuevos nodos que acabo de crear, serán los nuevos hijos de root
		for (TreeIndexNode newRootChild : newRootChilds) {
			//mientras el nodos esté en underflow le indexo hijos viejos de root y lo elimino de la lista temporal
			while(newRootChild.isInUnderflow() && oldRootChildsTempListIterator.hasNext()){
				newRootChild.indexChild(oldRootChildsTempListIterator.next());
				oldRootChildsTempListIterator.remove();
			}
			//indexo el nuevo hijo de la raiz
			((TreeIndexNode) this.root).indexChild(newRootChild);
		}
		((TreeIndexNode) this.root).setLevel(this.root.getLevel()+1);
		
		for (TreeIndexNode newRootChild : newRootChilds) {
			this.updateNode(newRootChild);
		}
		this.updateNode(this.root);
//				System.out.println("After split. Record = ("+record.toString()+"): \n" + this.toString());
	}

	public TreeNode instantiateNewIndexNodeAndSave(int level) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		int newNodeNumber = allocateNodeSpace(NON_ROOT_NODE_BLOCKS_QTY);
		TreeIndexNode node = new TreeIndexNode(this, newNodeNumber, 1);
		node.setLevel(level);
		this.updateNode(node);
		return node;
	}

	public TreeNode instantiateNewLeafNodeAndSave() throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		int newNodeNumber = allocateNodeSpace(NON_ROOT_NODE_BLOCKS_QTY);
		TreeNode node = new TreeLeafNode(this, newNodeNumber);
		this.updateNode(node);
		return node;
	}

	// Creacion y cargado de nodos
	protected TreeNode instantiateRootNode(int level) {
		// la raiz siempre tiene numero de nodo 0
		TreeNode node = null;
		if (level == 0) {
			node = new TreeLeafNode(this, 0, ROOT_NODE_BLOCKS_QTY);
		} else if (level >= 0) {
			node = new TreeIndexNode(this, 0, ROOT_NODE_BLOCKS_QTY);
			((TreeIndexNode) node).setLevel(level);
		} else {
			throw new IllegalArgumentException("Condicion Level>0, pero vino: " + level);
		}
		return node;
	}

	/**
	 * Carga el archivo Carga el nodo raiz
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException {
		this.blockFile.load();
		if (this.blockFile.getBlockCount() > Tree.ROOT_NODE_BLOCKS_QTY) {
			// hay mas nodos que una raiz --> la raiz es un indice
			this.root = new TreeIndexNode(this, 0, Tree.ROOT_NODE_BLOCKS_QTY);
		} else {
			if (this.blockFile.getBlockCount() == Tree.ROOT_NODE_BLOCKS_QTY) {
				// hay solamente un nodo raiz --> la raiz es hoja
				this.root = new TreeLeafNode(this, 0, Tree.ROOT_NODE_BLOCKS_QTY);
			} else {
				throw new RecordSerializationException("Archivo de B# Tree invÃ¡lido.");
			}
		}
		// la raiz siempre tiene numero de nodo 0
		deserializeNode(0, this.root);
	}

	protected void setRoot(TreeNode root) {
		this.root = root;
	}

	@Override
	public String toString() {
		return "Raiz: " + this.getRoot().toString();
	}

	/**
	 * Guarda el nodo en el archivo
	 */
	public void updateNode(TreeNode node) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		if (node.getLevel() == 0 && node instanceof TreeIndexNode) {
			throw new RuntimeException("Un nodo indice no puede tener nivel 0. Nodo: " + node.toString());
		}
		List<byte[]> serializationParts = node.serializeInParts(this.getNodeSize());
		if (serializationParts.size() > node.getBlockQty()) {
			throw new BlockFileOverflowException("La cantidad de partes es mayor a la esperada.", node.getBlockQty(), serializationParts.size());
		}
		for (int i = 0; i < serializationParts.size(); i++) {
			byte[] part = serializationParts.get(i);
			this.blockFile.write(i + node.getNodeNumber(), part);
			// TODO falta el offset que es el numero de bloque
		}
	}

	public void updateRecord(Record record) throws IOException {
		this.root.updateRecord(record);		
	}
	
}
