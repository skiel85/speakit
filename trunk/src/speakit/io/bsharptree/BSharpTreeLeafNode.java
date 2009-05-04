package speakit.io.bsharptree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BSharpTreeLeafNode<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> extends BSharpTreeNode<RECTYPE, KEYTYPE> {

	private BSharpTreeLeafNodeRecord record;
	private BSharpTree<RECTYPE, KEYTYPE> tree;
	private int size;

	public BSharpTreeLeafNode(BSharpTree<RECTYPE, KEYTYPE> tree) {
		this.tree = tree;
	}

	public BSharpTreeLeafNode(BSharpTree<RECTYPE, KEYTYPE> tree, int size) {
		this(tree);
		this.size = size;
	}

	@Override
	public boolean contains(KEYTYPE key) throws IOException, RecordSerializationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertRecord(RECTYPE record) throws IOException, RecordSerializationException {
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
	public void balance(List<BSharpTreeNode<RECTYPE, KEYTYPE>> nodes) {
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
		
		//Extraigo todos los elementos que exceden a la capacidad mínima.
		while (this.record.serialize().length > this.getMinimumCapacity()){
			stack.add(this.record.extractLastElement());
		}
		
		//Reinserto el último para estar por encima de la capacidad mínima.
		this.record.getElements().add(stack.pop());
		
		//Creo una lista con los elementos extraidos.
		ArrayList<BSharpTreeNodeElement> result = new ArrayList<BSharpTreeNodeElement>();
		while(!stack.empty()){
			result.add(stack.pop());
		}
		
		//Devuelvo la lista de elementos extraidos.
		return result;
	}
	
	public void passOneElementTo(BSharpTreeLeafNode<RECTYPE, KEYTYPE> node) {
		node.record.getElements().add(this.record.extractLastElement());
	}
}