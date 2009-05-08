package speakit.io.bsharptree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.WrongBlockNumberException;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;

@SuppressWarnings("unchecked")
public abstract class TreeNode {

	private final Tree tree;
	private final int size;
	private int nodeNumber;

	public TreeNode(Tree tree, int size) {
		this.tree = tree;
		this.size = size;
	}

	public boolean contains(Field key) throws IOException, RecordSerializationException {
		return this.getRecord(key) != null;
	}

	public abstract Record getRecord(Field key) throws IOException, RecordSerializationException;

	public abstract void insertRecord(Record record) throws IOException, RecordSerializationException;

	public abstract int getLevel();

	private List<TreeNodeElement> extractExcedent(boolean upper) throws RecordSerializationException, IOException {
		Stack<TreeNodeElement> stack = new Stack<TreeNodeElement>();

		// Extraigo todos los elementos que exceden a la capacidad mínima.
		while (this.isInOverflow()) {
			if (upper) {
				stack.add(this.extractLastElement());
			} else {
				stack.add(this.extractFirstElement());
			}
		}

		// Creo una lista con los elementos extraidos.
		ArrayList<TreeNodeElement> result = new ArrayList<TreeNodeElement>();
		while (!stack.empty()) {
			result.add(stack.pop());
		}

		// Devuelvo la lista de elementos extraidos.
		return result;
	}

	public List<TreeNodeElement> extractUpperExcedent() throws RecordSerializationException, IOException {
		return this.extractExcedent(true);
	}

	public List<TreeNodeElement> extractLowerExcedent() throws RecordSerializationException, IOException {
		return this.extractExcedent(false);
	}

	public void passMaximumCapacityExcedentToTheRight(TreeNode rightNode) throws RecordSerializationException, IOException {
		rightNode.insertElements(this.extractUpperExcedent());
	}

	public void passMaximumCapacityExcedentToTheLeft(TreeNode leftNode) throws RecordSerializationException, IOException {
		leftNode.insertElements(this.extractLowerExcedent());
	}

	public void passMinimumCapacityExcedentToTheRight(TreeNode rightNode) throws RecordSerializationException, IOException {
		rightNode.insertElements(this.extractMinimumCapacityExcedent());
	}

	public void insertElements(List<TreeNodeElement> elements) {
		for (TreeNodeElement sharpTreeNodeElement : elements) {
			this.insertElement(sharpTreeNodeElement);
		}
	}

	protected abstract void insertElement(TreeNodeElement element);

	protected abstract TreeNodeElement extractLastElement();

	protected abstract TreeNodeElement extractFirstElement();

	public abstract List<TreeNodeElement> getElements();

	// public abstract List<BSharpTreeNodeElement>
	// extractMinimumCapacityExcedent() throws RecordSerializationException,
	// IOException;

	public List<TreeNodeElement> extractMinimumCapacityExcedent() throws RecordSerializationException, IOException {
		Stack<TreeNodeElement> stack = new Stack<TreeNodeElement>();

		// Extraigo todos los elementos que exceden a la capacidad mínima.
		while (!this.isInUnderflow()) {
			stack.add(this.extractLastElement());
		}

		// Reinserto el último para estar por encima de la capacidad mínima.
		this.insertElement(stack.pop());

		// Creo una lista con los elementos extraidos.
		ArrayList<TreeNodeElement> result = new ArrayList<TreeNodeElement>();
		while (!stack.empty()) {
			result.add(stack.pop());
		}

		// Devuelvo la lista de elementos extraidos.
		return result;
	}

	/**
	 * Deprecado: No se debería tener necesidad de acceder al registro del nodo
	 * para obtener los valores de su estado.
	 */
	@Deprecated
	protected abstract TreeNodeRecord getNodeRecord();

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

	protected Tree getTree() {
		return this.tree;
	}

	protected int getSize() {
		return this.size;
	}

	public int getNodeNumber() {
		return this.nodeNumber;
	}

	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber = nodeNumber;
		this.getNodeRecord().setNodeNumber(nodeNumber);
	}

	public abstract Field getNodeKey();

	public abstract List<TreeNodeElement> extractAllElements();

	public abstract TreeNode createSibling() throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException;

	public byte[] serialize() throws RecordSerializationException, IOException {
		return this.getNodeRecord().serialize();
	}

	public void deserialize(byte[] data) throws RecordSerializationException, IOException {
		this.getNodeRecord().deserialize(data);
	}

	public List<byte[]> serializeInParts(int partSize) throws RecordSerializationException, IOException {
		return this.getNodeRecord().serializeInParts(partSize);
	}

	public void deserializeFromParts(List<byte[]> serializationParts) throws IOException {
		this.getNodeRecord().deserializeFromParts(serializationParts);
	}
}
