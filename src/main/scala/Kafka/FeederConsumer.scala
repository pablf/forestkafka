package Kafka

import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}

import java.util.Properties
import scala.jdk.CollectionConverters.SeqHasAsJava

// FeederConsumer consumes data about the status of Feeders in a Forest (topic "Feeders") and raise an alert.
class FeederConsumer {

  // Food quantity must be between 0 and 100.
  // Different quantities may trigger alerts.
  val MustReplenish = 30
  val ScarceFood = 10

  // Create Consumer.
  val prop: Properties = new Properties()
  prop.put("bootstrap.servers", "localhost:9092")
  prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  prop.put("value.serializer", "org.apache.kafka.common.serialization.IntSerializer")
  prop.put("group.id", "FeederConsumer")

  val consumer: KafkaConsumer[String, Int] = new KafkaConsumer(prop)
  val topics: List[String] = List("Feeders")

  consumer.subscribe(topics.asJava)



  // Consumes the state of the feeders.
  def start(expr: => Boolean = true): Unit =
    while (expr) {
      val records: ConsumerRecords[String, Int] = consumer.poll(100)
      records.forEach(
        record => record.value() match {
          case x if x < ScarceFood => triggerAlert("ScarceFood")
          case x if x < MustReplenish => triggerAlert("MustReplenish")
          case _ =>
        }
      )
    }

  def triggerAlert(message: String): Unit = {
    // This must be used to alert someone to replenish feeders.
  }

}
