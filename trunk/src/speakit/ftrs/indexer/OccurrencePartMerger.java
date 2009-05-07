package speakit.ftrs.indexer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import speakit.FileManager;
import speakit.io.record.IntegerField;
import speakit.io.record.RecordFactory;
import speakit.io.recordfile.SecuentialRecordFile;

public class OccurrencePartMerger implements RecordFactory{
	private SecuentialRecordFile<OccurrenceRecord, IntegerField> outputFile;
	private ArrayList<String> parts;
	private ArrayList<Buffer> buffers;
	private static int MAX_SIZE_PARTS = 10;
	private FileManager manager;
	
	
	public OccurrencePartMerger(SecuentialRecordFile<OccurrenceRecord, IntegerField> outputFile, ArrayList<String> sortedParts) {
		this.buffers = new ArrayList<Buffer>();
		this.outputFile = outputFile;
		this.parts = sortedParts;
		manager = new FileManager();
	}
	
	public void merge() {
		loadBuffers(parts);
		merge(parts);
		freeBuffers(parts);
	}
	
	private void freeBuffers(ArrayList<String> subPart) {
		for (Buffer buffer : buffers) {
			buffer.releaseFile();
		}
		for (String string : subPart) {
			File f = new File(string);
			f.delete();
		}
		
	}

	private void merge(ArrayList<String> subPart) {
		while (true){
			Collections.sort(buffers);
			OccurrenceRecord record = buffers.get(0).next();
			if (record == null)
				return;
			try {
				outputFile.insertRecord(record);
			} catch (IOException ie) {
				//no escribo nada
			}
		}
	}


	private void loadBuffers(ArrayList<String> subPart) {
		for (String string : subPart) {
			try {
				Buffer buff = new Buffer(openFile(string), MAX_SIZE_PARTS);
				buffers.add(buff);
			} catch (IOException e) {
				//Particion corrupta, no la tengo en cuenta
			}
		}
	}

	private SecuentialRecordFile<OccurrenceRecord, IntegerField> openFile(String name) throws IOException{
		File file = manager.openFile(name);
		SecuentialRecordFile<OccurrenceRecord, IntegerField> recordFile = new SecuentialRecordFile<OccurrenceRecord, IntegerField>(file, this);
		return recordFile;
	}
	
	
	@Override
	public OccurrenceRecord createRecord() {
		return new OccurrenceRecord();
	}


private class Buffer implements Comparable<Buffer> {
	ArrayList<OccurrenceRecord> records;
	SecuentialRecordFile<OccurrenceRecord, IntegerField> recordFile;
	private int maxSize;
	public Buffer(SecuentialRecordFile<OccurrenceRecord, IntegerField> secuentialRecordFile, int maxSize){
		records = new ArrayList<OccurrenceRecord>();
		recordFile = secuentialRecordFile;
		this.maxSize = maxSize;
		this.fill();
	}
	
	public OccurrenceRecord next(){
		if (records.isEmpty()) {
			this.fill();
			if (records.isEmpty())
				return null;
		}
		OccurrenceRecord record = records.get(0);
		records.remove(0);
		return record;
	}
	public void releaseFile() {
		try {
			recordFile.close();
			recordFile = null;
		} catch (IOException e) {
			
		}
	}
	
	public boolean hasNext() {
		return !records.isEmpty();
	}
	
	public void fill() {
		records.clear();
		try {
			while (!recordFile.eof() && records.size() < maxSize) {
				records.add(recordFile.readRecord());
			}
		} catch (IOException e) {
			//Si hay una excepcion, simplemente dejo de cargar el buffer
		}
	}

	/**
	 * @return
	 * this < o => -1;
	 * this == o => 0;
	 * this > o => 1;
	 */
	@Override
	public int compareTo(Buffer o) {
	//Comparacion entre los buffers, si alguno esta vacio, el menor es el otro, sino
	//devuelvo la comparacion de los contenidos
		if (o == null)
			return -1;
		if (!this.hasNext())
			return 1;
		if (!o.hasNext())
			return -1;
		OccurrenceRecord rec = this.records.get(0);
		OccurrenceRecord recO = o.records.get(0);
		if (rec.compareTo(recO) == 0) {
			return rec.getDocument().getLong() < recO.getDocument().getLong() ? -1 : 1;
		} else {
			return rec.compareTo(recO);
		}
	}
	
}

}