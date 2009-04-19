package speakit.io;

import java.io.IOException;


/**
 * Maneja un archivo directo organizado en bloques de tamaño configurable.
 * 
 */
public interface BasicBlocksFile extends BlocksFile {

	/**
	 * Crea un bloque nuevo al final del archivo y devuelve el id
	 * 
	 * @return
	 * @throws IOException 
	 */
	int appendBlock() throws IOException;
	
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

	/**
	 * Devuelve la cantidad de bloques del archivo
	 * @return
	 * @throws IOException 
	 */
	int getBlockCount() throws IOException;
}
