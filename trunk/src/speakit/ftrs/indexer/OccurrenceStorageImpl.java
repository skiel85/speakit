package speakit.ftrs.indexer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import speakit.io.record.IntegerField;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.SecuentialRecordFile;

public class OccurrenceStorageImpl implements OccurrenceStorage, RecordFactory {

	private int BUFFER_LIMIT;
	private static int BUFFER_LIMIT_DEFAULT = 128;
	ArrayList<Occurrence> buffer;
	private Integer partNumber = new Integer(0);
	private static String SORT_PREFIX = "sortedPart";
	private static String PART_EXTENSION = ".part";
	
	private ArrayList<String> parts;
	
	public OccurrenceStorageImpl() throws IOException{
		BUFFER_LIMIT = BUFFER_LIMIT_DEFAULT;
		initialize();
	}
	
	public OccurrenceStorageImpl(int bufferSize) throws IOException{
		BUFFER_LIMIT = bufferSize;
		initialize();
	}

	private void initialize() {
		buffer = new ArrayList<Occurrence>();
		parts = new ArrayList<String>();
	}
	@Override
	public void addOccurrence(Occurrence occ) {
		buffer.add(occ);
		if (buffer.size() == BUFFER_LIMIT) {
			sort();
			flush();
		}
	}

	private void sort() {
		Collections.sort(buffer); 		
	}

	private void flush() {
		try {
			if (buffer.size() == 0)
				return;
			SecuentialRecordFile<OccurrenceRecord, IntegerField> recordFile = createNewPartitionFile();
			for (Iterator<Occurrence> it = buffer.iterator(); it.hasNext();) {
				Occurrence occurrence = it.next();
				addEntry(occurrence.getTermId(), occurrence.getDocument(), recordFile);
			}
			buffer.clear();
		} catch (IOException e) {
			//no hago nada por el momento
		}
	}
	
	private SecuentialRecordFile<OccurrenceRecord, IntegerField> createNewPartitionFile() throws IOException{
		String tempName = getTempFileName();
		File part = new File(tempName);
		parts.add(tempName);
		part.setWritable(true);
		SecuentialRecordFile<OccurrenceRecord, IntegerField> recordFile = new SecuentialRecordFile<OccurrenceRecord, IntegerField>(part, this);
		return recordFile;
	}
	
	private SecuentialRecordFile<OccurrenceRecord, IntegerField> createOutputFile() throws IOException{
		File out = new File(getOutputFileName());
		out.setWritable(true);
		//out.setReadable(true);
		SecuentialRecordFile<OccurrenceRecord, IntegerField> recordFile = new SecuentialRecordFile<OccurrenceRecord, IntegerField>(out, this);
		return recordFile;
	}
	
	private String getOutputFileName() {
		return "mergedList.merge";
	}

	private String getTempFileName() {
		String name = new String(SORT_PREFIX + partNumber.toString() + PART_EXTENSION);
		partNumber++;
		return name;
	}
	
	private void addEntry(int termId, long document, SecuentialRecordFile<OccurrenceRecord, IntegerField> recordFile) {
		OccurrenceRecord record = new OccurrenceRecord(termId, document);
		try {
			recordFile.insertRecord(record);
		} catch (RecordSerializationException re) {
			//por ahora no se hace nada
		} catch (IOException ioe) {
		
		}
	}
	
	@Override
	public ArrayList<Occurrence> getApearanceListFor(int termId) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Devuelve la lista completa de apariciones ordenadas por termino y nro de documento
	 */
	@Override
	public ArrayList<Occurrence> getSortedAppearanceList() {
		ArrayList<Occurrence> list = null;
		try {
		flush();
		SecuentialRecordFile<OccurrenceRecord, IntegerField> output = createOutputFile();
		OccurrencePartMerger merger = new OccurrencePartMerger(output, parts);
		merger.merge();
		list = generateSortedList(output);
		cleanParts();
		} catch (IOException e) {
			//No se puede generar el archvo se salida... q garron!
		}
		return list;
	}
	
	private void cleanParts() {
		for (Iterator<String> iterator = parts.iterator(); iterator.hasNext();) {
			String part = iterator.next();
			File file = new File(part);
			file.delete();
		}
	}

	private ArrayList<Occurrence> generateSortedList(SecuentialRecordFile<OccurrenceRecord, IntegerField> output) throws IOException {
		output.resetReadOffset();
		ArrayList<Occurrence> list = new ArrayList<Occurrence>();
		while (!output.eof()) {
			OccurrenceRecord record = output.readRecord();
			list.add(new Occurrence(record.getTerm().getInteger(), record.getDocument().getLong()));
		}
		return list;
	}

	@Override
	public OccurrenceRecord createRecord() {
		return new OccurrenceRecord();
	}

}
