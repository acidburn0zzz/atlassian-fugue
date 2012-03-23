package com.atlassian.fugue;

import static com.atlassian.fugue.Either.getOrThrow;
import static com.atlassian.fugue.Either.right;
import static com.atlassian.fugue.UtilityFunctions.bool2String;
import static com.atlassian.fugue.UtilityFunctions.int2String;
import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.io.IOException;
import java.util.NoSuchElementException;

public class EitherRightTest {
  private static final Integer ORIGINAL_VALUE = 1;
  final Either<Boolean, Integer> either = right(ORIGINAL_VALUE);

  @Test public void rightGet() {
    assertThat(either.right().get(), is(ORIGINAL_VALUE));
  }

  @Test public void rightIsDefined() {
    assertThat(either.right().isDefined(), is(true));
  }

  @Test public void leftIsDefined() {
    assertThat(either.left().isDefined(), is(false));
  }

  @Test public void isRight() {
    assertThat(either.isRight(), is(true));
  }

  @Test public void isLeft() {
    assertThat(either.isLeft(), is(false));
  }

  @Test public void getRight() {
    assertThat(either.getRight(), is(1));
  }

  @Test(expected = NoSuchElementException.class) public void getLeft() {
    either.getLeft();
  }

  @Test public void swap() {
    final Either<Integer, Boolean> swapped = either.swap();
    assertThat(swapped.isLeft(), is(true));
    assertThat(swapped.left().get(), is(either.right().get()));
    assertThat(swapped.left().get(), is(ORIGINAL_VALUE));
  }

  @Test public void map() {
    assertThat(either.fold(bool2String, int2String), is(valueOf(ORIGINAL_VALUE)));
  }

  @Test public void mapLeft() {
    assertThat(either.left().map(bool2String).left().isEmpty(), is(true));
  }

  @Test public void mapRight() {
    assertThat(either.right().map(int2String).right().get(), is(valueOf(ORIGINAL_VALUE)));
  }

  @Test public void toStringTest() {
    assertThat(either.toString(), is("Either.Right(1)"));
  }

  @Test public void hashCodeTest() {
    assertThat(either.hashCode(), is(ORIGINAL_VALUE.hashCode()));
  }

  @Test public void equalsItself() {
    assertThat(either.equals(either), is(true));
  }

  @Test public void notEqualsNull() {
    assertThat(either.equals(null), is(false));
  }

  @Test public void notThrowsException() throws IOException {
    final Either<IOException, String> either = right("boo yaa!");
    assertThat(getOrThrow(either), is("boo yaa!"));
  }
}