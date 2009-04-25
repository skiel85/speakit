package speakit.io;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import speakit.dictionary.files.RecordSerializationException;

/**
 * Es un archivo de bloques que extiene la funcionalidad de BlocksFile agregando
 * la posibilidad de eliminar bloques y de crear nuevos reutilizando eliminados
 * 
 */
public class RemovableBlockFile extends BlockFile {

	public RemovableBlockFile(File file) {
		super(file);
	}

	/**
	 * Crea un bloque nuevo. Si hay bloques eliminados se reutilizan, sino se
	 * agrega uno al final.
	 * 
	 * @return
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public Block getNewBlock() throws IOException, RecordSerializationException {
		int blockCount = this.getBlockCount();
		RemovableBlock block;
		for (int i = 0; i < blockCount; i++) {
			block = (RemovableBlock) this.getBlock(i);
			if (block.isRemoved()) {
				block.setRemoved(false);
				this.clearBlock(block);
				return block;
			}
		}
		block = (RemovableBlock) super.getNewBlock();
		return block;
	}

	@Override
	protected Block createBlock(int blockNumber) {
		return new RemovableBlock(blockNumber);
	}

	/**
	 * Marca un bloque como eliminado
	 * 
	 * @param block
	 * @throws BlocksFileOverflowException
	 * @throws WrongBlockNumberException
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public void removeBlock(RemovableBlock block) throws BlocksFileOverflowException, WrongBlockNumberException, IOException, RecordSerializationException {
		block.setRemoved(true);
		saveBlock(block);
	}

	/**
	 * Marca un bloque como eliminado
	 * 
	 * @param block
	 * @throws BlocksFileOverflowException
	 * @throws WrongBlockNumberException
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public void removeBlock(int blockNumber) throws BlocksFileOverflowException, WrongBlockNumberException, IOException, RecordSerializationException {
		removeBlock((RemovableBlock) this.getBlock(blockNumber));
	}

	public Iterator<Block> iterator() {
		try {
			return new RemovableBlockFileIterator(this);
		} catch (RecordSerializationException e) {
			return null;
		}
	}

}
