package speakit.ftrs.index;

import java.io.IOException;
import java.util.ArrayList;

import speakit.Configuration;
import speakit.FileManager;
import speakit.io.File;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.io.recordfile.DirectRecordFile;

//TODO implementar el índice
public class InvertedIndex implements File, RecordFactory<InvertedIndexRecord> {

	private static final String	DATAFILE	= "InvertedIndexData.dat";
	private DirectRecordFile<InvertedIndexRecord, StringField>	invertedListsFile;
	// protected ArrayList<InvertedIndexRecord> records;

	public InvertedIndex() {
		// records = new ArrayList<InvertedIndexRecord>();
	}

	// public boolean exists(String word) {
	// return false;
	// }

	public InvertedIndexRecord getDocumentsFor(String word) throws RecordSerializationException, IOException { 
		return invertedListsFile.getRecord(new StringField(word));
	}

	public void updateRecords(ArrayList<InvertedIndexRecord> records) throws RecordSerializationException, IOException {
		for (InvertedIndexRecord record : records) {
			this.updateRecord(record);
		}
	}

	public void updateRecord(InvertedIndexRecord record) throws RecordSerializationException, IOException {
		StringField key = record.getKey();
		InvertedIndexRecord retrievedRecord = this.invertedListsFile.getRecord(key);
		if (retrievedRecord == null) {
			int recordBlockNumber = (int) this.invertedListsFile.insertRecord(record);
			this.indexRecord(record, recordBlockNumber);
		} else {
			InvertedIndexRecord mergedRecord = mergeRecords(record, retrievedRecord);
			this.invertedListsFile.updateRecord(mergedRecord);
		}
	}

	/**
	 * Indexa el registro asociándolo al numero de bloque donde fué insertado
	 * 
	 * @param record
	 * @param recordBlockNumber
	 */
	private void indexRecord(InvertedIndexRecord record, int recordBlockNumber) {
		// TODO Implementar la indexación
	}

	/**
	 * copia todos los elementos de la lista invertida de from a to.
	 * 
	 * @param from
	 * @param to
	 * @return to modificado
	 */
	private InvertedIndexRecord mergeRecords(InvertedIndexRecord from, InvertedIndexRecord to) {
		InvertedList listFrom = from.getInvertedList();
		InvertedList listTo = to.getInvertedList();
		for (int i = 0; i < listFrom.size(); i++) {
			TermOcurrence eachItem = listFrom.get(i);
			listTo.add(eachItem);
		}
		to.setInvertedList(listTo);
		return to;
	}
	
	private void createDataFile(FileManager fileManager) throws IOException {
		this.invertedListsFile = new DirectRecordFile<InvertedIndexRecord, StringField>(fileManager.openFile(DATAFILE), this);
	}
	
	public void load(FileManager filemanager, Configuration conf) throws IOException {
		createDataFile(filemanager);
		this.invertedListsFile.load();
	}

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		createDataFile(filemanager);
		this.invertedListsFile.create(conf.getBlockSize());
	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return filemanager.exists(DATAFILE);
	}

	@Override
	public InvertedIndexRecord createRecord() {
		return new InvertedIndexRecord();
	}

}
