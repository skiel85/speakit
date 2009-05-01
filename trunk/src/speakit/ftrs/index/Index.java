package speakit.ftrs.index;

import java.io.IOException;
import java.util.ArrayList;

import speakit.Configuration;
import speakit.FileManager;
import speakit.io.File;

//TODO implementar
public class Index implements File{

	protected ArrayList<IndexRecord> records;
	
	public Index(){
		records=new ArrayList<IndexRecord>();
	}

	public boolean exists(String word) {
		return false;
	}

	public IndexRecord getDocumentsFor(String word) {
		for (int i = 0; i < records.size(); i++) {
			IndexRecord each = this.records.get(i);
			if(each.term.equals(word)){ 
				return each;
			}
		}
		return null;
	}

	public void updateRecords(ArrayList<IndexRecord> records) {
		for(IndexRecord record:records){
			this.updateRecord(record);
		}
	}
	
	public void updateRecord(IndexRecord record) {
		for (int i = 0; i < records.size(); i++) {
			IndexRecord each = this.records.get(i);
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
