package speakit.compression.lzp.test;


import java.io.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;


import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.SpeakitLogger;
import speakit.TextDocument;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.lzp.LZP;
import speakit.compression.lzp.LzpProbabilityTable;
import speakit.test.SpeakitTest;

public class TestLZP {

	private LZP lzp;
	ByteArrayOutputStream out;
	TextDocument document = new TextDocument("AOIAOIOAOIAOIO");
	TextDocument docVacio = new TextDocument("");
	TextDocument docFrase = new TextDocument("Este compresor, anda como el tujes");
	TextDocument docWikiChars = new TextDocument("La codificaci�n de caracteres es el m�todo que permite convertir un car�cter de un lenguaje natural (alfabeto o silabario) en un s�mbolo de otro sistema de representaci�n, como un n�mero o una secuencia de pulsos el�ctricos en un sistema electr�nico, aplicando normas o reglas de codificaci�n.");
	private static final String	LOREM_IPSUM								= "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam commodo molestie est eget ornare. Fusce pellentesque dolor sed risus sodales accumsan. Proin fringilla, turpis ac aliquam dapibus, eros eros pharetra lacus, rutrum volutpat purus sapien in purus. Phasellus quam diam, tincidunt vel ultrices vitae, tempus vel nisl. Fusce lacinia ornare massa ac commodo. Suspendisse malesuada nibh vitae lacus ultricies eget mattis arcu bibendum. Aenean felis erat, faucibus porttitor sagittis sed, ultrices in dolor. Sed malesuada tortor ut quam semper ut sodales augue ultricies. Nam sit amet mauris dolor. Cras nec ante sed leo rhoncus commodo. Aenean accumsan sapien eget elit consequat pulvinar.\n Integer non condimentum erat. Aliquam malesuada tellus sed felis ullamcorper semper. Morbi vitae eros nisi, et facilisis eros. Duis ultricies fermentum nunc id dictum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin id velit sem, sit amet sollicitudin ligula. Aliquam eget turpis urna, at tempus mauris. Suspendisse id velit vitae est consectetur consectetur. Cras nec est non augue tempor fermentum. Cras tellus nunc, ultrices vel pharetra vitae, egestas eu nisl. Nullam ultricies dolor ac neque tempor blandit. In sodales facilisis metus vitae venenatis. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Fusce faucibus augue eu lorem tincidunt ultrices. Aliquam lobortis convallis aliquet. Vestibulum eleifend quam id diam aliquet malesuada vel non turpis. Nam interdum metus vel sem tristique posuere. In hac habitasse platea dictumst. Fusce ac mi est.\n Aliquam convallis metus quis augue feugiat in venenatis urna mattis. Vivamus quis elit augue. Proin id gravida lorem. Duis eget purus et mauris scelerisque cursus. Ut congue augue eu augue viverra nec porttitor felis euismod. Curabitur vulputate, augue id tincidunt vehicula, magna tellus gravida arcu, ut malesuada orci purus in velit. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Duis pellentesque, massa in hendrerit vulputate, purus eros mollis arcu, sit amet luctus nisi felis in purus. Integer varius elit vitae magna lacinia mollis. Ut vitae elit est, eget pulvinar tellus. Phasellus sit amet lectus velit. Quisque tincidunt dictum turpis id aliquam. Nulla vestibulum sollicitudin metus, at venenatis augue rhoncus vestibulum. Fusce eget sem sapien.\n Quisque quis convallis odio. Proin accumsan placerat sapien, at blandit tellus gravida sit amet. Morbi tempor nulla nec nibh varius a convallis ligula vulputate. Nunc quis ante ut dui viverra accumsan. Aliquam nibh nisi, gravida sed iaculis sed, scelerisque vel enim. Proin vitae facilisis mi. Duis egestas convallis urna, eu malesuada felis cursus a. Nulla gravida suscipit quam. Nullam in mi sed massa adipiscing mattis. In luctus lobortis erat, sit amet pellentesque massa elementum non. Proin rutrum lacinia aliquet. Morbi lacinia pretium diam scelerisque sodales.\n Maecenas feugiat cursus rhoncus. Sed ipsum orci, elementum at tincidunt eget, luctus ut augue. Suspendisse potenti. Nam at sem nisi. Sed venenatis lobortis urna, et iaculis orci luctus interdum. Fusce convallis mauris eget nulla vestibulum rutrum id eget turpis. Nunc non vulputate purus. Phasellus interdum gravida nibh ut faucibus. Nulla tincidunt adipiscing accumsan. Sed porta leo justo, id vulputate diam. Maecenas pretium placerat malesuada. Donec dictum, sapien sit amet tempor interdum, ipsum elit hendrerit nisl, et mollis diam risus nec mi. Sed bibendum, turpis eu aliquam fermentum, augue ligula pharetra sem, interdum accumsan quam sem non nibh. Phasellus placerat turpis id felis semper facilisis. Nam lectus dolor, faucibus vitae tristique in, adipiscing id est. Sed tempor leo nec lorem rutrum non gravida nibh bibendum. Donec lectus purus, pulvinar et vulputate a, aliquet vel tellus. Maecenas vitae dolor nec tortor porta sollicitudin.";
	private static final String	popLife = "Nunca est� bueno ver correr a una azafata. Nos puede pasar por el costado a las chapas un bombero, un m�dico, un ingeniero nuclear, el grupo SWAT entero o el mism�simo Batman en persona, y a lo sumo nos provocar� un tibio \"uh, que loco, qu� estar� ocurriendo aqu�\". Pero si una aeromoza apura un cachito la marcha por el pasillo en pleno vuelo, indefectiblemente nos invadir� la sensaci�n de que estamos en los albores de una horrible cat�strofe, aunque la chica finalmente termine abarajando inofensivamente el carrito de las bebidas que se le piantaba a su compa�era. Lo �nico peor que te puede pasar es que te toque Benjamin Linus de compa�ero de asiento.\nAntes de todo eso, ustedes se preguntar�n c�mo llegu� al avi�n y qu� fue de la vida de Pop Life en estos d�as. Es que nadie se mete con la inteligentzia ricotera y sale indemne de ello: a ra�z del �ltimo post en el que promet�a hablar de la vuelta de los Redondos y terminaba haciendo notar una nueva forma de lanzamiento de fruta period�stica llamada MADTV, recib� una carta del grupo fundamentalista Al Beilinson inst�ndome a rectificarme bajo los t�rminos pertinentes, caso contrario -y ahora cito textualmente- \"procederemos a bajarle el comedor a cadenazos\". Apreciando mi dentadura, pero convencido de que no hab�a nada de lo que arrepentirse, les respond� con una carta documento que dec�a \"�sta voy a rectificar\" y ten�a el dibujo de un mu�equito tom�ndose los genitales, tras lo cual decid� exiliarme por unos d�as, hasta que se les pase la bronca o hasta que vuelvan Los Redondos, lo que suceda primero.\nDe modo que me tom� el primer avi�n que encontr� en Aeroparque antes de que me deg�ellen, con la esperanza de que fuera a Mar del Plata, Cataratas del Iguaz� o alg�n destino tur�stico similar. Pero no: me toc� Catamarca, y para all� fui, con un se�or de unos 45 a�os e id�ntica cantidad de cabellos como compa�ero de asiento. Hugo se llamaba.\n\n- �Vos qu� hac�s? - me pregunt�, mientras jugaba con un libro de Deepak Chopra que no sab�a c�mo abrir.\n- Soy panadero. Y le vendo armas a Ir�n.\n- Ah.\nLe pareci� normal.\n- Yo me dedico a la importaci�n y exportaci�n - agreg�.\n- Como Art Vandelay.\n- �Qui�n?\n- Nada.\n\n\nSac� una caja de bombones y me convid�. Le dije que no. Se puso cargoso y le agarr� uno que parec�a estar relleno de dulce de leche pero que termin� teniendo algo que si no era mayonesa le pegaba en el palo. Ah� fue cuando corri� la azafata. Me hice el dormido el resto del viaje.\nBajamos en el Aeropuerto Internacional de Catamarca, que resulta ser tambi�n la terminal de �mnibus y el mercado central. Ah� me estaban esperando Arnaldo y Mario, mis amigos de la clandestinidad, famosos por organizar los eventos m�s interesantes de la escena cultual catamarque�a, como escraches a tributistas a Sabina, quemas p�blicas de discos de Los Nocheros o la famosa \"Noche del Poste\", que consist�a en juntarse en la plaza del pueblo de noche a mirar un poste. Ten�an planes inmediatos para m�.\n- Hoy te llevamos a ver a Carac�.\n- �A qui�n?\n- Carac�. Dicen que son los Maroon 5 catamarque�os.\n- Uh.\n- Ten�s que ver al baterista, es buen�simo. Cuando se fue Palmer de ELP lo iba a reemplazar �l, pero lo bocharon los otros dos porque el apellido es Cardozo, y llamarse Emerson, Lake & Cardozo les pareci� medio mersa.\n- Y, s�. Qu� l�stima.\n- S�, un baj�n. Bueno, hoy los vemos.\n\n\nY los vimos nom�s. Tocaban en un local que se llamaba \"P�nico y Locura en la Patagonia\", una sucursal de otro boliche fundado en Comodoro Rivadavia que manten�a su nombre incluso a miles de kil�metros de la Patagonia porque al due�o no se le ocurr�a nada mejor. Salieron a escena y, a decir verdad, m�s que los Maroon 5 catamarque�os me parecieron una mezcla de Pedro y Pablo con 2 Minutos, m�s la actitud de DJ Der� y la pera de Carca. Hicieron su hit \"Entr� a Stephen que est� por llover\" y una turba iracunda dio por terminado su concierto inmediatamente.\n\n- �Y? �Te gustaron? - pregunt� Mario.\n- Uh, s�, me meo en los pantalones, se me corre el maquillaje." +
						"- Porte�o de mierda.\n\n\nY me tuve que exiliar otra vez, al ver mis dientes en peligro de derrumbe nuevamente por la amenaza de una horda de catamarque�os indignados con mi juicio musical. No hab�a para otro avi�n, as� que me tom� un micro de la empresa Todos Moriremos SRL, con destino al departamento de Cochinoca, Jujuy. Una vez all�, s�lo, sin dinero y acosado por fans de los Redondos y de Carac�, me enter� de una noticia que incre�blemente no trascendi� en los medios internacionales: el lamentable deceso del Renegado, justo cuando estaba de visita en el mencionado pueblo norte�o. Las pruebas en la foto a continuaci�n.";
	TextDocument stress = new TextDocument(popLife);
	
	@Before
	public void setUp() throws Exception {
		out = new ByteArrayOutputStream();
		lzp = new LZP(out);

		//SpeakitLogger.activate();

	}
	@Test
	public void testCompress() throws IOException {
		lzp.compress(docFrase);
		printTables();
		System.out.println(out);
	}
	
	
	@Test
	public void testDecompress() throws IOException {
		byte[] compressedbytes = compress(document);
		lzp = new LZP(out);
		TextDocument result = lzp.decompress(new ByteArrayInputStream(compressedbytes));
		System.out.println(result.getText());
		System.out.println("Fin de la descompresion");
		Assert.assertTrue(document.getText().equals(result.getText()));
		printTables();
	}
	
	@Test
	public void testDecompress2() throws IOException {
		byte[] compressedbytes = compress(docWikiChars);
		lzp = new LZP(out);
		TextDocument result = lzp.decompress(new ByteArrayInputStream(compressedbytes));
		System.out.println(result.getText());
		System.out.println("Fin de la descompresion");
		Assert.assertTrue(docWikiChars.getText().equals(result.getText()));
	}
	
	@Test
	public void testCompressDecompres() throws IOException  {
		long initTime = Calendar.getInstance().getTimeInMillis();
		byte[] compressedbytes = compress(new TextDocument(LOREM_IPSUM));
		System.out.println("Fin de la compresion");
		System.out.println("Tiempo transcurrido (ms)= " + (Calendar.getInstance().getTimeInMillis() - initTime));
		initTime = Calendar.getInstance().getTimeInMillis();
		lzp = new LZP(out);
		TextDocument result = lzp.decompress(new ByteArrayInputStream(compressedbytes));
		System.out.println(result.getText());
		System.out.println("Fin de la descompresion");
		System.out.println("Tiempo transcurrido (ms)= " + (Calendar.getInstance().getTimeInMillis() - initTime));
		Assert.assertTrue(LOREM_IPSUM.equals(result.getText()));
	}
	@Ignore
	@Test
	public void testStress() throws IOException{
		long initTime = Calendar.getInstance().getTimeInMillis();
		
		byte[] compressedbytes = compress(stress);
		System.out.println("Tiempo transcurrido (ms)= " + (Calendar.getInstance().getTimeInMillis() - initTime));
		lzp = new LZP(out);
		
		initTime = Calendar.getInstance().getTimeInMillis();
		TextDocument result = lzp.decompress(new ByteArrayInputStream(compressedbytes));
		System.out.println(result.getText());
		System.out.println("Fin de la descompresion");
		System.out.println("Tiempo transcurrido (ms)= " + (Calendar.getInstance().getTimeInMillis() - initTime));
		Assert.assertTrue(stress.getText().equals(result.getText()));
	}
	
	private void printTables() {
		HashMap<String, LzpProbabilityTable> tables = lzp.getTables();
		Set<String> collection = tables.keySet();
		for (Iterator<String> iterator = collection.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println("Context: " + string + "\n");
			System.out.println((ProbabilityTable)tables.get(string).getProbabilityTable() + "\n");		
		}
	}
	
	private byte[] compress(TextDocument textDocument) throws IOException {
		this.lzp.compress(textDocument);
		// SpeakitLogger.Log(out.toString());
		return out.toByteArray();
	}
}
