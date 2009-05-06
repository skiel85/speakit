package speakit.io.bsharptree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import speakit.io.record.ArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;

public class BSharpTreeIndexNodeRecord extends Record<IntegerField> {
	private IntegerField nodeNumber = new IntegerField();
	private IntegerField leftChild = new IntegerField();
	private ArrayField<BSharpTreeIndexNodeElement> elements = new ArrayField<BSharpTreeIndexNodeElement>() {
		@Override
		protected BSharpTreeIndexNodeElement createField() {
			return new BSharpTreeIndexNodeElement();
		}

	};

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
	
	public void setLeftChildNodeNumber(int nodeNumber) {
		this.leftChild.setInteger(nodeNumber);
	}

	public Iterator<BSharpTreeIndexNodeElement> getElementsIterator() {
		return this.elements.iterator();
	}

	public List<BSharpTreeNodeElement> getElements() {
		ArrayList<BSharpTreeNodeElement> result = new ArrayList<BSharpTreeNodeElement>(this.elements.getArray());
		return result;
	}

	public void insertElement(BSharpTreeNodeElement element) {
		this.elements.addItem((BSharpTreeIndexNodeElement) element);
		this.elements.sort();
	}

	public void setNodeNumber(int i) {
		this.nodeNumber.setInteger(i);
	}

	public int getNodeNumber() {
		return this.nodeNumber.getInteger();
	}
	
	@Override
	protected String getStringRepresentation() {
		return "B#IN{num:"+this.nodeNumber.toString()+",Lchild:"+this.leftChild.toString()+",elements:"+this.elements.toString()+"}";
	}
}
