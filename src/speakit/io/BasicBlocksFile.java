package speakit.io;

import java.io.IOException;


/**
 * Maneja un archivo directo organizado en bloques de tamaño configurable.
 * 
 */
public interface BasicBlocksFile {

	/**
	 * Crea un bloque nuevo al final del archivo y devuelve el id
	 * 
	 * @return
	 * @throws IOException 
	 */
	int appendBlock() throws IOException;
	
	/**
	 * Crea el archivo en disco dividido en bloques del tamaño elegido y lo deja disponible para su uso
	 * @param blockSize
	 * @throws IOException
	 */
	void create(int blockSize) throws IOException  ; 
	/**
	 * Obtiene el tamaño de bloque que se usa en todo el archivo, es decir, los tamaños de las porciones en que se divide el archivo.
	 * @return
	 */
	int getBlockSize();
	
	/**
	 * Obtiene el tamaño del archivo.
	 * @return
	 * @throws IOException
	 */
	long getFileSize() throws IOException;
	
	/**
	 * Carga el archivo desde el disco y lo deja disponible para su uso
	 * @throws IOException
	 */
	void load() throws IOException; 
	
	/**
	 * lee bytes de un bloque específico
	 * @param blockNumber
	 * @param content
	 * @throws IOException 
	 */
	byte[] read(int blockNumber) throws IOException;
	
	/**
	 * Graba bytes en un bloque específico
	 * @param blockNumber
	 * @param content
	 * @throws IOException 
	 */
	void write(int blockNumber, byte[] content) throws IOException,BlocksFileOverflowException,WrongBlockNumberException;
}
