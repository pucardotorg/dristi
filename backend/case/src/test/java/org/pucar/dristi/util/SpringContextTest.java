package org.pucar.dristi.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpringContextTest {

    @Mock
    private ApplicationContext mockApplicationContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SpringContext.clearApplicationContext();
    }

    @Test
    public void testSetApplicationContext_NullContext_ThrowsException() {
        SpringContext springContext = new SpringContext();  // Create an instance
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            springContext.setApplicationContext(null);  // Try to set a null ApplicationContext
        });

        String expectedMessage = "ApplicationContext must not be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetApplicationContext_NotSet_ThrowsException() {
        // Create a new instance without setting the ApplicationContext
        SpringContext springContext = new SpringContext();

        Exception exception = assertThrows(IllegalStateException.class, SpringContext::getApplicationContext);

        String expectedMessage = "ApplicationContext has not been set";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetApplicationContext_ValidContext_ReturnsContext() {
        SpringContext springContext = new SpringContext();  // Create an instance
        springContext.setApplicationContext(mockApplicationContext);  // Manually set ApplicationContext
        ApplicationContext context = SpringContext.getApplicationContext();
        assertNotNull(context);
        assertEquals(mockApplicationContext, context);
    }

    @Test
    public void testGetBean_ValidBean_ReturnsBean() {
        SpringContext springContext = new SpringContext();  // Create an instance
        springContext.setApplicationContext(mockApplicationContext);  // Manually set ApplicationContext
        MyBean mockBean = new MyBean();
        when(mockApplicationContext.getBean(MyBean.class)).thenReturn(mockBean);

        MyBean bean = SpringContext.getBean(MyBean.class);

        assertNotNull(bean);
        assertEquals(mockBean, bean);
        verify(mockApplicationContext).getBean(MyBean.class);
    }

    @Test
    public void testGetBean_ContextNotSet_ThrowsException() {
        // Ensure ApplicationContext is not set
        SpringContext springContext = new SpringContext(); // Create a new instance

        Exception exception = assertThrows(IllegalStateException.class, () -> SpringContext.getBean(MyBean.class));

        String expectedMessage = "ApplicationContext has not been set";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Helper class to simulate a bean
    public static class MyBean {
    }
}
