/**
  * Created by Cristina on 11/8/2016.
  */
import java.util._
import java.io._
import java.net._
import ObservableServer._
//remove if not needed
import scala.collection.JavaConversions._

object ObservableServer {

  val CLIENT_CONNECTED = "#OS:Client connected."

  val CLIENT_DISCONNECTED = "#OS:Client disconnected."

  val CLIENT_EXCEPTION = "#OS:Client exception."

  val LISTENING_EXCEPTION = "#OS:Listening exception."

  val SERVER_CLOSED = "#OS:Server closed."

  val SERVER_STARTED = "#OS:Server started."

  val SERVER_STOPPED = "#OS:Server stopped."
}

class ObservableServer(port: Int) extends Observable {

  val CLIENT_CONNECTED = "#OS:Client connected."

  val CLIENT_DISCONNECTED = "#OS:Client disconnected."

  val CLIENT_EXCEPTION = "#OS:Client exception."

  val LISTENING_EXCEPTION = "#OS:Listening exception."

  val SERVER_CLOSED = "#OS:Server closed."

  val SERVER_STARTED = "#OS:Server started."

  val SERVER_STOPPED = "#OS:Server stopped."

  private var service: AdaptableServer = new AdaptableServer(port, this)

  def listen() {
    service.listen()
  }

  def stopListening() {
    service.stopListening()
  }

  def close() {
    service.close()
  }

  def sendToAllClients(msg: Array[String]) {
    service.sendToAllClients(msg)
  }

  def isListening(): Boolean = service.isListening

  def getClientConnections(): Array[Thread] = service.getClientConnections

  def getNumberOfClients(): Int = service.getNumberOfClients

  def getPort(): Int = service.getPort

  def setPort(port: Int) {
    service.setPort(port)
  }

  def setTimeout(timeout: Int) {
    service.setTimeout(timeout)
  }

  def setBacklog(backlog: Int) {
    service.setBacklog(backlog)
  }

  def clientConnected(client: ConnectionToClient) {
    synchronized {
      setChanged()
      notifyObservers(CLIENT_CONNECTED)
    }
  }

   def clientDisconnected(client: ConnectionToClient) {
    synchronized {
      setChanged()
      notifyObservers(CLIENT_DISCONNECTED)
    }
  }

  def clientException(client: ConnectionToClient, exception: Throwable) {
    synchronized {
      setChanged()
      notifyObservers(CLIENT_EXCEPTION)
      try {
        client.close()
      } catch {
        case e: Exception =>
      }
    }
  }

  def listeningException(exception: Throwable) {
    synchronized {
      setChanged()
      notifyObservers(LISTENING_EXCEPTION)
      stopListening()
    }
  }

  def serverStopped() {
    synchronized {
      setChanged()
      notifyObservers(SERVER_STOPPED)
    }
  }

   def serverClosed() {
    synchronized {
      setChanged()
      notifyObservers(SERVER_CLOSED)
    }
  }

  def serverStarted() {
    synchronized {
      setChanged()
      notifyObservers(SERVER_STARTED)
    }
  }

   def handleMessageFromClient(message: AnyRef, client: ConnectionToClient) {
    synchronized {
      setChanged()
      notifyObservers(message)
    }
  }
}
