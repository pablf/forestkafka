import cats.effect.{IO, ExitCode, IOApp}
import Kafka._
import DB.DB


// Main object starts server with Kafka consumers.
object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = for {
    // Definition of database and Kafka consumers and producers.
    imageProcessor <- IO(new ImageProcessor)
    feederConsumer <- IO(new FeederConsumer)

    db <- IO(new DB)
    consumer <- IO(new Consumer(db))

    _ <- IO(imageProcessor.start(true)).start
    _ <- IO(feederConsumer.start(true)).start
    _ <- IO(consumer.start(true)).start
  } yield ExitCode.Success

}