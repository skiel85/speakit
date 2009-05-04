package speakit.io.bsharptree;

import java.io.IOException;
import java.util.List;

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

	public abstract boolean contains(Field key) throws IOException, RecordSerializationException;

	public abstract Record getRecord(Field key) throws IOException, RecordSerializationException;

	public abstract void insertRecord(Record record) throws IOException, RecordSerializationException;

	public abstract int getLevel();

	public abstract void balance(List<BSharpTreeNode> nodes);

	public abstract void insertElements(List<BSharpTreeNodeElement> allRecords);

	public abstract List<BSharpTreeNodeElement> getElements();

	public abstract List<BSharpTreeNodeElement> extractMinimumCapacityExcedent() throws RecordSerializationException, IOException;

	protected abstract Record getNodeRecord();

	public int getMaximumCapacity() {
		return this.tree.getNodeSize() * this.size;
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
	
	protected BSharpTree getTree(){
		return this.tree;
	}
}
