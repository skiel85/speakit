package speakit.io.bsharptree;

import speakit.io.record.IntegerField;
import speakit.io.record.Record;

public abstract class BSharpTreeNodeRecord extends Record<IntegerField> {
	private IntegerField nodeNumber = new IntegerField();
	
	public int getNodeNumber() {
		return this.nodeNumber.getInteger();
	}
	
	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber.setInteger(nodeNumber);
	}
	
	@Override
	public IntegerField getKey() {
		return this.nodeNumber;
	}
	
	public abstract void insertElement(BSharpTreeNodeElement element);
	
	public abstract BSharpTreeNodeElement extractLastElement();
	public abstract BSharpTreeNodeElement extractFirstElement(); 
	public abstract int getLevel() ;
}
