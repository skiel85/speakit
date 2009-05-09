package speakit.io.bsharptree;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.WrongBlockNumberException;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;

/**
 * Nodo del árbol B#.
 */
@SuppressWarnings("unchecked")
public abstract class TreeNode {

	private final Tree tree;
	private final int size;
	private int nodeNumber;

	public TreeNode(Tree tree,int nodeNumber, int size) {
		this.nodeNumber = nodeNumber;
		this.tree = tree;
		this.size = size;
	}

	public final void clearElements() {
		this.getElements().clear();
	}

	public boolean contains(Field key) throws IOException, RecordSerializationException {
		return this.getRecord(key) != null;
	}

	protected abstract TreeNodeRecord createNodeRecord(int nodeNumber);

	public abstract TreeNode createSibling() throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException;

	public void deserialize(byte[] data, int nodeNumber) throws RecordSerializationException, IOException {
		TreeNodeRecord nodeRecord = this.createNodeRecord(nodeNumber);		
		nodeRecord.deserialize(data);
		this.load(nodeRecord);
	}

	public void deserializeFromParts(List<byte[]> serializationParts, int nodeNumber) throws IOException {
		TreeNodeRecord nodeRecord = this.createNodeRecord(nodeNumber);
		nodeRecord.deserializeFromParts(serializationParts);
		this.load(nodeRecord);
	}

	public final List<TreeNodeElement> extractAllElements() {
		List<TreeNodeElement> result = new ArrayList<TreeNodeElement>(this.getElements());
		this.getElements().clear();
		//this.clearElements();
		return result;
	}

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

	public final TreeNodeElement extractFirstElement() {
		TreeNodeElement element = this.getElements().get(0);
		this.getElements().remove(0);
		return element;
	}

	public final TreeNodeElement extractLastElement() {
		if(this.getElements().size()>0){
			TreeNodeElement element = this.getElements().get(this.getElements().size() - 1);
			this.getElements().remove(this.getElements().size() - 1);
			return element;	
		}else{
			throw new RuntimeException("Tree: el nodo "+this.getNodeNumber()+" está vacío y no puede estarlo, no es posible hacer extractLastElement.");
		}
	}

	public List<TreeNodeElement> extractLowerExcedent() throws RecordSerializationException, IOException {
		return this.extractExcedent(false);
	}

	public List<TreeNodeElement> extractMinimumCapacityExcedent() throws RecordSerializationException, IOException {
		Stack<TreeNodeElement> stack = new Stack<TreeNodeElement>();

		// Extraigo todos los elementos que exceden a la capacidad mínima.
		while (!this.isInUnderflow()) {
			stack.add(this.extractLastElement());
		}

		// Reinserto el último para estar por encima de la capacidad mínima.
		if(!stack.isEmpty()) {
			this.insertElement(stack.pop());
		}
		else {
			throw new AssertionError("No hay excedente a la mínima capacidad del nodo.");
		}

		// Creo una lista con los elementos extraidos.
		ArrayList<TreeNodeElement> result = new ArrayList<TreeNodeElement>();
		while (!stack.empty()) {
			result.add(stack.pop());
		}

		// Devuelvo la lista de elementos extraidos.
		return result;
	}

	/**
	 * Devuelve la minima cantidad de elementos q me dejen el nodo por debajo del valor de underflow
	 * @return
	 * @throws RecordSerializationException
	 * @throws IOException
	 */
	public List<TreeNodeElement> extractUpperExcedent() throws RecordSerializationException, IOException {
		return this.extractExcedent(true);
	}

	public int getBlockQty() {
		return this.size;
	}

	public TreeNodeElement getElement(Field key) throws IOException, RecordSerializationException {
		Iterator<TreeNodeElement> it = this.getElements().iterator();
		while (it.hasNext()) {
			TreeNodeElement element = it.next();
			if (element.getKey().compareTo(key) == 0) {
				return element;
			}
		}
		return null;
	}

	// public abstract List<BSharpTreeNodeElement>
	// extractMinimumCapacityExcedent() throws RecordSerializationException,
	// IOException;

	public final TreeNodeElement getElement(int index) {
		return this.getElements().get(index);
	}

	public int getElementCount() {
		return this.getElements().size();
	}

	protected abstract List<TreeNodeElement> getElements();

	public final Iterator<TreeNodeElement> getElementsIterator() {
		return this.getElements().iterator();
	}

	public abstract int getLevel();

	public int getMaximumCapacity() {
		return this.tree.getNodeSize() * this.size;
	}

	public int getMinimumCapacity() {
		return this.getMaximumCapacity() * 2 / 3;
	}

	public abstract Field getNodeKey();

	public int getNodeNumber() {
		return this.nodeNumber;
	}

	public abstract Record getRecord(Field key) throws IOException, RecordSerializationException;

	protected int getSize() {
		return this.size;
	}

	protected Tree getTree() {
		return this.tree;
	}

	public void insertElement(TreeNodeElement element) {
		this.getElements().add((TreeNodeElement) element);
		Collections.sort(this.getElements());
	}

	public void insertElements(List<TreeNodeElement> elements) {
		for (TreeNodeElement sharpTreeNodeElement : elements) {
			this.insertElement(sharpTreeNodeElement);
		}
	}

	public abstract void insertRecord(Record record) throws IOException, RecordSerializationException;

	public final boolean isInOverflow() throws RecordSerializationException, IOException {
		return (this.serialize().length > this.getMaximumCapacity());
	}

	public final boolean isInUnderflow() throws RecordSerializationException, IOException {
		return (this.serialize().length < this.getMinimumCapacity());
	}

	protected void load(TreeNodeRecord nodeRecord) {
		this.nodeNumber = nodeRecord.getNodeNumber();
	}

	public void passMaximumCapacityExcedentToTheLeft(TreeNode leftNode) throws RecordSerializationException, IOException {
		leftNode.insertElements(this.extractLowerExcedent());
	}

	public void passMaximumCapacityExcedentToTheRight(TreeNode rightNode) throws RecordSerializationException, IOException {
		rightNode.insertElements(this.extractUpperExcedent());
	}

	public void passMinimumCapacityExcedentToTheRight(TreeNode rightNode) throws RecordSerializationException, IOException {
		rightNode.insertElements(this.extractMinimumCapacityExcedent());
	}

	protected void removeElement(int elementIndex) {
		this.getElements().remove(elementIndex);
	}

	protected void save(TreeNodeRecord nodeRecord) {
		nodeRecord.setNodeNumber(nodeNumber);
	}

	private TreeNodeRecord saveToRecord() {
		TreeNodeRecord nodeRecord = this.createNodeRecord(this.nodeNumber);
		this.save(nodeRecord);
		return nodeRecord;
	}

	public byte[] serialize() throws RecordSerializationException, IOException {
		return saveToRecord().serialize();
	}

	public List<byte[]> serializeInParts(int partSize) throws RecordSerializationException, IOException {
		return saveToRecord().serializeInParts(partSize);
	}

	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber = nodeNumber;
	}

	protected String formatNodeNumber(int number) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumIntegerDigits(2);		
		return numberFormat.format(number);
	}
	
	protected String getUnderflowMark() {
		try {
			return (this.isInUnderflow()?"(underflow)":"");
		}  catch (IOException e) {
			return "(error calculando underflow)";
		}
	}
	
	protected String getItemCountString() {
		return "("+this.getElementCount()+")";
	}

	public abstract List<TreeNode> getChildren() throws IOException;

	public abstract void updateRecord(Record record) throws IOException;
}
