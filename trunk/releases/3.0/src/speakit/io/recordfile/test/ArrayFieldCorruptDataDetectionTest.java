package speakit.io.recordfile.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import speakit.io.record.ArrayFieldWithFactory;
import speakit.io.record.IntegerField;
import speakit.io.record.RecordSerializationException;

public class ArrayFieldCorruptDataDetectionTest {
	 
	private ArrayFieldWithFactory<IntegerField>	sut;

	@Before
	public void setUp(){
		sut = new ArrayFieldWithFactory<IntegerField>(IntegerField.createFactory());
		sut.addItem(new IntegerField(12345));
		sut.addItem(new IntegerField(46666));
		sut.addItem(new IntegerField(437));
		sut.addItem(new IntegerField(8984));
		sut.addItem(new IntegerField(12689));
		sut.addItem(new IntegerField(123123));
	}
	
	@Test
	public void test() throws RecordSerializationException, IOException{
		ArrayFieldWithFactory deserialized = new ArrayFieldWithFactory<IntegerField>(IntegerField.createFactory());
		FieldTest.testFieldSerialization(sut, deserialized);		
	}
	
	@Test
	public void testCorruptingData() throws RecordSerializationException, IOException{
		byte[] serialization = this.sut.serialize();
		
		byte[] corruptSerialization = serialization;
		corruptSerialization[serialization.length/2]=4;
		corruptSerialization[0]=99;
		corruptSerialization[1]=99;
		corruptSerialization[2]=99;
		corruptSerialization[3]=99;

		ArrayFieldWithFactory deserialized = new ArrayFieldWithFactory<IntegerField>(IntegerField.createFactory());
		try{
			deserialized.deserialize(corruptSerialization);
			Assert.fail();
		}catch(RecordSerializationException ex){
			return;
		}
		Assert.fail();
	}
}


