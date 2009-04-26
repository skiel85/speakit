package speakit.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;


public class BasicBlockFileImpl implements BasicBlockFile {
	private int blockSize;
	private final File file;
	private int HEADER_BLOCK_QTY = 1;

	private RandomAccessFile randomFile;

	public BasicBlockFileImpl(File file) {
		this.file = file;
	}

	@Override
	public int appendBlock() throws IOException {
		// me posiciono al final del archivo
		this.randomFile.seek(this.getFileSize());
		// escribo un bloque vacío
		this.randomFile.write(new byte[this.blockSize], 0, this.blockSize);
		// devuelvo el numero del último bloque
		return getLastBlockNumber();
	}

	private int getLastBlockNumber() throws IOException {
		return ((int) (this.getFileSize() / this.getBlockSize()) - HEADER_BLOCK_QTY - 1);
	}

	/**
	 * Crea el archivo y escribe el bloque header con un campo que indica el
	 * tamaño de bloque.
	 */
	@Override
	public void create(int blockSize) throws IOException {
		this.blockSize = blockSize;
		randomFile = new RandomAccessFile(file, "rw");
		// escribo el primer bloque

		randomFile.write(new byte[this.blockSize]);
		// Graba el header
		saveHeader();
	}

	private int getActualBlockNumberFromUserBlockNumber(int userBlockNumber) {
		return userBlockNumber + this.HEADER_BLOCK_QTY;
	}

	/**
	 * Calcula la posición física del comienzo del bloque a partir de un numero
	 * de bloque
	 * 
	 * @param blockNumber
	 * @return
	 * @throws IOException
	 */
	private long getActualPositioFromUserBlockNumber(int blockNumber) throws IOException {
		return ((long) getActualBlockNumberFromUserBlockNumber(blockNumber)) * this.getBlockSize();
	}

	@Override
	public int getBlockSize() {
		return this.blockSize;
	}

	@Override
	public long getFileSize() throws IOException {
		return this.randomFile.length();
	}

	/**
	 * Abre el archivo y lee los primeros bytes de control para obtener el
	 * header
	 */
	@Override
	public void load() throws IOException {
		randomFile = new RandomAccessFile(file, "rw");
		loadHeader();
	}

	/**
	 * Lee el encabezado carga el tamaño de bloque
	 * 
	 * @throws IOException
	 */
	private void loadHeader() throws IOException {
		// creo un campo entero para deserializar el numero de bloque desde el
		// archivo
		IntegerField blockSizeField = new IntegerField();
		// creo un array de un tamaño justo para que entre el contenido de la
		// serializacion de un campo entero
		byte[] content = new byte[blockSizeField.getSerializationSize()];
		// leo tantos bytes del archivo como indique el tamaño le array
		randomFile.read(content, 0, content.length);
		// deserializo el campo entero que contiene el numero de bloque
		blockSizeField.deserialize(new ByteArrayInputStream(content));

		// Graba el tamaño de bloque
		this.blockSize = blockSizeField.getInteger();
	}

	/**
	 * A partir de un array devuelve otro con el tamaño pedido. Si el tamaño
	 * nuevo es mayor que el tamaño del array original se completa con ceros, y
	 * si es menor se recorta.
	 * 
	 * @param content
	 * @param toSize
	 * @return
	 */
	private byte[] paddingRight(byte[] content, int toSize) {
		return Arrays.copyOf(content, toSize);
	}

	@Override
	public byte[] read(int blockNumber) throws IOException {
		validateBlockNumber(blockNumber);
		byte[] content = new byte[this.blockSize];
		// posiciona el archivo en la posicion donde comienza el bloque
		randomFile.seek(getActualPositioFromUserBlockNumber(blockNumber));
		// lee los bytes
		randomFile.read(content, 0, this.blockSize);
		return content;
	}

	@Override
	public void write(int blockNumber, byte[] content) throws IOException, BlockFileOverflowException, WrongBlockNumberException {
		if (content.length > this.blockSize) {
			throw new BlockFileOverflowException(this.blockSize, content.length);
		}
		validateBlockNumber(blockNumber);
		writeBlockOnPosition(getActualPositioFromUserBlockNumber(blockNumber), content);
	}

	protected void validateBlockNumber(int blockNumber) throws IOException, WrongBlockNumberException {
		int actualBlockNumber = getActualBlockNumberFromUserBlockNumber(blockNumber);
		if (!(actualBlockNumber >= 0 & blockNumber <= getLastBlockNumber())) {
			throw new WrongBlockNumberException(blockNumber);
		}
	}

	private void writeBlockOnPosition(long position, byte[] content) throws IOException {
		// rellena con ceros el array de bytes
		randomFile.seek(position);
		// completo el array para que mida lo mismo que el bloque
		byte[] contentWithRightPadding = paddingRight(content, this.blockSize);
		// lo escribo
		randomFile.write(contentWithRightPadding, 0, this.blockSize);
	}

	/**
	 * Graba el encabezado, incluye el tamaño de bloque
	 * 
	 * @throws IOException
	 */
	private void saveHeader() throws IOException {
		IntegerField blockSizeField = new IntegerField(this.blockSize);
		ByteArrayOutputStream headerContentStream = new ByteArrayOutputStream(this.blockSize);
		blockSizeField.serialize(headerContentStream);
		randomFile.seek(0L);
		randomFile.write(headerContentStream.toByteArray());
	}

	/**
	 * Devuelve la cantidad de bloques de usuario que hay en el archivo
	 */
	@Override
	public int getBlockCount() throws IOException {
		return getActualBlockNumberFromUserBlockNumber(getLastBlockNumber());
	}

}
