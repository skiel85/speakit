package speakit.io.bsharptree.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.ftrs.index.InvertedIndexIndexRecord;
import speakit.ftrs.index.InvertedIndexIndexRecordEncoder;
import speakit.io.bsharptree.RecordEncoder;
import speakit.io.bsharptree.Tree;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.test.TestFileManager;

public class BalanceLeafNodesTest {
	private Tree<InvertedIndexIndexRecord, StringField> sut;

	private TestFileManager filemanager;
	private RecordEncoder encoder;
	private TextDocument wikipediaArticle;
	
	/**
	 * Simula un numero de bloque. Devuelve el tama�o de la palabra como numero
	 * de bloque
	 * 
	 * @param word
	 * @return
	 */
	private static int simulateBlockNumber(String word) {
		return word.length();
	}
	/**
	 * Preparo el arbol para q este a una palabra (California) de neceistar un split en una de las hojas
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.filemanager = new TestFileManager("");
		encoder = new InvertedIndexIndexRecordEncoder();
		this.sut = new Tree<InvertedIndexIndexRecord, StringField>(filemanager.openFile("FullBSTree.dat"), InvertedIndexIndexRecord.createRecordFactory(), encoder);
		this.sut.create(440);
		wikipediaArticle = new TextDocument(
				0,
				"El brote de gripe A (H1N1) de 2009,60 causado por una variante del Influenzavirus A originalmente de origen porcino (subtipo H1N1), Seg�n la Organizaci�n Mundial de la Salud (OMS), los primeros casos de influenza en M�xico se detectaron el 11 de abril en el estado mexicano de Veracruz, pero el primer enfermo registrado en el mundo fue un ni�o de 10 a�os de edad quien enferm� el 30 de marzo en San Diego, Estados Unidos61 que no habia tenido ning�n contacto con cerdos y adem�s no habia ten�do ning�n antecedente de haber viajado a M�xico. Al mes se extendio por varios estados de M�xico (Distrito Federal, Estado de M�xico y San Luis Potos�) y Estados Unidos (Texas y yo", //California),  para exportarse a partir de entonces, con aparici�n de numerosos casos en otros pa�ses de pacientes que hab�an viajado a M�xico. Se han constatado unos pocos casos de contagios indirectos, de personas que no han estado en dicha regi�n, que se han dado en Espa�a, Alemania, Corea del Sur y Reino Unido.62 El 29 de abril la Organizaci�n Mundial de la Salud (OMS) la clasific� como de nivel de alerta cinco, es decir, pandemia inminente.63 Ese nivel de alerta no define la gravedad de la enfermedad producida por el virus, sino su extensi�n geogr�fica.",
				true);
		
//		wikipediaArticle = new TextDocument("a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your");
		for (String word : wikipediaArticle) {
			// if (word.equals("h1n1")) {
			// System.out.println(word);
			// System.out.println(this.sut.toString());
			// }
			sut.insertRecord(new InvertedIndexIndexRecord(word, simulateBlockNumber(word)));
		}
	}

	/**
	 * Prueba obtener todos los registros
	 * 
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	//@Test
	public void testRetrieveAllRecords() throws RecordSerializationException, IOException {
		testRetrieveAllRecords(this.sut, wikipediaArticle);
		//System.out.println(this.sut.toString());
	}
	public static void testRetrieveAllRecords(Tree<InvertedIndexIndexRecord, StringField> sut, TextDocument words) throws RecordSerializationException, IOException {
		for (String word : words) {
			StringField key = new StringField(word);
			InvertedIndexIndexRecord record = sut.getRecord(key);
			verifyCorrectRecord(record, word, key);
		}
	}

	private static void verifyCorrectRecord(InvertedIndexIndexRecord record, String word, StringField key) {
		// verifica que el record obtenido sea el correcto
		Assert.assertNotNull("El arbol no devolvi� ning�n registro cuando se le pidi� uno que hab�a sido insertado. Palabra buscada: " + key.toString(), record);
		Assert.assertEquals(0, record.getKey().compareTo(key));
		Assert.assertEquals(simulateBlockNumber(word), record.getBlockNumber());
	}
	
	@Test
	public void testBalanceChildsWithUpperExcedent() throws IOException {
		String splitter = "california";
		//dos de la raiz, mas 3 hijos
		Assert.assertEquals(5, this.sut.getBlockFile().getBlockCount());
		//System.out.println("Insertando: " + splitter);
		//System.out.println(sut.toString());
		//inserto para q haga un split
		sut.insertRecord(new InvertedIndexIndexRecord(splitter, simulateBlockNumber(splitter)));
		//System.out.println(sut.toString());
		Assert.assertEquals("Split desencadenado, se esperaban 6 bloques", 5, this.sut.getBlockFile().getBlockCount());
		
		testRetrieveAllRecords(this.sut, wikipediaArticle);
		
		StringField key = new StringField(splitter);
		InvertedIndexIndexRecord record = sut.getRecord(key);
		verifyCorrectRecord(record, splitter, key);
		
		}
	@Test
	public void testBalanceChildsWithLowerExcedent() throws IOException {
		String splitter = "zorro";
		//dos de la raiz, mas 3 hijos
		Assert.assertEquals(5, this.sut.getBlockFile().getBlockCount());
		//System.out.println("Insertando: " + splitter);
		//System.out.println(sut.toString());
		//inserto para q haga un split
		sut.insertRecord(new InvertedIndexIndexRecord(splitter, simulateBlockNumber(splitter)));
		//System.out.println(sut.toString());
		Assert.assertEquals("Split desencadenado, se esperaban 6 bloques", 5, this.sut.getBlockFile().getBlockCount());
		
		testRetrieveAllRecords(this.sut, wikipediaArticle);
		
		StringField key = new StringField(splitter);
		InvertedIndexIndexRecord record = sut.getRecord(key);
		verifyCorrectRecord(record, splitter, key);
		}
	
	@Test
	public void testBalanceChildsWithNonBalanceableOverflow() throws IOException {
		//("marzo")("mes")("mexicano")("mexico")("mundial")("mundo")
		String str1 = "marciano";
		String str2 = "mercano";
		String str3 = "merquero";
		sut.insertRecord(new InvertedIndexIndexRecord(str1, simulateBlockNumber(str1)));
		sut.insertRecord(new InvertedIndexIndexRecord(str2, simulateBlockNumber(str2)));
		sut.insertRecord(new InvertedIndexIndexRecord(str3, simulateBlockNumber(str3)));
		String splitter = "zorroMequetrefeConCaraDeUnHijoDeLaChingadaMadre";
		//dos de la raiz, mas 3 hijos
		Assert.assertEquals(5, this.sut.getBlockFile().getBlockCount());
		//System.out.println("Insertando: " + splitter);
		//System.out.println(sut.toString());
		//inserto para q haga un split
		sut.insertRecord(new InvertedIndexIndexRecord(splitter, simulateBlockNumber(splitter)));
		//System.out.println(sut.toString());	
		Assert.assertEquals("Se forzo un split, se esperaban 6 bloques", 6, this.sut.getBlockFile().getBlockCount());
		
		testRetrieveAllRecords(this.sut, wikipediaArticle);
		
		StringField key = new StringField(str1);
		InvertedIndexIndexRecord record = sut.getRecord(key);
		verifyCorrectRecord(record, str1, key);
		
		key = new StringField(str2);
		record = sut.getRecord(key);
		verifyCorrectRecord(record, str2, key);
		
		key = new StringField(str3);
		record = sut.getRecord(key);
		verifyCorrectRecord(record, str3, key);
		
		key = new StringField(splitter);
		record = sut.getRecord(key);
		verifyCorrectRecord(record, splitter, key);
		}
	}
