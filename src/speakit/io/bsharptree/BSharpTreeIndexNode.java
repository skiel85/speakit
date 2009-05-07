package speakit.io.bsharptree;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@SuppressWarnings("unchecked")
public class BSharpTreeIndexNode extends BSharpTreeNode {

	private BSharpTreeIndexNodeRecord record;

	public BSharpTreeIndexNode(BSharpTree tree, int size) {
		super(tree, size);
		this.record = new BSharpTreeIndexNodeRecord();
	}

	@Override
	protected BSharpTreeNodeRecord getNodeRecord() {
		return this.record;
	}

	@Override
	public Record getRecord(Field key) throws IOException, RecordSerializationException {
		int nodeNumberWhereToSearch = this.getChildFor(key);
		BSharpTreeNode nodeWhereToInsert = this.getTree().getNode(nodeNumberWhereToSearch, this);
		return nodeWhereToInsert.getRecord(key);
	}

	@Override
	public void insertRecord(Record record) throws IOException, RecordSerializationException {
		int nodeNumberWhereToInsert = this.getChildFor(record.getKey());
		BSharpTreeNode nodeWhereToInsert = this.getTree().getNode(nodeNumberWhereToInsert, this);
		nodeWhereToInsert.insertRecord(record);
	}

	private int getChildFor(Field key) {
		int childForKey = this.record.getLeftChildNodeNumber();
		Iterator<BSharpTreeIndexNodeElement> it = this.record.getElementsIterator();

		boolean found = false;
		while (it.hasNext() && !found) {
			BSharpTreeIndexNodeElement element = it.next();
			if (key.compareTo((Field) element.getKey()) > 0) {
				childForKey = element.getRightChildNodeNumber();
			} else {
				found = true;
			}
		}

		return childForKey;
	}

	public void insertElement(BSharpTreeIndexNodeElement element) {
		this.record.insertElement(element);
	}

	public BSharpTreeIndexNodeElement getElement(Field key) throws IOException, RecordSerializationException {
		Iterator<BSharpTreeNodeElement> it = this.record.getElements().iterator();
		while (it.hasNext()) {
			BSharpTreeIndexNodeElement element = (BSharpTreeIndexNodeElement) it.next();
			if (element.getKey().compareTo(key) == 0) {
				return element;
			}
		}
		return null;
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public void indexChild(BSharpTreeNode newChild) {
		if (this.getLeftChildNodeNumber() == 0) {
			this.record.setLeftChildNodeNumber(newChild.getNodeNumber());
		} else {
			BSharpTreeIndexNodeElement element = new BSharpTreeIndexNodeElement();
			element.setKey(newChild.getNodeKey());
			element.setRightChild(newChild.getNodeNumber());
			this.insertElement(element);
		}
	}

	@Override
	public List<BSharpTreeNodeElement> getElements() {
		return this.record.getElements();
	}

	@Override
	public void insertElements(List<BSharpTreeNodeElement> elements) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public List<BSharpTreeNodeElement> extractMinimumCapacityExcedent() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public void setNodeNumber(int i) {
		this.record.setNodeNumber(i);
	}

	public int getLeftChildNodeNumber() {
		return this.record.getLeftChildNodeNumber();
	}

	@Override
	public Field getNodeKey() {
		return this.getElements().get(0).getKey();
	}

	@Override
	public int getNodeNumber() {
		return this.record.getNodeNumber();
	}

	@Override
	protected BSharpTreeNodeElement extractFirstElement() {
		return record.extractFirstElement();
	}

	@Override
	protected BSharpTreeNodeElement extractLastElement() {
		return record.extractLastElement();
	}

	public boolean balanceChilds() throws IOException {
		BSharpTreeNode lastNode = this.getTree().getNode(this.getLeftChildNodeNumber(), this);
		Iterator<BSharpTreeNodeElement> elementIt = this.getElements().iterator();
		while (elementIt.hasNext()) {
			BSharpTreeIndexNodeElement indexElement = (BSharpTreeIndexNodeElement) elementIt.next();
			BSharpTreeNode node = this.getTree().getNode(indexElement.getRightChildNodeNumber(), this);
			lastNode.passMaximumCapacityExcedentToTheRight(node);
			lastNode = node;
		}
		return childrenAreInOverflow();
	}

	private boolean childrenAreInOverflow() throws RecordSerializationException, IOException {
		BSharpTreeNode lastNode = this.getTree().getNode(this.getLeftChildNodeNumber(), this);
		if (lastNode.isInOverflow()) {
			return true;
		} else {
			Iterator<BSharpTreeNodeElement> elementIt = this.getElements().iterator();
			while (elementIt.hasNext()) {
				BSharpTreeIndexNodeElement indexElement = (BSharpTreeIndexNodeElement) elementIt.next();
				BSharpTreeNode node = this.getTree().getNode(indexElement.getRightChildNodeNumber(), this);
				if (node.isInOverflow()) {
					return true;
				}
			}
			return false;
		}
	}

	private void splitChildsOf(int elementIndex) throws IOException {
		BSharpTreeNode[] childs = this.getChildsOf(elementIndex);
		BSharpTreeNode leftChild = childs[0];
		BSharpTreeNode rightChild = childs[1];

		this.record.removeElement(elementIndex);
		
		BSharpTreeNode middleChild = leftChild.createSibling();
		leftChild.insertElements(rightChild.extractAllElements());
		leftChild.passMinimumCapacityExcedentToTheRight(middleChild);
		middleChild.passMinimumCapacityExcedentToTheRight(rightChild);
		
		this.indexChild(middleChild);
		this.indexChild(rightChild);

	}

	private BSharpTreeNode[] getChildsOf(int elementIndex) throws IOException {
		BSharpTreeNode leftChild;
		if (elementIndex == 0) {
			leftChild = this.getTree().getNode(this.record.getLeftChildNodeNumber(), this);
		} else {
			BSharpTreeIndexNodeElement leftElement = (BSharpTreeIndexNodeElement) this.getElements().get(elementIndex - 1);
			leftChild = this.getTree().getNode(leftElement.getRightChildNodeNumber(), this);
		}
		BSharpTreeNode rightChild;
		BSharpTreeIndexNodeElement element = (BSharpTreeIndexNodeElement) this.getElements().get(elementIndex);
		rightChild = this.getTree().getNode(element.getRightChildNodeNumber(), this);

		return new BSharpTreeNode[] { leftChild, rightChild };
	}

	@Override
	public List<BSharpTreeNodeElement> extractAllElements() {
		return this.record.extractAllElements();
	}

	@Override
	public BSharpTreeNode createSibling() {
		return new BSharpTreeIndexNode(this.getTree(), this.getSize());
	}

}