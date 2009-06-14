package speakit.compression.lzp.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.SpeakitLogger;
import speakit.TextDocument;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.lzp.LZP;

public class TestLZP {

	private LZP lzp;
	ByteArrayOutputStream out;
	TextDocument document = new TextDocument("AOIAOIOAOIAOIO");
	TextDocument docVacio = new TextDocument("");
	TextDocument docWikiChars = new TextDocument("La codificación de caracteres es el método que permite convertir un carácter de un lenguaje natural (alfabeto o silabario) en un símbolo de otro sistema de representación, como un número o una secuencia de pulsos eléctricos en un sistema electrónico, aplicando normas o reglas de codificación.");
	@Before
	public void setUp() throws Exception {
		out = new ByteArrayOutputStream();
		lzp = new LZP(out);
	}
	@Test
	public void testCompress() throws IOException {
		//SpeakitLogger.activate();
		lzp.compress(document);
		//printTables();
		System.out.println(out);
	}
	
	@Test
	public void testDecompress() throws IOException {
		byte[] compressedbytes = compress(document);
		SpeakitLogger.activate();
		lzp = new LZP(out);
		TextDocument result = lzp.decompress(new ByteArrayInputStream(compressedbytes));
		System.out.println(result.getText());
		System.out.println("Fin de la descompresion");
		Assert.assertTrue(document.getText().equals(result.getText()));
	}
	
	@Ignore
	@Test
	public void testDecompress2() throws IOException {
		byte[] compressedbytes = compress(docWikiChars);
		SpeakitLogger.activate();
		lzp = new LZP(out);
		TextDocument result = lzp.decompress(new ByteArrayInputStream(compressedbytes));
		System.out.println(result.getText());
		System.out.println("Fin de la descompresion");
		Assert.assertTrue(docWikiChars.getText().equals(result.getText()));
	}
	
	private void printTables() {
		HashMap<String, ProbabilityTable> tables = lzp.getTables();
		Set<String> collection = tables.keySet();
		for (Iterator<String> iterator = collection.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			SpeakitLogger.Log("Context: " + string + "\n");
			SpeakitLogger.Log(tables.get(string) + "\n");		
		}
	}
	
	private byte[] compress(TextDocument textDocument) throws IOException {
		this.lzp.compress(textDocument);
		// SpeakitLogger.Log(out.toString());
		return out.toByteArray();
	}
}
