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
	private List<TreeNodeElement> elements;
	private int nextSecuenceNodeNumber;

	public TreeLeafNode(Tree tree) {
		this(tree, 1);
	}

	public TreeLeafNode(Tree tree, int size) {
		super(tree, size);
		this.elements = new ArrayList<TreeNodeElement>();
	}

	@Override
	protected TreeNodeRecord createNodeRecord() {
		return new TreeLeafNodeRecord(this.getTree());
	}

	@Override
	public TreeNode createSibling() throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		return this.getTree().createLeafNodeAndSave();
	}

	@Override
	protected List<TreeNodeElement> getElements() {
		return this.elements;
	}

	@Override
	public int getLevel() {
		return 0;
	}

	// @Override
	// public List<BSharpTreeNodeElement> extractMinimumCapacityExcedent()
	// throws RecordSerializationException, IOException {
	// Stack<BSharpTreeNodeElement> stack = new Stack<BSharpTreeNodeElement>();
	//
	// // Extraigo todos los elementos que exceden a la capacidad mínima.
	// while (!this.isInUnderflow()) {
	// stack.add(this.record.extractLastElement());
	// }
	//
	// // Reinserto el último para estar por encima de la capacidad mínima.
	// this.record.insertElement(stack.pop());
	//
	// // Creo una lista con los elementos extraidos.
	// ArrayList<BSharpTreeNodeElement> result = new
	// ArrayList<BSharpTreeNodeElement>();
	// while (!stack.empty()) {
	// result.add(stack.pop());
	// }
	//
	// // Devuelvo la lista de elementos extraidos.
	// return result;
	// }

	public int getNextSecuenceNodeNumber() {
		return this.nextSecuenceNodeNumber;
	}

	@Override
	public Field getNodeKey() {
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
		String result = this.getNodeNumber() + ": ";
		for (TreeNodeElement element : this.elements) {
			result += "(" + element.getKey().toString() + ")";
		}
		return result;
	}
}