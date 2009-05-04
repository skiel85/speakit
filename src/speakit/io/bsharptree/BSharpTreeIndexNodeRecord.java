package speakit.io.bsharptree;

import java.util.Iterator;
import java.util.List;

import speakit.io.record.ArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;

public class BSharpTreeIndexNodeRecord extends Record<IntegerField> {
	private IntegerField nodeNumber = new IntegerField();
	private IntegerField leftChild = new IntegerField();
	private ArrayField<BSharpTreeIndexNodeElement> elements = new ArrayField<BSharpTreeIndexNodeElement>();

	@Override
	protected Field[] getFields() {
		return new Field[] { this.leftChild, this.elements, };
	}

	@Override
	public IntegerField getKey() {
		return this.nodeNumber;
	}

	public int getLeftChildNodeNumber() {
		return this.leftChild.getInteger();
	}

	public Iterator<BSharpTreeIndexNodeElement> getElementsIterator() {
		return this.elements.iterator();
	}
	
}
