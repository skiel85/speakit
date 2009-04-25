package speakit.io;

import java.io.IOException;

/**
 * Maneja un archivo directo organizado en bloques de tama�o configurable.
 * 
 */
public interface BasicBlockFile {
	/**
	 * Crea el archivo en disco dividido en bloques del tama�o elegido y lo deja
	 * disponible para su uso
	 * 
	 * @param blockSize
	 * @throws IOException
	 */
	public abstract void create(int blockSize) throws IOException;

	/**
	 * Obtiene el tama�o de bloque que se usa en todo el archivo, es decir, los
	 * tama�os de las porciones en que se divide el archivo.
	 * 
	 * @return
	 */
	public abstract int getBlockSize();

	/**
	 * Obtiene el tama�o del archivo.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract long getFileSize() throws IOException;

	/**
	 * Carga el archivo desde el disco y lo deja disponible para su uso
	 * 
	 * @throws IOException
	 */
	public abstract void load() throws IOException;
	
	
	/**
	 * Crea un bloque nuevo al final del archivo y devuelve el id
	 * 
	 * @return
	 * @throws IOException
	 */
	int appendBlock() throws IOException;

	/**
	 * lee bytes de un bloque espec�fico
	 * 
	 * @param blockNumber
	 * @param content
	 * @throws IOException
	 */
	byte[] read(int blockNumber) throws IOException;

	/**
	 * Graba bytes en un bloque espec�fico
	 * 
	 * @param blockNumber
	 * @param content
	 * @throws IOException
	 */
	void write(int blockNumber, byte[] content) throws IOException, BlockFileOverflowException, WrongBlockNumberException;

	/**
	 * Devuelve la cantidad de bloques del archivo
	 * 
	 * @return
	 * @throws IOException
	 */
	int getBlockCount() throws IOException;
}
