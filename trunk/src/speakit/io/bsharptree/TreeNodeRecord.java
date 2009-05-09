package speakit.io.bsharptree;

import speakit.io.record.IntegerField;
import speakit.io.record.Record;

public abstract class TreeNodeRecord extends Record<IntegerField> {
	private IntegerField nodeNumber = new IntegerField();

	public abstract void addElement(TreeNodeElement element);

	@Override
	public IntegerField getKey() {
		return this.nodeNumber;
	}

	public abstract int getLevel();

	public int getNodeNumber() {
		return this.nodeNumber.getInteger();
	}

	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber.setInteger(nodeNumber);
	}
}
