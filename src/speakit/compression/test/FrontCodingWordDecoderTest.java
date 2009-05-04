package speakit.compression.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;

import speakit.compression.FrontCodedWord;
import speakit.compression.FrontCodingWordDecoder;


public class FrontCodingWordDecoderTest {

	private FrontCodingWordDecoder decoder;

	@Before
	public void setUp() throws Exception {
		decoder = new FrontCodingWordDecoder();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testLargeText() {
		

		Assert.assertEquals(decoder.decode(new FrontCodedWord((short) 0, "codo")), "codo");
		Assert.assertEquals(decoder.decode(new FrontCodedWord((short) 3, "azo")), "azo");
		Assert.assertEquals(decoder.decode(new FrontCodedWord((short) 3, "earse")), "earse");
		Assert.assertEquals(decoder.decode(new FrontCodedWord((short) 4, "ra")), "ra");
		Assert.assertEquals(decoder.decode(new FrontCodedWord((short) 2, "don")), "don");
		Assert.assertEquals(decoder.decode(new FrontCodedWord((short) 4, "ura")), "ura");
	}
}
