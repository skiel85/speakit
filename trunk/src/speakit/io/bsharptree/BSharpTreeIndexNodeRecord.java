package speakit.io.bsharptree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import speakit.io.record.ArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;

public class BSharpTreeIndexNodeRecord extends BSharpTreeNodeRecord {
	private IntegerField leftChild = new IntegerField();
	private IntegerField level = new IntegerField();
	
	private ArrayField<BSharpTreeIndexNodeElement> elements = new ArrayField<BSharpTreeIndexNodeElement>() {
		@Override
		protected BSharpTreeIndexNodeElement createField() {
			return new BSharpTreeIndexNodeElement();
		}

	}; 

	@Override
	protected Field[] getFields() {
		return new Field[] { this.level,this.leftChild, this.elements };
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

	public void removeElement(int index) {
		this.elements.removeItem(index);
	}

	public BSharpTreeNodeElement getElement(int index) {
		return this.elements.get(index);
	}

	@Override
	protected String getStringRepresentation() {
		return "IN "+ this.getNodeNumber() + ",LChild:" + this.leftChild.toString() + "," + this.elements.toString();
	}

	public BSharpTreeNodeElement extractLastElement() {
		BSharpTreeNodeElement element = this.elements.get(this.elements.size() - 1);
		this.elements.removeItem(this.elements.size() - 1);
		return element;
	}

	public BSharpTreeNodeElement extractFirstElement() {
		BSharpTreeNodeElement element = this.elements.get(0);
		this.elements.removeItem(0);
		return element;
	}

	public List<BSharpTreeNodeElement> extractAllElements() {
		ArrayList<BSharpTreeNodeElement> elementList = new ArrayList<BSharpTreeNodeElement>();
		for (BSharpTreeNodeElement element : this.elements) {
			elementList.add(element);
		}
		this.elements.clear();
		return elementList;
	}

	public void setLevel(int level) {
		this.level.setInteger(level);
	}
 
	public int getLevel() {
		return this.level.getInteger();
	}
}
