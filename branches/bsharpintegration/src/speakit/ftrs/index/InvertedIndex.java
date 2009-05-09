package speakit.ftrs.index;

import java.io.IOException;
import java.util.ArrayList;

import speakit.Configuration;
import speakit.FileManager;
import speakit.io.File;
import speakit.io.bsharptree.Tree;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.io.recordfile.DirectRecordFile;

public class InvertedIndex implements File, RecordFactory {

	private static final String									DATAFILE		= "InvertedIndexData.dat";
	private static final String									INDEXFILENAME	= "BSharpTree.dat";
	private DirectRecordFile<InvertedIndexRecord, StringField>	invertedListsFile;
	private Tree<InvertedIndexIndexRecord, StringField>			index;

	public InvertedIndex() {

	}
	
	public InvertedIndexRecord getDocumentsFor(String word) throws RecordSerializationException, IOException {
		StringField key = new StringField(word);
		return getRecord(key);
	}

	private InvertedIndexRecord getRecord(StringField key) throws IOException, RecordSerializationException {
		// utilizo el indice para obtener la posicion del registro en el archivo
		InvertedIndexIndexRecord indexRecord = index.getRecord(key);
		if (indexRecord == null) {
			// la clave no está en el indice
			return null;
		} else {
			int recordBlockNumber = indexRecord.getBlockNumber();
			// busco el registro en el archivo de datos pero le paso el numero
			// de bloque
			return invertedListsFile.getRecord(key, recordBlockNumber);
		}
	}

	public void updateRecords(ArrayList<InvertedIndexRecord> records) throws RecordSerializationException, IOException {
		for (InvertedIndexRecord record : records) {
			this.updateRecord(record);
		}
	}

	public void updateRecord(InvertedIndexRecord record) throws RecordSerializationException, IOException {
		StringField key = record.getKey();
		//registro de indice
		InvertedIndexIndexRecord indexRecord = index.getRecord(key);		
		if (indexRecord == null) {
			//Indexa el registro asociándolo al numero de bloque donde fué insertado
			long insertedRecordBlock = this.invertedListsFile.insertRecord(record);
			this.index.insertRecord(new InvertedIndexIndexRecord( key.getString(), (int) insertedRecordBlock));
		} else {
			//mergeo los registros
			InvertedIndexRecord originalRecord = this.invertedListsFile.getRecord(key);
			InvertedIndexRecord mergedRecord = mergeRecords(record, originalRecord);
			//actualizo el archivo de datos
			long updatedBlockNumber = this.invertedListsFile.updateRecord(mergedRecord,indexRecord.getBlockNumber());
			//si el registro cambió de bloque lo reindexo
			if((int)updatedBlockNumber!=indexRecord.getBlockNumber()){
				indexRecord.setBlockNumber((int) updatedBlockNumber);
				this.index.updateRecord(indexRecord);
			}
		}
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
		this.index = new Tree<InvertedIndexIndexRecord, StringField>(fileManager.openFile(INDEXFILENAME), this);
	}

	public void load(FileManager filemanager, Configuration conf) throws IOException {
		createDataFile(filemanager);
		this.invertedListsFile.load();
		this.index.load();
	}

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		createDataFile(filemanager);
		this.invertedListsFile.create(conf.getBlockSize());
		this.index.create(conf.getBlockSize());
	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return filemanager.exists(DATAFILE) && filemanager.exists(INDEXFILENAME);
	}

	@Override
	public InvertedIndexRecord createRecord() {
		return new InvertedIndexRecord();
	}

	public String toString() {
		return invertedListsFile.toString();
	}
}
