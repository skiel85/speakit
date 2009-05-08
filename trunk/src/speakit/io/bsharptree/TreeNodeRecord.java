package speakit.io.bsharptree;

import speakit.io.record.IntegerField;
import speakit.io.record.Record;

public abstract class TreeNodeRecord extends Record<IntegerField> {
	private IntegerField nodeNumber = new IntegerField();

	public abstract TreeNodeElement extractFirstElement();

	public abstract TreeNodeElement extractLastElement();

	@Override
	public IntegerField getKey() {
		return this.nodeNumber;
	}

	public abstract int getLevel();

	public int getNodeNumber() {
		return this.nodeNumber.getInteger();
	}

	public abstract void insertElement(TreeNodeElement element);

	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber.setInteger(nodeNumber);
	}
}
