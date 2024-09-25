package org.pucar.dristi.web.controller;

import org.pucar.dristi.service.Service;
import org.pucar.dristi.web.model.DocumentTypesRequest;
import org.pucar.dristi.web.model.Ocr;
import org.pucar.dristi.web.model.OcrRequest;
import org.pucar.dristi.web.model.OcrSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;


@RestController
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private final Service service;

    @Autowired
    public Controller(Service service) {
        this.service = service;
    }


    @PostMapping("/ocr")
    public ResponseEntity<Ocr> callOCR(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "keywords", required = false) List<String> keywords,
            @RequestParam(value = "distanceCutoff", required = false) Integer distanceCutoff,
            @RequestParam(value = "documentType", required = false) String documentType,
            @RequestParam(value = "extractData", required = false) Boolean extractData) {

        logger.info("Received request to process image with file: ");
        OcrRequest ocrRequest = new OcrRequest()
                .setKeywords(keywords)
                .setDistanceCutoff(distanceCutoff)
                .setDocumentType(documentType)
                .setExtractData(extractData);
        Ocr response = service.callOCR(file.getResource(), ocrRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/documentTypes")
    public Set<String> getDocumentTypes(@RequestBody DocumentTypesRequest documentTypesRequest) {
        return service.getDocumentTypes();
    }

    @PostMapping("/verify")
    public ResponseEntity<Ocr> verifyFileStoreDocument(@RequestBody OcrRequest ocrRequest) {

        logger.info("Received request to verify image {}, for documentType: {}", ocrRequest.getFileStoreId(), ocrRequest.getDocumentType());

        Ocr response = service.verifyFileStoreDocument(ocrRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/data")
    public ResponseEntity<List<Ocr>> getOcrData(@RequestBody OcrSearchRequest ocrSearchRequest) {
        List<Ocr> response = service.getOcrByFilingNumber(ocrSearchRequest.getFilingNumber());
        return ResponseEntity.ok(response);
    }

}
