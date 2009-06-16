package speakit.compression.lzp;

import java.io.IOException;
import java.util.HashMap;

import speakit.Configuration;
import speakit.FileManager;
import speakit.compression.arithmetic.Context;
import speakit.ftrs.index.InvertedIndexIndexRecord;
import speakit.io.File;
import speakit.io.bsharptree.Tree;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;


public class LZPTable implements File{

		private static final int BLOCK_SIZE = 2048;
		private static final int DEFAULT_CONTEXT_LENGTH = 2;
		private static String TABLE_FILE_NAME = "LzpWorkTable.dat";
		private HashMap<String, Integer> matchs;
		private Tree<LzpTableRecord, StringField>	matchTable;
		private int minContextLength;
		/**
		 */
		
		public LZPTable(int minContextLength) {
			matchs = new HashMap<String, Integer>();
			this.minContextLength = minContextLength;
			
		}
		
		public LZPTable() {			
			this(DEFAULT_CONTEXT_LENGTH);
		}
		
		public Integer getLastMatchPosition(Context context) throws RecordSerializationException, IOException{
			/*if (matchs.containsKey(context.toString())) {
				return matchs.get(context.toString());
			}*/
			StringField key = new StringField(context.toString());
			if (matchTable.contains(key))
				return matchTable.getRecord(key).getMatchPosition().getInteger();
			return -1;
		}

			
		/**
		 * @throws IOException 
		 * @throws RecordSerializationException 
		 */
		public void update(Context context, Integer position) throws RecordSerializationException, IOException{
			/*if (isActualizable(context)) {
				if (matchs.containsKey(context.toString())) {
					matchs.remove(context.toString());
				}
				matchs.put(context.toString(), position);
			}*/
			StringField key = new StringField(context.toString());
			LzpTableRecord record = new LzpTableRecord(context.toString(), position);
			if (isActualizable(context)) {
				if (matchTable.contains(key)) {
					matchTable.updateRecord(record);
				} else {
					matchTable.insertRecord(record);
				}
			}
		}
		
		public String toString() {
			return matchs.toString();
		}
		
		private boolean isActualizable(Context context) {
			return context.size() >= minContextLength;
		}

		public void cleanTempFiles(FileManager fileManager) throws IOException{
			matchTable.close();
			//fileManager.openFile(TABLE_FILE_NAME);
			fileManager.destroyFiles();
		}
		
		@Override
		public void load(FileManager fileManager, Configuration conf) throws IOException {
			this.matchTable = new Tree<LzpTableRecord, StringField>(fileManager.openFile(createFile().getName()), LzpTableRecord.createRecordFactory());
			this.matchTable.load();
		}

		@Override
		public void install(FileManager fileManager, Configuration conf) throws IOException {
			//this.matchTable = new Tree<LzpTableRecord, StringField>(fileManager.openFile(TABLE_FILE_NAME), LzpTableRecord.createRecordFactory());
			this.matchTable = new Tree<LzpTableRecord, StringField>(fileManager.openFile(createFile().getName()), LzpTableRecord.createRecordFactory());
				//createDataFile(filemanager);
			this.matchTable.create(BLOCK_SIZE);
		}

		private java.io.File createFile() throws IOException {
			java.io.File file = java.io.File.createTempFile("LzpTable", "dat");
			file.deleteOnExit();
			return file;
		}
		

		@Override
		public boolean isInstalled(FileManager filemanager) throws IOException {
			return filemanager.exists(TABLE_FILE_NAME);
		}
		
		
}
