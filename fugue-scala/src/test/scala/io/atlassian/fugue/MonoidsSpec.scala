/*
   Copyright 2015 Atlassian

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

package io.atlassian.fugue

import java.math.BigInteger
import java.util.Arrays.asList
import java.util.stream.{ Collectors, StreamSupport }

import io.atlassian.fugue.Either.right
import Monoids._
import io.atlassian.fugue.Option.some
import io.atlassian.fugue.law.MonoidTests

class MonoidsSpec extends TestSuite {

  test("intAddition") {
    intAddition.append(1, 2) shouldEqual 3
    check(MonoidTests(intAddition))
  }

  test("intMultiplication") {
    intMultiplication.append(2, 3) shouldEqual 6
    check(MonoidTests(intMultiplication))
  }

  test("bigintAddition") {
    bigintAddition.append(BigInteger.valueOf(2), BigInteger.valueOf(3)) shouldEqual BigInteger.valueOf(5)
    check(MonoidTests(bigintAddition))
  }

  test("bigintMultiplication") {
    bigintMultiplication.append(BigInteger.valueOf(2), BigInteger.valueOf(3)) shouldEqual BigInteger.valueOf(6)
    check(MonoidTests(bigintMultiplication))
  }

  test("longAddition") {
    longAddition.append(1L, 2L) shouldEqual 3L
    check(MonoidTests(longAddition))
  }

  test("longMultiplication") {
    longMultiplication.append(2L, 3L) shouldEqual 6L
    check(MonoidTests(longMultiplication))
  }

  test("disjunction") {
    disjunction.append(false, true) shouldEqual true
    disjunction.append(true, true) shouldEqual true
    check(MonoidTests(disjunction))
  }

  test("exclusiveDisjunction") {
    exclusiveDisjunction.append(false, true) shouldEqual true
    exclusiveDisjunction.append(true, true) shouldEqual false
    check(MonoidTests(exclusiveDisjunction))
  }

  test("conjunction") {
    conjunction.append(false, true) shouldEqual false
    conjunction.append(true, true) shouldEqual true
    check(MonoidTests(conjunction))
  }

  test("string") {
    string.append("a", "b") shouldEqual "ab"
    check(MonoidTests(string))
  }

  test("unit") {
    check(MonoidTests(unit))
  }

  test("list") {
    list[String]().append(asList("a"), asList("b")) shouldEqual asList("a", "b")
    check(MonoidTests(list[Integer]()))
  }

  test("iterable") {
    StreamSupport.stream(iterable[String]().append(asList("a"), asList("b")).spliterator(), false).collect(Collectors.toList[String]) shouldEqual asList("a", "b")
  }

  test("firstOption") {
    firstOption[String]().append(some("a"), some("b")) shouldEqual some("a")
    check(MonoidTests(firstOption[Integer]()))
  }

  test("lastOption") {
    lastOption[String]().append(some("a"), some("b")) shouldEqual some("b")
    check(MonoidTests(lastOption[Integer]()))
  }

  test("option") {
    option(Semigroups.intMaximum).append(some(1), some(2)) shouldEqual some(2)
    option(Semigroups.intMaximum).append(some(3), some(2)) shouldEqual some(3)
    check(MonoidTests(option(Semigroups.intMaximum)))
  }

  test("either") {
    val m = either(Semigroups.intMaximum, string)

    m.append(right("a"), right("b")) shouldEqual right("ab")
    m.append(Either.left(1), Either.left(2)) shouldEqual Either.left(2)
    check(MonoidTests(m))
  }

}