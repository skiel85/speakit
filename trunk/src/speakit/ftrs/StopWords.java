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
		String text = "a ac� ah� ajena ajenas ajeno ajenos al algo alg�n alguna algunas alguno algunos all� alli all� ambos ampleamos ante antes aquel aquella aquellas aquello aquellos aqui aqu� arriba asi atras aun aunque bajo bastante bien cabe cada casi cierta ciertas cierto ciertos como c�mo con conmigo conseguimos conseguir consigo consigue consiguen consigues contigo contra cual cuales cualquier cualquiera cualquieras cuan cu�n cuando cuanta cu�nta cuantas cu�ntas cuanto cu�nto cuantos cu�ntos de dejar del dem�s demas demasiada demasiadas demasiado demasiados dentro desde donde dos el �l ella ellas ello ellos empleais emplean emplear empleas empleo en encima entonces entre era eramos eran eras eres es esa esas ese eso esos esta estaba estado estais estamos estan estar estas este esto estos estoy etc fin fue fueron fui fuimos gueno ha hace haceis hacemos hacen hacer haces hacia hago hasta incluso intenta intentais intentamos intentan intentar intentas intento ir jam�s junto juntos la largo las lo los mas m�s me menos mi m�a mia mias mientras mio m�o mios mis misma mismas mismo mismos modo mucha muchas much�sima much�simas much�simo much�simos mucho muchos muy nada ni ningun ninguna ningunas ninguno ningunos no nos nosotras nosotros nuestra nuestras nuestro nuestros nunca os otra otras otro otros para parecer pero poca pocas poco pocos podeis podemos poder podria podriais podriamos podrian podrias por por qu� porque primero primero desde puede pueden puedo pues que qu� querer quien qui�n quienes quienesquiera quienquiera quiza quizas sabe sabeis sabemos saben saber sabes se segun ser si s� siempre siendo sin s�n sino so sobre sois solamente solo somos soy sr sra sres sta su sus suya suyas suyo suyos tal tales tambi�n tambien tampoco tan tanta tantas tanto tantos te teneis tenemos tener tengo ti tiempo tiene tienen toda todas todo todos tomar trabaja trabajais trabajamos trabajan trabajar trabajas trabajo tras t� tu tus tuya tuyo tuyos ultimo un una unas uno unos usa usais usamos usan usar usas uso usted ustedes va vais valor vamos van varias varios vaya verdad verdadera vosotras vosotros voy vuestra vuestras vuestro vuestros y ya yo";
		ArrayList<String> wordsIterable = new ArrayList<String>();
		TextDocument document = new TextDocument(text);
		for (String word : document) {
				wordsIterable.add(word);
		}
		return wordsIterable;
	}
	
}