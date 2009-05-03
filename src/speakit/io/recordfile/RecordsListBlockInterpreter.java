package speakit.io.recordfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import speakit.io.blockfile.Block;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;

public class RecordsListBlockInterpreter<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> {
	
	private final Block	block;
	private RecordFactory<RECTYPE>	recordFactory;

	public RecordsListBlockInterpreter(Block block,RecordFactory<RECTYPE> recordFactory){
		this.block = block;
		this.recordFactory = recordFactory;
	}

	void updateRecord(RECTYPE updatingRecord) throws RecordSerializationException {
		List<RECTYPE> records = this.getRecords();
		int recordToUpdatePosition = findRecordPosition(updatingRecord, records);
		if(recordToUpdatePosition==-1){
			throw new IllegalArgumentException("El registro a actualizar no pertenece al archivo.");	
		}else{
			records.set(recordToUpdatePosition, updatingRecord);
		}
		this.saveRecords(records);
	}

	private int findRecordPosition(RECTYPE record, List<RECTYPE> records) {
		for (int i = 0; i < records.size(); i++) {
			RECTYPE eachRecord = records.get(i);
			if(eachRecord.getKey().compareTo(record.getKey())==0){
				return i;
			}
		}
		return -1;
	}
	
	private List<RECTYPE> getRecords() throws RecordSerializationException {
		List<RECTYPE> records = new ArrayList<RECTYPE>();
		ByteArrayInputStream is = new ByteArrayInputStream(block.getContent());

		while (is.available() > 0) {
			RECTYPE each = this.recordFactory.createRecord();
			each.deserialize(is);
			records.add(each);
		}
		return records;
	}

	private void saveRecords(List<RECTYPE> records) throws RecordSerializationException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		for (RECTYPE each : records) {
			each.serialize(os);
		}
		block.setContent(os.toByteArray());
	}
	
	public RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException {
		List<RECTYPE> records = this.getRecords();
		for (RECTYPE eachRecord : records) {
			if (eachRecord.compareToKey(key) == 0) {
				return eachRecord;
			}
		}
		return null;
	}

	public Block getBlock() {
		return block;
	}
}
