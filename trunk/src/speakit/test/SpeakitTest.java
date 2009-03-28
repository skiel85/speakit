package speakit.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.Speakit;
import speakit.TextDocument;

public class SpeakitTest {

	private SpeakitTestFileSet fileSet;
	private Speakit sut;

	@Before
	public void setUp() throws Exception {
		this.fileSet = new SpeakitTestFileSet();
		this.sut = new Speakit();
		this.sut.load(this.fileSet);
	}

	@After
	public void tearDown() throws Exception {
		this.fileSet.destroyFiles();
	}

	public TextDocument createTextDocument(String text) throws FileNotFoundException, IOException {
		File file = File.createTempFile(this.getClass().getName(), ".txt");
		OutputStream os = new FileOutputStream(file);
		os.write(text.getBytes());
		os.close();

		TextDocument textDocument = this.sut.getTextDocumentFromFile(file.getAbsolutePath());

		file.delete();

		return textDocument;
	}

	@Test
	public void testCreateTextDocument() throws FileNotFoundException, IOException {
		Iterator<String> it = createTextDocument("Hola mundo.").iterator();
		Assert.assertEquals("hola", it.next());
		Assert.assertEquals("mundo", it.next());
		Assert.assertFalse(it.hasNext());

	}

	@Test
	public void testAddDocumentAndGetUnknownWords() throws IOException {
		Iterator<String> it = this.sut.addDocument(createTextDocument("Ser o no ser, esa es la cuestión.")).iterator();
		Assert.assertEquals("ser", it.next());
		Assert.assertEquals("o", it.next());
		Assert.assertEquals("no", it.next());
		Assert.assertEquals("esa", it.next());
		Assert.assertEquals("es", it.next());
		Assert.assertEquals("la", it.next());
		Assert.assertEquals("cuestión", it.next());
		Assert.assertFalse(it.hasNext());
	}
}
