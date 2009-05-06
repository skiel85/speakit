package speakit.io.recordfile.test;

import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;
import speakit.io.record.StringField;

public class TestVariableRecord extends Record<IntegerField> {

	public IntegerField	id		= new IntegerField();
	public StringField	text	= new StringField();

	public TestVariableRecord(int id, String text){
		this.id.setInteger(id);
		this.text.setString(text);
	}
	
	@Override
	protected Field[] getFields() { 
		return new Field[]{this.id, this.text};
	}

	@Override
	public IntegerField getKey() {
		return this.id;
	}
	
	@Override
	protected String getStringRepresentation() {
		return "TestVariableRecord{id:"+this.id.toString()+",text:"+this.text.toString()+"}";
	}

}
