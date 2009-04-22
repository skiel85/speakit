package speakit.ftrs;

import java.util.ArrayList;

import speakit.documentstorage.DocumentList;
import speakit.documentstorage.DocumentRepository;
import speakit.ftrs.index.Index;
import speakit.ftrs.index.InversedList;

public class FTRS implements FTRSInterface {

	protected DocumentRepository repository;
	protected Index index;
	@Override
	public DocumentList search(String word) {
		Index index = getIndex();
		if(index.exists(word)){
			InversedList inversedList = index.getInversedList(word);
			DocumentRepository repository = getDocumentRepository();
			DocumentList result = repository.getDocumentList(inversedList);
			return result;
		}else{
			return null;
		}
		
		
		
	}

	private DocumentRepository getDocumentRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	private Index getIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InversedList getRankedList(ArrayList<InversedList> inversedLists) {
		// TODO Auto-generated method stub
		return null;
	}

}
