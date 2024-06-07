package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.ERROR_WHILE_FETCHING_FROM_ADVOCATE;

@Slf4j
@Component
public class CaseUtil {

	public static void enrichLinkedCaseForUpdate(CourtCase courtCase, AuditDetails auditDetails) {
		if(courtCase!=null && courtCase.getLinkedCases()!=null){
			List<LinkedCase> linkedCaseList = courtCase.getLinkedCases().stream()
					.filter(linkedCase -> linkedCase.getId() == null)
					.collect(Collectors.toList());
			if(!linkedCaseList.isEmpty()){
				linkedCaseList.forEach(linkedCase -> {
					linkedCase.setId(UUID.randomUUID());
					linkedCase.setAuditdetails(auditDetails);
					linkedCase.getDocuments().forEach(document -> {
						document.setId(String.valueOf(UUID.randomUUID()));
						document.setDocumentUid(document.getId());
					});
				});
			}
		}
	}

	public static void enrichStatutAndSectionsForUpdate(CourtCase courtCase, AuditDetails auditDetails) {
		if(courtCase!=null && courtCase.getStatutesAndSections()!=null){
			List<StatuteSection> statuteSectionList = courtCase.getStatutesAndSections().stream()
					.filter(statuteSection -> statuteSection.getId() == null)
					.collect(Collectors.toList());
			if(!statuteSectionList.isEmpty()){
				statuteSectionList.forEach(statuteSection -> {
					statuteSection.setId(UUID.randomUUID());
					statuteSection.setStrSections(listToString(statuteSection.getSections()));
					statuteSection.setStrSubsections(listToString(statuteSection.getSubsections()));
					statuteSection.setAuditdetails(auditDetails);
				});
			}
		}
	}

	public static void enrichLitigantsForUpdate(CourtCase courtCase, AuditDetails auditDetails) {
		if(courtCase!=null && courtCase.getLitigants()!=null){
			List<Party> litigantList = courtCase.getLitigants().stream()
					.filter(party -> party.getId() == null)
					.collect(Collectors.toList());
			if(!litigantList.isEmpty()){
				litigantList.forEach(party -> {
					party.setId((UUID.randomUUID()));
					party.setAuditDetails(auditDetails);
					if (party.getDocuments() != null) {
						party.getDocuments().forEach(document -> {
							document.setId(String.valueOf(UUID.randomUUID()));
							document.setDocumentUid(document.getId());
						});
					}
				});
			}
		}
	}

	public static void enrichRepresentivesForUpdate(CourtCase courtCase, AuditDetails auditDetails) {
		if(courtCase!=null && courtCase.getRepresentatives()!=null){
			List<AdvocateMapping> representivesList = courtCase.getRepresentatives().stream()
					.filter(advocateMapping -> advocateMapping.getId() == null)
					.collect(Collectors.toList());
			if(!representivesList.isEmpty()){
				representivesList.forEach(advocateMapping -> {
					advocateMapping.setId(String.valueOf(UUID.randomUUID()));
					advocateMapping.setAuditDetails(auditDetails);
					if (advocateMapping.getDocuments() != null) {
						advocateMapping.getDocuments().forEach(document -> {
							document.setId(String.valueOf(UUID.randomUUID()));
							document.setDocumentUid(document.getId());
						});
					}

					if(advocateMapping.getRepresenting()!=null){
						List<Party> representingList = advocateMapping.getRepresenting().stream()
								.filter(party -> party.getId() == null)
								.collect(Collectors.toList());
						representingList.forEach(partyRepresenting -> {
							partyRepresenting.setId((UUID.randomUUID()));
							partyRepresenting.setCaseId(courtCase.getId().toString());
							partyRepresenting.setAuditDetails(auditDetails);
							if (partyRepresenting.getDocuments() != null) {
								partyRepresenting.getDocuments().forEach(document -> {
									document.setId(String.valueOf(UUID.randomUUID()));
									document.setDocumentUid(document.getId());
								});
							}
						});
					}
				});
			}
		}
	}

	public static void enrichDocumentsForUpdate(CourtCase courtCase) {
		List<Document> documentList = courtCase.getDocuments().stream()
				.filter(document -> document.getId() == null)
				.collect(Collectors.toList());
		if(!documentList.isEmpty()){
			documentList.forEach(party -> {
				courtCase.getDocuments().forEach(document -> {
					document.setId(String.valueOf(UUID.randomUUID()));
					document.setDocumentUid(document.getId());
				});
			});
		}
	}

	public static String listToString(List<String> list) {
		StringBuilder stB = new StringBuilder();
		boolean isFirst = true;
		for (String doc : list) {
			if (isFirst) {
				isFirst = false;
				stB.append(doc);
			} else {
				stB.append("," + doc);
			}
		}

		return stB.toString();
	}
}