package speakit.io.bsharptree;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BSharpTreeIndexNode<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> extends BSharpTreeNode<RECTYPE, KEYTYPE> {

	private BSharpTreeIndexNodeRecord record;
	private final BSharpTree<RECTYPE, KEYTYPE> tree;

	public BSharpTreeIndexNode(BSharpTree<RECTYPE, KEYTYPE> tree) {
		this.tree = tree;
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
		int nodeNumberWhereToInsert = this.record.getLeftChildNodeNumber();
		Iterator<BSharpTreeIndexNodeElement> it = this.record.getElementsIterator();
		
		boolean found = false;
		while(it.hasNext() && !found) {
			BSharpTreeIndexNodeElement element = it.next();
			if(record.compareToKey((KEYTYPE) element.getKey())>0) {
				nodeNumberWhereToInsert=element.getRightChildNodeNumber();
			}
			else {
				found = true;
			}
		}
		
		BSharpTreeNode nodeWhereToInsert = this.tree.getNode(nodeNumberWhereToInsert);
		nodeWhereToInsert.insertRecord(record);
	}

	@Override
	public boolean isInOverflow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getLevel() {
		throw new NotImplementedException();
	}

	@Override
	public void balance(List<BSharpTreeNode<RECTYPE, KEYTYPE>> nodes) {
		throw new NotImplementedException();
	}

	public void indexChild(BSharpTreeNode<RECTYPE, KEYTYPE> newChild) {
		throw new NotImplementedException();
	}

	@Override
	public List<BSharpTreeNodeElement> getElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertElements(List<BSharpTreeNodeElement> elements) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<BSharpTreeNodeElement> extractMinimumCapacityExcedent() {
		return null;
	}
}