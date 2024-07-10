package org.pucar.dristi.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class PDFSignatureService {

    public void extractSignature(File pdfFile) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            List<PDSignature> signatures = document.getSignatureDictionaries();
            for (PDSignature signature : signatures) {
                System.out.println("Signature found: " + signature.getName());
                // Extract more information as needed
            }
        }
    }
}
