package speakit.ftrs.indexer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import speakit.FileManager;
import speakit.io.record.IntegerField;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.SecuentialRecordFile;

public class OccurrenceStorageImpl implements OccurrenceStorage, RecordFactory<OccurrenceRecord> {

	private static int BUFFER_LIMIT = 128;
	ArrayList<Occurrence> buffer;
	private Integer partNumber = new Integer(0);
	private static String prefix = "sortedPart";
	private static String extension = ".part";
	private ArrayList<String> parts;
	private FileManager fileManager = new FileManager();
	
	
	public OccurrenceStorageImpl() throws IOException{
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
			flush();
		}
	}

	private void flush() {
		try {
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
	
	private String getTempFileName() {
		String name = new String(prefix + partNumber.toString() + extension);
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

	@Override
	public ArrayList<Occurrence> getSortedAppearanceList() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public OccurrenceRecord createRecord() {
		return new OccurrenceRecord();
	}

}
