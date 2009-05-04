package speakit.io.bsharptree;

import java.util.ArrayList;
import java.util.List;

import speakit.io.record.ArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;

public class BSharpTreeLeafNodeRecord extends Record<IntegerField> {
	private IntegerField nodeNumber = new IntegerField();
	private ArrayField<BSharpTreeLeafNodeElement> elements = new ArrayField<BSharpTreeLeafNodeElement>();
	private IntegerField nextSecuenceNodeNumber = new IntegerField();

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
		BSharpTreeNodeElement element = this.elements.getItem(this.elements.getSize() - 1);
		this.elements.removeItem(this.elements.getSize() - 1);
		return element;
	}
	
	public void insertElement(BSharpTreeNodeElement element) {
		this.elements.addItem((BSharpTreeLeafNodeElement) element);
		this.elements.sort();
	}
}
