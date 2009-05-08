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
	private String generatedMergeFile = "";
	
	private ArrayList<String> parts;
	private boolean dataProcessed = false;
	
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
			recordFile.close();
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
	
	private SecuentialRecordFile<OccurrenceRecord, IntegerField> createOutputFile(String fileName) throws IOException{
		File out = new File(fileName);
		out.setWritable(true);
		//out.setReadable(true);
		SecuentialRecordFile<OccurrenceRecord, IntegerField> recordFile = new SecuentialRecordFile<OccurrenceRecord, IntegerField>(out, this);
		return recordFile;
	}
	
	private void eraseOutputFile(String outputFile){
		File out = new File(outputFile);
		out.delete();
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
		ArrayList<Occurrence> list = null;
		try {
		String mergedFile = processFile();		
		SecuentialRecordFile<OccurrenceRecord, IntegerField> output = createOutputFile(mergedFile); 
		list = generateSortedListForTerm(output, termId);
		output.close();
		//eraseOutputFile(mergedFile);
		//deleteParts();
		} catch (IOException e) {
			//No se puede generar el archvo de salida... q garron!
		}
		return list;
	}

	private String processFile() throws IOException {
		//por si quedan registros en los buffers
		if (dataProcessed )
			return generatedMergeFile;
		sort();
		flush();
		OccurrencePartMerger merger = new OccurrencePartMerger(parts);
		String mergedFile = merger.merge();
		dataProcessed = true;
		generatedMergeFile = mergedFile;
		return mergedFile;
	}
	private ArrayList<Occurrence> generateSortedListForTerm( SecuentialRecordFile<OccurrenceRecord, IntegerField> output, int termId) throws IOException {
		output.resetReadOffset();
		ArrayList<Occurrence> list = new ArrayList<Occurrence>();
		while (!output.eof()) {
			OccurrenceRecord record = output.readRecord();
			if (record.getTerm().getInteger() == termId)
				list.add(new Occurrence(record.getTerm().getInteger(), record.getDocument().getLong()));
			else if (record.getTerm().getInteger() > termId) {
				//Se encontro un termino mayr, y dado q esta ordenado, ya no voy a encontrar mas de los q buscaba
				return list;
			}
		}
		return list;
	}

	/**
	 * Devuelve la lista completa de apariciones ordenadas por termino y nro de documento
	 */
	/*
	@Override
	public ArrayList<Occurrence> getSortedAppearanceList() {
		ArrayList<Occurrence> list = null;
		try {
		//por si quedan registros en los buffers
		flush();
		SecuentialRecordFile<OccurrenceRecord, IntegerField> output = createOutputFile();
		OccurrencePartMerger merger = new OccurrencePartMerger(output, parts);
		merger.merge();
		list = generateSortedList(output);
		output.close();
		eraseOutputFile();
		deleteParts();
		} catch (IOException e) {
			//No se puede generar el archvo de salida... q garron!
		}
		return list;
	}
*/
	/**
	 * Devuelve la lista completa de apariciones ordenadas por termino y nro de documento
	 */
	@Override
	public ArrayList<Occurrence> getSortedAppearanceList() {
		ArrayList<Occurrence> list = null;
		try {
		String mergedFile = processFile();
		SecuentialRecordFile<OccurrenceRecord, IntegerField> output = createOutputFile(mergedFile); 
		list = generateSortedList(output);
		output.close();
		eraseOutputFile(mergedFile);
		deleteParts();
		} catch (IOException e) {
			//No se puede generar el archvo de salida... q garron!
		}
		return list;
	}
	
	private void deleteParts() {
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
