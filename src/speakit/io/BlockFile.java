package speakit.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import speakit.dictionary.files.RecordSerializationException;

public class BlockFile implements Iterable<Block> {

	private BasicBlockFileImpl file;

	public BlockFile(File file) {
		this.file = new BasicBlockFileImpl(file);
	}

	/**
	 * Crea el archivo en disco dividido en bloques del tamaño elegido y lo deja
	 * disponible para su uso
	 * 
	 * @param blockSize
	 * @throws IOException
	 */
	public void create(int blockSize) throws IOException {
		this.file.create(blockSize);
	}

	/**
	 * Obtiene el tamaño de bloque que se usa en todo el archivo, es decir, los
	 * tamaños de las porciones en que se divide el archivo.
	 * 
	 * @return
	 */
	public int getBlockSize() {
		return this.file.getBlockSize();
	}

	/**
	 * Obtiene el tamaño del archivo.
	 * 
	 * @return
	 * @throws IOException
	 */
	public long getFileSize() throws IOException {
		return this.file.getFileSize();
	}

	/**
	 * Carga el archivo desde el disco y lo deja disponible para su uso
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException {
		this.file.load();
	}

	/**
	 * Crea un bloque nuevo. Si hay bloques eliminados se reutilizan, sino se
	 * agrega uno al final.
	 * 
	 * @return
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public Block getNewBlock() throws IOException, RecordSerializationException {
		return getBlock(this.file.appendBlock());
	}

	/**
	 * Obtiene un bloque del archivo
	 * 
	 * @param blockNumber
	 * @return
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public Block getBlock(int blockNumber) throws IOException, RecordSerializationException {
		Block block = this.createBlock(blockNumber);
		block.deserialize(this.file.read(blockNumber));
		return block;
	}

	/**
	 * Crea un nuevo bloque
	 * 
	 * @param blockNumber
	 * @return
	 */
	protected Block createBlock(int blockNumber) {
		return new Block(blockNumber);
	}

	/**
	 * Guarda un bloque en el archivo
	 * 
	 * @param block
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public void saveBlock(Block block) throws IOException, RecordSerializationException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		block.serialize(outputStream);
		this.file.write(block.getBlockNumber(), outputStream.toByteArray());
	}

	protected int getBlockCount() throws IOException {
		return this.file.getBlockCount();
	}

	/**
	 * Limplia el bloque, lo marca como no eliminado, lo guarda y lo devuelve
	 * 
	 * @param block
	 * @return
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	protected Block clearBlock(Block block) throws IOException, RecordSerializationException {
		block.clear();
		this.saveBlock(block);
		return block;
	}

	@Override
	public Iterator<Block> iterator() {
		return new BlockFileIterator(this);
	}
}
