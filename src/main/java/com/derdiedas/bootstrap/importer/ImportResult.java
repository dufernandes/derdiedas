package com.derdiedas.bootstrap.importer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImportResult {
    private long numberOfSuccessfullyImportedWords;
    private long numberOfFailedWordsToImport;
    private long numberOfExistingWordsNotImported;
}
