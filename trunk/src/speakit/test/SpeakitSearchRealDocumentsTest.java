package speakit.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.Configuration;
import speakit.FileManager;
import speakit.Speakit;
import speakit.TextDocument;
import speakit.documentstorage.TextDocumentList;


public class SpeakitSearchRealDocumentsTest {

	public static final TextDocument	ARTICLE_AUTOESTIMA	= new TextDocument(1, "En psicología, la Autoestima es la opinión emocional profunda que las personas tienen de sí mismos, y que sobrepasa en sus causas la racionalización y la lógica de dicho individuo, también se puede expresar como el amor que tenemos hacia nosotros mismos. \nLa alta autoestima es quererse a uno mismo y querer a los demás. Significa saber que eres valioso, digno, y afirmarlo. Implica respetarte a ti mismo y enseñar a los demás a hacerlo.\n Está relacionada con otras variables psicológicas como son locus de control y expectativa de autoeficacia. De forma que un locus de control interno implica, generalmente, una alta autoestima, y viceversa; mientras que alta expectativa de autoeficacia para ciertos comportamientos y situaciones suele estar asociada a una alta autoestima, y viceversa.\n Autoestima pedagogía \n El concepto de Autoestima ha tenido papeles preponderantes en la toma de decisiones en ciertos sistemas educativos. En particular, a principios de la década de 1990, en Estados Unidos y otros países anglosajones, la autoestima se convirtió en un concepto en boga entre algunos teóricos de la pedagogía. La teoría propuesta en ese entonces, era que la autoestima es una causa de las actitudes constructivas en los individuos, y no su consecuencia, diciéndose que si por ejemplo, un estudiante tiene buena autoestima, entonces tendría buenos resultados académicos. En esta corriente encontramos a autores como Goleman que apoyan que la autoestima influye en el desarrollo del coeficiente intelectual. Esta teoría adquirió una gran notoriedad e influencia entre los responsables del sistema educativo estadounidense, pero con escasa o nula validación estadística y revisión científica.\n Y sin embargo, a pesar de la carencia de pruebas de tales afirmaciones, los efectos en la forma de estructurar los planes de estudio y los cursos fueron de importantes a mayúsculos. La idea de promover la autoestima de los estudiantes, llevó en algunos lugares a la supresión de cualquier medida utilizada para distinguir a los mejores estudiantes, para no afectar la autoestima de los que no obtenían buenas notas. Medidas que por lo general obtuvieron resultados contrarios a los que buscaban, al no incentivar el desempeño sino la uniformidad.\n La autoestima es una o la mejor base de asentamiento de los sentimientos, desde el punto de vista racional y siempre lo que se tiene que fomentar es la capacidad del individuo y el sentirse como tal y la ausencia de todos los sentimientos negativos sobre su capacidad e inseguridad. La autoestima es muy importante en la comunicación interpersonal...");
	public static final TextDocument	ARTICLE_EFECTO_TETRIS	= new TextDocument(2, "El efecto tetris es la habilidad de cualquier actividad, a la que una persona haya dedicado suficiente tiempo, de controlar los pensamientos, imágenes mentales y sueños del individuo. El nombre del efecto proviene del juego tetris, en el que el jugador debe rotar y mover bloques con diferentes formas mientras caen. Si el jugador puede acomodar los bloques para que formen una línea horizontal completa, esta desaparecerá. El objetivo principal es evitar que los bloques llenen toda la pantalla. Gente que juega al tetris por mucho tiempo, se da cuenta que termina pensando como acomodar diferentes formas que se encuentra en el mundo real, como por ejemplo edificios o baldosas. En este sentido, el juego del tetris es como una forma de hábito. También estas personas pueden ver formas cayendo en los costados de su campo visual, en lo que se llama visión periférica, o también al cerrar los ojos. En este sentido, el efecto es una forma de alucinación.");
	public static final TextDocument	ARTICLE_INSTINTO	= new TextDocument(3, "El instinto desde la Biología se define como una pauta hereditaria de comportamiento cuyas características son:\n Es común a toda la especie, las excepciones y variabilidad son mínimas, explicándose por el instinto mismo.\n Posee finalidad adaptativa.\n Es de carácter complejo, es decir, consta de una serie de pasos para su producción: percepción de la necesidad, búsqueda del objeto, percepción del objeto, utilización del objeto, satisfacción y cancelación del estado de necesidad.\n Es global, compromete a todo el organismo vivo..");
	public static final TextDocument	ARTICLE_ABSTRACCION	= new TextDocument(4, "La abstracción (Lat. abstractio = sacar fuera de) es un proceso que implica reducir los componentes fundamentales de información de un fenómeno para conservar sus rasgos más relevantes con el objetivo de formar categorías o conceptos. Por ejemplo, abstraer de un sauce el concepto de árbol, implica retener solamente la información (características, funciones, etc) del sauce que se pueden aplicar para ser incluido dentro de la categoría general de los árboles.\n Una pregunta esencial en psicología consiste en intentar explicar este proceso de abstracción. Por ejemplo, como las personas logran formar conceptos a partir de experiencias con objetos individuales.\n El psicólogo Piaget propone que el sujeto extrae información de los objetos (abstracción simple) o de sus propias acciones sobre los objetos (abstracción reflexiva).\n En educación, la idea de abstraer, se relaciona con el momento en que el conocimiento entra a formar parte de la vida del sujeto; inicialmente en una categoría mental y se confirma con un comportamiento explícito que nos permite ver que se ha logrado la \"abstracción\"");
	public static final TextDocument	ARTICLE_CONECTIVISMO	= new TextDocument(5, "El conectivismo es una teoría del aprendizaje para la era digital que ha sido desarrollada por George Siemens basado en el análisis de las limitaciones del conductismo, el cognitivismo y el constructivismo, para explicar el efecto que la tecnología ha tenido sobre la manera en que actualmente vivimos, nos comunicamos y aprendemos.\n El conectivismo es la integración de los principios explorados por las teorías del caos, redes neuronales, complejidad y auto-organización. El aprendizaje es un proceso que ocurre dentro de una amplia gama de ambientes que no están necesariamente bajo el control del individuo. Es por esto que él mismo (entendido como conocimiento aplicable) puede residir fuera del ser humano, por ejemplo dentro de una organización o una base de datos, y se enfoca en la conexión especializada en conjuntos de información que nos permite aumentar cada vez más nuestro estado actual de conocimiento.\n Esta teoría es conducida por el entendimiento de que las decisiones están basadas en la transformación acelerada de los basamentos. Continuamente nueva información es adquirida dejando obsoleta la anterior. La habilidad para discernir entre la información que es importante y la que es trivial es vital, así como la capacidad para reconocer cuándo esta nueva información altera las decisiones tomadas en base a información pasada.\n El punto de inicio del conectivismo es el individuo. El conocimiento personal se hace de una red, que alimenta de información a organizaciones e instituciones, que a su vez retroalimentan información en la misma red, que finalmente termina proveyendo nuevo aprendizaje al individuo. Este ciclo de desarrollo del conocimiento permite a los aprendices mantenerse actualizados en el campo en el cual han formado conexiones.");
	public static final TextDocument	ARTICLE_ADOLESCENCIA	= new TextDocument(6, "La adolescencia (del latín \"adolescere\": crecer, desarrollarse) es un continuo de la existencia del joven, en donde se realiza la transición entre el infante o niño de edad escolar y el adulto. Esta transición de cuerpo y mente, proviene no solamente de sí mismo, sino que se conjuga con su entorno, el cual es trascendental para que los grandes cambios fisiológicos que se produce en el individuo lo hagan llegar a la edad adulta. La adolescencia es un fenómeno biológico, cultural y social, por lo tanto sus límites no se asocian solamente a características físicas.\n A diferencia de la pubertad, que comienza a una edad determinada a los doce o trece debido a cambios hormonales, la adolescencia puede variar mucho en edad y en duración en cada individuo pues está relacionada no solamente con la maduración de la psiquis del individuo sino que depende de factores psico-sociales más amplios y complejos, originados principalmente en el seno familiar.[cita requerida]\n Muchas culturas difieren en cuál es la edad en la que las personas llegan a ser adultas. En diversas regiones, el paso de la adolescencia a la edad adulta va unido a ceremonias y/o fiestas.");
	public static final TextDocument	ARTICLE_EQUIPAMIENTO	= new TextDocument(7, "En el fútbol asociación, el equipamiento se refiere a la indumentaria y accesorios que deben llevar los jugadores a lo largo del desarrollo de un partido oficial de la FIFA. Las Reglas del juego establecen un equipamiento básico que cada jugador debe usar, además de prohibir aquellos otros objetos que puedan resultar peligrosos tanto para el que los lleva como para otra persona. Algunas competiciones pueden estipular otras restricciones, como la regulación del tamaño de los escudos en las camisetas o aclarar que en partidos en que los dos equipos vistan colores idénticos o similares, el equipo visitante debe cambiar a un equipamiento diferente.\n Los futbolistas generalmente llevan números identificatorios —denominados dorsales— en la parte trasera de sus camisetas y en ocasiones en la parte delantera y pantalones. Originalmente, un equipo usaba los números del 1 al 11, correspondiéndose abiertamente con su posición dentro del campo, pero a nivel profesional, esta práctica se suplantó por la numeración del equipo, en el que cada miembro lleva un número fijo a lo largo de una determinada temporada o competición.");
	public static final TextDocument	ARTICLE_PORTUGA	        = new TextDocument(8,	"A Bandeira Nacional da Índia foi aprovada na sua forma atual durante uma reunião ad hoc da Assembleia Constituinte realizada em 22 de julho de 1947, vinte e dois dias antes da independência indiana do Império Britânico em 15 de agosto. Ela foi usada como bandeira nacional do Domínio da Índia entre 15 de agosto de 1947 e 26 de janeiro de 1950 e, logo após, da República da Índia. Na Índia, o termo \"tricolor\" quase sempre é utilizado para referir-se a sua bandeira nacional.\nA bandeira nacional, adotada em 1947, baseia-se na do Congresso Nacional Indiano, desenhada por Pingali Venkayya. A bandeira é um tricolor horizontal: \"açafrão escuro\" no topo, branco no meio e verde na parte inferior. No centro, há uma roda azul-marinho com vinte e quatro raios, conhecida como Ashoka Chakra, extraída do Capitel do Leão de Ashoka erguido em cima do Pilar de Ashoka em Sarnath. O diâmetro desse Chacra é três-quartos da altura da faixa branca. A relação da largura da bandeira para o seu comprimento é 2:3. Ela é também a bandeira de guerra do exército da Índia, hasteada diariamente em instalações militares indianas.");
	
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
		
		TextDocumentList list = new TextDocumentList();
		list.add(ARTICLE_ABSTRACCION);
		list.add(ARTICLE_ADOLESCENCIA);
		list.add(ARTICLE_AUTOESTIMA);
		list.add(ARTICLE_CONECTIVISMO);
		list.add(ARTICLE_EFECTO_TETRIS);
		list.add(ARTICLE_INSTINTO);
		list.add(ARTICLE_PORTUGA);
		
		this.sut.addDocuments(list,3);
		this.sut.addDocument(ARTICLE_EQUIPAMIENTO);
		/*this.sut.addDocument(ARTICLE_EFECTO_TETRIS);
		this.sut.addDocument(ARTICLE_INSTINTO);
		this.sut.addDocument(ARTICLE_ABSTRACCION);
		this.sut.addDocument(ARTICLE_CONECTIVISMO);
		this.sut.addDocument(ARTICLE_ADOLESCENCIA);*/
	}

	@After
	public void tearDown() throws Exception {
		this.fileManager.destroyFiles();
	}
	
	@Test
	public void testSearch() throws IOException{ 
		TextDocumentList result = this.sut.search(new TextDocument("componentes Biología"));
		
		List<TextDocument> resultList = new ArrayList<TextDocument>();
		for (TextDocument textDocument : result) {
		
			resultList.add(textDocument);
		}
		//System.out.println(sut.printIndexForDebug());
		
		Assert.assertTrue("Deberia contenerlo",resultList.contains(ARTICLE_ABSTRACCION));
		Assert.assertTrue("Deberia contenerlo",resultList.contains(ARTICLE_INSTINTO));
	}
	
	@Test
	public void testSearchPortugues() throws IOException{ 
		TextDocumentList result = this.sut.search(new TextDocument("relação"));
		
		List<TextDocument> resultList = new ArrayList<TextDocument>();
		for (TextDocument textDocument : result) {
			resultList.add(textDocument);
		}
		//System.out.println(sut.printIndexForDebug());
		Assert.assertTrue("Deberia contenerlo",resultList.contains(ARTICLE_PORTUGA));
	}
	@Test
	public void testSearchWithTildes() throws IOException{ 
		TextDocumentList result = this.sut.search(new TextDocument("latín"));
		
		List<TextDocument> resultList = new ArrayList<TextDocument>();
		for (TextDocument textDocument : result) {
			resultList.add(textDocument);
		}
		//System.out.println(sut.printIndexForDebug());
		Assert.assertTrue("Deberia contenerlo",resultList.contains(ARTICLE_ADOLESCENCIA));
	}
	
	@Test
	public void testSearchWithStopWords() throws IOException{ 
		TextDocumentList result = this.sut.search(new TextDocument("de"));
		
		List<TextDocument> resultList = new ArrayList<TextDocument>();
		for (TextDocument textDocument : result) {
			resultList.add(textDocument);
		}
		//System.out.println(sut.printIndexForDebug());
		Assert.assertEquals("Deberia ser un resultado vacio por ser una Stop Word", 0, resultList.size());
	}
	
}
