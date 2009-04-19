package speakit.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import speakit.dictionary.serialization.IntegerField;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SimpleBlocksFile  implements BlocksFile {
	private RandomAccessFile randomFile;
	private final File	file;
	private int	blockSize;

	public SimpleBlocksFile(File file) {
		this.file = file;
	} 

	@Override
	public int appendBlock() throws IOException {
		throw new NotImplementedException();
	}

	/**
	 * Crea el archivo y escribe el bloque header con un campo que indica el tamaño de bloque.
	 */
	@Override
	public void create(int blockSize) throws IOException{
		this.blockSize=blockSize;
		randomFile=new RandomAccessFile(file,"rw");
		
		//Graba el header
		saveHeader();
	}

	/**
	 * Graba el tamaño de bloque
	 * @throws IOException
	 */
	private void saveHeader() throws IOException {
		IntegerField blockSizeField = new IntegerField(this.blockSize);
		ByteArrayOutputStream headerContentStream = new ByteArrayOutputStream(this.blockSize);
		blockSizeField.serialize(headerContentStream);
		randomFile.write(headerContentStream.toByteArray());
	}

	@Override
	public int getBlockSize() {
		return this.blockSize;
	}

	@Override
	public byte[] getBytes(int blockNumber) {
		throw new NotImplementedException();
	}

	/**
	 * Abre el archivo y lee los primeros bytes de control para obtener el header
	 */
	@Override
	public void load() throws IOException {
		randomFile=new RandomAccessFile(file,"rw");
		loadHeader();	
	}

	/**
	 *  Carga el tamaño de bloque
	 * @throws IOException
	 */
	private void loadHeader() throws IOException {
		//Obtiene el tamaño de bloque
		IntegerField blockSizeField = new IntegerField();
		byte[] content=new byte[blockSizeField.getSerializationSize()];
		randomFile.read(content,0,content.length);  
		blockSizeField.deserialize(new ByteArrayInputStream(content));
		
		//Graba el tamaño de bloque
		this.blockSize=blockSizeField.getInteger();
	}

	@Override
	public void setBytes(int blockNumber, byte[] content) {
		throw new NotImplementedException();
	}

	@Override
	public long getSize() {
		throw new NotImplementedException();
	}

	 

}
