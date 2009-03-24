package speakit;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.sound.sampled.AudioFileFormat;

import datos.capturaaudio.core.SimpleAudioRecorder;
import datos.capturaaudio.exception.SimpleAudioRecorderException;

import speakit.dictionary.AudioDictionary;
import speakit.dictionary.MockAudioDictionary;
import speakit.wordreader.MockWordReader;
import speakit.wordreader.WordReader;

/**
 * Clase Encargada de manejar la interaccion con el usuario
 */
public class Menu {	
	protected AudioDictionary audioDictionary;
	protected WordReader wordReader;
	
	public Menu() {
		wordReader = new MockWordReader();
		audioDictionary = new MockAudioDictionary();
	}
	private AudioDictionary getAudioDictionary() {
		return audioDictionary;
	}

	private WordReader getWordReader() {		
		return wordReader;
	}	
	/**
	 * Despliega el menu principal
	 * @throws SimpleAudioRecorderException 
	 */
	public void display() throws SimpleAudioRecorderException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader entrada = new BufferedReader (isr);
		System.out.println("Speak It!");
		boolean running = true;
		while(running){
			int opt;
			System.out.println(
			"Menu Principal\n" +
			"	1.- Procesar archivo de Texto\n" +
			"	2.- Reproducir Archivo\n" +
			"\n" +
			"	0.- Salir");
			try
			{
				opt=Integer.parseInt(entrada.readLine());			
				switch(opt){
					case 1:
					displayReadTextSubMenu(entrada);
					break;
		
					case 2:
					displayPlayFileSubMenu(entrada);
					break;
		
					case 0:
					System.out.println("Terminado");
					running = false;
					break;
				}
			} catch (IOException e) {
				//No hago nada, el menu continua corriendo
			}
		}
	}
	/**
	 * Despliega el menu para procesar los archivos de texto
	 * @param entrada
	 * @throws IOException
	 * @throws SimpleAudioRecorderException 
	 */
	private void displayReadTextSubMenu(BufferedReader entrada) throws IOException, SimpleAudioRecorderException
	{
		String path;
		System.out.println(
		"Leer archivo de Texto\n" +		
		"Ingrese el path a continuación: \n");		
		path = entrada.readLine();
		processTextFile(path);
	}
	
	/**Procesa el archivo de texto, extrayendo las palabras y registrandolas en el repositorio
	 * de audio
	 * 
	 * @param path path del archivo a procesar
	 * @throws SimpleAudioRecorderException 
	 */
	public void processTextFile(String path) throws SimpleAudioRecorderException {
		// TODO Evaluar la opcion de crear una clase manejadora de archivos de entrada
		File file = new File(path);
		if (file.exists())
		{
			System.out.println("El archivo existe.");
			doProcessFile(file);
		}else
		{
			System.out.println("Error. El archivo no existe.");
		}
	}
	
	/** Recibe un File y lo procesa 
	 * 
	 * @param file archivo a procesar
	 * @throws SimpleAudioRecorderException 
	 */
	private void doProcessFile(File file) throws SimpleAudioRecorderException {
		while(getWordReader().hasNext())		
		{
			try {
				String word = getWordReader().next();				
				if (!getAudioDictionary().contains(word)) {
					byte[] audioWord = this.getNewAudioWord(word);
					getAudioDictionary().addEntry(word, audioWord);
					System.out.println("La palabra fue agregada exitosamente.");
				}
			}
			catch (IOException io) {
				//TODO Resolver la excepcion
			}
		}
	}

	/**
	 * Despliega el menu para la reproduccion de los archivos.
	 * @param entrada
	 * @throws IOException
	 */
	private void displayPlayFileSubMenu(BufferedReader entrada) throws IOException {
		int opt;
		System.out.println(
		"Reproducir Archivo\n" +
		"	1.- Reproducir Archivo\n" +		
		"\n" +
		"	0.- Volver");
		opt = Integer.parseInt(entrada.readLine());			
		switch(opt){
			case 1:
			System.out.println("reproducir archivo");
			try {
			Thread.sleep(1000);
			} catch (InterruptedException ie)
			{}
			break;	
			
			case 0:
			break;
		}
		
	}
	/**
	 * Dada la representacion en texto de la palabra, se procede a obtener el audio
	 * @param word
	 * @return
	 * @throws IOException
	 * @throws SimpleAudioRecorderException 
	 */
	public byte[] getNewAudioWord(String word) throws IOException, SimpleAudioRecorderException {
		char option;
		boolean recording = true;
		byte[] newAudioWord = null;
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader entrada = new BufferedReader (isr);
		System.out.println(
				"Se ha detectado una nueva palabra.\n" +
				"	La palabra '" + word + "'" + " no se encuentra registrada.\n" +
				"	Presione 'f' y enter para iniciar la captura del audio e 'i' y enter para finalizarla."  
				);
		while(recording) {			
			option = entrada.readLine().charAt(0);		
			switch(option){
				case 'f':
				case 'F':
					//Inicio la captura
					output=new ByteArrayOutputStream();
					recorder=new SimpleAudioRecorder(AudioFileFormat.Type.WAVE,output );
					recorder.init();
					recorder.startRecording();
					System.out.println("Grabando nueva palabra. " +
										"Presione 'i' para finalizar.");
					newAudioWord = startRecording(entrada);
					recording = false;
					break;
			}
		}
		newAudioWord = confirmateAudioWord(word, newAudioWord, entrada);
		return newAudioWord;
	}
	ByteArrayOutputStream output=null;
	SimpleAudioRecorder recorder=null;
	
	/**
	 * confirma que el contenido del audio sea correcto, si no lo es, vuelve a grabarlo
	 * 
	 * @param word la palabra en formato texto
	 * @param oldAudioWord la grabacion a confirmar
	 * @param entrada La consola para escribir
	 * @return La nueva palabra en audio o la anterior de confirmarse
	 * @throws IOException
	 * @throws SimpleAudioRecorderException 
	 */
	private byte[] confirmateAudioWord(String word, byte[] oldAudioWord, BufferedReader entrada) throws IOException, SimpleAudioRecorderException{		
		char option;
		System.out.println("Verifique la palabra '" + word + "'.");
		System.out.println("Reproduciendo...");
		//Reproduzco el audio grabado
		System.out.println("Si está conforme, presione 's'.\n" +
							"De lo contrario presione 'n' e ingresela nuevamente.");		
		while(true) {			
			option = entrada.readLine().charAt(0);		
			switch(option){
			case 's':
			case 'S':
				return oldAudioWord;
			case 'n':
			case 'N':
				//Inicio nuevamente la captura				
				return getNewAudioWord(word);
			}
		}
	}
	
	/**
	 * Inicia la obtencion de una palabra en formato audio y muestra la opcion de detener
	 * @param entrada la consola donde escribir
	 * @return la palabra en formato audio
	 * @throws IOException
	 */
	private byte[] startRecording(BufferedReader entrada) throws IOException {		
		char option;
		//Inicio grabacion
		while(true){
			option = entrada.readLine().charAt(0);			
			switch(option){
				case 'i':
				case 'I':
					System.out.println("Grabaciòn finalizada.");
					return stopRecording();
			}
		}
		
	}

	/**
	 * Finaliza la grabacion 
	 * @return la palabra en formato audio
	 */
	private byte[] stopRecording() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
