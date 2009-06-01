package speakit.compression.arithmetic;

import java.io.IOException;

public interface BitWriter {

	void write(String bits) throws IOException;
}
