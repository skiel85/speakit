package speakit.compression.arithmetic.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.Range;

public class Range32BitTest {

	private Range				sut; 
	@Before
	public void setUp() throws Exception {
		sut = new Range( 32);
	}

	@After
	public void tearDown() throws Exception {

	}

/**
 * Prueba que soporte rangos de 32 bits. Cuando no lo soportaba emitia todos ceros.
 * @throws IOException
 */
	@Test
	public void testSetBoundsShorterThanPrecicion() throws IOException {
		sut.zoomIn(.8, 0.12);
		sut.zoomIn(.8, 0.12);
		sut.zoomIn(.8, 0.12);
		sut.zoomIn(.8, 0.12);
		sut.zoomIn(.8, 0.12);
		sut.zoomIn(.8, 0.12);

		Assert.assertFalse("000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".equals(sut.flush()));

	}

}
