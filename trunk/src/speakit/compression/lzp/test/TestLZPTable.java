package speakit.compression.lzp.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.compression.arithmetic.Context;
import speakit.compression.arithmetic.Symbol;
import speakit.compression.lzp.LZPTable;
import speakit.io.record.RecordSerializationException;

public class TestLZPTable {

	LZPTable table;
	@Before
	public void setUp() throws Exception {
		table = new LZPTable();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testUpdate() throws RecordSerializationException, IOException {
		Context ctx = getContext("AB"); 
		table.update(ctx, 1);
		Assert.assertEquals(1, table.getLastMatchPosition(ctx).intValue());
		//uso dos instancias distintas de context, con el mismo valor
		table.update(getContext("BC"), 2);
		Assert.assertEquals(2, table.getLastMatchPosition(getContext("BC")).intValue());
		
		table.update(getContext("CD"), 3);
		Assert.assertEquals(3, table.getLastMatchPosition(getContext("CD")).intValue());
		
		table.update(getContext("AB"), 4);
		Assert.assertEquals(4, table.getLastMatchPosition(getContext("AB")).intValue());
	}

	private Context getContext(String value) {
		Context context = new Context(value.length());
		for (int i = 0; i < value.length(); i++) {
			context.add(new Symbol(value.charAt(i)));
		}
		return context;
	}

}
