package io.atlassian.fugue.extras;

import java.util.function.Function;
import java.util.function.Predicate;

public class Functions {
  public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
    return t -> predicate.test(t);
  }
}
