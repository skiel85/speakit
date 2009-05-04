package speakit.io.recordfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import speakit.io.blockfile.Block;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;

/**
 * Interpreta un bloque de bytes como una lista de registros y provee métodos
 * para trabajar sobre ella.
 * 
 * @author Nahuel
 * 
 * @param <RECTYPE>
 * @param <KEYTYPE>
 */
public class RecordsListBlockInterpreter<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> {

	private final Block				block;
	private RecordFactory<RECTYPE>	recordFactory;
	private List<RECTYPE>			records;

	/**
	 * @param block
	 *            bloque de bytes. Se supone con sus registros ordenados.
	 * @param recordFactory
	 * @throws RecordSerializationException
	 */
	public RecordsListBlockInterpreter(Block block, RecordFactory<RECTYPE> recordFactory) throws RecordSerializationException {
		this.block = block;
		this.recordFactory = recordFactory;
		this.loadRecords();
	}

	/**
	 * Actualiza un registro. Busca un registro con la misma clave y lo
	 * reemplaza.
	 * 
	 * @param updatingRecord
	 * @throws RecordSerializationException
	 */
	void updateRecord(RECTYPE updatingRecord) throws RecordSerializationException {
		int recordToUpdatePosition = findRecordPosition(updatingRecord.getKey());
		if (recordToUpdatePosition == -1) {
			throw new IllegalArgumentException("El registro a actualizar no pertenece al archivo.");
		} else {
			records.set(recordToUpdatePosition, updatingRecord);
		}
	} 

	/**
	 * Devuelve la posicion dentro de la lista de registros donde se encuentra
	 * uno con la clave pasada como parámetro.
	 * 
	 * @param record
	 * @return
	 */
	private int findRecordPosition(KEYTYPE key) {
		//return binarySearch(key, 0, this.records.size() - 1);
		for (int i = 0; i < records.size(); i++) {
			RECTYPE item = records.get(i);
			if(item.getKey().compareTo(key)==0){
				return i;
			}
		}
		return -1;
	}

	/**
	 * Obtiene un registro de la lista con la clave indicada.
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException {
		int recordPosition = findRecordPosition(key);
		if(recordPosition<0){
			return null;
		}
		return this.records.get(recordPosition);
	}

	/**
	 * realiza una busqueda binaria dentro de la lista de registros
	 * 
	 * @param list
	 * @param key
	 * @param low
	 * @param high
	 * @return
	 */
	private int binarySearch(KEYTYPE key, int low, int high) {
		if (high < low) {
			return -1;
		}

		int mid = low + ((high - low) / 2);
		if (this.records.get(mid).compareToKey(key) > 0)
			return binarySearch(key, low, mid - 1);
		else if (this.records.get(mid).compareToKey(key) < 0)
			return binarySearch(key, mid + 1, high);
		else
			return mid;
	}

	/**
	 * Carga la lista de registros a partir del contenido del bloque
	 * 
	 * @throws RecordSerializationException
	 */
	private void loadRecords() throws RecordSerializationException {
		this.records = new ArrayList<RECTYPE>();
		ByteArrayInputStream is = new ByteArrayInputStream(block.getContent());

		while (is.available() > 0) {
			RECTYPE each = this.recordFactory.createRecord();
			each.deserialize(is);
			records.add(each);
		}
	}

	/**
	 * Inserta un registro en la lista
	 * @param newRecord
	 */
	public void insertRecord(RECTYPE newRecord) {
		this.records.add(newRecord);
		sort();
	}

	/**
	 * Devuelve el bloque wrappeado con el contenido modificado.
	 * 
	 * @return
	 * @throws RecordSerializationException
	 */
	public Block getBlock() throws RecordSerializationException {
		this.saveRecords();
		return block;
	}

	/**
	 * Serializa la lista de registros en el bloque.
	 * 
	 * @throws RecordSerializationException
	 */
	private void saveRecords() throws RecordSerializationException {
		sort();
		block.clear();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		for (RECTYPE each : records) {
			each.serialize(os);
		}
		block.setContent(os.toByteArray());
	}

	private void sort() {
		Collections.sort(this.records);
	}
	
	public List<RECTYPE> getRecords(){
		return this.records;
	}
}
