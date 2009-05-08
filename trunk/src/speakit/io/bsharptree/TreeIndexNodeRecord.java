package speakit.io.bsharptree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import speakit.io.record.ArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;

public class TreeIndexNodeRecord extends TreeNodeRecord {
	private IntegerField leftChild = new IntegerField();
	private IntegerField level = new IntegerField();
	
	private ArrayField<TreeIndexNodeElement> elements = new ArrayField<TreeIndexNodeElement>() {
		@Override
		protected TreeIndexNodeElement createField() {
			return new TreeIndexNodeElement();
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

	public Iterator<TreeIndexNodeElement> getElementsIterator() {
		return this.elements.iterator();
	}

	public List<TreeNodeElement> getElements() {
		ArrayList<TreeNodeElement> result = new ArrayList<TreeNodeElement>(this.elements.getArray());
		return result;
	}

	public void insertElement(TreeNodeElement element) {
		this.elements.addItem((TreeIndexNodeElement) element);
		this.elements.sort();
	}

	public void removeElement(int index) {
		this.elements.removeItem(index);
	}

	public TreeNodeElement getElement(int index) {
		return this.elements.get(index);
	}

	@Override
	protected String getStringRepresentation() {
		return "IN "+ this.getNodeNumber() + ",LChild:" + this.leftChild.toString() + "," + this.elements.toString();
	}

	public TreeNodeElement extractLastElement() {
		TreeNodeElement element = this.elements.get(this.elements.size() - 1);
		this.elements.removeItem(this.elements.size() - 1);
		return element;
	}

	public TreeNodeElement extractFirstElement() {
		TreeNodeElement element = this.elements.get(0);
		this.elements.removeItem(0);
		return element;
	}

	public List<TreeNodeElement> extractAllElements() {
		ArrayList<TreeNodeElement> elementList = new ArrayList<TreeNodeElement>();
		for (TreeNodeElement element : this.elements) {
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
