package com.brielmayer.teda.parser;

import com.brielmayer.teda.model.DocumentType;
import com.brielmayer.teda.parser.xlsx.XlsxDocumentParser;

public class ParserFactory {

    public static Parser getParser(DocumentType documentType) {
        switch (documentType) {
            case EXCEL:
                return new XlsxDocumentParser();
            default:
                throw new IllegalArgumentException("Unsupported file type");
        }
    }
}
