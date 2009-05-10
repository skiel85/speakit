package speakit.ftrs;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import speakit.Configuration;
import speakit.FileManager;
import speakit.TextDocument;

/**
 * Clase que obtiene los stop words considerados
 * 
 */

public class StopWordsFilter implements speakit.io.File {
	
	private static String STOP_WORDS = "StopWords.txt";
	//private FileManager fileManager;
	//private Configuration configuration;
	private ArrayList<String> stopWords;
	
	public StopWordsFilter() {
		stopWords = new ArrayList<String>();
	}
	
	public StopWordsFilter(FileManager fileManager, Configuration configuration) throws IOException {
		//this.fileManager = fileManager;
		//this.configuration = configuration;
		this();
		if (!isInstalled(fileManager)) {
			install(fileManager, new Configuration());
		}
		load(fileManager, configuration);
	}
	
	public String getFileName() {
		return STOP_WORDS;
	}
	
	
	/**
	 * Devuelve un iterable con todos los stop words 
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getDefaultStopWords(){
		String text = "a aca ahi ajena ajenas ajeno ajenos al algo algun alguna algunas alguno algunos alla alli ambos ampleamos ante antes aquel aquella aquellas aquello aquellos aqui aqui arriba asi atras aun aunque bajo bastante bien cabe cada casi cierta ciertas cierto ciertos como con conmigo conseguimos conseguir consigo consigue consiguen consigues contigo contra cual cuales cualquier cualquiera cualquieras cuan cuán cuando cuanta cuánta cuantas cuántas cuanto cuánto cuantos cuántos de dejar del demás demas demasiada demasiadas demasiado demasiados dentro desde donde dos el él ella ellas ello ellos empleais emplean emplear empleas empleo en encima entonces entre era eramos eran eras eres es esa esas ese eso esos esta estaba estado estais estamos estan estar estas este esto estos estoy etc fin fue fueron fui fuimos gueno ha hace haceis hacemos hacen hacer haces hacia hago hasta incluso intenta intentais intentamos intentan intentar intentas intento ir jamás junto juntos la largo las lo los mas más me menos mi mía mia mias mientras mio mío mios mis misma mismas mismo mismos modo mucha muchas muchísima muchísimas muchísimo muchísimos mucho muchos muy nada ni ningun ninguna ningunas ninguno ningunos no nos nosotras nosotros nuestra nuestras nuestro nuestros nunca os otra otras otro otros para parecer pero poca pocas poco pocos podeis podemos poder podria podriais podriamos podrian podrias por por qué porque primero primero desde puede pueden puedo pues que qué querer quien quién quienes quienesquiera quienquiera quiza quizas sabe sabeis sabemos saben saber sabes se segun ser si sí siempre siendo sin sín sino so sobre sois solamente solo somos soy sr sra sres sta su sus suya suyas suyo suyos tal tales también tambien tampoco tan tanta tantas tanto tantos te teneis tenemos tener tengo ti tiempo tiene tienen toda todas todo todos tomar trabaja trabajais trabajamos trabajan trabajar trabajas trabajo tras tú tu tus tuya tuyo tuyos ultimo un una unas uno unos usa usais usamos usan usar usas uso usted ustedes va vais valor vamos van varias varios vaya verdad verdadera vosotras vosotros voy vuestra vuestras vuestro vuestros y ya yo";
		ArrayList<String> wordsIterable = new ArrayList<String>();
		TextDocument document = new TextDocument(text);
		for (String word : document) {
				wordsIterable.add(word);
		}
		return wordsIterable;
	}

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		//this.fileManager = filemanager;
		//this.configuration = conf;
		create(filemanager, getDefaultStopWords());
	}
	
	private void create(FileManager fileManager, ArrayList<String> stopWords) throws FileNotFoundException, IOException {
		FileOutputStream fo = new FileOutputStream(fileManager.openFile(STOP_WORDS));
		String separator = new String("\n");
		for (String sw : stopWords) {
			fo.write(sw.getBytes());
			fo.write(separator.getBytes());
		}
		fo.flush();
		fo.close();
	}
	
	public boolean isStopWord(String word) {
		if (stopWords.size() != 0)
			return stopWords.contains(word);
		else
			return getDefaultStopWords().contains(word);
	}
	public ArrayList<String> getStopWords(FileManager fileManager, Configuration configuration) {
		try {
			load(fileManager, configuration);
			return stopWords;
		} catch (IOException e) {
			return getDefaultStopWords();
		}
	}

	
	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return filemanager.exists(STOP_WORDS);
	}


	@Override
	public void load(FileManager fileManager, Configuration conf) throws IOException {
		if (!fileManager.exists(STOP_WORDS)) {
			create(fileManager, getDefaultStopWords());
			stopWords.clear();
			stopWords = new ArrayList<String>(getDefaultStopWords());
		} else {
			stopWords.clear();
			BufferedReader input =  new BufferedReader(new FileReader(STOP_WORDS));
			try {
		        String line = null; //not declared within while loop
		        /*
		        * readLine is a bit quirky :
		        * it returns the content of a line MINUS the newline.
		        * it returns null only for the END of the stream.
		        * it returns an empty String if two newlines appear in a row.
		        */
		        while (( line = input.readLine()) != null){
		        	if (line.trim() != "")
		        		stopWords.add(line);
		          //System.out.println(line);
		        }
			}
		    finally {
		        input.close();
		    }
		}
	}
	
	/*
	public void addNewStopWord(String stopWord) throws FileNotFoundException, IOException {
		if (!stopWords.contains(stopWord)) {
			stopWords.add(stopWord);
			Collections.sort(stopWords);
			java.io.File f = new File(STOP_WORDS);
			f.delete();
			create(fileManager, stopWords);
		}
		load(fileManager, configuration);
	}
*/
	
	public TextDocument getRelevantWords(TextDocument dirtyDocument) throws IOException {
		String text = "";
		for (String word : dirtyDocument) {
			if (!isStopWord(word))
				text += (word + " ");
		}
		return new TextDocument(dirtyDocument.getId(), text.trim() );
	}
	
}
