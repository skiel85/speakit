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
public class BSharpTreeLeafNode extends BSharpTreeNode {
	private BSharpTreeLeafNodeRecord record;
	private RecordEncoder encoder;

	public BSharpTreeLeafNode(BSharpTree tree, int size, RecordEncoder encoder) {
		super(tree, size);
		this.encoder = encoder;
		this.record = new BSharpTreeLeafNodeRecord(tree, encoder);
	}

	@Override
	protected BSharpTreeNodeRecord getNodeRecord() {
		return this.record;
	}

	public BSharpTreeLeafNodeElement getElement(Field key) throws IOException, RecordSerializationException {
		Iterator<BSharpTreeNodeElement> it = this.record.getElements().iterator();
		while (it.hasNext()) {
			BSharpTreeLeafNodeElement element = (BSharpTreeLeafNodeElement) it.next();
			if (element.getRecord().compareToKey(key) == 0) {
				return (BSharpTreeLeafNodeElement) element;
			}
		}
		return null;
	}

	@Override
	public Record getRecord(Field key) throws IOException, RecordSerializationException {
		BSharpTreeLeafNodeElement element = this.getElement(key);
		if (element != null) {
			return element.getRecord();
		} else {
			return null;
		}
	}

	@Override
	public void insertRecord(Record record) throws IOException, RecordSerializationException {
		BSharpTreeLeafNodeElement element = new BSharpTreeLeafNodeElement(record);
		this.record.insertElement(element);
	}

	@Override
	public int getLevel() {
		return 0;
	}

	@Override
	public List<BSharpTreeNodeElement> getElements() {
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

	public void passOneElementTo(BSharpTreeLeafNode node) {
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
		return ((BSharpTreeLeafNodeElement) this.record.getElements().get(0)).getRecord().getKey();
	}

	@Override
	public void setNodeNumber(int nodeNumber) {
		this.record.setNodeNumber(nodeNumber);
	}

	@Override
	public int getNodeNumber() {
		return this.record.getNodeNumber();
	}

	@Override
	protected BSharpTreeNodeElement extractFirstElement() {
		return this.record.extractFirstElement();
	}

	@Override
	protected BSharpTreeNodeElement extractLastElement() {
		return this.record.extractLastElement();
	}

	@Override
	public BSharpTreeNode createSibling() throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		return this.getTree().createLeafNode();
	}

	@Override
	public List<BSharpTreeNodeElement> extractAllElements() {
		return this.record.extractAllElements();
	}

	@Override
	protected void insertElement(BSharpTreeNodeElement element) {
		this.record.insertElement(element);
	}

	@Override
	public String toString() {
		String result = this.getNodeNumber() + ": ";
		for (BSharpTreeNodeElement element : this.record.getElements()) {
			result += "(" + element.getKey().toString() + ")";
		}
		return result;
	}

}