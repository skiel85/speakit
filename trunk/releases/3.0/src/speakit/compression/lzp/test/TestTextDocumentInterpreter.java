package speakit.compression.lzp.test;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.compression.arithmetic.Symbol;
import speakit.compression.lzp.TextDocumentInterpreter;

public class TestTextDocumentInterpreter {
	private TextDocumentInterpreter interpreter;
	private TextDocument document;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void TestGetMatchLength() {
		document = new TextDocument("chau chaucha");
		interpreter = new TextDocumentInterpreter(document);
		interpreter.advance(7);
		Assert.assertEquals(7, interpreter.getCurrentPosition().intValue());
		
		Assert.assertEquals(new Symbol('a'), interpreter.getActualSymbol());
		//c h [a] u _ c h [a] u c  h  a
		//0 1  2  3 4 5 6  7  8 9 10 11
		Integer matchLength = interpreter.getMatchLength(2);
		Assert.assertEquals(2, matchLength.intValue());
		
		//hubo un match de 2, entonces el pointer quedo en 9
		//c h a u _ c h a u [c] h  a
		//0 1 2 3 4 5 6 7 8  9  10 11
		Assert.assertEquals("Posicion del cursor luego de un match", 9, interpreter.getCurrentPosition().intValue());
		
		interpreter.advance(2);
		//c h a u _ c h [a] u c  h [a]
		//0 1 2 3 4 5 6  7  8 9 10  11
		Assert.assertEquals(1, interpreter.getMatchLength(7).intValue());
		
		//c h a u [_] c h a u [c] h  a
		//0 1 2 3  4  5 6 7 8  9  10 11
		interpreter.setPosition(9);
		Assert.assertEquals(0, interpreter.getMatchLength(4).intValue());
	}
}
