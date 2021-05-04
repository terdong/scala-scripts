/*
 * © 2021 DongHee Kim <terdong@gmail.com>
 */

package com.teamgehem.scala_scripts.wii

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class WiiFileNameChangerSpec extends AnyFlatSpec with Matchers{
  val idRegex = "\\[(.*?)\\]".r.anchored
  val sampleString = "[RUUK01]\nOriginal title = Animal Crossing Wii\nSize = 322961408\nMD5 hash = 881FBB24FAA3E0C3B564E2F5E51F9B70\nRegion = KOR\nFile type = WBFS\nPartition code = G\nIOS version = 36\n\n[PDUE01]\nOriginal title = Another SUPER MARIO BROS\nSize = 568328192\nMD5 hash = 9EE7BF907EDF3147E34F027D5D89FE41\nRegion = NTSC\nFile type = WBFS\nPartition code = UG\nIOS version = 53"
  "a regex" should "find specific word in sampleString" in {

    /*val result = Using(Resource.getAsStream("disc.info")){ is: InputStream =>
      is.lines.filter(regex.findFirstIn(_).isDefined).foreach(println)
    }*/
    idRegex.findAllIn(sampleString).toSeq shouldBe Seq("[RUUK01]","[PDUE01]")
  }

  it should "parse the strings by key and value" in {

    val expectedResult = Map(("RUUK01" -> "Animal_Crossing_Wii"), ("PDUE01" -> "Another_SUPER_MARIO_BROS"))

    sampleString.split('\n').filter(_.nonEmpty).foldLeft(("", Map.empty[String, String])){ (pair, value) =>
      val prevKey = pair._1
      val map = pair._2
      value match {
        case idRegex(id) => (id, map)
        case v if v.contains("Original title") =>{
          (prevKey, map + (prevKey -> v.split('=').last.substring(1).replace(' ', '_')))
        }
        case _ => pair
      }
    }._2 shouldBe expectedResult
  }
}
