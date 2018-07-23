package io.atlassian.fugue.extras;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Substitutions for methods provided by google guava that uses
 * java.util.function types instead of guava types,
 */
public class Predicates {

  public static <T> Predicate<T> equalTo(final T x) {
    return y -> Objects.equals(x, y);
  }

  public static <T> Predicate<T> in(Collection<? extends T> target) {
    return t -> target.contains(t);
  }

  @SafeVarargs public static <T> Predicate<T> and(final Predicate<T> first, final Predicate<T>... many) {
    return foldLeft(Arrays.asList(many).iterator(), first, (lhs, rhs) -> ((t) -> lhs.test(t) && rhs.test(t)));
  }

  public static <T> Predicate<T> or(final Predicate<? super T> first, final Predicate<? super T> second) {
    return (t) -> first.test(t) || second.test(t);
  }

  public static <T> Predicate<T> not(final Predicate<T> predicate) {
    return (t) -> !predicate.test(t);
  }

  public static <T> Predicate<T> notNull() {
    return (t) -> t != null;
  }

  public static <T> boolean any(final Iterable<T> iterable, final Predicate<? super T> predicate) {
    return foldLeft(iterable.iterator(), false, (x, y) -> x || predicate.test(y));
  }

  public static <T> boolean all(final Iterable<T> iterable, final Predicate<? super T> predicate) {
    return foldLeft(iterable.iterator(), true, (x, y) -> x && predicate.test(y));
  }

  public static <A, B> Predicate<A> compose(final Predicate<B> predicate, final Function<A, ? extends B> function) {
    return t -> predicate.test(function.apply(t));
  }

  /**
   * fold by recursion, takes O(1) time in the best case takes O(N) memory in
   * the worst case
   */
  public static <S, T> S foldRight(final Iterator<T> iter, final S zero, final BiFunction<T, Supplier<S>, S> f) {
    if (iter.hasNext()) {
      T t = iter.next();
      return f.apply(t, () -> foldRight(iter, zero, f));
    } else {
      return zero;
    }
  }

  /**
   * fold by accumulation, takes O(N) time in the best case takes O(1) memory in
   * the worst case
   */
  public static <S, T> S foldLeft(final Iterator<T> iter, final S zero, final BiFunction<S, T, S> f) {
    S ret = zero;
    while (iter.hasNext()) {
      ret = f.apply(ret, iter.next());
    }
    return ret;
  }

}
