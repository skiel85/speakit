package speakit.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Permite que una secci�n de un RandomAccessFile sea accedido como un
 * OutputStream
 */
public class RandomAccessFileOutputStream extends OutputStream {
	private RandomAccessFile file;
	private long startPosition;
	private long currentPosition;

	/**
	 * Constructor para crear un OutputStream que escribir� al final de un
	 * archivo de acceso aleatorio.
	 * 
	 * @param randomAccessFile
	 *            Archivo donde escribir.
	 * 
	 * @throws IOException
	 */
	public RandomAccessFileOutputStream(RandomAccessFile randomAccessFile) throws IOException {
		file = randomAccessFile;
		startPosition = randomAccessFile.length();
		currentPosition = randomAccessFile.length();
	}

	/**
	 * Obtiene la posici�n actual en el archivo.
	 * 
	 * @return Posici�n actual en el archivo.
	 */
	public long getPosition() {
		return currentPosition;
	}

	/**
	 * Cantidad de bytes realmente escritos.
	 * 
	 * @return La cantidad de bytes realmente escritos.
	 */
	public long getLengthWritten() {
		return currentPosition - startPosition;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(byte[] b, int offset, int length) throws IOException {
		file.seek(currentPosition);
		file.write(b, offset, length);
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(int b) throws IOException {
		file.seek(currentPosition);
		file.write(b);
	}
}
