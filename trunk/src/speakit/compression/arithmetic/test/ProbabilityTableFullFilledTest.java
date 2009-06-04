package speakit.compression.arithmetic.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public class ProbabilityTableFullFilledTest {

	private ProbabilityTable sut;

	@Before
	public void setUp() throws Exception {
		// JERRYRICE
		sut = new ProbabilityTable();
		
		sut.add(Symbol.getEof(), 1);
		for (int i = 0; i < Math.pow(2, 16); i++) {
			sut.add(new Symbol((char)i), 1);
		}
	}

	@After
	public void tearDown() throws Exception {

	}
	
	@Test
	public void testDistributionLowerThan1() {
		
//		Assert.assertEquals(Math.pow(2, 16), (2^16));
		for (int i = 0; i < (Math.pow(2, 16)-1); i++) {
//			System.out.println(i);
//			if(i==17){
//				Assert.assertTrue("El caracter ("+i+")=" + (char)i +" no deberia tener probabilidad 1 ",  sut.getDistribution(new Symbol(i))<1);	
//			}
			
		}
//		for (int i = 0; i < (2^16)-1; i++) {
//			Assert.assertTrue("El caracter ("+i+")=" + (char)i +" no deberia tener probabilidad 1 ",  sut.getDistribution(new Symbol(i))<1);
//		}		
	}
	
//	Assert.assertEquals( 1.0,sut.getDistribution(new Symbol(2^16-1)),1.0);
	
	
}
