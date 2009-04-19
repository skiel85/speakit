package speakit.io;

import java.io.IOException;

/**
 * Maneja un archivo directo organizado en bloques de tamaño configurable.
 * 
 */
public interface BlocksFile {

	/**
	 * lee bytes de un bloque específico
	 * @param blockNumber
	 * @param content
	 */
	byte[] getBytes(int blockNumber);
	
	/**
	 * Graba bytes en un bloque específico
	 * @param blockNumber
	 * @param content
	 */
	void setBytes(int blockNumber, byte[] content); 
	/**
	 * Crea un bloque nuevo al final del archivo y devuelve el id
	 * 
	 * @return
	 * @throws IOException 
	 */
	int appendBlock() throws IOException;
	
	int getBlockSize(); 
	long getSize(); 
	void create(int blockSize) throws IOException  ;
	void load() throws IOException;
}
