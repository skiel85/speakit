package speakit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import speakit.documentstorage.TextDocumentList;
import speakit.ftrs.Installable;
import speakit.io.record.RecordSerializationException;

public interface SpeakitInterface extends Installable {

	/**
	 * Abre todos los archivos necesarios y lo deja listo para su uso.
	 */
	public abstract void load() throws IOException;

	/**
	 * Agrega el documento al sistema y devuelve la lista de palabras
	 * desconocidas
	 */
	public abstract Iterable<String> addDocument(TextDocument doc) throws IOException;
	
	/**
	 * Agrega los documentos al sistema y devuelve la lista de documentos
	 * para luego obtener las palabras desconocidas de cada uno
	 */
	public abstract Iterable<String> addDocuments(TextDocumentList docs) throws IOException;
	
	/**
	 * Devuelve una documento de audio
	 */
	public abstract WordAudioDocument convertToAudioDocument(TextDocument doc) throws IOException;

	/**
	 * Registra un WordAudio en el sistema. Indexa la palabra con el audio
	 * 
	 * @throws RecordSerializationException
	 */
	public abstract void addWordAudio(WordAudio audio) throws IOException, RecordSerializationException;

	/**
	 * Este es un metodo utilitario que crea un TextDocument a partir de un path
	 * de un archivo
	 */
	public abstract TextDocument getTextDocumentFromFile(String path) throws FileNotFoundException, IOException;

	public abstract TextDocumentList search(TextDocument searchText) throws IOException;

	public abstract String printIndexForDebug();
	
	public ArrayList<String> getInvalidWordsForSearch(TextDocument consultation);

}