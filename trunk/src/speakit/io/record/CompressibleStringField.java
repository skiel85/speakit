package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import speakit.compression.FrontCodedWord;
import speakit.compression.FrontCodingWordDecoder;
import speakit.compression.FrontCodingWordEncoder;

/**
 * Es un string que tiene se serializa y deserializa utilizando el compresor.
 * @author Nahuel
 *
 */
public class CompressibleStringField extends StringField{

	public CompressibleStringField(String text){
		super(text);
	}
	
	public CompressibleStringField(){
		super("");
	}
	 
	public long deserialize(InputStream in, FrontCodingWordDecoder decoder) throws IOException {
		ShortField matchingCharacters = new ShortField();
		StringField endingCharacters = new StringField();
		long bytesRead = matchingCharacters.deserialize(in);
		bytesRead += endingCharacters.deserialize(in);
		FrontCodedWord codedWord = new FrontCodedWord(matchingCharacters.getShort(), endingCharacters.getString());
		String word = decoder.decode(codedWord);
		this.setString(word);
		return bytesRead;
	}
 
	public long serialize(OutputStream out, FrontCodingWordEncoder encoder) throws IOException {
		FrontCodedWord codedWord = encoder.encode(this.getString());
		ShortField matchingCharacters = new ShortField(codedWord.getMatchingCharacters());
		StringField endingCharacters = new StringField(codedWord.getEndingCharacters());
		return matchingCharacters.serialize(out) + endingCharacters.serialize(out);
	}

}
