package speakit.dictionary.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import javax.naming.BinaryRefAddr;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sun.management.FileSystem;

public class SerializableIntegerTest {

	private ByteArrayOutputStream out;

	@Before
	public void setUp() throws Exception {
		out = new ByteArrayOutputStream();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCompleteSerialization() {
		fail("Not yet implemented");
	}


}
