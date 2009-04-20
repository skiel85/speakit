package speakit.ftrs;

import speakit.documentstorage.DocumentList;
import speakit.documentstorage.DocumentRepository;
import speakit.ftrs.index.Index;
import speakit.ftrs.index.InversedList;

public class FTRS implements FTRSInterface {

	protected DocumentRepository repository;
	protected Index index;
	@Override
	public DocumentList getDocumentsFor(String word) {
		Index index = getIndex();
		if (index.exists(word)) {
			InversedList inversedList = index.getInversedList(word);
			DocumentRepository repository = getDocumentRepository();
			DocumentList result = repository.getDocumentList(inversedList);
			return result;
		}
		else
		//TODO Lanzar excepcion de lista no encontrada? devolver null?
			return null;
	}

	private DocumentRepository getDocumentRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	private Index getIndex() {
		// TODO Auto-generated method stub
		return null;
	}

}
