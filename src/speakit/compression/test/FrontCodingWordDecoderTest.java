package speakit.compression.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.compression.FrontCodedWord;
import speakit.compression.FrontCodingWordDecoder; 

@Ignore
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
		"codo codazo codearse codera cordon corazon".split(" ");

		Assert.assertEquals(decoder.decode(new FrontCodedWord((short) 0,"codo")), "codo");
		Assert.assertEquals(decoder.decode(new FrontCodedWord((short) 3,"azo")), "codazo");
		Assert.assertEquals(decoder.decode(new FrontCodedWord((short) 3,"earse")), "codearse");
		Assert.assertEquals(decoder.decode(new FrontCodedWord((short) 4,"ra")), "codera");
		Assert.assertEquals(decoder.decode(new FrontCodedWord((short) 2,"don")), "cordon");
		Assert.assertEquals(decoder.decode(new FrontCodedWord((short) 4,"ura")), "cordura");
	}
}

