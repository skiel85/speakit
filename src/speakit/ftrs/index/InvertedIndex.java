package speakit.ftrs.index;

import java.io.IOException;
import java.util.ArrayList;

import speakit.Configuration;
import speakit.FileManager;
import speakit.io.File;

//TODO implementar
public class InvertedIndex implements File {

	protected ArrayList<InvertedIndexRecord> records;

	public InvertedIndex() {
		records = new ArrayList<InvertedIndexRecord>();
	}

	public boolean exists(String word) {
		return false;
	}

	public InvertedIndexRecord getDocumentsFor(String word) {
		for (int i = 0; i < records.size(); i++) {
			InvertedIndexRecord each = this.records.get(i);
			if (each.getTerm().equals(word)) {
				return each;
			}
		}
		return null;
	}

	public void updateRecords(ArrayList<InvertedIndexRecord> records) {
		for (InvertedIndexRecord record : records) {
			this.updateRecord(record);
		}
	}

	public void updateRecord(InvertedIndexRecord record) {
		for (int i = 0; i < records.size(); i++) {
			InvertedIndexRecord each = this.records.get(i);
			if (each.getTerm().equals(record.getTerm())) {
				records.set(i, mergeRecords(record,each));
				return;
			}
		}
		this.records.add(record);
	}

	/**
	 * copia todos los elementos de la lista invertida de from a to.
	 * @param from
	 * @param to
	 * @return to modificado
	 */
	private InvertedIndexRecord mergeRecords(InvertedIndexRecord from, InvertedIndexRecord to) {
		InvertedList listFrom = from.getInvertedList();
		InvertedList listTo = to.getInvertedList();
		for (int i = 0; i < listFrom.size(); i++) {
			InvertedListItem eachItem = listFrom.get(i);
			listTo.add(eachItem);
		}
		to.setInvertedList(listTo);
		return to;
	}

	public void load(FileManager filemanager, Configuration conf) {
		// TODO implementar
	}

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		// TODO implementar
	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return true;
	}

}
