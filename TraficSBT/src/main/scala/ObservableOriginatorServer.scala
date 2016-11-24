/**
  * Created by Cristina on 11/8/2016.
  */
//remove if not needed
import scala.collection.JavaConversions._

class ObservableOriginatorServer(port: Int) extends ObservableServer(port) {

  override  def handleMessageFromClient(message: AnyRef, client: ConnectionToClient) {
    synchronized {
      setChanged()
      notifyObservers(new OriginatorMessage(client, message))
    }
  }

  override  def clientConnected(client: ConnectionToClient) {
    synchronized {
      setChanged()
      notifyObservers(new OriginatorMessage(client, CLIENT_CONNECTED))
    }
  }

  override  def clientDisconnected(client: ConnectionToClient) {
    synchronized {
      setChanged()
      notifyObservers(new OriginatorMessage(client, CLIENT_DISCONNECTED))
    }
  }

  override  def clientException(client: ConnectionToClient, exception: Throwable) {
    synchronized {
      setChanged()
      notifyObservers(new OriginatorMessage(client, CLIENT_EXCEPTION + exception.getMessage))
    }
  }

  override  def listeningException(exception: Throwable) {
    synchronized {
      setChanged()
      notifyObservers(new OriginatorMessage(null, LISTENING_EXCEPTION + exception.getMessage))
    }
  }

  override  def serverStarted() {
    synchronized {
      setChanged()
      notifyObservers(new OriginatorMessage(null, SERVER_STARTED))
    }
  }

  override  def serverStopped() {
    synchronized {
      setChanged()
      notifyObservers(new OriginatorMessage(null, SERVER_STOPPED))
    }
  }

  override  def serverClosed() {
    synchronized {
      setChanged()
      notifyObservers(new OriginatorMessage(null, SERVER_CLOSED))
    }
  }
}
