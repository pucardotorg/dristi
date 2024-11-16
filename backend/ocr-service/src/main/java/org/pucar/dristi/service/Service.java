package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import jakarta.annotation.PostConstruct;
import net.minidev.json.JSONArray;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Properties;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.OcrRepository;
import org.pucar.dristi.util.FileStoreUtil;
import org.pucar.dristi.util.MdmsFetcher;
import org.pucar.dristi.util.Util;
import org.pucar.dristi.web.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@org.springframework.stereotype.Service
public class Service {

    private static final Logger log = LoggerFactory.getLogger(Service.class);

    private final Util utils;
    private final Properties properties;
    private MdmsFetcher mdmsFetcher;
    private ObjectMapper objectMapper;
    private FileStoreUtil fileStoreUtil;
    private Producer producer;
    private final OcrRepository ocrRepository;
    private Map<String, List<String>> keyWordsByDocument;

    @Autowired
    public Service(Util utils, Properties properties, MdmsFetcher mdmsFetcher,
                   ObjectMapper objectMapper, FileStoreUtil fileStoreUtil,
                   Producer producer, OcrRepository ocrRepository) {
        this.utils = utils;
        this.properties = properties;
        this.mdmsFetcher = mdmsFetcher;
        this.objectMapper = objectMapper;
        this.fileStoreUtil = fileStoreUtil;
        this.producer = producer;
        this.ocrRepository = ocrRepository;
    }

    @PostConstruct
    void getMdmsForOCR() throws JsonProcessingException {
        try {
            JSONArray jsonArray = this.mdmsFetcher.getMdmsForOCR((String) null);
            ObjectReader reader = this.objectMapper.readerFor(this.objectMapper.getTypeFactory().constructCollectionType(List.class, DocumentType.class));
            List<DocumentType> documentTypes = reader.readValue(jsonArray.toString());
            log.info("DocumentTypes: {}", documentTypes.toString());
            this.keyWordsByDocument = documentTypes.stream().collect(Collectors.toMap(DocumentType::getType, DocumentType::getKeyWords));
        } catch (IOException e) {
            log.error("Error occurred while reading OCR Document Types from MDMS Config", e);
            throw new CustomException("OCR_DOCUMENT_TYPES_READING_ERROR", e.getMessage());
        }
    }

    public Set<String> getDocumentTypes() {
        return this.keyWordsByDocument.keySet();
    }


    private Ocr processOcrResponse(OcrResponse ocrResponse, OcrRequest ocrRequest) {
        Ocr ocr = new Ocr();
        ocr.setId(UUID.randomUUID())
                .setTenantId(properties.getStateLevelTenantId())
                .setFileStoreId(ocrRequest.getFileStoreId())
                .setFilingNumber(ocrRequest.getFilingNumber())
                .setDocumentType(ocrRequest.getDocumentType());
        if (null != ocrResponse.getMessage()) {
            ocr.setMessage(ocrResponse.getMessage());
            if (ocrResponse.getMessage().contains(RETRY_WITH_A_BETTER_QUALITY_IMAGE_MESSAGE)) {
                ocr.setCode(RETRY_WITH_A_BETTER_QUALITY_IMAGE_CODE);
            }
            else {
                ocr.setCode(OCR_ERROR);
            }
        } else if (null != ocrResponse.getKeywordCounts()) {
            Map<String, Integer> keyWordCounts = ocrResponse.getKeywordCounts();
            List<String> missingKeywords = new ArrayList<>();
            for (String keyword : ocrRequest.getKeywords()) {
                if (keyWordCounts.get(keyword) == 0) {
                    missingKeywords.add(keyword);
                }
            }
            if (missingKeywords.size() == ocrRequest.getKeywords().size()) {
                ocr.setMessage("Not a valid " + ocrRequest.getDocumentType());
                ocr.setCode(NOT_A_VALID_DOCUMENT);
            }
            log.info(missingKeywords.toString());
        }
        try {
            OcrPersist ocrPersist = new OcrPersist().setOcr(ocr);
            ocrPersist.setRequestInfo(ocrRequest.getRequestInfo());
            String message = objectMapper.writeValueAsString(ocrPersist);
            log.info(message);
            producer.push(properties.getOcrTopic(), message);
        } catch (Exception e) {
            log.error("error in pushing to kafka");
        }
        return ocr;
    }

    public Ocr verifyFileStoreDocument(OcrRequest ocrRequest) {

        Resource resource = fileStoreUtil.getFileFromStore(ocrRequest.getFileStoreId());
        return callOCR(resource, ocrRequest);

    }

    public Ocr callOCR(Resource resource, OcrRequest ocrRequest) {

        if (ocrRequest.getKeywords() == null) {
            ocrRequest.setKeywords(this.keyWordsByDocument.get(ocrRequest.getDocumentType()));
        }
        String url = properties.getOcrHost() + properties.getOcrEndPoint();
        log.info(ocrRequest.getKeywords().toString());
        OcrResponse ocrResponse = utils.callOCR(url, resource, ocrRequest).getBody();
        log.info(ocrResponse.toString());

        return processOcrResponse(ocrResponse, ocrRequest);
    }
    public List<Ocr> getOcrByFilingNumber(String filingNumber)
    {
        return ocrRepository.findByFilingNumber(filingNumber);
    }
}
