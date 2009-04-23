package speakit.documentstorage;

import speakit.ftrs.index.InversedList;

public interface DocumentRepository {

	public DocumentList getDocumentList(InversedList inversedList);
}
