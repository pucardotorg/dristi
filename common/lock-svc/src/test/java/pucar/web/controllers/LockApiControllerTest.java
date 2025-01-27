package pucar.web.controllers;

import org.egov.common.contract.models.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pucar.service.LockService;
import pucar.web.models.Lock;
import pucar.web.models.LockRequest;
import pucar.web.models.LockResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LockApiControllerTest {

    @Mock
    private LockService lockService;

    @InjectMocks
    private LockApiController lockApiController;

    private RequestInfoWrapper requestInfoWrapper;
    private LockRequest lockRequest;
    private Lock lock;

    @BeforeEach
    void setUp() {
        requestInfoWrapper = new RequestInfoWrapper(new RequestInfo());
        lock = new Lock();
        lock.setUniqueId("12345");
        lock.setTenantId("tenant1");
        lockRequest = new LockRequest(new RequestInfo(), lock);
    }

    @Test
    void testIsLocked_ReturnsTrue() {
        when(lockService.isLocked(any(), anyString(), anyString())).thenReturn(true);

        ResponseEntity<LockResponse> response = lockApiController.isLocked(requestInfoWrapper, "12345", "tenant1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getLock().getIsLocked());
    }

    @Test
    void testSetLock_Success() {
        when(lockService.setLock(any(), any())).thenReturn(lock);

        ResponseEntity<LockResponse> response = lockApiController.setLock(lockRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().getLock());
    }

    @Test
    void testReleaseLock_Success() {
        when(lockService.releaseLock(any(), anyString(), anyString())).thenReturn(true);

        ResponseEntity<LockResponse> response = lockApiController.releaseLock(requestInfoWrapper, "12345", "tenant1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertFalse( response.getBody().getLock().getIsLocked());
    }
}
