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

import static com.atlassian.fugue.Either.left;
import static com.atlassian.fugue.Either.right;

import java.lang.reflect.Constructor;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import javax.annotation.Nullable;

public class UtilityFunctions {
  public static final Predicate<Integer> isEven = dividableBy(2);

  public static final Function2<Integer, Integer, Integer> sum = new Function2<Integer, Integer, Integer>() {
    @Override public Integer apply(final Integer a, final Integer b) {
      return a + b;
    }
  };

  public static final Function2<Integer, Integer, Integer> subtract = new Function2<Integer, Integer, Integer>() {
    @Override public Integer apply(final Integer a, final Integer b) {
      return a - b;
    }
  };

  public static final Function2<Integer, Integer, Integer> product = new Function2<Integer, Integer, Integer>() {
    @Override public Integer apply(final Integer a, final Integer b) {
      return a * b;
    }
  };

  public static final Predicate<Integer> dividableBy(final int div) {
    return new Predicate<Integer>() {
      @Override public boolean apply(Integer input) {
        return input % div == 0;
      }
    };
  }

  public static Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
    public Integer apply(final Integer integer) {
      return integer + 1;
    }
  };

  public static Function<Integer, Integer> square = new Function<Integer, Integer>() {
    @Override public Integer apply(Integer input) {
      return input * input;
    }
  };

  public static Function<Boolean, String> bool2String = new Function<Boolean, String>() {
    public String apply(final Boolean b) {
      return String.valueOf(b);
    }
  };
  public static Function<Integer, String> int2String = new Function<Integer, String>() {
    public String apply(final Integer i) {
      return String.valueOf(i);
    }
  };

  public static Function<String, String> reverse = new Function<String, String>() {
    public String apply(final String from) {
      return new StringBuilder(from).reverse().toString();
    }
  };

  public static Function2<String, Integer, Option<Character>> charAt = new Function2<String, Integer, Option<Character>>() {
    @Override public Option<Character> apply(String s, Integer i) {
      return s != null && i != null && i >= 0 && i < s.length() ? Option.some(s.charAt(i)) : Option.<Character> none();
    }
  };

  public static Function<Pair<String, Integer>, Option<String>> leftOfString = new Function<Pair<String, Integer>, Option<String>>() {
    @Override public Option<String> apply(@Nullable Pair<String, Integer> pair) {
      return pair != null && pair.left() != null && pair.right() != null && pair.right() >= 0 && pair.right() <= pair.left().length() ? Option
        .some(pair.left().substring(0, pair.right())) : Option.<String> none();
    }
  };

  public static Function<String, Function<Integer, Boolean>> hasMinLength = new Function<String, Function<Integer, Boolean>>() {
    @Override public Function<Integer, Boolean> apply(@Nullable final String text) {
      return new Function<Integer, Boolean>() {
        @Override public Boolean apply(@Nullable Integer min) {
          return (text == null ? "" : text).length() >= (min == null ? 0 : min);
        }
      };
    }
  };

  public static Function<Object, String> toStringFunction() {
    return com.google.common.base.Functions.toStringFunction();
  }

  static <A> Function<Class<A>, Either<Exception, A>> defaultCtor() {
    return new Function<Class<A>, Either<Exception, A>>() {
      @Override public Either<Exception, A> apply(final Class<A> klass) {
        try {
          final Constructor<A> declaredConstructor = klass.getDeclaredConstructor();
          declaredConstructor.setAccessible(true);
          return right(declaredConstructor.newInstance());
        } catch (final Exception e) {
          return left(e);
        }
      }
    };
  }

}