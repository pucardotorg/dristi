package org.drishti.esign.service;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.security.ExternalSignatureContainer;

import java.io.InputStream;
import java.security.GeneralSecurityException;

public class MyExternalSignatureContainer implements ExternalSignatureContainer {


    private final byte[] signatureBytes;
    private final PdfName filter;
    private final PdfName subFilter;

    public MyExternalSignatureContainer(byte[] signatureBytes, PdfName filter, PdfName subFilter) {
        this.signatureBytes = signatureBytes;
        this.filter = filter;
        this.subFilter = subFilter;
    }

    @Override
    public byte[] sign(InputStream inputStream) throws GeneralSecurityException {
        return signatureBytes;
    }

    @Override
    public void modifySigningDictionary(PdfDictionary pdfDictionary) {

        if (filter != null) {
            pdfDictionary.put(PdfName.FILTER, filter);
        }
        if (subFilter != null) {
            pdfDictionary.put(PdfName.SUBFILTER, subFilter);
        }

    }
}
