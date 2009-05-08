package speakit.ftrs;


import java.util.ArrayList;

import speakit.TextDocument;

/**
 * Clase que obtiene los stop words considerados
 * 
 */

public class StopWords {
	
	
	/**
	 * Devuelve un iterable con todos los stop words 
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getStopWords(){
		String text = "un una unas unos uno sobre todo tambi�n tras otro alg�n alguno alguna algunos algunas ser es soy eres somos sois estoy esta estamos estais estan como en para atras porque por qu� estado estaba ante antes siendo ambos pero por poder puede puedo podemos podeis pueden fui fue fuimos fueron hacer hago hace hacemos haceis hacen cada fin incluso primero desde conseguir consigo consigue consigues conseguimos consiguen ir voy va vamos vais van vaya gueno ha tener tengo tiene tenemos teneis tienen el la lo las los su aqui mio tuyo ellos ellas nos nosotros vosotros vosotras si dentro solo solamente saber sabes sabe sabemos sabeis saben ultimo largo bastante haces muchos aquellos aquellas sus entonces tiempo verdad verdadero verdadera cierto ciertos cierta ciertas intentar intento intenta intentas intentamos intentais intentan dos bajo arriba encima usar uso usas usa usamos usais usan emplear empleo empleas emplean ampleamos empleais valor muy era eras eramos eran modo bien cual cuando donde mientras quien con entre sin trabajo trabajar trabajas trabaja trabajamos trabajais trabajan podria podrias podriamos podrian podriais yo aquel ";
		ArrayList<String> wordsIterable = new ArrayList<String>();
		TextDocument document = new TextDocument(text);
		for (String word : document) {
				wordsIterable.add(word);
		}
		return wordsIterable;
	}
	
}
