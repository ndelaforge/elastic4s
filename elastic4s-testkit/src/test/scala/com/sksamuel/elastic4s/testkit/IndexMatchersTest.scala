package com.sksamuel.elastic4s.testkit

import com.sksamuel.elastic4s.ElasticDsl._
import org.scalatest.WordSpec

class IndexMatchersTest extends WordSpec with IndexMatchers with ElasticSugar {

  val indexname = getClass.getSimpleName.toLowerCase

  client.execute {
    bulk(
      index into indexname / "tubestops" fields("name" -> "south kensington", "line" -> "district"),
      index into indexname / "tubestops" fields("name" -> "earls court", "line" -> "district", "zone" -> 2),
      index into indexname / "tubestops" fields("name" -> "cockfosters", "line" -> "picadilly") id 3,
      index into indexname / "tubestops" fields("name" -> "bank", "line" -> "northern")
    )
  }.await

  blockUntilCount(4, indexname)

  "index matchers" should {
    "match on index document count" in {
      indexname should haveCount(4)
    }
    "support doc exists" in {
      indexname should containDoc(3)
    }
  }
}
