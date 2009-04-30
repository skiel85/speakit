package speakit.ftrs;

import java.io.IOException;

import speakit.Configuration;
import speakit.FileManager;

public interface Installable {

	public abstract void install(FileManager filemanager, Configuration conf) throws IOException;
	public abstract boolean isInstalled(FileManager filemanager) throws IOException;

}