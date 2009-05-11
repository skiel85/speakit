package speakit.io.bsharptree.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.bsharptree.TreeIndexNode;
import speakit.io.bsharptree.TreeIndexNodeElement;
import speakit.io.bsharptree.TreeNode;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class TreeNodeTest {

	private TreeNode sut;

	@Before
	public void setUp() throws Exception {
		this.sut = new TreeIndexNode(null,-1, 1);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInsertAndRetrieveElement() throws RecordSerializationException, IOException {
		this.sut.insertElement(new TreeIndexNodeElement(new StringField("hola"), 1));
		this.sut.insertElement(new TreeIndexNodeElement(new StringField("mundo"), 3));
		TreeIndexNodeElement retrievedElement = (TreeIndexNodeElement) this.sut.getElement(new StringField("mundo"));
		Assert.assertEquals("mundo", ((StringField) retrievedElement.getKey()).getString());
		Assert.assertEquals(3, retrievedElement.getRightChildNodeNumber());
	}

	@Test
	public void testInsertsElementsOrdered() throws RecordSerializationException, IOException {
		this.sut.insertElement(new TreeIndexNodeElement(new StringField("adios"), 3));
		this.sut.insertElement(new TreeIndexNodeElement(new StringField("mundo"), 1));
		this.sut.insertElement(new TreeIndexNodeElement(new StringField("cruel"), 2));
		TreeIndexNodeElement firstElement = (TreeIndexNodeElement) this.sut.getElement(0);
		Assert.assertEquals("adios", ((StringField) firstElement.getKey()).getString());
		Assert.assertEquals(3, firstElement.getRightChildNodeNumber());
		TreeIndexNodeElement secondElement = (TreeIndexNodeElement) this.sut.getElement(1);
		Assert.assertEquals("cruel", ((StringField) secondElement.getKey()).getString());
		Assert.assertEquals(2, secondElement.getRightChildNodeNumber());
		TreeIndexNodeElement thirdElement = (TreeIndexNodeElement) this.sut.getElement(2);
		Assert.assertEquals("mundo", ((StringField) thirdElement.getKey()).getString());
		Assert.assertEquals(1, thirdElement.getRightChildNodeNumber());
	}
}
