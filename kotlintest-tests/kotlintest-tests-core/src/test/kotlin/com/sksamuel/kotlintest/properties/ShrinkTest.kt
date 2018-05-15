package com.sksamuel.kotlintest.properties

import io.kotlintest.matchers.numerics.shouldBeLessThan
import io.kotlintest.matchers.string.shouldHaveLength
import io.kotlintest.properties.Gen
import io.kotlintest.properties.Shrinker
import io.kotlintest.properties.StringShrinker
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowAny
import io.kotlintest.specs.StringSpec

class ShrinkTest : StringSpec({

  "should report shrinked values for arity 1 ints" {
    shouldThrowAny {
      assertAll(Gen.int()) {
        it.shouldBeLessThan(5)
      }
    }.message shouldBe "Property failed for\n0: 5 (shrunk from 2147483647)\nafter 2 attempts"
  }

  "should shrink arity 2 strings" {
    shouldThrowAny {
      assertAll(Gen.string(), Gen.string()) { a, b ->
        (a.length + b.length).shouldBeLessThan(4)
      }
    }.message shouldBe
        "Property failed for\n0: <empty string>\n1: aaaa (shrunk from \nabc\n123\n)\nafter 3 attempts"
  }
//
//  "should report shrinked values for arity 2 positiveIntegers" {
//    shouldThrowAny {
//      assertAll(Gen.positiveIntegers(), Gen.positiveIntegers()) { a, b ->
//        Math.abs(a + b) shouldBe lt(4)
//      }
//    }.message shouldBe "Property failed for\n0: 1\n1: 3\nafter 2 attempts"
//  }
//
//  "should report shrinked values for arity 3 positiveIntegers" {
//    shouldThrowAny {
//      assertAll(Gen.positiveIntegers(), Gen.positiveIntegers(), Gen.positiveIntegers()) { a, b, c ->
//        a.toLong() + b.toLong() + c.toLong() shouldBe 4L
//      }
//    }.message shouldBe "Property failed for\n0: 1\n1: 1\n2: 1\nafter 1 attempts"
//  }
//
//  "should report shrinked values for arity 4 negativeIntegers" {
//    shouldThrowAny {
//      assertAll(Gen.negativeIntegers(), Gen.negativeIntegers(), Gen.negativeIntegers(), Gen.negativeIntegers()) { a, b, c, d ->
//        a + b + c + d shouldBe 4
//      }
//    }.message shouldBe "Property failed for\n0: -1\n1: -1\n2: -1\n3: -1\nafter 1 attempts"
//  }
//
//  "should report shrinked values for arity 1 doubles" {
//    shouldThrowAny {
//      assertAll(Gen.double()) { a ->
//        a shouldBe lt(3.0)
//      }
//    }.message shouldBe "Property failed for\n0: 3.999999999999999\nafter 2 attempts"
//  }
//
//  "should report shrinked values for choose" {
//    shouldThrowAny {
//      assertAll(Gen.choose(5, 15)) { a ->
//        a shouldBe lte(7)
//      }
//    }.message!! should startWith("Property failed for\n0: 8\nafter")
//  }

  "should shrink strings to empty string"{
    val gen = object : Gen<String> {
      override fun random(): Sequence<String> = generateSequence { "asjfiojoqiwehuoahsuidhqweqwe" }
      override fun constants(): Iterable<String> = emptyList()
      override fun shrinker(): Shrinker<String>? = StringShrinker
    }
    shouldThrowAny {
      assertAll(gen) { a ->
        a.shouldHaveLength(10)
      }
    }.message shouldBe "Property failed for\n0: <empty string> (shrunk from asjfiojoqiwehuoahsuidhqweqwe)\nafter 1 attempts"
  }

  "should shrink strings to min failing size"{
    val gen = object : Gen<String> {
      override fun random(): Sequence<String> = generateSequence { "asjfiojoqiwehuoahsuidhqweqwe" }
      override fun constants(): Iterable<String> = emptyList()
      override fun shrinker(): Shrinker<String>? = StringShrinker
    }
    shouldThrowAny {
      assertAll(gen) { a ->
        a.padEnd(10, '*').shouldHaveLength(10)
      }
    }.message shouldBe "Property failed for\n0: aaaaaaaaaaa (shrunk from asjfiojoqiwehuoahsuidhqweqwe)\nafter 1 attempts"
  }
})