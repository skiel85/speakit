package speakit.documentstorage;

import speakit.TextDocument;
import speakit.dictionary.audiofile.AudioFile;

/**
 * Clase q modela un documento
 * 
 * @author LG
 * 
 */
public class Document {
	protected TextDocument textDocument;
	protected AudioFile audioDocument;

	public TextDocument asTextDocument() {
		return textDocument;
	}

	public AudioFile asAudioDocument() {
		return audioDocument;
	}

	public String getPreview() {
		return textDocument.getPreview();
	}
}
