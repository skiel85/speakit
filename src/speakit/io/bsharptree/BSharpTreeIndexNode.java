package speakit.io.bsharptree;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BSharpTreeIndexNode extends BSharpTreeNode {

	private BSharpTreeIndexNodeRecord record;
	private final BSharpTree tree;

	public BSharpTreeIndexNode(BSharpTree tree) {
		this.tree = tree;
	}

	@Override
	public boolean contains(Field key) throws IOException, RecordSerializationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Record getRecord(Field key) throws IOException, RecordSerializationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertRecord(Record record) throws IOException, RecordSerializationException {
		int nodeNumberWhereToInsert = this.record.getLeftChildNodeNumber();
		Iterator<BSharpTreeIndexNodeElement> it = this.record.getElementsIterator();

		boolean found = false;
		while (it.hasNext() && !found) {
			BSharpTreeIndexNodeElement element = it.next();
			if (record.compareToKey((Field) element.getKey()) > 0) {
				nodeNumberWhereToInsert = element.getRightChildNodeNumber();
			} else {
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
	public void balance(List<BSharpTreeNode> nodes) {
		throw new NotImplementedException();
	}

	public void indexChild(BSharpTreeNode newChild) {
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