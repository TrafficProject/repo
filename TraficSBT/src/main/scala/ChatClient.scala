/**
  * Created by Cristina on 11/8/2016.
  */
import java.io._
//remove if not needed
import scala.collection.JavaConversions._

class ChatClient(host: String, port: Int, var clientUI: ChatIF) extends AbstractClient(host, port) {

  openConnection()

  def handleMessageFromServer(msg: AnyRef) {
    clientUI.display(msg.toString)
  }

  def handleMessageFromClientUI(message: AnyRef) {
    try {
      sendToServer(message)
    } catch {
      case e: IOException => {
        clientUI.display("Could not send message to server.  Terminating client.")
        quit()
      }
    }
  }

  def quit() {
    try {
      closeConnection()
    } catch {
      case e: IOException =>
    }
    System.exit(0)
  }
}
