package speakit.io.bsharptree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;

@SuppressWarnings("unchecked")
public abstract class BSharpTreeNode {

	private final BSharpTree tree;
	private final int size;

	public BSharpTreeNode(BSharpTree tree, int size) {
		this.tree = tree;
		this.size = size;
	}

	public boolean contains(Field key) throws IOException, RecordSerializationException {
		return this.getRecord(key) != null;
	}

	public abstract Record getRecord(Field key) throws IOException, RecordSerializationException;

	public abstract void insertRecord(Record record) throws IOException, RecordSerializationException;

	public abstract int getLevel();

	private List<BSharpTreeNodeElement> extractExcedent(boolean upper) throws RecordSerializationException, IOException {
		Stack<BSharpTreeNodeElement> stack = new Stack<BSharpTreeNodeElement>();

		// Extraigo todos los elementos que exceden a la capacidad mínima.
		while (this.isInOverflow()) {
			if (upper) {
				stack.add(this.extractLastElement());
			} else {
				stack.add(this.extractFirstElement());
			}
		}

		// Creo una lista con los elementos extraidos.
		ArrayList<BSharpTreeNodeElement> result = new ArrayList<BSharpTreeNodeElement>();
		while (!stack.empty()) {
			result.add(stack.pop());
		}

		// Devuelvo la lista de elementos extraidos.
		return result;
	}

	
	
	public List<BSharpTreeNodeElement> extractUpperExcedent() throws RecordSerializationException, IOException {
		return this.extractExcedent(true);
	}

	public List<BSharpTreeNodeElement> extractLowerExcedent() throws RecordSerializationException, IOException {
		return this.extractExcedent(false);
	}

	public void passMaximumCapacityExcedentToTheRight(BSharpTreeNode rightNode) throws RecordSerializationException, IOException {
		rightNode.insertElements(this.extractUpperExcedent());
	}

	public void passMaximumCapacityExcedentToTheLeft(BSharpTreeNode leftNode) throws RecordSerializationException, IOException {
		leftNode.insertElements(this.extractLowerExcedent());
	}
	
	public void passMinimumCapacityExcedentToTheRight(BSharpTreeNode rightNode) throws RecordSerializationException, IOException {
		rightNode.insertElements(this.extractMinimumCapacityExcedent());
	}

	protected abstract BSharpTreeNodeElement extractLastElement();

	protected abstract BSharpTreeNodeElement extractFirstElement();

	public abstract void insertElements(List<BSharpTreeNodeElement> allRecords);

	public abstract List<BSharpTreeNodeElement> getElements();

	public abstract List<BSharpTreeNodeElement> extractMinimumCapacityExcedent() throws RecordSerializationException, IOException;

	protected abstract BSharpTreeNodeRecord getNodeRecord();

	public int getMaximumCapacity() {
		return this.tree.getNodeSize() * this.size;
	}

	public int getBlockQty() {
		return this.size;
	}

	public int getMinimumCapacity() {
		return this.getMaximumCapacity() * 2 / 3;
	}

	public boolean isInOverflow() throws RecordSerializationException, IOException {
		return (this.getNodeRecord().serialize().length > this.getMaximumCapacity());
	}

	public boolean isInUnderflow() throws RecordSerializationException, IOException {
		return (this.getNodeRecord().serialize().length < this.getMinimumCapacity());
	}

	protected BSharpTree getTree() {
		return this.tree;
	}
	
	protected int getSize() {
		return this.size;
	}

	public abstract int getNodeNumber();

	public abstract Field getNodeKey();

	public abstract List<BSharpTreeNodeElement> extractAllElements();
	
	public abstract BSharpTreeNode createSibling();
}
