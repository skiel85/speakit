package speakit.io.record.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.record.ArrayField;
import speakit.io.record.StringField;

public class ArrayFieldTest {
	private ArrayField<StringField>	sut	= new ArrayField<StringField>(){
		@Override
		protected StringField createField() {
			return new StringField();
		}
	};
	private byte[]	serialization;

	@Before
	public void setUp() throws Exception {
		this.sut.addItem(new StringField("hola"));
		this.sut.addItem(new StringField("mundo"));
		 
		serialization = this.sut.serialize();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddAndRetrieve() throws IOException {
		Iterator<StringField> it = this.sut.iterator();
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals("hola", it.next().getString());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals("mundo", it.next().getString());
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testGetSize() {
		Assert.assertEquals(2, this.sut.size());
	}

	@Test
	public void testRetrieveByIndex() {
		Assert.assertEquals("hola", this.sut.get(0).getString());
		Assert.assertEquals("mundo", this.sut.get(1).getString());
	}

	@Test
	public void testSerialization() throws IOException {
		ArrayField<StringField> deserialized = new ArrayField<StringField>(){
			@Override
			protected StringField createField() {
				return new StringField();
			}
		};
		
		ByteArrayInputStream stream = new ByteArrayInputStream(serialization);
		deserialized.deserialize(stream);
		Assert.assertEquals(0, stream.available());
		Assert.assertEquals(this.sut.size(), deserialized.size());
		int itemCounter=0;
		for (int i = 0; i < this.sut.size(); i++) {
			Assert.assertEquals(this.sut.get(i).getString(), deserialized.get(i).getString());
			itemCounter++;
		}
		Assert.assertEquals(2, itemCounter);
		Assert.assertEquals(this.sut.toString(), deserialized.toString());
	}	

	@Test
	public void testSerializationSize() throws IOException {		
		Assert.assertEquals(this.serialization.length,sut.getSerializationSize());
	}

	@Test
	public void testThrowsIndexOutOfBoundException() {
		try {
			this.sut.get(-1);
			Assert.fail();
		} catch (IndexOutOfBoundsException e) {
			return;
		} catch (Throwable e) {
			Assert.fail();
		}

	}
}
