package speakit.ftrs.test;

import java.io.IOException;

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
	public void testApplyFilters() throws IOException{
		TextDocument document = new TextDocument ("habia una casa en donde        estabamos todos, eramos -muchos- y no sabiamos como hacer para resolver el problema.");
		Assert.assertEquals("habia casa estabamos sabiamos resolver problema", this.ftrs.applyFilters(document).getText());
	}
}
