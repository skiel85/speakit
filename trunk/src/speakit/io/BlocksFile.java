package speakit.io;

import java.io.File;
import java.io.IOException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Es un archivo de bloques que extiene la funcionalidad de BasicBlocksFile agregando la posibilidad de eliminar bloques y de crear nuevos reutilizando eliminados
 *
 */
public class BlocksFile implements BasicBlocksFile {

	private BasicBlocksFile bfile;
	
	public BlocksFile(File file) {
		bfile=new BasicBlocksFileImpl(file);
	}


	@Override
	public void create(int blockSize) throws IOException {
		bfile.create(blockSize);
	}

	@Override
	public int getBlockSize() {
		return bfile.getBlockSize();
	}

	@Override
	public long getFileSize() throws IOException {
		return bfile.getFileSize();
	}

	@Override
	public void load() throws IOException {
		bfile.load();
	}

	@Deprecated
	@Override
	public byte[] read(int blockNumber) throws IOException {
		return bfile.read(blockNumber);
	}

	@Deprecated
	@Override
	public void write(int blockNumber, byte[] content) throws IOException, BlocksFileOverflowException, WrongBlockNumberException {
		bfile.write(blockNumber, content);
	}
	
	

	/**
	 * Crea un bloque nuevo. Si hay bloques eliminados se reutilizan, sino se agrega uno al final.
	 * @return
	 * @throws IOException
	 */
	public BytesBlock getNewBlock() throws IOException {
		int blockCount = this.bfile.getBlockCount();
		for(int i = 0;i<blockCount;i++){
			BytesBlock block = this.getBlock(i);
			if (block.getIsRemoved()){ 
				return prepareNewBlock(block);
			}
		}
		return prepareNewBlock(this.getBlock(bfile.appendBlock()));
	}
	
	private BytesBlock prepareNewBlock(BytesBlock block) throws IOException{
		block.prepareAsNew();
		this.saveBlock(block);
		return block;
	}
	
	private BytesBlock getBlock(int blockNumber) throws IOException {
		BytesBlock block = new BytesBlock(blockNumber);
		block.deserialize(this.bfile.read(block.getBlockNumber()));
		return block;
	}


	@Deprecated 
	@Override
	public int appendBlock() throws IOException {
		return bfile.appendBlock();
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
		this.bfile.write(block.getBlockNumber(), blockSerialization);
	}
	
	@Deprecated 
	@Override
	public int getBlockCount() {
		throw new NotImplementedException();
	}

}
