package com.derdiedas.bootstrap.importer;

import java.io.IOException;

public interface WordsImporter {

    ImportResult doImport() throws IOException;

    String getSourceName();
}
