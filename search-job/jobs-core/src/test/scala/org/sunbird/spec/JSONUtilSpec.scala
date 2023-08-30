package org.sunbird.spec
import com.fasterxml.jackson.databind.JsonMappingException
import org.sunbird.job.util.JSONUtil
import org.apache.commons.lang3.StringUtils
import org.scalatest.{FlatSpec, Matchers}
class JSONUtilSpec extends FlatSpec with Matchers {


  "serializing a valid Map object" should "Should serialize the object" in {
    val value: String = JSONUtil.serialize(Map("identifier" -> "do_1234", "status" -> "Draft"))
    assert(StringUtils.equalsIgnoreCase(value, "{\"identifier\":\"do_1234\",\"status\":\"Draft\"}"))
  }

  "serializing a valid List object" should "Should serialize the object" in {
    val value: String = JSONUtil.serialize(List("identifier", "do_1234", "status", "Draft"))
    assert(StringUtils.equalsIgnoreCase(value, "[\"identifier\",\"do_1234\",\"status\",\"Draft\"]"))
  }

  "deserializing a stringified map" should "Should deserialize the string to map" in {
    val value: Map[String, AnyRef] = JSONUtil.deserialize[Map[String, AnyRef]]("{\"identifier\":\"do_1234\",\"status\":\"Draft\"}")
    assert(value != null)
    assert(value.getOrElse("status", "").asInstanceOf[String] == "Draft")
  }

  "deserializing a stringified list to map" should "Should throw Exception" in {
    assertThrows[JsonMappingException] {
      JSONUtil.deserialize[Map[String, AnyRef]]("[\"identifier\",\"do_1234\",\"status\",\"Draft\"]")
    }
  }

  "deserializing a stringified list" should "Should deserialize the string to list" in {
    val value: List[String] = JSONUtil.deserialize[List[String]]("[\"identifier\",\"do_1234\",\"status\",\"Draft\"]")
    assert(value != null)
    assert(value.size == 4)
  }

}
