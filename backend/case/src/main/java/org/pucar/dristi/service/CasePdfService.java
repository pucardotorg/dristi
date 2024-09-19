package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.util.CasePdfUtil;
import org.pucar.dristi.util.FileStoreUtil;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.CASE_PDF_SERVICE_EXCEPTION;

@Service
@Slf4j
public class CasePdfService {

    private final Configuration config;

    private final CasePdfUtil casePdfUtil;

    private final CaseRepository caseRepository;

    private final FileStoreUtil fileStoreUtil;

    private final ObjectMapper mapper;

    private Producer producer;

    @Autowired
    public CasePdfService(Configuration config, CasePdfUtil casePdfUtil, CaseRepository caseRepository, FileStoreUtil fileStoreUtil, ObjectMapper mapper, Producer producer) {
        this.config = config;
        this.casePdfUtil = casePdfUtil;
        this.caseRepository = caseRepository;
        this.fileStoreUtil = fileStoreUtil;
        this.mapper = mapper;
        this.producer = producer;
    }

    public CourtCase generatePdf(CaseSearchRequest body) {
        log.info("Received Generate Pdf for case search criteria: {}", body);

        try {
            caseRepository.getCases(body.getCriteria(), body.getRequestInfo());
            CourtCase courtCase = body.getCriteria().get(0).getResponseList().get(0);
            if (!CollectionUtils.isEmpty(courtCase.getDocuments())) {
                for (Document document : courtCase.getDocuments()) {
                    JsonNode additionalDetailsNode = mapper.convertValue(document.getAdditionalDetails(), JsonNode.class);
                    if (additionalDetailsNode != null && additionalDetailsNode.has("fields")) {
                        for (JsonNode fieldNode : additionalDetailsNode.get("fields")) {
                            if ("FILE_CATEGORY".equals(fieldNode.get("key").asText())
                                    && "CASE_GENERATED_DOCUMENT".equals(fieldNode.get("value").asText())) {
                                log.info("Document with FILE_CATEGORY 'CASE_GENERATED_DOCUMENT' already exists, bypassing PDF generation.");
                                return courtCase;
                            }
                        }
                    }
                }
            }
            RequestInfo requestInfo = body.getRequestInfo();
            CaseRequest caseRequest = CaseRequest.builder().requestInfo(requestInfo).cases(courtCase).build();
            StringBuilder uri = new StringBuilder(config.getDristiCasePdfHost()).append(config.getDristiCasePdfPath());
            ByteArrayResource byteArrayResource = casePdfUtil.generateCasePdf(caseRequest, uri);
            Document document = fileStoreUtil.saveDocumentToFileStore(byteArrayResource.getByteArray(), courtCase.getTenantId());
            if(document.getId() == null) {
                document.setId(String.valueOf(UUID.randomUUID()));
                document.setDocumentUid(document.getId());
            }
            if (CollectionUtils.isEmpty(courtCase.getDocuments())) {
                courtCase.setDocuments(Collections.singletonList(document));
            } else {
                courtCase.getDocuments().add(document);
            }
            producer.push(config.getCaseUpdateTopic(), caseRequest);

            return courtCase;
        } catch (Exception e) {
            log.error("Error generating PDF for case", e);
            throw new CustomException(CASE_PDF_SERVICE_EXCEPTION, "Error generating PDF for case");
        }
    }
}
