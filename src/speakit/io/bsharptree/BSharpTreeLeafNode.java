package speakit.io.bsharptree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BSharpTreeLeafNode extends BSharpTreeNode {

	private BSharpTreeLeafNodeRecord record;
	private BSharpTree tree;
	private int size;

	public BSharpTreeLeafNode(BSharpTree tree) {
		this.tree = tree;
		this.record = new BSharpTreeLeafNodeRecord();
	}

	public BSharpTreeLeafNode(BSharpTree tree, int size) {
		this(tree);
		this.size = size;
	}

	@Override
	public boolean contains(Field key) throws IOException, RecordSerializationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Record getRecord(Field key) throws IOException, RecordSerializationException {
		Iterator<BSharpTreeNodeElement> it = this.record.getElements().iterator();
		while (it.hasNext()) {
			BSharpTreeLeafNodeElement element = (BSharpTreeLeafNodeElement) it.next();
			if (element.getRecord().compareToKey(key) == 0) {
				return (Record) element.getRecord();
			}
		}
		return null;
	}

	@Override
	public void insertRecord(Record record) throws IOException, RecordSerializationException {
		BSharpTreeLeafNodeElement element = new BSharpTreeLeafNodeElement(record);
		this.record.insertElement(element);
	}

	@Override
	public boolean isInOverflow() throws RecordSerializationException, IOException {
		return (this.record.serialize().length > this.getMaximumCapacity());
	}

	@Override
	public int getLevel() {
		return 0;
	}

	@Override
	public void balance(List<BSharpTreeNode> nodes) {
		throw new NotImplementedException();
	}

	@Override
	public List<BSharpTreeNodeElement> getElements() {
		return this.record.getElements();
	}

	@Override
	public void insertElements(List<BSharpTreeNodeElement> allRecords) {
		// TODO Auto-generated method stub

	}

	public int getMaximumCapacity() {
		return this.tree.getNodeSize() * this.size;
	}

	public int getMinimumCapacity() {
		return this.getMaximumCapacity() * 2 / 3;
	}

	@Override
	public List<BSharpTreeNodeElement> extractMinimumCapacityExcedent() throws RecordSerializationException, IOException {
		Stack<BSharpTreeNodeElement> stack = new Stack<BSharpTreeNodeElement>();

		// Extraigo todos los elementos que exceden a la capacidad mínima.
		while (this.record.serialize().length > this.getMinimumCapacity()) {
			stack.add(this.record.extractLastElement());
		}

		// Reinserto el último para estar por encima de la capacidad mínima.
		this.record.getElements().add(stack.pop());

		// Creo una lista con los elementos extraidos.
		ArrayList<BSharpTreeNodeElement> result = new ArrayList<BSharpTreeNodeElement>();
		while (!stack.empty()) {
			result.add(stack.pop());
		}

		// Devuelvo la lista de elementos extraidos.
		return result;
	}

	public void passOneElementTo(BSharpTreeLeafNode node) {
		node.record.getElements().add(this.record.extractLastElement());
	}
}