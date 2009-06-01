package speakit.compression.arithmetic.test;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.Binary;
import speakit.compression.arithmetic.Range;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class BinaryTest { 

 	@Test
	public void testConvertToInt() {
		Assert.assertEquals(163, Binary.bitStringToInt("10100011"));
	} 
}

