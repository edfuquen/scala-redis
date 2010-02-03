package com.redis

class KeyList( connection: Connection  ){

  var cReturn = false

  //If more keys, return next key.
  //End of KeyList returns null.
  def next: String = {
    if( !cReturn ) {
      val keyStr = new StringBuilder
      var keyChar = connection.readchar

      while( keyChar != 32 
        && !(cReturn && keyChar == 10) ) {

        if( keyChar == 13 ) {
          cReturn = true
        } else {
          cReturn = false
        }
        
        if( !cReturn ) {
          keyStr.append( keyChar.asInstanceOf[Char] )
        }

        keyChar = connection.readchar
      }
      keyStr.toString
    } else {
      null
    }
  }

}
