package speakit.io.recordfile;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import speakit.io.blockfile.Block;
import speakit.io.blockfile.BlockFile;
import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.RemovableBlockFile;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;

//TODO metodo updateRecord. Si el update dentro del bloque provoca overflow hay que meterlo en un bloque nuevo, pero esto lo tiene que hacer el cliente de esta clase. 

public class DirectRecordFile<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> implements RecordFile<RECTYPE, KEYTYPE> {
	private BlockFile				blocksFile;
	private RecordFactory	recordFactory;

	public DirectRecordFile(File file, RecordFactory recordFactory) {
		this.blocksFile = new RemovableBlockFile(file);
//		this.blocksFile = new LinkedBlockFile(file);
		this.recordFactory = recordFactory;
	}

	public void create(int blockSize) throws IOException {
		this.blocksFile.create(blockSize);
	}

	public void load() throws IOException {
		this.blocksFile.load();
	}

	@Override
	public boolean contains(KEYTYPE key) throws IOException, RecordSerializationException {
		RECTYPE record = this.getRecord(key);
		return (record != null);
	}

	@Override
	public RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException {
		RecordsListBlockInterpreter<RECTYPE, KEYTYPE> block=this.findBlock(key);
		if(block!=null){
			return block.getRecord(key);
		}
		return null;
	}

	private RecordsListBlockInterpreter<RECTYPE, KEYTYPE> getBlock(int blockNumber) throws RecordSerializationException, IOException {
		Block block = this.blocksFile.getBlock(blockNumber);
		return this.asRecordsBlock(block);
	}

	public RECTYPE getRecord(KEYTYPE key, int blockNumber) throws IOException, RecordSerializationException {
		RecordsListBlockInterpreter<RECTYPE, KEYTYPE> block = getBlock(blockNumber);
		return block.getRecord(key);
	}

	@Override
	public long insertRecord(RECTYPE record) throws IOException, RecordSerializationException {
		Iterator<Block> blockIterator = this.blocksFile.iterator();
		Block blockToInsert;
		while (blockIterator.hasNext()) {
			try { 
				blockToInsert = blockIterator.next();
				this.insertRecord(record, blockToInsert);
				return blockToInsert.getBlockNumber();
			} catch (BlockFileOverflowException e) {
				// Dejado intencionalmente en blanco.
				// En caso de overflow se continúa intentando insertar en el
				// siguiente bloque.
			}
		}
 
		blockToInsert = this.blocksFile.getNewBlock();
		this.insertRecord(record, blockToInsert);
		return blockToInsert.getBlockNumber(); 
	}

	public int insertRecord(RECTYPE record, int blockNumber) throws IOException, RecordSerializationException {
		return this.insertRecord(record,this.blocksFile.getBlock(blockNumber));
	}
	
	private int insertRecord(RECTYPE record, Block block) throws RecordSerializationException, IOException  {
		RecordsListBlockInterpreter<RECTYPE, KEYTYPE> recordsBlock = asRecordsBlock(block);
		recordsBlock.insertRecord(record);
		this.saveBlock(recordsBlock);
		return block.getBlockNumber();
	}

	private void saveBlock(RecordsListBlockInterpreter<RECTYPE, KEYTYPE> block) throws RecordSerializationException, IOException {
		this.blocksFile.saveBlock(block.getBlock());
	}

	public int createBlock() throws RecordSerializationException, IOException {
		return this.blocksFile.getNewBlock().getBlockNumber();
	}

	/**
	 * Busca el bloque en donde se encuentre un registro con la clave indicada.
	 * @param key
	 * @return
	 * @throws RecordSerializationException
	 * @throws IOException
	 */
	private RecordsListBlockInterpreter<RECTYPE, KEYTYPE> findBlock(KEYTYPE key) throws RecordSerializationException, IOException {
		for (Block block : this.blocksFile) {
			RecordsListBlockInterpreter<RECTYPE, KEYTYPE> eachBlock = this.asRecordsBlock(block);
			if (eachBlock.getRecord(key) != null) {
				return eachBlock;
			}
		}
		return null;
	}

	private RecordsListBlockInterpreter<RECTYPE, KEYTYPE> asRecordsBlock(Block block) throws RecordSerializationException {
		return new RecordsListBlockInterpreter<RECTYPE, KEYTYPE>(block, this.recordFactory);
	}

	/**
	 * Actualiza un registro del archivo. Como no se le pasa el numero de bloque
	 * se busca en todos los bloques secuencialmente.
	 * 
	 * @param record
	 * @return
	 * @throws RecordSerializationException
	 * @throws IOException
	 */
	public boolean updateRecord(RECTYPE record) throws RecordSerializationException, IOException {
		RecordsListBlockInterpreter<RECTYPE, KEYTYPE> block = findBlock(record.getKey());
		updateRecord(record, block);
		return false;
	}

	/**
	 * Actualiza un registro del archivo dentro del bloque indicado.
	 * 
	 * @param record
	 * @return
	 * @throws RecordSerializationException
	 * @throws IOException
	 */
	public boolean updateRecord(RECTYPE record, int blockNumber) throws RecordSerializationException, IOException {
		RecordsListBlockInterpreter<RECTYPE, KEYTYPE> block = this.getBlock(blockNumber);
		updateRecord(record, block);
		return false;
	}

	/**
	 * Si el registro no entra en el mismo bloque, se elimina de allí y se vuelve a insertar. Si luego de actualizar el registro, este no entra en el bloque original, se lo reinserta en el archivo, de esta forma se alojará en algun nodo libre.
	 * TODO si hubo cambio los indices deben reindexar el registro en el nuevo bloque!!
	 *  
	 * @param record
	 * @param block
	 * @return El número de bloque donde se aloja el registro. Si no hubo cambio de bloque devuelve -1. 
	 * @throws RecordSerializationException
	 * @throws IOException
	 */
	private long updateRecord(RECTYPE record, RecordsListBlockInterpreter<RECTYPE,KEYTYPE> recordBlock) throws RecordSerializationException, IOException {
		recordBlock.updateRecord(record);
		try{
			Block block = recordBlock.getBlock();
			this.blocksFile.saveBlock(block);
			return -1;
		}catch(BlockFileOverflowException ex){
			recordBlock.deleteRecord(record);
			Block block = recordBlock.getBlock();
			this.blocksFile.saveBlock(block);
			return this.insertRecord(record);
		} 
	}
}
