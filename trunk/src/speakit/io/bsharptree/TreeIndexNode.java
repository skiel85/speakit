package speakit.io.bsharptree;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.WrongBlockNumberException;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;

@SuppressWarnings("unchecked")
public class TreeIndexNode extends TreeNode {
	private TreeIndexNodeRecord record;

	public TreeIndexNode(Tree tree, int size) {
		super(tree, size);
		this.record = new TreeIndexNodeRecord();
	}

	@Override
	protected TreeNodeRecord getNodeRecord() {
		return this.record;
	}

	@Override
	public Record getRecord(Field key) throws IOException, RecordSerializationException {
		int nodeNumberWhereToSearch = this.getChildFor(key);
		TreeNode nodeWhereToInsert = this.getTree().getNode(nodeNumberWhereToSearch, this);
		return nodeWhereToInsert.getRecord(key);
	}

	/**
	 * Inserta un registro recursivamente y balancea o splittea si hace falta
	 */
	@Override
	public void insertRecord(Record record) throws IOException, RecordSerializationException {
		int nodeNumberWhereToInsert = this.getChildFor(record.getKey());
		TreeNode nodeWhereToInsert = this.getTree().getNode(nodeNumberWhereToInsert, this);
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
			this.getTree().saveNode(nodeWhereToInsert);
		}
	}

	/**
	 * Devuelve el indice de la clave que está entre medio de el nodo overflow y
	 * el nodo con el que voy a splitear
	 * 
	 * @param node
	 * @return
	 */
	private int getElementIndexThatPointsToNode(TreeNode node) {
		if (this.record.getLeftChildNodeNumber() == node.getNodeNumber()) {
			return 0;
		} else {
			Iterator<TreeNodeElement> iterator = this.record.getElements().iterator();
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

	private int getChildFor(Field key) {
		int childForKey = this.record.getLeftChildNodeNumber();
		Iterator<TreeIndexNodeElement> it = this.record.getElementsIterator();

		boolean found = false;
		while (it.hasNext() && !found) {
			TreeIndexNodeElement element = it.next();
			if (key.compareTo((Field) element.getKey()) > 0) {
				childForKey = element.getRightChildNodeNumber();
			} else {
				found = true;
			}
		}

		return childForKey;
	}

	public TreeIndexNodeElement getElement(Field key) throws IOException, RecordSerializationException {
		Iterator<TreeNodeElement> it = this.record.getElements().iterator();
		while (it.hasNext()) {
			TreeIndexNodeElement element = (TreeIndexNodeElement) it.next();
			if (element.getKey().compareTo(key) == 0) {
				return element;
			}
		}
		return null;
	}

	@Override
	public int getLevel() {
		return 80;
	}

	public void indexChild(TreeNode newChild) {
		if (this.getLeftChildNodeNumber() == 0) {
			this.record.setLeftChildNodeNumber(newChild.getNodeNumber());
		} else {
			TreeIndexNodeElement element = new TreeIndexNodeElement();
			element.setKey(newChild.getNodeKey());
			element.setRightChild(newChild.getNodeNumber());
			this.insertElement(element);
		}
	}

	@Override
	public List<TreeNodeElement> getElements() {
		return this.record.getElements();
	}

	public int getLeftChildNodeNumber() {
		return this.record.getLeftChildNodeNumber();
	}

	@Override
	public Field getNodeKey() {
		return this.getElements().get(0).getKey();
	}

	@Override
	protected TreeNodeElement extractFirstElement() {
		return record.extractFirstElement();
	}

	@Override
	protected TreeNodeElement extractLastElement() {
		return record.extractLastElement();
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

	private void splitChildsOf(Field key) throws IOException {
		int elementIndex = this.getElementIndexOf(key);
		this.splitChildsOf(elementIndex);
	}

	/**
	 * Devuelve la posición de la clave especificada dentro del array de
	 * elementos.
	 * 
	 * @param key
	 * @return
	 */
	private int getElementIndexOf(Field key) {
		List<TreeNodeElement> elements = this.record.getElements();
		for (int i = 0; i < elements.size(); i++) {
			TreeNodeElement element = elements.get(i);
			if (element.getKey().compareTo(key) == 0) {
				return i;
			}
		}
		throw new IllegalArgumentException("La clave pasada no está en el array de elementos");
	}

	private void splitChildsOf(int elementIndex) throws IOException {
		TreeNode[] childs = this.getChildsOf(elementIndex);
		TreeNode leftChild = childs[0];
		TreeNode rightChild = childs[1];

		this.record.removeElement(elementIndex);

		// this.getTree().createNode(this);
		TreeNode middleChild = leftChild.createSibling();
		leftChild.insertElements(rightChild.extractAllElements());
		leftChild.passMinimumCapacityExcedentToTheRight(middleChild);
		middleChild.passMinimumCapacityExcedentToTheRight(rightChild);

		this.indexChild(middleChild);
		this.indexChild(rightChild);

		this.getTree().saveNode(leftChild);
		this.getTree().saveNode(middleChild);
		this.getTree().saveNode(rightChild);
	}

	private TreeNode[] getChildsOf(int elementIndex) throws IOException {
		TreeNode leftChild;
		if (elementIndex == 0) {
			leftChild = this.getTree().getNode(this.record.getLeftChildNodeNumber(), this);
		} else {
			TreeIndexNodeElement leftElement = (TreeIndexNodeElement) this.getElements().get(elementIndex - 1);
			leftChild = this.getTree().getNode(leftElement.getRightChildNodeNumber(), this);
		}
		TreeNode rightChild;
		TreeIndexNodeElement element = (TreeIndexNodeElement) this.getElements().get(elementIndex);
		rightChild = this.getTree().getNode(element.getRightChildNodeNumber(), this);

		return new TreeNode[] { leftChild, rightChild };
	}

	@Override
	public List<TreeNodeElement> extractAllElements() {
		return this.record.extractAllElements();
	}

	@Override
	public TreeNode createSibling() throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		return this.getTree().createIndexNodeAndSave(this.getLevel());
	}

	@Override
	public void insertElement(TreeNodeElement element) {
		this.record.insertElement(element);
	}
	
	@Override
	public String toString() {
		String result = this.getNodeNumber() + ": " + this.record.getLeftChildNodeNumber();
		for (TreeNodeElement element : this.record.getElements()) {
			TreeIndexNodeElement indexElement = (TreeIndexNodeElement) element;
			result += "(" + element.getKey().toString() + ")" + indexElement.getRightChildNodeNumber();
		}
		return result;
	}
 
	public int setLevel(int level) {
		return this.record.getLevel();
	}

}