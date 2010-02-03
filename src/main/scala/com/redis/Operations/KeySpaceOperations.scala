package com.redis.operations

import com.redis.SocketOperations

/**
 * Redis key space operations
 *
 */

trait KeySpaceOperations{
  
  val connection: Connection
  var db: Int
  
  // KEYS
  // returns all the keys matching the glob-style pattern.
  def keys(pattern: String): Option[Array[String]] = {
    connection.write("KEYS "+pattern+"\r\n")
    connection.readResponse match {
      case Some(s: String) => Some(s.split(" "))
      case _ => None
    }
  }

  // KEYS
  // returns KeyList matching the glob-style pattern.
  def keysList(pattern: String): Option[KeyList] = {
    connection.write("KEYS "+pattern+"\r\n")
    val bulkLine = connection.readline
    bulkLine.substring( 0, 1 ) match {
      case SocketOperations.BULK => Some(new KeyList(connection))
      case _ => None
    }
  }
  
  // RANDKEY
  // return a randomly selected key from the currently selected DB.
  def randomKey: Option[String] = {
    connection.write("RANDOMKEY\r\n")
    connection.readResponse match {
      case Some(s: String) => Some(s.split('+')(1))
      case _ => None
    }
  }
  
  // RENAME (oldkey, newkey)
  // atomically renames the key oldkey to newkey.
  def rename(oldkey: String, newkey: String): Boolean = {
    connection.write("RENAME "+oldkey+" "+newkey+"\r\n")
    connection.readBoolean
  }
  
  // RENAMENX (oldkey, newkey)
  // rename oldkey into newkey but fails if the destination key newkey already exists.
  def renamenx(oldkey: String, newkey: String): Boolean = {
    connection.write("RENAMENX "+oldkey+" "+newkey+"\r\n")
    connection.readBoolean
  }
  
  // DBSIZE
  // return the size of the db.
  def dbSize: Option[Int] = {
    connection.write("DBSIZE\r\n")
    connection.readInt
  }
}
