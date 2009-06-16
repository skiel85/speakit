package speakit.ftrs.index;

import speakit.compression.frontcoding.FrontCodingWordDecoder;
import speakit.compression.frontcoding.FrontCodingWordEncoder;
import speakit.io.bsharptree.RecordEncoder;
import speakit.io.record.Record;

public class InvertedIndexIndexRecordEncoder extends RecordEncoder {

	FrontCodingWordEncoder	wordEncoder;
	FrontCodingWordDecoder	wordDecoder; 

	@Override
	public void clear() {
		wordEncoder = new FrontCodingWordEncoder();
		wordDecoder = new FrontCodingWordDecoder(); 
	}

	@Override
	public Record createRecord() { 
		return new InvertedIndexFrontCodedIndexRecord();
	}

	@Override
	public Record decode(Record record) {
//		recordIdCounter++;
		InvertedIndexFrontCodedIndexRecord encoded = (InvertedIndexFrontCodedIndexRecord) record;
		return new InvertedIndexIndexRecord(this.wordDecoder.decode(encoded.getEncodedKey()), encoded.getBlockNumber());
	}

	@Override
	public Record encode(Record record) {
//		recordIdCounter++;
		InvertedIndexIndexRecord original = (InvertedIndexIndexRecord) record;
		return new InvertedIndexFrontCodedIndexRecord(this.wordEncoder.encode(original.getKey().getString()), original.getBlockNumber());
	}

}
