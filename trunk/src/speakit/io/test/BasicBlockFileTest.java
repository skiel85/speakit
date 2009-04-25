package speakit.io.test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.BasicBlockFile;
import speakit.io.BasicBlockFileImpl;
import speakit.io.BlockFileOverflowException;
import speakit.io.WrongBlockNumberException;

public class BasicBlockFileTest {

	private static final int BLOCK_SIZE = 256;

	File file;

	BasicBlockFile sut;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		BasicBlockFile createdFile;
		createdFile = new BasicBlockFileImpl(this.file);
		createdFile.create(BLOCK_SIZE);

		sut = new BasicBlockFileImpl(this.file);
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
	public void testWriteAndRead() throws Exception {
		sut.appendBlock();
		byte[] content = new byte[] { 1, 5, 3 };
		int newBlockNumber = sut.appendBlock();
		sut.write(newBlockNumber, content);
		// meto un bloque en el medio para verificar que utilice el número de
		// bloque
		sut.write(sut.appendBlock(), new byte[] { 23 });
		Assert.assertArrayEquals(content, Arrays.copyOf(sut.read(newBlockNumber), 3));
	}

	/**
	 * probar que arroje blockoverflowexception si se intenta escribir mas bytes
	 * de lo soportado
	 * 
	 * @throws IOException
	 * @throws BlockFileOverflowException
	 */
	@Test
	public void testWriteWithOverflow() throws Exception {
		int overflowBlockSize = sut.getBlockSize() + sut.getBlockSize() / 2;
		byte[] content = new byte[overflowBlockSize];
		int newBlockNumber = sut.appendBlock();
		try {
			sut.write(newBlockNumber, content);
			Assert.fail("Se esperaba excepción indicando que el bloque no entra");
		} catch (BlockFileOverflowException e) {
			// creo un bloque con el tamaño necesario para que entre utilzando
			// la informacion de la excepcion
			int blockSize = overflowBlockSize - e.getOverflowLenght();
			content = new byte[blockSize];

			// deberia funcionar bien
			sut.write(newBlockNumber, content);

			// creo un bloque con un byte mas que el bloque que entra
			int littleOverflowSize = blockSize + 1;
			content = new byte[littleOverflowSize];
			try {
				sut.write(littleOverflowSize, content);
				Assert.fail("Se esperaba excepción indicando que el bloque no entra");
			} catch (BlockFileOverflowException ex) {

			}
		}

	}

	@Test
	public void testCantUseOutOfBoundsBlocks() throws IOException, WrongBlockNumberException, BlockFileOverflowException {
		byte[] content = new byte[sut.getBlockSize()];
		try {
			sut.write(0, content);
			Assert.fail("Se esperaba excepción");
		} catch (WrongBlockNumberException e) {

		}
		// luego de agregar un bloque, el bloque 0 debe estar disponible
		sut.appendBlock();
		sut.write(0, content);

		try {
			sut.read(1);
			Assert.fail("Se esperaba excepción");
		} catch (WrongBlockNumberException e) {

		}
	}

	@Test
	public void testBlockCount() throws IOException {
		Assert.assertEquals(0, this.sut.getBlockCount());
		this.sut.appendBlock();
		Assert.assertEquals(1, this.sut.getBlockCount());
	}

	// TODO: Si bien hoy el header del archivo se guarda solo
	// la primera vez, puede ser que mas adelante no. Entonces probar que luego
	// de escribir algo en el archivo(ejemplo: apendear un bloque), que el
	// header se guarde bien, es decir que haga un seek(0) y no se escriba en
	// cualquier lado destruyendo el archivo.

}
