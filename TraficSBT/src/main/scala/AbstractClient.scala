/**
  * Created by Cristina on 11/8/2016.
  */
import java.io._
import java.net._

import scala.beans.BeanProperty
//remove if not needed

abstract class AbstractClient(@BeanProperty val host1: String, @BeanProperty val port1: Int)
  extends Runnable {

  private var clientSocket: Socket = _

  private var output: ObjectOutputStream = _

  private var input: ObjectInputStream = _

  private var clientReader: Thread = _

  private var readyToStop: Boolean = false

  private var host: String = host1

  private var port: Int =port1

  def openConnection() {
    if (isConnected) return
    try {
      clientSocket = new Socket(host, port)
      output = new ObjectOutputStream(clientSocket.getOutputStream)
      input = new ObjectInputStream(clientSocket.getInputStream)
    } catch {
      case ex: IOException => {
        try {
          closeAll()
        } catch {
          case exc: Exception =>
        }
        throw ex
      }
    }
    clientReader = new Thread(this)
    readyToStop = false
    clientReader.start()
  }

  def sendToServer(msg: AnyRef) {
    if (clientSocket == null || output == null) throw new SocketException("socket does not exist")
        output.writeObject(msg)
  }

  def forceResetAfterSend() {
    output.reset()
  }

  def closeConnection() {
    readyToStop = true
    try {
      closeAll()
    } finally {
      connectionClosed()
    }
  }

  def isConnected(): Boolean = {
    clientReader != null && clientReader.isAlive
  }

  def getPort(): Int ={
    port
  }
  def getHost(): String ={
    host
  }

  def setPort(p: Int)= {
    port=p
  }
  def setHost(h: String)= {
    host=h
  }

  def getInetAddress(): InetAddress = clientSocket.getInetAddress

  def run() {
    connectionEstablished()
    var msg: AnyRef = null
    try {
      while (!readyToStop) {
        msg = input.readObject()
        handleMessageFromServer(msg)
      }
    } catch {
      case exception: Exception => if (!readyToStop) {
        try {
          closeAll()
        } catch {
          case ex: Exception =>
        }
        connectionException(exception)
      }
    } finally {
      clientReader = null
    }
  }

  protected def connectionClosed() {
  }

  protected def connectionException(exception: Exception) {
  }

  protected def connectionEstablished() {
  }

  protected def handleMessageFromServer(msg: AnyRef): Unit

  private def closeAll() {
    try {
      if (clientSocket != null) clientSocket.close()
      if (output != null) output.close()
      if (input != null) input.close()
    } finally {
      output = null
      input = null
      clientSocket = null
    }
  }
}

