package speakit.io;

import java.io.IOException;

public interface BlocksFile {

	/**
	 * Crea el archivo en disco dividido en bloques del tamaño elegido y lo deja
	 * disponible para su uso
	 * 
	 * @param blockSize
	 * @throws IOException
	 */
	public abstract void create(int blockSize) throws IOException;

	/**
	 * Obtiene el tamaño de bloque que se usa en todo el archivo, es decir, los
	 * tamaños de las porciones en que se divide el archivo.
	 * 
	 * @return
	 */
	public abstract int getBlockSize();

	/**
	 * Obtiene el tamaño del archivo.
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

}