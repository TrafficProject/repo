/**
  * Created by Cristina on 11/8/2016.
  */
import java.io._
import java.net._
import java.util._
//remove if not needed
import scala.collection.JavaConversions._

class AdaptableServer(port: Int, private var server: ObservableServer) extends AbstractServer(port) {

  override def clientConnected(client: ConnectionToClient) {
    server.clientConnected(client)
  }

  override  def clientDisconnected(client: ConnectionToClient) {
    server.clientDisconnected(client)
  }

  override  def clientException(client: ConnectionToClient, exception: Throwable) {
    server.clientException(client, exception)
  }

  override  def listeningException(exception: Throwable) {
    server.listeningException(exception)
  }

  override  def serverStopped() {
    server.serverStopped()
  }

 override  def serverStarted() {
    server.serverStarted()
  }

  override  def serverClosed() {
    server.serverClosed()
  }

   def handleMessageFromClient(msg: AnyRef, client: ConnectionToClient) {
    server.handleMessageFromClient(msg, client)
  }
}
