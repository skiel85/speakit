package speakit.documentstorage;

import speakit.ftrs.index.InvertedList;

public interface DocumentRepository {

	public DocumentList getDocumentList(InvertedList invertedList);
}
