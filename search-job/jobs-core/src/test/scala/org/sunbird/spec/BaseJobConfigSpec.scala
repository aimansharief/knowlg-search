package org.sunbird.spec

import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.ProducerConfig
import org.scalatest.{FlatSpec, Matchers}
import org.sunbird.job.BaseJobConfig

import java.util.Properties

class BaseJobConfigSpec extends FlatSpec with Matchers {

  val config = ConfigFactory.load("base-test.conf")
  val jobName = "Test-job"
  val baseJobConfig = new BaseJobConfig(config, jobName)

  "kafkaConsumerProperties" should "return properties with correct values" in {
    val properties: Properties = baseJobConfig.kafkaConsumerProperties
    properties.getProperty("bootstrap.servers") shouldBe config.getString("kafka.broker-servers")
    properties.getProperty("group.id") shouldBe config.getString("kafka.groupId")
    properties.getProperty("isolation.level") shouldBe "read_committed"
    properties.getProperty("auto.offset.reset") shouldBe config.getString("kafka.auto.offset.reset")
  }

  "kafkaProducerProperties" should "return properties with correct values" in {
    val properties: Properties = baseJobConfig.kafkaProducerProperties
    properties.getOrDefault("bootstrap.servers","") shouldBe config.getString("kafka.broker-servers")
    properties.getOrDefault("linger.ms","") shouldBe 10
    properties.getOrDefault("batch.size","") shouldBe 65536
    properties.getOrDefault("compression.type","") shouldBe "snappy"
  }

  "getString" should "return value from config or default" in {
    val key = "kafka.groupId"
    val defaultValue = "default-value"
    val expectedValue = config.getString(key)
    baseJobConfig.getString(key, defaultValue) shouldBe expectedValue
    baseJobConfig.getString("nonexistent.key", defaultValue) shouldBe defaultValue
  }

  "getInt" should "return value from config or default" in {
    val key = "redis.port"
    val defaultValue = 123
    val expectedValue = config.getInt(key)
    baseJobConfig.getInt(key, defaultValue) shouldBe expectedValue
    baseJobConfig.getInt("nonexistent.key", defaultValue) shouldBe defaultValue
  }

  "getBoolean" should "return value from config or default" in {
    val key = "task.checkpointing.compressed"
    val defaultValue = false
    val expectedValue = config.getBoolean(key)
    baseJobConfig.getBoolean(key, defaultValue) shouldBe expectedValue
    baseJobConfig.getBoolean("nonexistent.key", defaultValue) shouldBe defaultValue
  }
}
