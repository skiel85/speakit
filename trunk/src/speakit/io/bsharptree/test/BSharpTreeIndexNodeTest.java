package speakit.io.bsharptree.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.bsharptree.BSharpTreeIndexNode;
import speakit.io.bsharptree.BSharpTreeIndexNodeElement;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class BSharpTreeIndexNodeTest {

	private BSharpTreeIndexNode sut;

	@Before
	public void setUp() throws Exception {
		this.sut = new BSharpTreeIndexNode(null, 1);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInsertAndRetrieveElement() throws RecordSerializationException, IOException {		
		this.sut.insertElement(new BSharpTreeIndexNodeElement(new StringField("hola"), 1));
		this.sut.insertElement(new BSharpTreeIndexNodeElement(new StringField("mundo"), 3));
		BSharpTreeIndexNodeElement retrievedElement = (BSharpTreeIndexNodeElement) this.sut.getElement(new StringField("mundo"));
		Assert.assertEquals("mundo", ((StringField)retrievedElement.getKey()).getString());
		Assert.assertEquals(3, retrievedElement.getRightChildNodeNumber());
	}
	
	@Test
	public void testInsertsElementsOrdered() throws RecordSerializationException, IOException {
		this.sut.insertElement(new BSharpTreeIndexNodeElement(new StringField("adios"), 3));
		this.sut.insertElement(new BSharpTreeIndexNodeElement(new StringField("mundo"), 1));
		this.sut.insertElement(new BSharpTreeIndexNodeElement(new StringField("cruel"), 2));
		BSharpTreeIndexNodeElement retrievedElement = (BSharpTreeIndexNodeElement) this.sut.getElement(new StringField("mundo"));
		Assert.assertEquals("mundo", ((StringField)retrievedElement.getKey()).getString());
		Assert.assertEquals(1, retrievedElement.getRightChildNodeNumber());
	}
}
