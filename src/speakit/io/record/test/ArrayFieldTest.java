package speakit.io.record.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.record.ArrayField;
import speakit.io.record.StringField;

public class ArrayFieldTest {
	private ArrayField<StringField> sut = new ArrayField<StringField>();

	@Before
	public void setUp() throws Exception {
		this.sut.addItem(new StringField("hola"));
		this.sut.addItem(new StringField("mundo"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testThrowsIndexOutOfBoundException() {
		try {
			this.sut.getItem(-1);
			Assert.fail();
		}
		catch (IndexOutOfBoundsException e ){
			return;
		}
		catch (Throwable e){
			Assert.fail();
		}
		
	}

	@Test
	public void testRetrieveByIndex() {
		Assert.assertEquals("hola", this.sut.getItem(0).getString());
		Assert.assertEquals("mundo", this.sut.getItem(1).getString());
	}
	
	@Test
	public void testGetSize() {
		Assert.assertEquals(2, this.sut.getSize());
	}

	@Test
	public void testSerialization() throws IOException {
		ArrayField<StringField> deserialized = new ArrayField<StringField>();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		this.sut.serialize(os);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		deserialized.deserialize(is);
		Assert.assertEquals(this.sut.getSize(), deserialized.getSize());
		for (StringField field : this.sut) {
			Assert.assertEquals(field.getString(), field.getString());
		}
	}
}
