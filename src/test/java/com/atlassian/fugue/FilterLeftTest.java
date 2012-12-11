package com.atlassian.fugue;

import static com.atlassian.fugue.Eithers.filterLeft;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class FilterLeftTest {
  @Test public void testThatRightOnlyFiltersLeftToEmpty() {
    final List<Either<Integer, String>> it = ImmutableList.of(Either.<Integer, String> right("one"), Either.<Integer, String> right("three"),
      Either.<Integer, String> right("2"));

    assertFalse(filterLeft(it).iterator().hasNext());
  }

  @Test public void testThatLeftOnlyFiltersLeftToSameContents() {
    final List<Either<Integer, String>> it = ImmutableList.of(Either.<Integer, String> left(1), Either.<Integer, String> left(333),
      Either.<Integer, String> left(22));

    final Iterator<Integer> lefts = filterLeft(it).iterator();
    for (Either<Integer, String> i : it) {
      // calling get on a Maybe is ill advised, but this is a test - if get
      // returns null it's a sign that something
      // bigger has gone wrong
      assertEquals(i.left().get(), lefts.next());
    }
  }

  @Test public void testThatMixedEithersFiltersLeftToExpectedContents() {
    final List<Either<Integer, String>> it = ImmutableList.of(Either.<Integer, String> left(1), Either.<Integer, String> right("three"),
      Either.<Integer, String> right("fore"), Either.<Integer, String> left(22));

    final Iterator<Integer> iterator = filterLeft(it).iterator();
    assertEquals(new Integer(1), iterator.next());
    assertEquals(new Integer(22), iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test public void testThatEmptyIterableFiltersLeftToEmptyIterable() {
    final List<Either<Integer, String>> it = Collections.emptyList();
    assertFalse(filterLeft(it).iterator().hasNext());
  }
}