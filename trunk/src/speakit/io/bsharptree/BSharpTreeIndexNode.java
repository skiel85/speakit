package speakit.io.bsharptree;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@SuppressWarnings("unchecked")
public class BSharpTreeIndexNode extends BSharpTreeNode {

	private BSharpTreeIndexNodeRecord record;
		public BSharpTreeIndexNode(BSharpTree tree, int size) {
		super(tree, size);
	}

	@Override
	protected Record getNodeRecord() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public boolean contains(Field key) throws IOException, RecordSerializationException {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public Record getRecord(Field key) throws IOException, RecordSerializationException {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
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

		BSharpTreeNode nodeWhereToInsert = this.getTree().getNode(nodeNumberWhereToInsert);
		nodeWhereToInsert.insertRecord(record);
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void balance(List<BSharpTreeNode> nodes) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public void indexChild(BSharpTreeNode newChild) {
		throw new NotImplementedException();
	}

	@Override
	public List<BSharpTreeNodeElement> getElements() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void insertElements(List<BSharpTreeNodeElement> elements) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public List<BSharpTreeNodeElement> extractMinimumCapacityExcedent() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

}