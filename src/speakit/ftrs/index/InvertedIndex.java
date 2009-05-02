package speakit.ftrs.index;

import java.io.IOException;
import java.util.ArrayList;

import speakit.Configuration;
import speakit.FileManager;
import speakit.io.File;

//TODO implementar
public class InvertedIndex implements File{

	protected ArrayList<InvertedIndexRecord> records;
	
	public InvertedIndex(){
		records=new ArrayList<InvertedIndexRecord>();
	}

	public boolean exists(String word) {
		return false;
	}

	public InvertedIndexRecord getDocumentsFor(String word) {
		for (int i = 0; i < records.size(); i++) {
			InvertedIndexRecord each = this.records.get(i);
			if(each.term.equals(word)){ 
				return each;
			}
		}
		return null;
	}

	public void updateRecords(ArrayList<InvertedIndexRecord> records) {
		for(InvertedIndexRecord record:records){
			this.updateRecord(record);
		}
	}
	
	public void updateRecord(InvertedIndexRecord record) {
		for (int i = 0; i < records.size(); i++) {
			InvertedIndexRecord each = this.records.get(i);
			if(each.term.equals(record.term)){
				records.set(i,record);
				return;
			}
		}
		this.records.add(record);
	}
 

	public void load(FileManager filemanager,Configuration conf) {
		 //TODO implementar
	}

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		//TODO implementar
	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return true;
	}
 
}
