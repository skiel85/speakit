package speakit.io.record;

import java.util.Iterator;

public class ArrayField<FIELDTYPE extends Field> extends CompositeField implements Iterable<FIELDTYPE> {
	private IntegerField size = new IntegerField();

	public ArrayField() {
		this.addField(this.size);
	}

	private void incrementSize() {
		this.size.setInteger(this.size.getInteger() + 1);
	}

	private void decrementSize() {
		this.size.setInteger(this.size.getInteger() - 1);
	}

	public void addItem(FIELDTYPE item) {
		this.incrementSize();
		this.addField(item);
	}

	public void removeItem(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		this.decrementSize();
		this.removeField(index + 1);
	}

	public void removeItem(FIELDTYPE field) {
		this.decrementSize();
		this.removeField(field);
	}

	@SuppressWarnings("unchecked")
	public FIELDTYPE getItem(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		return (FIELDTYPE) this.getField(index + 1);
	}

	public int getSize() {
		return this.size.getInteger();
	}

	@Override
	public Iterator<FIELDTYPE> iterator() {
		return new ArrayFieldIterator<FIELDTYPE>(this);
	}
}
