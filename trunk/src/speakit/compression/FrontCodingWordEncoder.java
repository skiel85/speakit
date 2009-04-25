package speakit.compression;

public class FrontCodingWordEncoder {

	public FrontCodedWord encode(String string) {
		return new FrontCodedWord((short) 0, string);
	}

}
