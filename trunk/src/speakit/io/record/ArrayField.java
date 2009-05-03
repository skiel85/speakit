package speakit.io.record;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayField<FIELDTYPE extends Field> extends CompositeField implements Iterable<FIELDTYPE> {
	private IntegerField size = new IntegerField();
	private ArrayList<FIELDTYPE> values = new ArrayList<FIELDTYPE>();

	@Override
	protected Field[] getFields() {
		return Field.JoinFields(new Field[] { this.size }, this.values.toArray(new Field[this.values.size()]));
	}

	private void incrementSize() {
		this.size.setInteger(this.size.getInteger() + 1);
	}

	private void decrementSize() {
		this.size.setInteger(this.size.getInteger() - 1);
	}

	public void addItem(FIELDTYPE item) {
		this.incrementSize();
		this.values.add(item);
	}

	public void removeItem(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		this.decrementSize();
		this.values.remove(index);
	}

	public void removeItem(FIELDTYPE field) {
		this.decrementSize();
		this.values.remove(field);
	}

	public FIELDTYPE getItem(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		return (FIELDTYPE) this.values.get(index);
	}

	public int getSize() {
		return this.size.getInteger();
	}

	@Override
	public Iterator<FIELDTYPE> iterator() {
		return new ArrayFieldIterator<FIELDTYPE>(this);
	}

	public void setArray(List<FIELDTYPE> array) {
		for (FIELDTYPE item : array) {
			this.addItem(item);
		}
	}

	public List<FIELDTYPE> getArray() {
		List<FIELDTYPE> result = new ArrayList<FIELDTYPE>();
		if (this.values.size()>0){
			for (int i = 0; i < this.getFieldCount(); i++) {
				result.add((FIELDTYPE) this.values.get(i));
			}
		}
		return result;
	}
}
