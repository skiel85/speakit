package speakit.io;

import java.io.File;
import java.io.IOException;

/**
 * Es un archivo de bloques que extiene la funcionalidad de BlocksFile agregando la posibilidad de eliminar bloques y de crear nuevos reutilizando eliminados
 *
 */
public class BytesBlocksFile implements BlocksFile {

	private BasicBlocksFile bbfile;
	
	public BytesBlocksFile(File file) {
		bbfile=new BasicBlocksFileImpl(file);
	}


	@Override
	public void create(int blockSize) throws IOException {
		bbfile.create(blockSize);
	}

	@Override
	public int getBlockSize() {
		return bbfile.getBlockSize();
	}

	@Override
	public long getFileSize() throws IOException {
		return bbfile.getFileSize();
	}

	@Override
	public void load() throws IOException {
		bbfile.load();
	} 
	

	/**
	 * Crea un bloque nuevo. Si hay bloques eliminados se reutilizan, sino se agrega uno al final.
	 * @return
	 * @throws IOException
	 */
	public BytesBlock getNewBlock() throws IOException {
		int blockCount = this.bbfile.getBlockCount();
		for(int i = 0;i<blockCount;i++){
			BytesBlock block = this.getBlock(i);
			if (block.getIsRemoved()){ 
				return prepareNewBlock(block);
			}
		}
		return prepareNewBlock(this.getBlock(bbfile.appendBlock()));
	}
	
	/**
	 * Limplia el bloque, lo marca como no eliminado, lo guarda y lo devuelve
	 * @param block
	 * @return
	 * @throws IOException
	 */
	private BytesBlock prepareNewBlock(BytesBlock block) throws IOException{
		block.prepareAsNew();
		this.saveBlock(block);
		return block;
	}
	
	/**
	 * Obtiene un bloque del archivo
	 * @param blockNumber
	 * @return
	 * @throws IOException
	 */
	private BytesBlock getBlock(int blockNumber) throws IOException {
		BytesBlock block = new BytesBlock(blockNumber);
		block.deserialize(this.bbfile.read(block.getBlockNumber()));
		return block;
	} 
	
	/**
	 * Marca un bloque como eliminado
	 * @param block
	 * @throws BlocksFileOverflowException
	 * @throws WrongBlockNumberException
	 * @throws IOException
	 */
	public void removeBlock(BytesBlock block) throws BlocksFileOverflowException, WrongBlockNumberException, IOException {
		block.setIsRemoved(true);
		saveBlock(block);
	}
	
	/**
	 * Marca un bloque como eliminado
	 * @param block
	 * @throws BlocksFileOverflowException
	 * @throws WrongBlockNumberException
	 * @throws IOException
	 */
	public void removeBlock(int blockNumber) throws BlocksFileOverflowException, WrongBlockNumberException, IOException {
		BytesBlock block = this.getBlock(blockNumber);
		block.setIsRemoved(true);
		saveBlock(block);
	}
	
	/**
	 * Guarda un bloque en el archivo
	 * @param block
	 * @throws IOException
	 */
	public void saveBlock( BytesBlock block) throws IOException{
		byte[] blockSerialization = block.serialize();
		this.bbfile.write(block.getBlockNumber(), blockSerialization);
	} 
}
