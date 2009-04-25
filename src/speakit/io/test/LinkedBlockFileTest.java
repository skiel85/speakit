package speakit.io.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.Block;
import speakit.io.BlocksFile;
import speakit.io.LinkedBlockFile;
import speakit.io.MultiBlocksFile;

public class LinkedBlockFileTest {

	private static final int BLOCK_SIZE = 14;// 14 es el mínimo soportado

	File file;

	LinkedBlockFile sut;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		BlocksFile createdFile;
		createdFile = new MultiBlocksFile(this.file);
		createdFile.create(BLOCK_SIZE);

		sut = new LinkedBlockFile(this.file);
		sut.load();
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testCanSaveLargeBlock() throws IOException {
		byte[] content = new byte[] { 9, 4, 2, 1, 5, 58, 9, 54, 32, 1, 1, 49, 4, 2, 34, 72, 62, 34, 6, 9, 42, 42, 123, 21 };
		Block newBlock = this.sut.getNewBlock();
		newBlock.setContent(content);
		this.sut.saveBlock(newBlock);
		Block loadedBlock = this.sut.getBlock(newBlock.getBlockNumber());
		Assert.assertArrayEquals(content, loadedBlock.getContent());
	}

	// TODO: peligro al deserializar bloques (ver cada clase en particular) que
	// pasa cuando se deserializa un bloque a partir de uno que estaba
	// eliminado, puede tener basura
	// TODO: peligro al deserializar bloques (ver cada clase en particular) que
	// pasa cuando se deserializa un bloque a partir de un serializationData
	// nulo
	// TODO: implementar que el eliminar de un bloque elimine todos los bloques
	// linkeados en forma encadenada
	// TODO: probar que al guardar un bloque encadenado reutilice los bloques
	// encadenados y si sobra alguno que se elimine

}
