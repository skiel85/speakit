package speakit.io.bsharptree;

import java.io.IOException;
import java.util.List;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;

public abstract class BSharpTreeNode {
	public abstract boolean contains(Field key) throws IOException, RecordSerializationException;

	public abstract Record getRecord(Field key) throws IOException, RecordSerializationException;

	public abstract void insertRecord(Record record) throws IOException, RecordSerializationException;

	public abstract boolean isInOverflow() throws RecordSerializationException, IOException;

	public abstract int getLevel();

	public abstract void balance(List<BSharpTreeNode> nodes);

	public abstract void insertElements(List<BSharpTreeNodeElement> allRecords);

	public abstract List<BSharpTreeNodeElement> getElements();
	
	public abstract List<BSharpTreeNodeElement> extractMinimumCapacityExcedent() throws RecordSerializationException, IOException;
}
