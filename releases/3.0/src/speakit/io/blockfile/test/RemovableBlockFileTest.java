package speakit.io.blockfile.test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.io.blockfile.Block;
import speakit.io.blockfile.RemovableBlock;
import speakit.io.blockfile.RemovableBlockFile;
import speakit.io.record.RecordSerializationException;

public class RemovableBlockFileTest {

	private static final int BLOCK_SIZE = 256;

	File file;

	RemovableBlockFile sut;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		RemovableBlockFile createdFile;
		createdFile = new RemovableBlockFile(this.file);
		createdFile.create(BLOCK_SIZE);

		sut = new RemovableBlockFile(this.file);
		sut.load();
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testCreationGivesConsecutiveBlockNumbers() throws IOException, RecordSerializationException {
		RemovableBlock first = (RemovableBlock) sut.getNewBlock();
		RemovableBlock second = (RemovableBlock) sut.getNewBlock();
		Assert.assertEquals(first.getBlockNumber() + 1, second.getBlockNumber());
	}

	@Test
	public void testReutilizesRemovedBlock() throws IOException, RecordSerializationException {
		sut.getNewBlock();
		RemovableBlock second = (RemovableBlock) sut.getNewBlock();
		sut.getNewBlock();
		sut.removeBlock(second);
		// verifico que reutiliza el bloque "second" eliminado
		Assert.assertEquals(second.getBlockNumber(), sut.getNewBlock().getBlockNumber());
	}

	@Test
	public void testIteratesOnlyOverActiveBlocks() throws IOException, RecordSerializationException {
		RemovableBlock first = (RemovableBlock) sut.getNewBlock();
		RemovableBlock second = (RemovableBlock) sut.getNewBlock();
		RemovableBlock third = (RemovableBlock) sut.getNewBlock();
		sut.removeBlock(third);
		RemovableBlock fourth = (RemovableBlock) sut.getNewBlock();
		RemovableBlock fifth = (RemovableBlock) sut.getNewBlock();
		sut.removeBlock(fifth);

		Iterator<Block> iterator = sut.iterator();
		// Assert.assertEquals(true,iterator.hasNext());
		Assert.assertEquals(first.getBlockNumber(), iterator.next().getBlockNumber());
		// Assert.assertEquals(true,iterator.hasNext());
		Assert.assertEquals(second.getBlockNumber(), iterator.next().getBlockNumber());
		// Assert.assertEquals(true,iterator.hasNext());
		Assert.assertEquals(fourth.getBlockNumber(), iterator.next().getBlockNumber());
		// para probar que no tire excepcion
		iterator.next();
		// Assert.assertEquals(false,iterator.hasNext());
	}

}
