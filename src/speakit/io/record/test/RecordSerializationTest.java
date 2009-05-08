package speakit.io.record.test;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.bsharptree.test.TestIndexRecord;
import speakit.io.record.RecordSerializationCorruptDataException;
import speakit.io.record.RecordSerializationException;

public class RecordSerializationTest {
	private List<byte[]>	serializationParts;
	private TestIndexRecord	sut;
	double partSize = 7.0;
	private byte[]	fullSerialization;
	@Before
	public void setUp() throws Exception {
		sut = new TestIndexRecord("esto es una prueba de serializacion en partes", 12);		
		serializationParts = sut.serializeInParts((int) partSize);
		fullSerialization = sut.serialize();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testSerializeInParts() throws RecordSerializationException, IOException {
		Assert.assertEquals(Math.ceil(sut.serialize().length / partSize), serializationParts.size());
	}
	
	@Test
	public void testThrowsExceptionWhenPartSizeEqualsToZeroSerializeInParts() throws RecordSerializationException, IOException {
		try{
			serializationParts = sut.serializeInParts(0);
		}catch(IllegalArgumentException ex){
			return;	
		}
		Assert.fail("Se esperaba IllegalArgumentException");
	}
	
	@Test
	public void testDeserializeFromParts() throws RecordSerializationException, IOException {
		TestIndexRecord deserialized = new TestIndexRecord("",0);
		deserialized.deserializeFromParts(serializationParts);
		Assert.assertEquals(0,sut.compareTo(deserialized));
	}
	
	@Test
	public void testCorruptDataDetection() throws RecordSerializationException, IOException {
		TestIndexRecord deserialized = new TestIndexRecord("",0);
		byte[] corruptData =fullSerialization;
		corruptData[corruptData.length/2]=9;
		try{
			deserialized.deserialize(corruptData);
			Assert.fail("Se esperaba excepción de datos corruptos.");
		}catch(RecordSerializationCorruptDataException ex){
			return;
		}
		Assert.fail("Se esperaba excepción de datos corruptos.");
	}

}
