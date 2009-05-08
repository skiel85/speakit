package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class ArrayField<FIELDTYPE extends Field> extends Field implements Iterable<FIELDTYPE> {
	
	private static final boolean	HASHED	= true;
	private ArrayList<FIELDTYPE> values = new ArrayList<FIELDTYPE>();

	@Override
	protected void actuallyDeserialize(InputStream in) throws IOException {
		IntegerField size = createIntegerField();//crea un campo entero con hash, para verificar que el tamaño sea un dato válido
		try{
			size.deserialize(in);	
		}catch (RecordSerializationCorruptDataException e) {
			throw new RecordSerializationException(e);
		}
		for (int i = 0; i < size.getInteger(); i++) {
			FIELDTYPE createdField = this.createField();
			deserializeField(in, createdField);
			this.values.add(createdField);
		}
	}

	@Override
	protected void actuallySerialize(OutputStream out) throws IOException {
		IntegerField size = createIntegerField();//crea un campo entero con hash, para persistir el hash actual
		size.setInteger(this.size());
		size.serialize(out);
		for (int i = 0; i < this.values.size(); i++) {
			serializeField(out, i);
		}
	}

	public void addItem(FIELDTYPE item) {
		this.values.add(item); 
	}

	public void clear() {
		this.values.clear(); 
	}

	@Override
	protected int compareToSameClass(Field o) {
		for (Field field : this.values) {
			int comparationResult = field.compareTo(o);
			if (comparationResult != 0)
				return comparationResult;
		}
		return 0;
	}

	protected abstract FIELDTYPE createField();

	private IntegerField createIntegerField() {
		return new IntegerField(HASHED);
	}

	private void deserializeField(InputStream in, FIELDTYPE createdField) throws IOException {
		createdField.deserialize(in);
	}

	public FIELDTYPE get(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		return (FIELDTYPE) this.values.get(index);
	}
	
	public List<FIELDTYPE> getArray() {
		List<FIELDTYPE> result = new ArrayList<FIELDTYPE>();
		if (this.values.size() > 0) {
			for (int i = 0; i < this.values.size(); i++) {
				result.add((FIELDTYPE) this.values.get(i));
			}
		}
		return result;
	}

	@Override
	public int getSerializationSize() throws RecordSerializationException, IOException {
		int accum = 0;
		for (Field field : this.values) {
			accum += field.getSerializationSize();
		}
		accum += this.createIntegerField().getSerializationSize();
		return accum;
	}

	@Override
	protected String getStringRepresentation() {
		String result="A[" + this.values.size() + "]{";
		int i=0;
		for (Field field : this.values) {
			result += (i!=0?",":"")+ "("  + field.toString() + ")";
			i++;
		}
		result+="}";
		return result;		
	} 
	
	@Override
	public Iterator<FIELDTYPE> iterator() {
		return new ArrayFieldIterator<FIELDTYPE>(this);
	}

	public void removeItem(FIELDTYPE field) {
		this.values.remove(field); 
	}

	public void removeItem(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		this.values.remove(index); 
	}

	private long serializeField(OutputStream out, int i) throws IOException {
		return this.values.get(i).serialize(out);
	}

	public void setArray(List<FIELDTYPE> array) {
		for (FIELDTYPE item : array) {
			this.addItem(item);
		}
	}
	
	public int size() {
		return this.values.size();
	} 
	
	public void sort() {
		Collections.sort(this.values);
	}
}
