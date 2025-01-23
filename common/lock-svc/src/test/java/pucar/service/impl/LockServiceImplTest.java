package pucar.service.impl;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pucar.config.Configuration;
import pucar.repository.LockRepository;
import pucar.util.IndividualUtil;
import pucar.web.models.Lock;
import pucar.web.models.AuditDetails;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LockServiceImplTest {

    @Mock
    private LockRepository repository;

    @Mock
    private Configuration configuration;

    @Mock
    private IndividualUtil individualUtil;

    @InjectMocks
    private LockServiceImpl lockService;

    private RequestInfo requestInfo;
    private Lock lock;

    @BeforeEach
    void setUp() {
        requestInfo = new RequestInfo();
        requestInfo.setUserInfo(new org.egov.common.contract.request.User());
        requestInfo.getUserInfo().setUuid("test-user");

        lock = new Lock();
        lock.setId(UUID.randomUUID().toString());
        lock.setUniqueId("12345");
        lock.setTenantId("tenant1");
        lock.setUserId("test-user");
        lock.setIsLocked(true);
        lock.setLockReleaseTime(System.currentTimeMillis() + 10000);
        lock.setAuditDetails(new AuditDetails());
    }

    @Test
    void testSetLock_Success() {
        when(repository.getLockByUniqueIdAndTenantIdAndIsLocked(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.empty());
        when(individualUtil.getIndividualId(any())).thenReturn("individualId");
        when(repository.save(any())).thenReturn(lock);

        Lock result = lockService.setLock(requestInfo, lock);
        assertNotNull(result);
        assertEquals("test-user", result.getUserId());
    }

    @Test
    void testSetLock_AlreadyLockedByAnotherUser() {
        Lock existingLock = new Lock();
        existingLock.setUserId("another-user");
        existingLock.setLockReleaseTime(System.currentTimeMillis()+120000);
        when(repository.getLockByUniqueIdAndTenantIdAndIsLocked(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.of(existingLock));

        CustomException exception = assertThrows(CustomException.class, () -> lockService.setLock(requestInfo, lock));
        assertEquals("SET_LOCK_EXCEPTION", exception.getCode());
    }

    @Test
    void testIsLocked_ReturnsTrueWhenLockedByAnotherUser() {
        Lock existingLock = new Lock();
        existingLock.setUserId("another-user");
        existingLock.setLockReleaseTime(System.currentTimeMillis()+120000);
        when(repository.getLockByUniqueIdAndTenantIdAndIsLocked(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.of(existingLock));

        boolean isLocked = lockService.isLocked(requestInfo, "12345", "tenant1");
        assertTrue(isLocked);
    }

    @Test
    void testIsLocked_ReturnsFalseWhenNoLockExists() {
        when(repository.getLockByUniqueIdAndTenantIdAndIsLocked(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.empty());

        boolean isLocked = lockService.isLocked(requestInfo, "12345", "tenant1");
        assertFalse(isLocked);
    }

    @Test
    void testReleaseLock_Success() {
        when(repository.getLockByUniqueIdAndTenantIdAndIsLocked(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.of(lock));

        boolean result = lockService.releaseLock(requestInfo, "12345", "tenant1");
        assertTrue(result);
        verify(repository, times(1)).delete(lock);
    }

    @Test
    void testReleaseLock_UnauthorizedUser() {
        Lock existingLock = new Lock();
        existingLock.setUserId("another-user");
        when(repository.getLockByUniqueIdAndTenantIdAndIsLocked(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.of(existingLock));

        CustomException exception = assertThrows(CustomException.class, () -> lockService.releaseLock(requestInfo, "12345", "tenant1"));
        assertEquals("UNAUTHORIZED", exception.getCode());
    }

}
