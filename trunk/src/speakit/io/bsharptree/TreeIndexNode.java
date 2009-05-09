package speakit.io.bsharptree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.WrongBlockNumberException;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;

@SuppressWarnings("unchecked")
public class TreeIndexNode extends TreeNode {

	private int level = -1;
	private int leftChildNodeNumber;
	List<TreeNodeElement> elements;

	public TreeIndexNode(Tree tree,int nodeNumber, int size) {
		super(tree,nodeNumber, size);
		this.elements = new ArrayList<TreeNodeElement>();
	}

	public boolean balanceChilds() throws IOException {
		TreeNode lastNode = this.getTree().getNode(this.getLeftChildNodeNumber(), this);
		Iterator<TreeNodeElement> elementIt = this.getElements().iterator();
		while (elementIt.hasNext()) {
			TreeIndexNodeElement indexElement = (TreeIndexNodeElement) elementIt.next();
			TreeNode node = this.getTree().getNode(indexElement.getRightChildNodeNumber(), this);
			lastNode.passMaximumCapacityExcedentToTheRight(node);
			lastNode = node;
		}
		return childrenAreInOverflow();
	}

	private boolean childrenAreInOverflow() throws RecordSerializationException, IOException {
		TreeNode lastNode = this.getTree().getNode(this.getLeftChildNodeNumber(), this);
		if (lastNode.isInOverflow()) {
			return true;
		} else {
			Iterator<TreeNodeElement> elementIt = this.getElements().iterator();
			while (elementIt.hasNext()) {
				TreeIndexNodeElement indexElement = (TreeIndexNodeElement) elementIt.next();
				TreeNode node = this.getTree().getNode(indexElement.getRightChildNodeNumber(), this);
				if (node.isInOverflow()) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	protected TreeNodeRecord createNodeRecord(int nodeNumber) {
		return new TreeIndexNodeRecord(nodeNumber);
	}

	@Override
	public TreeNode createSibling() throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		return this.getTree().instantiateNewIndexNodeAndSave(this.getLevel());
	}

	private int getChildFor(Field key) {
		int childForKey = this.getLeftChildNodeNumber();
		Iterator<TreeNodeElement> it = this.elements.iterator();

		boolean found = false;
		while (it.hasNext() && !found) {
			TreeIndexNodeElement element = (TreeIndexNodeElement) it.next();
			if (key.compareTo((Field) element.getKey()) >= 0) {
				childForKey = element.getRightChildNodeNumber();
			} else {
				found = true;
			}
		}

		return childForKey;
	}

	private TreeNode[] getChildsOf(int elementIndex) throws IOException {
		TreeNode leftChild;
		if (elementIndex == 0) {
			leftChild = this.getTree().getNode(this.getLeftChildNodeNumber(), this);
		} else {
			TreeIndexNodeElement leftElement = (TreeIndexNodeElement) this.getElements().get(elementIndex - 1);
			leftChild = this.getTree().getNode(leftElement.getRightChildNodeNumber(), this);
		}
		TreeNode rightChild;
		TreeIndexNodeElement element = (TreeIndexNodeElement) this.getElements().get(elementIndex);
		rightChild = this.getTree().getNode(element.getRightChildNodeNumber(), this);

		return new TreeNode[] { leftChild, rightChild };
	}

	/**
	 * Devuelve la posición de la clave especificada dentro del array de
	 * elementos.
	 * 
	 * @param key
	 * @return
	 */
	private int getElementIndexOf(Field key) {
		List<TreeNodeElement> elements = this.elements;
		for (int i = 0; i < elements.size(); i++) {
			TreeNodeElement element = elements.get(i);
			if (element.getKey().compareTo(key) == 0) {
				return i;
			}
		}
		throw new IllegalArgumentException("La clave pasada no está en el array de elementos");
	}

	/**
	 * Devuelve el indice de la clave que está entre medio de el nodo overflow y
	 * el nodo con el que voy a splitear
	 * 
	 * @param node
	 * @return
	 */
	private int getElementIndexThatPointsToNode(TreeNode node) {
		if (this.getLeftChildNodeNumber() == node.getNodeNumber()) {
			return 0;
		} else {
			Iterator<TreeNodeElement> iterator = this.elements.iterator();
			int counter = 0;
			while (iterator.hasNext()) {
				TreeIndexNodeElement eachElement = (TreeIndexNodeElement) iterator.next();// casteo
				// porque
				// son
				// elementos
				// de
				// este
				// mismo
				// nodo
				if (eachElement.pointsTo(node)) {
					return counter;
				}
				counter++;
			}
		}
		throw new IllegalArgumentException("El nodo no es apuntado por este nodo.");
	}

	@Override
	protected List<TreeNodeElement> getElements() {
		return this.elements;
	}

	public int getLeftChildNodeNumber() {
		return this.leftChildNodeNumber;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public Field getNodeKey() {
		return this.getElements().get(0).getKey();
	}

	@Override
	public Record getRecord(Field key) throws IOException, RecordSerializationException {
		int nodeNumberWhereToSearch = this.getChildFor(key);
		TreeNode nodeWhereToInsert = this.getTree().getNode(nodeNumberWhereToSearch, this);
		return nodeWhereToInsert.getRecord(key);
	}

	public void indexChild(TreeNode newChild) {
		if (this.getLeftChildNodeNumber() == 0) {
			this.setLeftChildNodeNumber(newChild.getNodeNumber());
		} else {
			TreeIndexNodeElement element = new TreeIndexNodeElement();
			element.setKey(newChild.getNodeKey());
			element.setRightChild(newChild.getNodeNumber());
			this.insertElement(element);
		}
	}
	
	/**
	 * Inserta un registro recursivamente y balancea o splittea si hace falta
	 */
	
	@Override
	public void insertRecord(Record record) throws IOException, RecordSerializationException {
		int nodeNumberWhereToInsert = this.getChildFor(record.getKey());
		TreeNode nodeWhereToInsert = this.getTree().getNode(nodeNumberWhereToInsert, this);
		if(nodeWhereToInsert.getNodeNumber()==this.getNodeNumber()){
			System.out.println(this.getTree().toString());
			throw new RuntimeException("El nodeNumberWhereToInsert es el mismo nodo.");
		}
		
		nodeWhereToInsert.insertRecord(record);

		// TODO balanceo
		if (nodeWhereToInsert.isInOverflow()) {
			// ...some beauty code...
		}
		// split
		if (nodeWhereToInsert.isInOverflow()) {
			TreeNode overflowNode = nodeWhereToInsert;
			int elementIndexThatPointsToNode = getElementIndexThatPointsToNode(overflowNode);
			splitChildsOf(elementIndexThatPointsToNode);
		} else {
			this.getTree().updateNode(nodeWhereToInsert);
		}
	}

	@Override
	protected void load(TreeNodeRecord nodeRecord) {
		super.load(nodeRecord);
		this.level=((TreeIndexNodeRecord)nodeRecord).getLevel();
		TreeIndexNodeRecord indexNodeRecord = (TreeIndexNodeRecord) nodeRecord;
		this.leftChildNodeNumber = indexNodeRecord.getLeftChildNodeNumber();
		for (TreeNodeElement element : indexNodeRecord.getElements()) {
			elements.add(element);
		}
	}

	@Override
	protected void save(TreeNodeRecord nodeRecord) {
		super.save(nodeRecord);
		((TreeIndexNodeRecord)nodeRecord).setLevel(this.level);
		TreeIndexNodeRecord indexNodeRecord = (TreeIndexNodeRecord) nodeRecord;
		indexNodeRecord.setLeftChildNodeNumber(getLeftChildNodeNumber());
		for (TreeNodeElement element : elements) {
			indexNodeRecord.addElement(element);
		}
	}

	public void setLeftChildNodeNumber(int leftChildNodeNumber) {
		this.leftChildNodeNumber = leftChildNodeNumber;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	private void splitChildsOf(Field key) throws IOException {
		int elementIndex = this.getElementIndexOf(key);
		this.splitChildsOf(elementIndex);
	}

	private void splitChildsOf(int elementIndex) throws IOException {
		TreeNode[] childs = this.getChildsOf(elementIndex);
		TreeNode leftChild = childs[0];
		TreeNode rightChild = childs[1];

		this.removeElement(elementIndex);

		// this.getTree().createNode(this);
		TreeNode middleChild = leftChild.createSibling();
		leftChild.insertElements(rightChild.extractAllElements());
		leftChild.passMinimumCapacityExcedentToTheRight(middleChild);
		middleChild.passMinimumCapacityExcedentToTheRight(rightChild);

		this.indexChild(middleChild);
		this.indexChild(rightChild);

		this.getTree().updateNode(leftChild);
		this.getTree().updateNode(middleChild);
		this.getTree().updateNode(rightChild);
	}

	@Override
	public String toString() {
		String result = this.getNodeNumber() + ": " + this.getLeftChildNodeNumber();
		ArrayList<Integer> childNodes = new ArrayList<Integer>();
		childNodes.add(this.leftChildNodeNumber);
		for (TreeNodeElement element : this.elements) {
			TreeIndexNodeElement indexElement = (TreeIndexNodeElement) element;
			result += "(" + element.getKey().toString() + ")" + indexElement.getRightChildNodeNumber();
			childNodes.add(indexElement.getRightChildNodeNumber());
		}
		
		for (Integer nodeNumber : childNodes) {
			try {
				result+="\n\t"+this.getTree().getNode(nodeNumber, this).toString();
			} catch (IOException e) {
				result+="IOException(nodo:"+nodeNumber+")";
			}
		}
		
		return result;
	}
}