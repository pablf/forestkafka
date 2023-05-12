package Kafka

import DB.DB
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}

import java.util.Properties
import scala.jdk.CollectionConverters.SeqHasAsJava

// It consumes notifications from Topic "Data" and "Feeders" and saves them in a MongoDB database.
class Consumer(db: DB) {

  // Create Consumer.
  val propConsumer: Properties = new Properties()
  propConsumer.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  val consumer: KafkaConsumer[String, String] = new KafkaConsumer(propConsumer)
  val topicsConsumer: List[String] = List("Data", "Feeders")

  consumer.subscribe(topicsConsumer.asJava)

  def start(expr: => Boolean = true): Unit =
    while (expr) {
      val records: ConsumerRecords[String, String] = consumer.poll(100)
      records.forEach(
        record => {
          // header must contain if it is an animal or a human.
          try {
            val header = new String(record.headers().lastHeader("Kind").value())
            db.add(record.value, header)
          } catch {
            case _: Throwable => {
              println("ERROR: Header not found.")
              db.add(record.value, "animals")
            }
          }
        }
      )
    }
}
