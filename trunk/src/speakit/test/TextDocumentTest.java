package speakit.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

import speakit.FileManager;
import speakit.TextDocument;
import speakit.compression.arithmetic.Symbol;

public class TextDocumentTest {

	@Test
	public void testWriteAndReadUnicode() throws IOException {
		String testText = "esto es una prueba de unicode:" + new Character((char) 300) + ", otro ejemplo sería: " + (char) 0x05A3;
		
		// El notepad dice que el encoding es "UCS-2 Big Endian"
		File file = new File("javaUnicode.txt");
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-16");
		writer.write(testText);
		writer.close();

		FileInputStream in = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(in, "UTF-16");

		// System.out.println(reader.getEncoding());
		String readString = new String();
		int current = 0;
		while ((current = reader.read()) >= 0) {
			readString += new Symbol((char) current).getChar();
		}

		// System.out.println(testText);
		// System.out.println(readString);
		Assert.assertEquals(testText, readString);
	}

//	@Test
//	public void testAnsiTextDocument() throws IOException {  
//		String testText = "esto es una prueba de ansi";  
//		String fileName = "testTextDocumenWithAnsi.txt";
//		String encoding = "";
// 
//		testReadFileWithTextDocument(testText, fileName, encoding);
//	}

	@Test
	public void testUnicodeWithUnicodeCharactersTextDocument() throws IOException {
		String testText = (char) 0x00A3 + "esto es una prueba de unicode:" + new Character((char) 300) + ", otro ejemplo sería: " + (char) 0x05A3;
		String fileName = "testUnicodeWithUnicodeCharactersTextDocument.txt";
		String encoding = "UTF-16";

		testReadFileWithTextDocument(testText, fileName, encoding);
	}
	@Test
	public void testUnicodeWithAnsiCharactersTextDocument() throws IOException {
		String testText = "esto es una prueba de ansi";
		String fileName = "testUnicodeWithAnsiCharactersTextDocument.txt";
		String encoding = "UTF-16";

		// TestFileManager filemanager = new
		// TestFileManager(this.getClass().toString());
		testReadFileWithTextDocument(testText, fileName, encoding);
	}

	private void testReadFileWithTextDocument(String testText, String fileName, String encoding) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		// TestFileManager filemanager = new
		// TestFileManager(this.getClass().toString());
		FileManager filemanager = new FileManager();
		File file = filemanager.openFile(fileName);

		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter writer;
		if (encoding.equals("")) {
			writer = new OutputStreamWriter(fos);
		} else {
			writer = new OutputStreamWriter(fos, encoding);
		}

		writer.write(testText);
		writer.close();

		TextDocument doc = new TextDocument();
		doc.loadFromFile(file);

		Assert.assertEquals(testText, doc.getText());

		filemanager.destroyFiles();
	}
}
