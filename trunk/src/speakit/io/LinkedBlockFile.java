package speakit.io;

import java.io.File;
import java.io.IOException;

import speakit.dictionary.files.RecordSerializationException;

public class LinkedBlockFile extends RemovableBlockFile {

	public LinkedBlockFile(File file) {
		super(file);
	}

	/**
	 * Guarda un bloque y si el bloque provoca overflow se reparte la carga en
	 * bloques que se encadenan.
	 * 
	 * @throws RecordSerializationException
	 */
	@Override
	public void saveBlock(Block block) throws IOException, RecordSerializationException {
		try {
			super.saveBlock(block);
			// TODO: aquí verificar que si tiene nextBlockNumber>0, esto querría
			// decir que se guardó sin necesidad de bloques nuevos
			// en ese caso habría que eliminar en cascada los bloques siguientes
		} catch (BlocksFileOverflowException ex) {
			// Solo trabaja con bloques de tipo LinkedBytesBlock
			LinkedBlock startBlock = new LinkedBlock(block.getBlockNumber());
			startBlock.copyFrom((LinkedBlock) block);

			// primero hay qye ver si el blockActual no tiene un siguiente, en
			// ese caso usar el siguiente
			LinkedBlock nextBlock = null;
			if (startBlock.getNextBlockNumber() >= 0) {
				nextBlock = (LinkedBlock) this.getBlock(startBlock.getNextBlockNumber());
			} else {
				nextBlock = (LinkedBlock) this.getNewBlock();
			}
			// corto la parte de overflow y se la asigno al bloque siguiente
			startBlock.truncateOverflow(ex.getOverflowLenght(), nextBlock);

			// grabo el primer pedazo de bloque
			super.saveBlock(startBlock);
			// grabo el bloque excedente llamando al mismo método
			// recursivamente,
			// teniendo en cuenta de que este nuevo bloque puede provocar
			// overflow
			// lo que iniciaria el proceso nuevamente
			this.saveBlock(nextBlock);
		}
	}

	/**
	 * Obtiene el bloque elegido. Si el bloque obtenido tiene bloque siguiente,
	 * se concatena el contenido al bloque anterior y así sucesivamente
	 * 
	 * @throws RecordSerializationException
	 */
	@Override
	public Block getBlock(int blockNumber) throws IOException, RecordSerializationException {
		LinkedBlock startBlock = (LinkedBlock) super.getBlock(blockNumber);
		if (startBlock.getNextBlockNumber() >= 0) {
			LinkedBlock current = startBlock;
			do {
				current = (LinkedBlock) super.getBlock(current.getNextBlockNumber());
				startBlock.appendNextBlockContent(current);
			} while (current.getNextBlockNumber() >= 0);
		}
		return startBlock;
	}

	@Override
	protected Block createBlock(int blockNumber) {
		return new LinkedBlock(blockNumber);
	}

}
