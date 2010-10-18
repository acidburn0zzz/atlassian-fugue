package com.atlassian.fage.functions;
import com.google.common.base.Function;
import com.google.common.base.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestFunctions
{
    @Mock private Supplier<String> supplier;
    @Mock private Function<String, Integer> function;
    @Mock private ExceptionAction exceptionAction; 
    @Mock private RuntimeException runtimeException;
    public static final int ATTEMPTS = 4;

    @Before
    public void setUp()
    {
        initMocks(this);
    }

    @Test
    public void testBasicSupplierAttempt()
    {
        String expected = "result";
        when(supplier.get()).thenReturn(expected);
        String result = Functions.attempt(supplier, ATTEMPTS);

        verify(supplier).get();
        assertEquals(expected, result);
    }
    
    @Test
    public void testBasicSupplierAttemptRetry()
    {
        when(supplier.get()).thenThrow(runtimeException);
        
        try
        {
            Functions.attempt(supplier, ATTEMPTS);
            fail("Expected a exception.");
        }
        catch(RuntimeException e)
        {
            assertEquals(runtimeException, e);
        }
        
        verify(supplier, times(ATTEMPTS)).get();
    }
    
    @Test
    public void testSupplierAttempt()
    {
        String expected = "result";
        when(supplier.get()).thenReturn(expected);
        String result = Functions.attempt(supplier, ATTEMPTS, exceptionAction);
        
        verify(supplier).get();
        assertEquals(expected, result);
        verifyZeroInteractions(exceptionAction);
    }
    
    @Test
    public void testSupplierAttemptRetry()
    {
        when(supplier.get()).thenThrow(runtimeException);
        
        try
        {
            Functions.attempt(supplier, ATTEMPTS, exceptionAction);
            fail("Expected a exception.");
        }
        catch(RuntimeException e)
        {
            assertEquals(runtimeException, e);
        }

        verify(supplier, times(ATTEMPTS)).get();
        verify(exceptionAction, times(ATTEMPTS)).act(runtimeException);
    }
    
    @Test 
    public void testSupplierAttemptEarlyExit()
    {
        final String expected = "success";
        when(supplier.get()).thenThrow(new RuntimeException("First attempt")).thenReturn(expected).thenThrow(new RuntimeException("Third attempt")).thenThrow(new RuntimeException("Fourth attempt"));
        
        String result = Functions.attempt(supplier, ATTEMPTS);
        assertEquals(expected, result);
        verify(supplier, times(2)).get();
        verifyNoMoreInteractions(supplier);
    }
    
    @Test
    public void testBasicFunctionAttempt()
    {
        Integer expected = 1;
        when(function.apply("1")).thenReturn(expected);
        Integer result = Functions.attempt(function, "1", ATTEMPTS);

        verify(function).apply("1");
        assertEquals(expected, result);
    }
    
    @Test
    public void testBasicFunctionAttemptRetry()
    {
        when(function.apply(anyString())).thenThrow(runtimeException);
        
        try
        {
            Functions.attempt(function, "application", ATTEMPTS);
            fail("Expected a exception.");
        }
        catch(RuntimeException e)
        {
            assertEquals(runtimeException, e);
        }
        
        verify(function, times(ATTEMPTS)).apply("application");
    }
    
    @Test
    public void testFunctionAttemptRetry()
    {
        when(function.apply("application")).thenThrow(runtimeException);
        
        try
        {
            Functions.attempt(function, "application", ATTEMPTS, exceptionAction);
            fail("Expected a exception.");
        }
        catch(RuntimeException e)
        {
            assertEquals(runtimeException, e);
        }

        verify(function, times(ATTEMPTS)).apply("application");
        verify(exceptionAction, times(ATTEMPTS)).act(runtimeException);
    }
}
