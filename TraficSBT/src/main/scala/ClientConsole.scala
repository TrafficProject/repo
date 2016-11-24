/**
  * Created by Cristina on 11/8/2016.
  */
import java.io._
import ClientConsole._
//remove if not needed
import scala.collection.JavaConversions._

object ClientConsole {

  val DEFAULT_PORT = 5555

  def main(args: Array[String]) {
    var host = ""
    val port = 0
    try {
      host = args(0)
    } catch {
      case e: ArrayIndexOutOfBoundsException => host = "localhost"
    }
    val chat = new ClientConsole(host, DEFAULT_PORT)
    chat.accept()
  }
}

class ClientConsole(host: String, port: Int) extends ChatIF {

  var client: ChatClient = _

  try {
    client = new ChatClient(host, port, this)
  } catch {
    case exception: IOException => {
      println("Error: Can't setup connection!" + " Terminating client.")
      System.exit(1)
    }
  }

  def accept() {
    try {
      val fromConsole = new BufferedReader(new InputStreamReader(System.in))
      val message: Array[String]= new Array[String](5)
      while (true) {
        message(0) = fromConsole.readLine()
        message(1) = fromConsole.readLine()
        message(2) = fromConsole.readLine()
        message(3) = fromConsole.readLine()
        message(4) = fromConsole.readLine()
        client.handleMessageFromClientUI(message)
      }
    } catch {
      case ex: Exception => println("Unexpected error while reading from console!")
    }
  }

  def display(message: String) {
    println("> " + message)
  }
}
