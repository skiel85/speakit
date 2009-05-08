package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.io.bsharptree.BSharpTree;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class BSharpTreeIndexNodeSplitTest {
	private BSharpTree<TestIndexRecord, StringField> sut;
	private File file;
	private Iterator<String> testStrings;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.sut = new BSharpTree<TestIndexRecord, StringField>(this.file,TestIndexRecord.createFactory());
		this.testStrings = new TextDocument(
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur lacinia eleifend ante ut suscipit. Pellentesque porta urna sit amet leo egestas eu rhoncus dui faucibus. In hac habitasse platea dictumst. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris ut massa ante. Suspendisse potenti. Curabitur a nisi non mi viverra elementum id et magna. Mauris eu ipsum eu nulla posuere bibendum. Suspendisse et elit magna. Sed malesuada, turpis eget dapibus vestibulum, augue arcu hendrerit mi, sit amet scelerisque ipsum nulla ullamcorper ipsum. Integer aliquet, leo ac commodo malesuada, augue justo auctor elit, vel auctor nulla mi ac nulla. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum ac justo sit amet massa varius tempus eu at ipsum. Etiam semper nisl ac nulla molestie vestibulum. Nunc nec ante at nisl tempus placerat.")
				.iterator();
		this.sut.create(32);
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

//	@Test
//	public void testRootOverflow() throws RecordSerializationException, IOException {
//		while (!this.sut.getRoot().isInOverflow()) {
//			this.sut.insertRecord(new TestIndexRecord(testStrings.next(), 2));
//		}
//	}
	
	
}
