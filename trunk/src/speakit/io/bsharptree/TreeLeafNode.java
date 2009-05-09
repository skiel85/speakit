package speakit.io.bsharptree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.WrongBlockNumberException;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;

@SuppressWarnings("unchecked")
public class TreeLeafNode extends TreeNode {
	private List<TreeNodeElement>	elements;
	private int						nextSecuenceNodeNumber;

	public TreeLeafNode(Tree tree, int nodeNumber) {
		this(tree, nodeNumber, 1);
	}

	public TreeLeafNode(Tree tree, int nodeNumber, int size) {
		super(tree, nodeNumber, size);
		this.elements = new ArrayList<TreeNodeElement>();
	}

	@Override
	protected TreeNodeRecord createNodeRecord(int nodeNumber) {
		return new TreeLeafNodeRecord(nodeNumber, this.getTree());
	}

	@Override
	public TreeNode createSibling() throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		return this.getTree().instantiateNewLeafNodeAndSave();
	}

	@Override
	protected List<TreeNodeElement> getElements() {
		return this.elements;
	}

	@Override
	public int getLevel() {
		return 0;
	}

	public int getNextSecuenceNodeNumber() {
		return this.nextSecuenceNodeNumber;
	}

	@Override
	public Field getNodeKey() {
		if (this.elements.size() == 0) {
			throw new RuntimeException("El nodo está vacío: " + this.toString());
		}
		return ((TreeLeafNodeElement) this.elements.get(0)).getRecord().getKey();
	}

	@Override
	public Record getRecord(Field key) throws IOException, RecordSerializationException {
		TreeLeafNodeElement element = (TreeLeafNodeElement) this.getElement(key);
		if (element != null) {
			return element.getRecord();
		} else {
			return null;
		}
	}

	@Override
	public void insertRecord(Record record) throws IOException, RecordSerializationException {
		TreeLeafNodeElement element = new TreeLeafNodeElement(record);
		this.insertElement(element);
	}

	@Override
	protected void load(TreeNodeRecord nodeRecord) {
		super.load(nodeRecord);
		TreeLeafNodeRecord leafNodeRecord = (TreeLeafNodeRecord) nodeRecord;
		this.nextSecuenceNodeNumber = leafNodeRecord.getNextSecuenceNodeNumber();
		for (TreeNodeElement element : leafNodeRecord.getElements()) {
			elements.add(element);
		}
	}

	public void passOneElementTo(TreeLeafNode node) {
		node.elements.add(this.extractLastElement());
	}

	@Override
	protected void save(TreeNodeRecord nodeRecord) {
		super.save(nodeRecord);
		TreeLeafNodeRecord leafNodeRecord = (TreeLeafNodeRecord) nodeRecord;
		leafNodeRecord.setNextSecuenceNodeNumber(getNextSecuenceNodeNumber());
		for (TreeNodeElement element : elements) {
			leafNodeRecord.addElement(element);
		}
	}

	public void setNextSecuenceNodeNumber(int nextSecuenceNodeNumber) {
		this.nextSecuenceNodeNumber = nextSecuenceNodeNumber;
	}

	@Override
	public String toString() {
		String result = formatNodeNumber(this.getNodeNumber()) + " L" +this.getLevel()+" "+ getUnderflowMark() +getItemCountString() + ":";
		for (TreeNodeElement element : this.elements) {
			result += "(" + element.getKey().toString() + ")";
		}
		return result;
	}

	public List<TreeNode> getChildren() throws IOException {
		return new ArrayList<TreeNode>();
	}
}