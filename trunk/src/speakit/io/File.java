package speakit.io;

import java.io.IOException;

import speakit.FileManager;
import speakit.ftrs.Installable;

/**
 * Representa a un archivo que se puede crear y cargar
 * @author Nahuel
 *
 */
public interface File extends Installable{
	void load(FileManager fileManager) throws IOException;
}
