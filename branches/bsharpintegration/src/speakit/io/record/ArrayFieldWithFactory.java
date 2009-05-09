package speakit.io.record;

public class ArrayFieldWithFactory<FIELDTYPE extends Field> extends ArrayField<FIELDTYPE> {

	private FieldFactory fieldFactory;
	public ArrayFieldWithFactory(FieldFactory fieldFactory){
		this.fieldFactory=fieldFactory;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected FIELDTYPE createField() {
		return (FIELDTYPE)fieldFactory.createField();
	}

}
