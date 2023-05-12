package DB

import org.mongodb.scala.bson.collection.Document
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.{MongoClient, MongoCollection}

class DB {

  val connection: String = ""

  val client = MongoClient()

  val db = client.getDatabase("forest")

  val animals: MongoCollection[Document] = db.getCollection("animals")
  val humans: MongoCollection[Document] = db.getCollection("humans")
  val alerts: MongoCollection[Document] = db.getCollection("alerts")

  def add(json: String, collectionName: String): Unit = {
    val collection =db.getCollection(collectionName)
    collection.insertOne(BsonDocument(json))
  }



}
