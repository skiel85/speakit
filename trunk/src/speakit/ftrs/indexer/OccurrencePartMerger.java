package speakit.ftrs.indexer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import speakit.FileManager;
import speakit.io.record.IntegerField;
import speakit.io.record.RecordFactory;
import speakit.io.recordfile.SecuentialRecordFile;

public class OccurrencePartMerger implements RecordFactory{
	private ArrayList<String> parts;
	private ArrayList<String> mergeParts;
	private ArrayList<Buffer> buffers;
	private static int MAX_SIZE_PARTS = 128;
	private FileManager manager;
	private Integer partNumber = 0;
	private static String FINAL_MERGE_NAME = "mergedList.merge";
	private static String PART_NAME = "mergePart";
	private static String EXTENSION_NAME = ".mergePart";
	
	
	public OccurrencePartMerger(ArrayList<String> sortedParts) throws IOException {
		this.buffers = new ArrayList<Buffer>();
		this.parts = sortedParts;
		mergeParts = new ArrayList<String>();
		manager = new FileManager();
		cleanFile(FINAL_MERGE_NAME);
	}
	
	/*private void merge() throws IOException{
		ArrayList<String> subPart = new ArrayList<String>();
		for (int i = 0; i < parts.size(); i++) {			
			subPart.add(parts.get(i));
			if (subPart.size() == MAX_SIZE_PARTS) {
				subMerge(subPart);
				subPart.clear();
			}
		}
		if (!subPart.isEmpty()) {
			subMerge(subPart);
		}
		if (!mergeParts.isEmpty())
			
	}*/
	
	private void cleanFile(String fileName) throws IOException {
		File file = new File(fileName);
		if (file.exists())
			if (!file.delete())
				throw new IOException("No puede realizarle el merge, dado que el archivo temporal esta siendo consultado.");
	}

	public String merge() throws IOException {
		/*if (parts.size() > MAX_SIZE_PARTS) {
				mergeInParts(parts);
		} else {
			subMerge(parts, FINAL_MERGE_NAME);
		}*/
		subMerge(parts, FINAL_MERGE_NAME);
		return FINAL_MERGE_NAME;
	}

	/*private void mergeInParts(ArrayList<String> partsToMerge) throws IOException {
		ArrayList<String> subMergedParts = new ArrayList<String>();
		for (int i = 0; i < partsToMerge.size() / MAX_SIZE_PARTS; i++) {
			String tempName = getTempMergeFile();
			subMerge(partsToMerge.subList(i * MAX_SIZE_PARTS, (i+1) * MAX_SIZE_PARTS), tempName);
			subMergedParts.add(tempName);
		}
		int rest = partsToMerge.size() % MAX_SIZE_PARTS; 
		if ( rest != 0) {
			//tengo un resto de registros
			String tempName = getTempMergeFile();
			subMerge(partsToMerge.subList(partsToMerge.size() - rest, partsToMerge.size()), tempName);
			subMergedParts.add(tempName);
		}
		subMerge(subMergedParts, FINAL_MERGE_NAME);
	}*/
	
	private void mergeInParts(ArrayList<String> partsToMerge) throws IOException {
		ArrayList<String> subMergedParts = new ArrayList<String>();
		String tempName = getTempMergeFile();
		subMerge(partsToMerge.subList(0, MAX_SIZE_PARTS), tempName);
		subMergedParts.add(tempName);
		
		tempName = getTempMergeFile();
		subMerge(partsToMerge.subList(MAX_SIZE_PARTS, 2 * MAX_SIZE_PARTS - 1), tempName);
		subMergedParts.add(tempName);
		
		subMerge(subMergedParts, FINAL_MERGE_NAME);
	}
	
	private void subMerge(List<String> subPart, String outputName) throws IOException {
		File partFile = new File(outputName);
		SecuentialRecordFile<OccurrenceRecord, IntegerField> tempOutFile = new SecuentialRecordFile<OccurrenceRecord, IntegerField>(partFile, this);		
		loadBuffers(subPart);
		merge(subPart, tempOutFile);
		tempOutFile.close();
		freeBuffers(subPart);
	}

	private String getTempMergeFile() {
		String name = PART_NAME + partNumber.toString() + EXTENSION_NAME;
		partNumber++;
		mergeParts.add(name);
		return name;
	}

	private void freeBuffers(List<String> subPart) {
		for (Buffer buffer : buffers) {
			buffer.releaseFile();
		}
		for (String string : subPart) {
			File f = new File(string);
			f.delete();
		}
		
	}

	private void merge(List<String> subPart, SecuentialRecordFile<OccurrenceRecord, IntegerField> outputFile) {
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


	private void loadBuffers(List<String> subPart) {
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
		if (records.isEmpty())
			return null;
		OccurrenceRecord record = records.get(0);
		records.remove(0);
		if (records.isEmpty()) {
			this.fill();
		}
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