package speakit.ftrs.test;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.ftrs.FTRSImpl;

public class FTRSImpleTest {

	private FTRSImpl ftrs;
	@Before
	public void setUp(){
		this.ftrs = new FTRSImpl();
	}
	@Test
	public void testApplyFilters(){
		TextDocument document = new TextDocument ("habia una casa en donde        estabamos todos, eramos -muchos-.");
		Assert.assertEquals("habia casa estabamos todos", this.ftrs.applyFilters(document).getText());
	}
}
