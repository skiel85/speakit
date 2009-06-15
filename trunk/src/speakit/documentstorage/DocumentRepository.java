package speakit.documentstorage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import speakit.Configuration;
import speakit.FileManager;
import speakit.TextDocument;
import speakit.compression.arithmetic.ArithmeticCompressor;
import speakit.compression.lzp.LZP;
import speakit.compression.ppmc.PPMC;
import speakit.io.File;
import speakit.io.record.LongField;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.OffsetRecordFile;

//TODO implementar esta clase
public class DocumentRepository implements File, RecordFactory {
	private OffsetRecordFile<DocumentRecord> recordFile;

	@Override
	public void load(FileManager fileManager, Configuration conf) throws IOException {
		this.recordFile = new OffsetRecordFile<DocumentRecord>(fileManager.openFile("DocumentRepository.dat"), this);
	}

	/**
	 * Obtiene un documento a partir de su id
	 * 
	 * @param docId
	 * @return
	 * @throws IOException
	 */
	public TextDocument getById(Long docId) throws IOException {
		DocumentRecord record = recordFile.getRecord(new LongField(docId));
		if (record == null) {
			return null;
		} else {
			return new TextDocument(record.getOffset(),record.getText());
		}
	}

	/**
	 * Obtiene un documento a partir de su id
	 * 
	 * @param docId
	 * @return
	 * @throws IOException
	 */
	public TextDocument getByIdOfCompressedDocument(Long docId) throws IOException {
		DocumentRecord record = recordFile.getRecord(new LongField(docId));
		String textFromDecompressedDocument = ""; 
		
		
		if (record == null) {
			return null;
		} else {
			
			int compressor = record.getCompressor().getInteger();

			//se usa el descompresor PPMC
			if(compressor == 1){
				ByteArrayInputStream in = new ByteArrayInputStream(record.getCompressedDocument().getBytes());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				PPMC ppmcCompressor = new PPMC(out,2);
				ppmcCompressor.decompress(in);
				textFromDecompressedDocument = new String(out.toByteArray());
				
				
			//se usa el descompresor LZP
			}else if(compressor == 2){
				ByteArrayInputStream in = new ByteArrayInputStream(record.getCompressedDocument().getBytes());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				LZP lzpCompressor = new LZP(out);
				TextDocument document = lzpCompressor.decompress(in);
				textFromDecompressedDocument = document.getText();
				
				
			//no se usa descompresor, porque no estaba comprimido	
			}else if(compressor == 3){
				textFromDecompressedDocument = new String(record.getCompressedDocument().getBytes());
			}
			
			return new TextDocument(record.getOffset(),textFromDecompressedDocument);
		}
	}
	
	
	/**
	 * Almacena un documento
	 * 
	 * @param doc
	 * @return
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public void store(TextDocument doc) throws IOException {
		DocumentRecord record = new DocumentRecord(doc.getText());
		recordFile.insertRecord(record);
		doc.setId(record.getOffset());
	}

	/**
	 * Almacena un documento comprimido
	 * 
	 * @param doc
	 * @return
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public void store(TextDocument doc, int compressor) throws IOException {
		
		//se usa el compresor PPMC
		if(compressor == 1){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				PPMC ppmcCompressor = new PPMC(out,2);
				ppmcCompressor.compress(doc);
				DocumentRecord record = new DocumentRecord(out.toByteArray(), 1);
				recordFile.insertRecord(record);
				doc.setId(record.getOffset());
				
		//se usa el compresor LZP
		}else if(compressor == 2){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
		    	LZP lzpCompressor = new LZP(out);
		    	lzpCompressor.compress(doc);
		    	DocumentRecord record = new DocumentRecord(out.toByteArray(), 2);
		    	recordFile.insertRecord(record);
				doc.setId(record.getOffset());
				
        //no se usa compresor, se guarda sin comprimir		
		}else if(compressor == 3){
				DocumentRecord record = new DocumentRecord(doc.getText().getBytes(), 3);
				recordFile.insertRecord(record);
				doc.setId(record.getOffset());
		}
		
	}
	
	
	@Override
	public DocumentRecord createRecord() {
		return new DocumentRecord();
	}

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		this.load(filemanager, conf);
	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return true;
	}
}
