package speakit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Configuration {
	private static final String SPEAKIT_CONF = "speakit.conf";
	private int blockSize = 0;
	private int trieDepth = 0;

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setTrieDepth(int trieDepth) {
		this.trieDepth = trieDepth;
	}

	public int getTrieDepth() {
		return trieDepth;
	}

	public void load(FileManager fileManager) throws FileNotFoundException, IOException {
		if (!fileManager.exists(SPEAKIT_CONF)) {
			this.blockSize = 512;
			this.trieDepth = 4;
			create(fileManager);
			// throw new
			// IOException("No se puede cargar la configuración: No se encuentra el archivo 'speakit.conf'. Es un archivo de texto con el siguiente formato: 'XXX YYY', donde XXX es el tamaño de bloque e YYY es la profundidad del trie.");
		}
		File file = fileManager.openFile(SPEAKIT_CONF);
		RandomAccessFile configFile = new RandomAccessFile(file, "r");

		String blockSizeString = "";
		String trieDepthString = "";
		try {
			blockSizeString = configFile.readLine();
			trieDepthString = configFile.readLine();
			this.blockSize = new Integer(blockSizeString);
			this.trieDepth = new Integer(trieDepthString);
		} catch (NumberFormatException ex) {
			throw new IOException("No se puede cargar la configuración: error en el formato del archivo. blockSizeString=" + blockSizeString + ", trieDepthString=" + trieDepthString);
		}

	}

	public void create(FileManager fileManager) throws FileNotFoundException, IOException {
		FileOutputStream fo = new FileOutputStream(fileManager.openFile(SPEAKIT_CONF));
		fo.write((this.getBlockSize() + "\n" + this.getTrieDepth()).getBytes());
		fo.flush();
		fo.close();
	}
}
