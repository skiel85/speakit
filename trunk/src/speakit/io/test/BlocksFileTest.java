package speakit.io.test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.BlocksFile;
import speakit.io.BlocksFileOverflowException;
import speakit.io.SimpleBlocksFile;

public class BlocksFileTest {

	private static final int	BLOCK_SIZE	= 256;

	File						file;

	BlocksFile					sut;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		SimpleBlocksFile createdFile;
		createdFile = new SimpleBlocksFile(this.file);
		createdFile.create(BLOCK_SIZE);

		sut = new SimpleBlocksFile(this.file);
		sut.load();
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testBlockSizeAfterLoaded() throws IOException {
		Assert.assertEquals(BLOCK_SIZE, sut.getBlockSize());
	}

	/**
	 * Apendeo 2 bloques y verifico numero de bloque devuelto y tamaño del
	 * archivo
	 * 
	 * @throws IOException
	 * @throws BlocksFileNotCreatedOrLoadedException
	 */
	@Test
	public void testAppend() throws IOException {
		int blockNumber = sut.appendBlock();
		Assert.assertEquals(sut.getBlockSize() * 2, sut.getFileSize());
		Assert.assertEquals(0, blockNumber);
		blockNumber = sut.appendBlock();
		Assert.assertEquals(sut.getBlockSize() * 3, sut.getFileSize());
		Assert.assertEquals(1, blockNumber);
	}

	@Test
	public void testWriteAndRead() throws IOException, BlocksFileOverflowException {
		sut.appendBlock();
		byte[] content = new byte[]{1, 5, 3};
		int newBlockNumber = sut.appendBlock();
		sut.write(newBlockNumber, content);
		// meto un bloque en el medio para verificar que utilice el número de
		// bloque
		sut.write(sut.appendBlock(), new byte[]{23});
		Assert.assertArrayEquals(content, Arrays.copyOf(sut.read(newBlockNumber), 3));
	}

	/**
	 * probar que arroje blockoverflowexception si se intenta escribir mas bytes
	 * de lo soportado
	 * 
	 * @throws IOException
	 */
	@Test
	public void testWriteWithOverflow() throws IOException {
		byte[] content = new byte[sut.getBlockSize() + 1];
		int newBlockNumber = sut.appendBlock();
		try {
			sut.write(newBlockNumber, content);
			Assert.fail("Se esperaba excepción");
		} catch (BlocksFileOverflowException e) {

		}
	}

	// TODO: que arroje excepcion si el numero de bloque pasado no existe o es
	// incorrecto
	
	// TODO: Si bien hoy el header del archivo se guarda solo
	// la primera vez, puede ser que mas adelante no. Entonces probar que luego
	// de escribir algo en el archivo(ejemplo: apendear un bloque), que el
	// header se guarde bien, es decir que haga un seek(0) y no se escriba en
	// cualquier lado destruyendo el archivo.

}
