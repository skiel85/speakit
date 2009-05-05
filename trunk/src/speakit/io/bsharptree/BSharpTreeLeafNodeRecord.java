package speakit.io.bsharptree;

import java.util.ArrayList;
import java.util.List;

import speakit.io.record.ArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;

public class BSharpTreeLeafNodeRecord extends Record<IntegerField> {
	private IntegerField nodeNumber = new IntegerField();
	private ArrayField<BSharpTreeLeafNodeElement> elements = new ArrayField<BSharpTreeLeafNodeElement>(){
		@Override
		protected BSharpTreeLeafNodeElement createField() {
			return new BSharpTreeLeafNodeElement(null);
		}
	};
	private IntegerField nextSecuenceNodeNumber = new IntegerField();

	public int getNodeNumber() {
		return this.nodeNumber.getInteger();
	}
	
	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber.setInteger(nodeNumber);
	}
	
	@Override
	protected Field[] getFields() {
		return new Field[] { this.elements, this.nextSecuenceNodeNumber };
	}

	@Override
	public IntegerField getKey() {
		return this.nodeNumber;
	}

	public List<BSharpTreeNodeElement> getElements() {
		ArrayList<BSharpTreeNodeElement> result = new ArrayList<BSharpTreeNodeElement>(this.elements.getArray());
		return result;
	}

	public BSharpTreeNodeElement extractLastElement() {
		BSharpTreeNodeElement element = this.elements.get(this.elements.size() - 1);
		this.elements.removeItem(this.elements.size() - 1);
		return element;
	}
	
	public void insertElement(BSharpTreeNodeElement element) {
		this.elements.addItem((BSharpTreeLeafNodeElement) element);
		this.elements.sort();
	}
}
