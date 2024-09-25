package org.pucar.dristi.util;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.encryption.EncryptionService;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.config.ServiceConstants;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateMapping;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Component
public class EncryptionDecryptionUtil {
    private final EncryptionService encryptionService;
    private final String stateLevelTenantId;
    private final boolean abacEnabled;
    private final IndividualService individualService;
    private final AdvocateUtil advocateUtil;
    private final Configuration config;

    @Autowired
    public EncryptionDecryptionUtil(@Qualifier("caseEncryptionServiceImpl") EncryptionService encryptionService,
                                    @Value("${state.level.tenant.id}") String stateLevelTenantId,
                                    @Value("${decryption.abac.enabled}") boolean abacEnabled,
                                    IndividualService individualService,
                                    AdvocateUtil advocateUtil, Configuration config) {
        this.encryptionService = encryptionService;
        this.stateLevelTenantId = stateLevelTenantId;
        this.abacEnabled = abacEnabled;
        this.individualService = individualService;
        this.advocateUtil = advocateUtil;
        this.config = config;
    }

    public <T> T encryptObject(Object objectToEncrypt, String key, Class<T> classType) {
        try {
            if (objectToEncrypt == null) {
                return null;
            }
            T encryptedObject = encryptionService.encryptJson(objectToEncrypt, key, stateLevelTenantId, classType);
            if (encryptedObject == null) {
                throw new CustomException("ENCRYPTION_NULL_ERROR", "Null object found on performing encryption");
            }
            return encryptedObject;
        } catch (CustomException e){
            log.error(e.getCode(),e.getMessage());
            throw e;
        } catch (IOException | HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while encrypting", e);
            throw new CustomException("ENCRYPTION_ERROR", "Error occurred in encryption process");
        } catch (Exception e) {
            log.error("Unknown Error occurred while encrypting", e);
            throw new CustomException("UNKNOWN_ERROR", "Unknown error occurred in encryption process");
        }

    }

    public <E, P> P decryptObject(Object objectToDecrypt, String key, Class<E> classType, RequestInfo requestInfo) {

        try {
            boolean objectToDecryptNotList = false;
            if (objectToDecrypt == null) {
                return null;
            } else if (requestInfo == null || requestInfo.getUserInfo() == null) {
                User userInfo = User.builder().uuid("no uuid").type("EMPLOYEE").build();
                requestInfo = RequestInfo.builder().userInfo(userInfo).build();
            }
            if (!(objectToDecrypt instanceof List)) {
                objectToDecryptNotList = true;
                objectToDecrypt = Collections.singletonList(objectToDecrypt);
            }
            final User encrichedUserInfo = getEncrichedandCopiedUserInfo(requestInfo.getUserInfo());
            requestInfo.setUserInfo(encrichedUserInfo);

            Map<String, String> keyPurposeMap = getKeyToDecrypt(objectToDecrypt, requestInfo);
            String purpose = keyPurposeMap.get(ServiceConstants.PURPOSE);

            if (key == null)
                key = keyPurposeMap.get("key");
            log.info("Decrypting:: requestInfo: {}, objectToDecrypt: {}, keyPurposeMap: {}",requestInfo,objectToDecrypt,keyPurposeMap);
            P decryptedObject = (P) encryptionService.decryptJson(requestInfo, objectToDecrypt, key, purpose, classType);
            if (decryptedObject == null) {
                throw new CustomException("DECRYPTION_NULL_ERROR", "Null object found on performing decryption");
            }

            if (objectToDecryptNotList) {
                decryptedObject = (P) ((List<E>) decryptedObject).get(0);
            }
            return decryptedObject;
        } catch (CustomException e){
            log.error(e.getCode(),e.getMessage());
            throw e;
        } catch (IOException | HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while decrypting", e);
            throw new CustomException("DECRYPTION_SERVICE_ERROR", "Error occurred in decryption process");
        } catch (Exception e) {
            log.error("Unknown Error occurred while decrypting", e);
            throw new CustomException("UNKNOWN_ERROR", "Unknown error occurred in decryption process");
        }
    }

    private boolean isUserDecryptingForAllowedRoles(User userInfo){

        for (Role role:userInfo.getRoles()){
            String code = role.getCode();
            if (code.equalsIgnoreCase(INTERNAL_MICROSERVICE_ROLE) || code.equalsIgnoreCase(NYAY_MITRA_ROLE) || code.equalsIgnoreCase(JUDGE_ROLE) || code.equalsIgnoreCase(FSO_ROLE) || code.equalsIgnoreCase(BENCH_CLERK_ROLE)){
                return true;
            }
        }
        return false;
    }




    public boolean isUserDecryptingForSelf(Object objectToDecrypt, RequestInfo requestInfo) {

        if (objectToDecrypt instanceof List<?> list) {
            if (list.isEmpty())
                return false;
            if (list.size() > 1)
                return false;
        } else {
            throw new CustomException("DECRYPTION_NOTLIST_ERROR", objectToDecrypt + " is not of type List of Object");
        }

        boolean isUserAdvocate = requestInfo.getUserInfo().getRoles().stream().anyMatch(role -> role.getCode().equalsIgnoreCase(ADVOCATE_ROLE));

        CourtCase courtCase = (CourtCase) list.get(0);

        boolean isUserCaseCreator =  courtCase.getAuditdetails().getCreatedBy().equalsIgnoreCase(requestInfo.getUserInfo().getUuid());

        if (isUserCaseCreator){
            return true;
        }

        String individualId = individualService.getIndividualId(requestInfo);

        List<AdvocateMapping> advocates = courtCase.getRepresentatives();

        if (isUserAdvocate && advocates != null) {
            List<Advocate> advocateResponse = advocateUtil.fetchAdvocatesByIndividualId(requestInfo,individualId);

            return advocates.stream().anyMatch(advocateMapping -> advocateMapping.getAdvocateId().equalsIgnoreCase(advocateResponse.get(0).getId().toString()));
        }

        List<Party> litigants = courtCase.getLitigants();
        if (litigants != null) {
            return litigants.stream().anyMatch(litigant -> litigant.getIndividualId().equalsIgnoreCase(individualId));
        }
        return false;

    }

    private boolean isDecryptionForIndividualUser(Object objectToDecrypt) {
        return ((List<?>) objectToDecrypt).size() == 1;
    }

    public Map<String,String> getKeyToDecrypt(Object objectToDecrypt, RequestInfo requestInfo) {
        Map<String,String> keyPurposeMap = new HashMap<>();

        if (!abacEnabled){
            keyPurposeMap.put("key", config.getCaseDecryptSelf());
            keyPurposeMap.put(ServiceConstants.PURPOSE,"AbacDisabled");
        }

        else if (isUserDecryptingForAllowedRoles(requestInfo.getUserInfo())){
            keyPurposeMap.put("key", config.getCaseDecryptSelf());
            keyPurposeMap.put(ServiceConstants.PURPOSE,"AllowedRole");
        }

        else if (isUserDecryptingForSelf(objectToDecrypt, requestInfo)){
            keyPurposeMap.put("key", config.getCaseDecryptSelf());
            keyPurposeMap.put(ServiceConstants.PURPOSE,"Self");
        }


        else if (isDecryptionForIndividualUser(objectToDecrypt)){
            keyPurposeMap.put("key", config.getCaseDecryptOther());
            keyPurposeMap.put(ServiceConstants.PURPOSE,"SingleSearchResult");
        }

        else{
            keyPurposeMap.put("key", config.getCaseDecryptOther());
            keyPurposeMap.put(ServiceConstants.PURPOSE,"BulkSearchResult");
        }

        return keyPurposeMap;
    }

    private User getEncrichedandCopiedUserInfo(User userInfo) {
        List<Role> newRoleList = new ArrayList<>();
        if (userInfo.getRoles() != null) {
            for (Role role : userInfo.getRoles()) {
                Role newRole = Role.builder().code(role.getCode()).name(role.getName()).id(role.getId()).build();
                newRoleList.add(newRole);
            }
        }

        if (newRoleList.stream().filter(role -> (role.getCode() != null) && (userInfo.getType() != null) && role.getCode().equalsIgnoreCase(userInfo.getType())).count() == 0) {
            Role roleFromtype = Role.builder().code(userInfo.getType()).name(userInfo.getType()).build();
            newRoleList.add(roleFromtype);
        }

        return User.builder().id(userInfo.getId()).userName(userInfo.getUserName()).name(userInfo.getName())
                .type(userInfo.getType()).mobileNumber(userInfo.getMobileNumber()).emailId(userInfo.getEmailId())
                .roles(newRoleList).tenantId(userInfo.getTenantId()).uuid(userInfo.getUuid()).build();
    }

}