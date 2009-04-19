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
	 * Constructor para crear un OutputStream que escribir� a partir de la
	 * posici�n indicada sobreescribiendo.
	 * 
	 * @param randomAccessFile
	 *            El archivo de donde leer los datos.
	 * @param startPosition
	 *            La posici�n donde comienza este stream.
	 * @param length
	 *            El largo de este InputStream.
	 */
	public RandomAccessFileOutputStream(RandomAccessFile randomAccessFile, long startPosition) {
		this.file = randomAccessFile;
		this.startPosition = startPosition;
		this.currentPosition = startPosition;
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
		currentPosition += length;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(int b) throws IOException {
		file.seek(currentPosition);
		file.write(b);
	}
}
