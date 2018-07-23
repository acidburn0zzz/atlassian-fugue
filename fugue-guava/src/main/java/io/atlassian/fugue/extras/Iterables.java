package io.atlassian.fugue.extras;

import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.function.Function;

public class Iterables {

  public static <F, T> Iterable<T> transform(final Iterable<F> fromIterable, final Function<? super F, ? extends T> function) {
    return com.google.common.collect.Iterables.transform(fromIterable, (x) -> function.apply(x));
  }

  public static <T> Iterable<T> filter(final Iterable<T> unfiltered, final Predicate<? super T> predicate) {
    return com.google.common.collect.Iterables.filter(unfiltered, (x) -> predicate.test(x));
  }

  public static <T> T find(Iterable<? extends T> iterable, Predicate<? super T> predicate, @Nullable T defaultValue) {
    return com.google.common.collect.Iterators.find(iterable.iterator(), t -> predicate.test(t), defaultValue);
  }

  public static <T> T find(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    T ret = find(iterable, predicate, null);
    if (ret == null) {
      throw new NoSuchElementException();
    }
    return ret;
  }
}
