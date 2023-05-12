# Forest Kafka

Forest Kafka is a template project to make easier the monitorization of forests. Class `Main` calls the classes in package `Kafka`:

* ImageProcessor is a template class made of a Kafka Consumer and a Kafka Producer. The consumer consumes from a topic `Image` an Byte Array representing an image
obtained through a sensor in the forest. Then it processes the image and extract the information that send to the topic `Data`.

* FeedersConsumer is a template class to trigger alerts when a sensor in a feeder detects low levels of food.
* Consumer reads from topic `Data` and writes to a MongoDB.
