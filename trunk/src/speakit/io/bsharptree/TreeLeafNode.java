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
public class TreeLeafNode extends TreeNode {
	private TreeLeafNodeRecord record;

	public TreeLeafNode(Tree tree) {
		this(tree, 1);
	}
	
	public TreeLeafNode(Tree tree, int size) {
		super(tree, size);
		this.record = new TreeLeafNodeRecord(tree, size);
	}

	@Override
	protected TreeNodeRecord getNodeRecord() {
		return this.record;
	}

	public TreeLeafNodeElement getElement(Field key) throws IOException, RecordSerializationException {
		Iterator<TreeNodeElement> it = this.record.getElements().iterator();
		while (it.hasNext()) {
			TreeLeafNodeElement element = (TreeLeafNodeElement) it.next();
			if (element.getRecord().compareToKey(key) == 0) {
				return (TreeLeafNodeElement) element;
			}
		}
		return null;
	}

	@Override
	public Record getRecord(Field key) throws IOException, RecordSerializationException {
		TreeLeafNodeElement element = this.getElement(key);
		if (element != null) {
			return element.getRecord();
		} else {
			return null;
		}
	}

	@Override
	public void insertRecord(Record record) throws IOException, RecordSerializationException {
		TreeLeafNodeElement element = new TreeLeafNodeElement(record);
		this.record.insertElement(element);
	}

	@Override
	public int getLevel() {
		return 0;
	}

	@Override
	public List<TreeNodeElement> getElements() {
		return this.record.getElements();
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

	public void passOneElementTo(TreeLeafNode node) {
		node.record.getElements().add(this.record.extractLastElement());
	}

	@Override
	public boolean isInOverflow() throws RecordSerializationException, IOException {
		return (this.record.serialize().length > this.getMaximumCapacity());
	}

	@Override
	public boolean isInUnderflow() throws RecordSerializationException, IOException {
		return (this.record.serialize().length < this.getMinimumCapacity());
	}

	@Override
	public Field getNodeKey() {
		return ((TreeLeafNodeElement) this.record.getElements().get(0)).getRecord().getKey();
	}

	@Override
	protected TreeNodeElement extractFirstElement() {
		return this.record.extractFirstElement();
	}

	@Override
	protected TreeNodeElement extractLastElement() {
		return this.record.extractLastElement();
	}

	@Override
	public TreeNode createSibling() throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		return this.getTree().createLeafNodeAndSave();
	}

	@Override
	public List<TreeNodeElement> extractAllElements() {
		return this.record.extractAllElements();
	}

	@Override
	protected void insertElement(TreeNodeElement element) {
		this.record.insertElement(element);
	}

	@Override
	public String toString() { 
		return record.toString();
	}  

}