package speakit.io.bsharptree;

import speakit.io.record.ArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;

public class TreeIndexNodeRecord extends TreeNodeRecord {
	public TreeIndexNodeRecord(int nodeNumber) {
		super(nodeNumber);
	}

	private IntegerField leftChild = new IntegerField();
	private IntegerField level = new IntegerField();

	private ArrayField<TreeIndexNodeElement> elements = new ArrayField<TreeIndexNodeElement>() {
		@Override
		protected TreeIndexNodeElement createField() {
			return new TreeIndexNodeElement();
		}

	};

	@Override
	public void addElement(TreeNodeElement element) {
		this.elements.addItem((TreeIndexNodeElement) element);
	}

	public ArrayField<TreeIndexNodeElement> getElements() {
		return this.elements;
	}

	@Override
	protected Field[] getFields() {
		return new Field[] { this.level, this.leftChild, this.elements };
	}

	public int getLeftChildNodeNumber() {
		return this.leftChild.getInteger();
	}

	public int getLevel() {
		return this.level.getInteger();
	}

	@Override
	protected String getStringRepresentation() {
		return "IN " + this.getNodeNumber() + ",LChild:" + this.leftChild.toString() + "," + this.elements.toString();
	}

	public void setLeftChildNodeNumber(int nodeNumber) {
		this.leftChild.setInteger(nodeNumber);
	}

	public void setLevel(int level) {
		this.level.setInteger(level);
	}
}
