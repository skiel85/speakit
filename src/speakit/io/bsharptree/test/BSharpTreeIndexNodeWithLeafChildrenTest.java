package speakit.io.bsharptree.test;

import org.junit.After;
import org.junit.Before;

import speakit.io.bsharptree.BSharpTreeIndexNode;
import speakit.io.bsharptree.BSharpTreeLeafNode;

public class BSharpTreeIndexNodeWithLeafChildrenTest {
	private BSharpTreeIndexNode sut;

	@Before
	public void setUp() throws Exception {
		BSharpTreeLeafNode node1 = new BSharpTreeLeafNode(null, 1);
		BSharpTreeLeafNode node2 = new BSharpTreeLeafNode(null, 1);
		BSharpTreeLeafNode node3 = new BSharpTreeLeafNode(null, 1);

	}

	@After
	public void tearDown() throws Exception {
	}

}
