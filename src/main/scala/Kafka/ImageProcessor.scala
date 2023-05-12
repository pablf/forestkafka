package Kafka

import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.header.internals.RecordHeader

import java.util.Properties
import scala.jdk.CollectionConverters.SeqHasAsJava

/*
 * ImageProcessor is made of a Kafka Consumer and a Kafka Producer.
 * The Kafka Consumer consumes images from a Forest sent from a sensor,
 * processes the images and send the information to a Topic "Data".
 */
class ImageProcessor extends App {

  // Create Consumer.
  val propConsumer: Properties = new Properties()
  propConsumer.put("bootstrap.servers", "localhost:9092")
  propConsumer.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  propConsumer.put("value.deserializer", "org.apache.kafka.common.serialization.BiteArrayDeserializer")
  propConsumer.put("group.id", "ImageConsumer")

  val consumer: KafkaConsumer[String, Array[Byte]] = new KafkaConsumer(propConsumer)
  val topicsConsumer: List[String] = List("Images")

  consumer.subscribe(topicsConsumer.asJava)

  // Create Producer.
  val propProducer: Properties = new Properties()
  propProducer.put("bootstrap.servers", "localhost:9092")
  propProducer.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  propProducer.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  propProducer.put("group.id", "DataProducer")

  val producer: KafkaProducer[String, String] = new KafkaProducer(propProducer)
  val topicsProducer: List[String] = List("Data")

  // Consumes, processes and produces.
  def start(expr: => Boolean = true): Unit =
    while (expr) {
      val records: ConsumerRecords[String, Array[Byte]] = consumer.poll(100)
      records.forEach(
        record => topicsProducer.foreach(
          topic => {
            val processed = processImage(record.value())
            producer.send(
              new ProducerRecord[String, String](
                topic,
                null,
                record.key(), 
                processed._1, 
                record.headers().add(new RecordHeader("Kind", processed._2.getBytes())))
            )
          }
        )
      )
    }

  // In a real application, processImage would be implemented using Deep Learning to extract information from the image.
  def processImage(image: Array[Byte]): (String, String) =
    ("", "animals")
}
