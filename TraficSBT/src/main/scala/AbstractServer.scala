/**
  * Created by Cristina on 11/8/2016.
  */
import java.io._
import java.net._

import scala.beans.BeanProperty
//remove if not needed

abstract class AbstractServer(@BeanProperty val port1: Int) extends Runnable {

  private var serverSocket: ServerSocket = null

  private var connectionListener: Thread = _

  private var timeout: Int = 500

  private var backlog: Int = 10

  private var port: Int =port1

  private var clientThreadGroup: ThreadGroup = new ThreadGroup("ConnectionToClient threads") {

    override def uncaughtException(thread: Thread, exception: Throwable) {
      clientException(thread.asInstanceOf[ConnectionToClient], exception)
    }
  }

  private var readyToStop: Boolean = false

  def listen() {
    if (!isListening) {
      if (serverSocket == null) {
        serverSocket = new ServerSocket(getPort, backlog)
      }
      serverSocket.setSoTimeout(timeout)
      readyToStop = false
      connectionListener = new Thread(this)
      connectionListener.start()
    }
  }

  def stopListening() {
    readyToStop = true
  }

  def close() {
    synchronized {
      if (serverSocket == null) return
      stopListening()
      try {
        serverSocket.close()
      } finally {
        val clientThreadList = getClientConnections
        for (i <- 0 until clientThreadList.length) {
          try {
            clientThreadList(i).asInstanceOf[ConnectionToClient]
              .close()
          } catch {
            case ex: Exception =>
          }
        }
        serverSocket = null
        serverClosed()
      }
    }
  }

  def sendToAllClients(msg: AnyRef) {
    val clientThreadList = getClientConnections
    for (i <- 0 until clientThreadList.length) {
      try {
        clientThreadList(i).asInstanceOf[ConnectionToClient]
          .sendToClient(msg)
      } catch {
        case ex: Exception =>
      }
    }
  }

  def isListening(): Boolean = (connectionListener != null)

  def getClientConnections(): Array[Thread] = {
    synchronized {
      val clientThreadList = Array.ofDim[Thread](clientThreadGroup.activeCount())
      clientThreadGroup.enumerate(clientThreadList)
      clientThreadList
    }
  }

  def getNumberOfClients(): Int = clientThreadGroup.activeCount()

  def setTimeout(timeout: Int) {
    this.timeout = timeout
  }

  def setBacklog(backlog: Int) {
    this.backlog = backlog
  }

  def setPort(p: Int)= {
    port=p
  }

  def getPort(): Int ={
    port
  }

  def run() {
    serverStarted()
    try {
      while (!readyToStop) {
        try {
          val clientSocket = serverSocket.accept()
          //synchronized (this) {
            val c = new ConnectionToClient(this.clientThreadGroup, clientSocket, this)
          //}
        } catch {
          case exception: InterruptedIOException =>
        }
      }
      serverStopped()
    } catch {
      case exception: IOException => if (!readyToStop) {
        listeningException(exception)
      } else {
        serverStopped()
      }
    } finally {
      readyToStop = true
      connectionListener = null
    }
  }

  def clientConnected(client: ConnectionToClient) {
  }

  def clientDisconnected(client: ConnectionToClient) {
    synchronized {
    }
  }

  def clientException(client: ConnectionToClient, exception: Throwable) {
    synchronized {
    }
  }

  def listeningException(exception: Throwable) {
  }

 def serverStarted() {
  }

  def serverStopped() {
  }

  def serverClosed() {
  }

  def handleMessageFromClient(msg: AnyRef, client: ConnectionToClient): Unit

  def receiveMessageFromClient(msg: AnyRef, client: ConnectionToClient) {
    synchronized {
      this.handleMessageFromClient(msg, client)
    }
  }
}
