package speakit.compression.arithmetic.test;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.Context;
import speakit.compression.arithmetic.Symbol;

public class TestContext {

	Context context;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSubContext() {
		context = new Context(10);
		for (int i = 0; i < 10; i++) {
			context.add(new Symbol(String.valueOf(i).charAt(0)));
		}
		Assert.assertEquals(Integer.valueOf(10), context.size());
		
		Context subContext = context.subContext(3);
		Assert.assertEquals(Integer.valueOf(3), subContext.size());
		Assert.assertEquals("789", subContext.toString());
		
		Context longSubContext = context.subContext(15);
		Assert.assertEquals(Integer.valueOf(10), longSubContext.size());
		Assert.assertEquals("0123456789", longSubContext.toString());
		
	}

	@Test
	public void testAdd() {
		context = new Context(10);
		for (int i = 0; i < 5; i++) {
			context.add(new Symbol(String.valueOf(i).charAt(0)));
		}
		Assert.assertEquals(Integer.valueOf(5), context.size());
		Assert.assertEquals("01234", context.toString());
	}
	
	@Test
	public void testAddOverLimit() {
		context = new Context(5);
		for (int i = 0; i < 10; i++) {
			context.add(new Symbol(String.valueOf(i).charAt(0)));
		}
		Assert.assertEquals(Integer.valueOf(5), context.size());
		Assert.assertEquals("56789", context.toString());
	}	

	@Test
	public void testSize() {
		context = new Context(10);
		for (int i = 0; i < 5; i++) {
			context.add(new Symbol(String.valueOf(i).charAt(0)));
		}
		Assert.assertEquals(Integer.valueOf(5), context.size());
	}

	@Test
	public void testEqualsObject() {
		context = new Context(10);
		for (int i = 0; i < 5; i++) {
			context.add(new Symbol(String.valueOf(i).charAt(0)));
		}
		
		Context notSameLong = new Context(11);
		for (int i = 0; i < 5; i++) {
			notSameLong.add(new Symbol(String.valueOf(i).charAt(0)));
		}
		Assert.assertTrue("Se toma en cuenta la logitud utilizada, no la maxima posible", context.equals(notSameLong));
		
		Context notSameChars = new Context(10);
		for (int i = 1; i < 6; i++) {
			notSameChars.add(new Symbol(String.valueOf(i).charAt(0)));
		}
		Assert.assertFalse("Tienen distintos caracteres", context.equals(notSameChars));
		
		Context equalCtxt = new Context(10);
		for (int i = 0; i < 5; i++) {
			equalCtxt.add(new Symbol(String.valueOf(i).charAt(0)));
		}
		Assert.assertTrue("Dos contextos con los mismos valores", context.equals(equalCtxt));
	}

}
