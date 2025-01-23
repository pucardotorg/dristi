package pucar.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pucar.config.Configuration;
import pucar.repository.LockRepository;
import pucar.service.LockService;
import pucar.util.IndividualUtil;
import pucar.web.models.AuditDetails;
import pucar.web.models.Lock;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class LockServiceImpl implements LockService {

    private final LockRepository repository;
    private final Configuration configuration;
    private final IndividualUtil individualUtil;

    @Autowired
    public LockServiceImpl(LockRepository repository, Configuration configuration, IndividualUtil individualUtil) {
        this.repository = repository;
        this.configuration = configuration;
        this.individualUtil = individualUtil;
    }

    @Override
    public Lock setLock(RequestInfo requestInfo, Lock lockDetails) {

        // if same guy is trying to create a lock and its already lock then dont throw error / dont create lock
        log.info("method:setLock, result=InProgress, uniqueId={},tenantId={}", lockDetails.getUniqueId(), lockDetails.getTenantId());

        Lock lock = getLock(lockDetails.getUniqueId(), lockDetails.getTenantId());
        String uuid = requestInfo.getUserInfo().getUuid();
        if (lock != null) {
            // check who created lock, if same guy is trying to create return the old lock
            log.info("Method:setLock, Result=InProgress,Lock is already present");
            String userId = lock.getUserId();
            if (userId.equalsIgnoreCase(uuid)) {
                return lock;
            } else {
                log.error("Method:setLock, Result=Error, Lock is already present,created by {}", lock.getIndividualId());
                throw new CustomException("SET_LOCK_EXCEPTION", "entity is already Locked");
            }
        }

        lockDetails.setId(UUID.randomUUID().toString());
        lockDetails.setLockReleaseTime(System.currentTimeMillis() + configuration.getLockDurationMillis());
        lockDetails.setLockDate(System.currentTimeMillis());
        String individualId = individualUtil.getIndividualId(requestInfo);
        lockDetails.setIndividualId(individualId);
        lockDetails.setIsLocked(true);
        lockDetails.setUserId(uuid);
        lockDetails.setAuditDetails(getCreateAuditDetails(requestInfo));
        Lock lockedResponse = null;
        try {
            lockedResponse = repository.save(lockDetails);

        } catch (Exception e) {
            log.error("Method:setLock, Result=Error, uniqueId={},tenantId={}", lockDetails.getUniqueId(), lockDetails.getTenantId());
            log.error("Error:", e);
            throw new CustomException("SET_LOCK_EXCEPTION", "Error occurred while creating a lock");
        }
        log.info("Method:setLock, Result=success, uniqueId={},tenantId={}", lockDetails.getUniqueId(), lockDetails.getTenantId());
        return lockedResponse;
    }

    @Override
    public Boolean isLocked(RequestInfo requestInfo, String uniqueId, String tenantId) {
        log.info("method:isLocked, result=inProgress , uniqueId={}, tenantId={}", uniqueId, tenantId);

        Lock lock = getLock(uniqueId, tenantId);
        if (lock != null) {
            String userId = lock.getUserId();
            String uuid = requestInfo.getUserInfo().getUuid();
            Boolean result = !uuid.equalsIgnoreCase(userId);
            log.info("method:isLocked, result=success, isLocked:{}",result);
            return result;
        }
        log.info("method:isLocked, result=success");
        return false;
    }


    @Override
    public Boolean releaseLock(RequestInfo requestInfo, String uniqueId, String tenantId) {
        log.info("method:releaseLock, result=inProgress , uniqueId={}, tenantId={}", uniqueId, tenantId);

        Optional<Lock> existingLock = repository.getLockByUniqueIdAndTenantIdAndIsLocked(uniqueId, tenantId, true);
        if (existingLock.isEmpty()) {

            log.info("method:releaseLock, result=success ,lock does not exist return true");
            return true;

        } else {

            Lock lock = existingLock.get();
            log.info("method:releaseLock, result=inProgress ,lock exist with lockId={}", lock.getId());

            // userId to validate , judge and other employee might not have individual id
            String uuid = requestInfo.getUserInfo().getUuid();
            String userId = lock.getUserId();
            if (!uuid.equalsIgnoreCase(userId)) {

                log.error("method:releaseLock, result=error ,lockId={}", lock.getId());
                throw new CustomException("UNAUTHORIZED", "You are not allowed to release this lock.");
            }
            // releasing the lock
            repository.delete(lock);
            log.info("method:releaseLock, result=success ,successfully released the lock");

            return true;

        }

    }

    private Lock getLock(String uniqueId, String tenantId) {
        log.info("method:getLock, result=InProgress, uniqueId={},tenantId={}", uniqueId, tenantId);

        Optional<Lock> lock = repository.getLockByUniqueIdAndTenantIdAndIsLocked(uniqueId, tenantId, true);

        if (lock.isPresent()) {

            Lock existingLock = lock.get();
            log.info("method:getLock, result=InProgress, lockId={}", existingLock.getId());

            Long expiryTime = existingLock.getLockReleaseTime();
            Long currentTime = System.currentTimeMillis();

            if (expiryTime < currentTime) {
                log.info("method:getLock, result=InProgress, lock is expired, removing lock with lockId={}", existingLock.getId());

                // Lock is expired, delete it
                repository.delete(existingLock);
                log.info("method:getLock, result=success");

                return null;
            } else {
                // Lock is still valid, return lock details
                log.info("method:getLock, result=success, return lock with id={}", lock.get().getId());
                return lock.get();
            }
        }

        return null;
    }


    private AuditDetails getCreateAuditDetails(RequestInfo requestInfo) {
        return AuditDetails.builder()
                .createdBy(requestInfo.getUserInfo().getUuid())
                .createdTime(System.currentTimeMillis())
                .lastModifiedBy(requestInfo.getUserInfo().getUuid())
                .lastModifiedTime(System.currentTimeMillis()).build();
    }

    private AuditDetails getUpdateAuditDetails(RequestInfo requestInfo, AuditDetails auditDetails) {
        auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUuid());
        auditDetails.setLastModifiedTime(System.currentTimeMillis());
        return auditDetails;
    }
}
