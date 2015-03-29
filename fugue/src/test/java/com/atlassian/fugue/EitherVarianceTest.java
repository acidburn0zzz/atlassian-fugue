/*
   Copyright 2011 Atlassian

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.atlassian.fugue;

import org.junit.Test;

import java.util.function.Predicate;

import static com.atlassian.fugue.Either.left;
import static com.atlassian.fugue.Either.right;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class EitherVarianceTest {
  private final Either<String, Integer> l = left("heyaa!");
  private final Either<String, Integer> r = right(12);

  @Test public void filterLeft() {
    Option<Either<String, Object>> filtered = l.left().filter(x -> true);
    assertThat(filtered.isDefined(), is(true));
    assertThat(filtered.get().left().isDefined(), is(true));
  }

  @Test public void filterRight() {
    Option<Either<Object, Integer>> filtered = r.right().filter(x -> true);
    assertThat(filtered.isDefined(), is(true));
    assertThat(filtered.get().right().isDefined(), is(true));
  }

  @Test public void forAll() {
    Predicate<Number> alwaysTrue = x -> true;
    assertThat(r.right().forall(alwaysTrue), equalTo(true));
  }

  @Test public void exist() {
    Predicate<CharSequence> alwaysTrue = x -> true;
    assertThat(l.left().exists(alwaysTrue), equalTo(true));
  }

  @Test public void forEach() {
    Count<Number> e = new Count<>();
    r.right().foreach(e);
    assertThat(e.count(), equalTo(1));
  }
}
