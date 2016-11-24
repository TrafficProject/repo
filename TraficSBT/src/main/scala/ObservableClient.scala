/**
  * Created by Cristina on 11/8/2016.
  */
import java.util._
import java.io._
import java.net._
import ObservableClient._
//remove if not needed
import scala.collection.JavaConversions._

object ObservableClient {

  val CONNECTION_EXCEPTION = "#OC:Connection error."

  val CONNECTION_CLOSED = "#OC:Connection closed."

  val CONNECTION_ESTABLISHED = "#OC:Connection established."
}

class ObservableClient(host: String, port: Int) extends Observable {

  private val service: AdaptableClient = new AdaptableClient(host, port, this)

  def openConnection() {
    service.openConnection()
  }

  def closeConnection() {
    service.closeConnection()
  }

  def sendToServer(msg: AnyRef) {
    service.sendToServer(msg)
  }

  def isConnected(): Boolean = service.isConnected

  def getPort(): Int = service.getPort

  def setPort(port: Int) {
    service.setPort(port)
  }

  def getHost(): String = service.getHost

  def setHost(host: String) {
    service.setHost(host)
  }

  def getInetAddress(): InetAddress = service.getInetAddress

   def handleMessageFromServer(message: AnyRef) {
    setChanged()
    notifyObservers(message)
  }

  def connectionClosed() {
    setChanged()
    notifyObservers(CONNECTION_CLOSED)
  }

  def connectionException(exception: Exception) {
    setChanged()
    notifyObservers(CONNECTION_EXCEPTION)
  }

   def connectionEstablished() {
    setChanged()
    notifyObservers(CONNECTION_ESTABLISHED)
  }
}
