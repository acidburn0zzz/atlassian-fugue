package com.atlassian.fage;

import java.util.NoSuchElementException;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestEither_TestRight
{
    private static final Integer ORIGINAL_VALUE = 1;
    final Either<Boolean, Integer> either = Either.right(ORIGINAL_VALUE);
    
    @Test
    public void testRight()
    {
        assertEquals(ORIGINAL_VALUE, either.right().get());
    }

    @Test
    public void testLeft()
    {
        assertFalse(either.left().isSet());
    }
    
    @Test
    public void testIsRight()
    {
        assertTrue(either.isRight());
    }
    
    @Test
    public void testIsLeft()
    {
        assertFalse(either.isLeft());
    }
    
    @Test
    public void testSwap()
    {
        Either<Integer, Boolean> swapped = either.swap();
        assertTrue(swapped.isLeft());
        assertEquals(either.right().get(), swapped.left().get());
        assertEquals(ORIGINAL_VALUE, swapped.left().get());
    }
    
    @Test
    public void testMap()
    {
        String actual = either.fold(UtilityFunctions.bool2String, UtilityFunctions.int2String);
        assertEquals(String.valueOf(ORIGINAL_VALUE), actual);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testMapLeft()
    {
        either.mapLeft(UtilityFunctions.bool2String);
    }
    
    @Test
    public void testMapRight()
    {
        String actual = either.mapRight(UtilityFunctions.int2String);
        assertEquals(String.valueOf(ORIGINAL_VALUE), actual);
    }
}
