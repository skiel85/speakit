package speakit.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.Configuration;
import speakit.FileManager;
import speakit.Speakit;
import speakit.TextDocument;
import speakit.documentstorage.TextDocumentList;


public class SpeakitSearchRealDocumentsTest {

	private static final TextDocument	ARTICLE_AUTOESTIMA	= new TextDocument(1, "En psicolog�a, la Autoestima es la opini�n emocional profunda que las personas tienen de s� mismos, y que sobrepasa en sus causas la racionalizaci�n y la l�gica de dicho individuo, tambi�n se puede expresar como el amor que tenemos hacia nosotros mismos. \nLa alta autoestima es quererse a uno mismo y querer a los dem�s. Significa saber que eres valioso, digno, y afirmarlo. Implica respetarte a ti mismo y ense�ar a los dem�s a hacerlo.\n Est� relacionada con otras variables psicol�gicas como son locus de control y expectativa de autoeficacia. De forma que un locus de control interno implica, generalmente, una alta autoestima, y viceversa; mientras que alta expectativa de autoeficacia para ciertos comportamientos y situaciones suele estar asociada a una alta autoestima, y viceversa.\n Autoestima pedagog�a \n El concepto de Autoestima ha tenido papeles preponderantes en la toma de decisiones en ciertos sistemas educativos. En particular, a principios de la d�cada de 1990, en Estados Unidos y otros pa�ses anglosajones, la autoestima se convirti� en un concepto en boga entre algunos te�ricos de la pedagog�a. La teor�a propuesta en ese entonces, era que la autoestima es una causa de las actitudes constructivas en los individuos, y no su consecuencia, dici�ndose que si por ejemplo, un estudiante tiene buena autoestima, entonces tendr�a buenos resultados acad�micos. En esta corriente encontramos a autores como Goleman que apoyan que la autoestima influye en el desarrollo del coeficiente intelectual. Esta teor�a adquiri� una gran notoriedad e influencia entre los responsables del sistema educativo estadounidense, pero con escasa o nula validaci�n estad�stica y revisi�n cient�fica.\n Y sin embargo, a pesar de la carencia de pruebas de tales afirmaciones, los efectos en la forma de estructurar los planes de estudio y los cursos fueron de importantes a may�sculos. La idea de promover la autoestima de los estudiantes, llev� en algunos lugares a la supresi�n de cualquier medida utilizada para distinguir a los mejores estudiantes, para no afectar la autoestima de los que no obten�an buenas notas. Medidas que por lo general obtuvieron resultados contrarios a los que buscaban, al no incentivar el desempe�o sino la uniformidad.\n La autoestima es una o la mejor base de asentamiento de los sentimientos, desde el punto de vista racional y siempre lo que se tiene que fomentar es la capacidad del individuo y el sentirse como tal y la ausencia de todos los sentimientos negativos sobre su capacidad e inseguridad. La autoestima es muy importante en la comunicaci�n interpersonal...");
	private static final TextDocument	ARTICLE_EFECTO_TETRIS	= new TextDocument(2, "El efecto tetris es la habilidad de cualquier actividad, a la que una persona haya dedicado suficiente tiempo, de controlar los pensamientos, im�genes mentales y sue�os del individuo. El nombre del efecto proviene del juego tetris, en el que el jugador debe rotar y mover bloques con diferentes formas mientras caen. Si el jugador puede acomodar los bloques para que formen una l�nea horizontal completa, esta desaparecer�. El objetivo principal es evitar que los bloques llenen toda la pantalla. Gente que juega al tetris por mucho tiempo, se da cuenta que termina pensando como acomodar diferentes formas que se encuentra en el mundo real, como por ejemplo edificios o baldosas. En este sentido, el juego del tetris es como una forma de h�bito. Tambi�n estas personas pueden ver formas cayendo en los costados de su campo visual, en lo que se llama visi�n perif�rica, o tambi�n al cerrar los ojos. En este sentido, el efecto es una forma de alucinaci�n.");
	private static final TextDocument	ARTICLE_INSTINTO	= new TextDocument(3, "El instinto desde la Biolog�a se define como una pauta hereditaria de comportamiento cuyas caracter�sticas son:\n Es com�n a toda la especie, las excepciones y variabilidad son m�nimas, explic�ndose por el instinto mismo.\n Posee finalidad adaptativa.\n Es de car�cter complejo, es decir, consta de una serie de pasos para su producci�n: percepci�n de la necesidad, b�squeda del objeto, percepci�n del objeto, utilizaci�n del objeto, satisfacci�n y cancelaci�n del estado de necesidad.\n Es global, compromete a todo el organismo vivo..");
	private static final TextDocument	ARTICLE_ABSTRACCION	= new TextDocument(4, "La abstracci�n (Lat. abstractio = sacar fuera de) es un proceso que implica reducir los componentes fundamentales de informaci�n de un fen�meno para conservar sus rasgos m�s relevantes con el objetivo de formar categor�as o conceptos. Por ejemplo, abstraer de un sauce el concepto de �rbol, implica retener solamente la informaci�n (caracter�sticas, funciones, etc) del sauce que se pueden aplicar para ser incluido dentro de la categor�a general de los �rboles.\n Una pregunta esencial en psicolog�a consiste en intentar explicar este proceso de abstracci�n. Por ejemplo, como las personas logran formar conceptos a partir de experiencias con objetos individuales.\n El psic�logo Piaget propone que el sujeto extrae informaci�n de los objetos (abstracci�n simple) o de sus propias acciones sobre los objetos (abstracci�n reflexiva).\n En educaci�n, la idea de abstraer, se relaciona con el momento en que el conocimiento entra a formar parte de la vida del sujeto; inicialmente en una categor�a mental y se confirma con un comportamiento expl�cito que nos permite ver que se ha logrado la \"abstracci�n\"");
	private static final TextDocument	ARTICLE_CONECTIVISMO	= new TextDocument(5, "El conectivismo es una teor�a del aprendizaje para la era digital que ha sido desarrollada por George Siemens basado en el an�lisis de las limitaciones del conductismo, el cognitivismo y el constructivismo, para explicar el efecto que la tecnolog�a ha tenido sobre la manera en que actualmente vivimos, nos comunicamos y aprendemos.\n El conectivismo es la integraci�n de los principios explorados por las teor�as del caos, redes neuronales, complejidad y auto-organizaci�n. El aprendizaje es un proceso que ocurre dentro de una amplia gama de ambientes que no est�n necesariamente bajo el control del individuo. Es por esto que �l mismo (entendido como conocimiento aplicable) puede residir fuera del ser humano, por ejemplo dentro de una organizaci�n o una base de datos, y se enfoca en la conexi�n especializada en conjuntos de informaci�n que nos permite aumentar cada vez m�s nuestro estado actual de conocimiento.\n Esta teor�a es conducida por el entendimiento de que las decisiones est�n basadas en la transformaci�n acelerada de los basamentos. Continuamente nueva informaci�n es adquirida dejando obsoleta la anterior. La habilidad para discernir entre la informaci�n que es importante y la que es trivial es vital, as� como la capacidad para reconocer cu�ndo esta nueva informaci�n altera las decisiones tomadas en base a informaci�n pasada.\n El punto de inicio del conectivismo es el individuo. El conocimiento personal se hace de una red, que alimenta de informaci�n a organizaciones e instituciones, que a su vez retroalimentan informaci�n en la misma red, que finalmente termina proveyendo nuevo aprendizaje al individuo. Este ciclo de desarrollo del conocimiento permite a los aprendices mantenerse actualizados en el campo en el cual han formado conexiones.");
	private static final TextDocument	ARTICLE_ADOLESCENCIA	= new TextDocument(6, "La adolescencia (del lat�n \"adolescere\": crecer, desarrollarse) es un continuo de la existencia del joven, en donde se realiza la transici�n entre el infante o ni�o de edad escolar y el adulto. Esta transici�n de cuerpo y mente, proviene no solamente de s� mismo, sino que se conjuga con su entorno, el cual es trascendental para que los grandes cambios fisiol�gicos que se produce en el individuo lo hagan llegar a la edad adulta. La adolescencia es un fen�meno biol�gico, cultural y social, por lo tanto sus l�mites no se asocian solamente a caracter�sticas f�sicas.\n A diferencia de la pubertad, que comienza a una edad determinada a los doce o trece debido a cambios hormonales, la adolescencia puede variar mucho en edad y en duraci�n en cada individuo pues est� relacionada no solamente con la maduraci�n de la psiquis del individuo sino que depende de factores psico-sociales m�s amplios y complejos, originados principalmente en el seno familiar.[cita requerida]\n Muchas culturas difieren en cu�l es la edad en la que las personas llegan a ser adultas. En diversas regiones, el paso de la adolescencia a la edad adulta va unido a ceremonias y/o fiestas.");
	
	private FileManager	fileManager;
	private Speakit		sut;

	@Before
	public void setUp() throws Exception {
		this.fileManager = new TestFileManager(this.getClass().getName());
		this.sut = new Speakit();
		Configuration conf = new Configuration();
		conf.setBlockSize(1024);
		conf.setTrieDepth(4);
		this.sut.install(this.fileManager, conf);
		this.sut.load(this.fileManager);
		
		this.sut.addDocument(ARTICLE_AUTOESTIMA);
		this.sut.addDocument(ARTICLE_EFECTO_TETRIS);
		this.sut.addDocument(ARTICLE_INSTINTO);
		this.sut.addDocument(ARTICLE_ABSTRACCION);
		this.sut.addDocument(ARTICLE_CONECTIVISMO);
		this.sut.addDocument(ARTICLE_ADOLESCENCIA);
	}

	@After
	public void tearDown() throws Exception {
		this.fileManager.destroyFiles();
	}
	
	@Test
	public void testSearch() throws IOException{ 
		TextDocumentList result = this.sut.search(new TextDocument("componentes Biolog�a"));
		Iterator<TextDocument> iterator = result.iterator();
		
		List<TextDocument> resultList = new ArrayList<TextDocument>();
		for (TextDocument textDocument : result) {
			resultList.add(textDocument);
		}
		System.out.println(sut.printIndexForDebug());
		Assert.assertTrue("Deberia contenerlo",resultList.contains(ARTICLE_ABSTRACCION));
		Assert.assertTrue("Deberia contenerlo",resultList.contains(ARTICLE_INSTINTO));
	}
}
