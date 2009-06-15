package speakit.io;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtils {

	public static void writeTextOnUnicodeFile(java.io.File file, String text) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-16");
		writer.write(text);
		writer.close();
	}

	public static boolean compareFiles(String path1, String path2) throws IOException {
		return compareFiles(new java.io.File(path1), new java.io.File(path2));
	}
	public static boolean compareFiles(java.io.File file1, java.io.File file2) throws IOException {
		FileReader reader1 = new FileReader(file1);
		FileReader reader2 = new FileReader(file2);

		char[] buffer1 = new char[1024];
		char[] buffer2 = new char[1024];
		int count1 = 0;
		int count2 = 0;

		boolean result = true;

		while (result && count1 != -1 && count2 != -1) {
			count1 = reader1.read(buffer1);
			count2 = reader2.read(buffer2);
			if (count1 != count2) {
				result = false;
			} else {
				for (int i = 0; i < count1 && result; i++) {
					if (buffer1[i] != buffer2[i]) {
						result = false;
					}
				}
			}
		}

		reader1.close();
		reader2.close();

		return result;
	}
}
