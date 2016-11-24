/**
  * Created by Cristina on 11/8/2016.
  */
import java.io._
import java.net._
import java.util
import java.util.HashMap
//remove if not needed
import scala.collection.JavaConversions._


class ConnectionToClient(group: ThreadGroup, private var clientSocket: Socket, private var server: AbstractServer)
  extends Thread(group, null.asInstanceOf[Runnable]) {

  private var input: ObjectInputStream = _

  private var output: ObjectOutputStream = _

  private var readyToStop: Boolean = false

  private var savedInfo: util.HashMap[String, AnyRef] = new HashMap(10)

  clientSocket.setSoTimeout(0)

  try {
    input = new ObjectInputStream(clientSocket.getInputStream)
    output = new ObjectOutputStream(clientSocket.getOutputStream)
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

  start()

  def sendToClient(msg: AnyRef) {
    if (clientSocket == null || output == null) throw new SocketException("socket does not exist")
    output.writeObject(msg)
  }


  def forceResetAfterSend() {
    output.reset()
  }

  def close() {
    readyToStop = true
    try {
      closeAll()
    } finally {
      server.clientDisconnected(this)
    }
  }

  def getInetAddress(): InetAddress = {
    if (clientSocket == null) null else clientSocket.getInetAddress
  }

  override def toString(): String = {
    if (clientSocket == null) null else clientSocket.getInetAddress.getHostName + " (" + clientSocket.getInetAddress.getHostAddress +
      ")"
  }

  def setInfo(infoType: String, info: AnyRef) {
    savedInfo.put(infoType, info)
  }

  def getInfo(infoType: String): AnyRef = savedInfo.get(infoType)

  override def run() {
    server.clientConnected(this)
    try {
      var msg: AnyRef = null
      while (!readyToStop) {
        msg = input readObject()
        server.receiveMessageFromClient(msg, this)
      }
    } catch {
      case exception: Exception => if (!readyToStop) {
        try {
          closeAll()
        } catch {
          case ex: Exception =>
        }
        server.clientException(this, exception)
      }
    }
  }

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

  override protected def finalize() {
    try {
      closeAll()
    } catch {
      case e: IOException =>
    }
  }



}
