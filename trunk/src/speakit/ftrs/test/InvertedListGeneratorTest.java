package speakit.ftrs.test;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.ftrs.InvertedListGenerator;
import speakit.ftrs.Lexicon;
import speakit.ftrs.index.InvertedList;


public class InvertedListGeneratorTest {
	TextDocument doc;
	TextDocument doc2;
	TextDocument doc3;
	ArrayList<TextDocument> docs;
	Lexicon lexico;
	
	@Before
	public void setUp() {
		doc = new TextDocument("hola hola como estas");
		doc.setId(1);
		doc2 = new TextDocument("hola ya me fui");
		doc2.setId(2);
		doc3 = new TextDocument("hola ya volvi");
		doc3.setId(3);
		docs = new ArrayList<TextDocument>();
		docs.add(doc);
		docs.add(doc2);
		docs.add(doc3);
		lexico = new Lexicon();
		lexico.add("hola");
		lexico.add("como");
		lexico.add("estas");
		lexico.add("ya");
		lexico.add("me");
		lexico.add("fui");
		lexico.add("volvi");
	}
	
	/**
	 * Smoke test. Solo tstea q no rompa con la generacion de listas invertidas.
	 * TODO Ajustarlo cuando inverted list tenga mas funcionalidad
	 */
	@Test
	public void testGenerateInvertedList() {
		InvertedListGenerator generator = new InvertedListGenerator();		
		generator.processTextDocuments(docs, lexico);
		InvertedList list = generator.generate(1);
		Assert.assertTrue("Termino 'hola' debe estar en 3 docs, pero el tamaño es " + list.size(), list.size() == 3);
		list = generator.generate(2);
		Assert.assertTrue("Termino 'como' debe estar en 1 doc, pero el tamaño es " + list.size(), list.size() == 1);
		list = generator.generate(3);
		Assert.assertTrue("Termino 'estas' debe estar en 1 doc1, pero el tamaño es " + list.size(), list.size() == 1);
		list = generator.generate(4);
		Assert.assertTrue("Termino 'ya' debe estar en 2 docs, pero el tamaño es " + list.size(), list.size() == 2);
		list = generator.generate(5);
		Assert.assertTrue("Termino 'me' debe estar en 1 docs, pero el tamaño es " + list.size(), list.size() == 1);
		list = generator.generate(6);
		Assert.assertTrue("Termino 'fui' debe estar en 1 docs, pero el tamaño es " + list.size(), list.size() == 1);
		list = generator.generate(7);
		Assert.assertTrue("Termino 'volvi' debe estar en 1 docs, pero el tamaño es " + list.size(), list.size() == 1);
	}
}
